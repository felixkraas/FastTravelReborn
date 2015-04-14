/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2015 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel;

import de.germanspacebuild.plugins.fasttravel.Listener.*;
import de.germanspacebuild.plugins.fasttravel.commands.*;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.io.language.Language;
import de.germanspacebuild.plugins.fasttravel.io.language.en;
import de.germanspacebuild.plugins.fasttravel.tabcomplete.FtTabComplete;
import de.germanspacebuild.plugins.fasttravel.task.CheckPlayerTask;
import de.germanspacebuild.plugins.fasttravel.util.DBType;
import de.germanspacebuild.plugins.fasttravel.util.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;

public class FastTravel extends JavaPlugin {

    public static final String PERMS_BASE = "fasttravelsigns.";

    static FastTravel instance;
    Configuration config;
    File dataDir;
    static File langDir;
    Metrics metrics;
    Economy economy;
    UpdateChecker updateChecker;
    IOManager io;
    DBType dbHandler;

    public boolean needUpdate;
    public String newVersion;


    @Override
    public void onEnable(){
        instance = this;
        config = this.getConfig();
        dataDir = this.getDataFolder();
        langDir = new File(getDataFolder(), "lang");

        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        if (!langDir.exists()) {
            langDir.mkdir();
        }


        setupConfig();

        setupEconomy();

        metricsInit();

        //Init language
        initLanguages();
        io = new IOManager(this);

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new FTPlayerListener(this), this);
        pm.registerEvents(new FTBlockListener(this), this);
        pm.registerEvents(new FTSignListener(this), this);
        pm.registerEvents(new FTEntityListener(), this);
        pm.registerEvents(new FTInventoryListener(this), this);

        //Commands
        getCommand("ft").setExecutor(new FastTravelCommand(this));
        getCommand("ftlist").setExecutor(new ListCommand(this));
        getCommand("ftclear").setExecutor(new ClearCommand(this));
        getCommand("ftauto").setExecutor(new AutoCommand(this));
        getCommand("ftdelete").setExecutor(new DeleteCommand(this));
        getCommand("ftmenu").setExecutor(new MenuCommand(this));
        getCommand("ftprice").setExecutor(new PriceCommand(this));
        getCommand("ftrange").setExecutor(new SetRangeCommand(this));

        //Tab-Completer
        getCommand("ft").setTabCompleter(new FtTabComplete());

        //Updatecheck
        updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/fasttravel/files.rss");

        if (updateChecker.updateFound()) {
            io.sendConsole(io.translate("Plugin.Update.Console").replace("%old", this.getDescription().getVersion())
                    .replaceAll("%new", updateChecker.getVersion()).replaceAll("%link", updateChecker.getLink()));
            needUpdate = true;
            newVersion = updateChecker.getLink();
        }


        if (config.getString("Plugin.Database").equalsIgnoreCase("FILE")) {
            this.dbHandler = DBType.File;
            FastTravelDB.init(this, "signs.yml", true);
        } else if (config.getString("Plugin.Database").equalsIgnoreCase("MySQL")) {
            this.dbHandler = DBType.MySQL;
        } else if (config.getString("Plugin.Database").equalsIgnoreCase("SQLite")) {
            this.dbHandler = DBType.SQLite;
        } else {
            io.sendConsole(io.translate("Plugin.InvalidDB"));
        }

        getServer().getScheduler().runTaskTimer(this, new CheckPlayerTask(this), 5*20, 1*20);

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        FastTravelDB.save();
    }

    public void setupConfig(){
        File confFile = new File(dataDir, "config.yml");
        try {
            if (!confFile.exists()) {
                confFile.createNewFile();
            }
            getConfig().load(confFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        config.addDefault("Plugin.Update", true);
        config.addDefault("Plugin.Debug.Enabled", false);
        config.addDefault("Plugin.Debug.Log", false);
        config.addDefault("Plugin.Metrics", true);
        config.addDefault("Plugin.Economy", false);
        config.addDefault("Plugin.Database", "FILE");
        config.addDefault("Travel.Cooldown", 0);
        config.addDefault("Travel.Warmup", 0);
        config.addDefault("Travel.Price", 0);
        config.addDefault("Travel.Range", true);
        config.addDefault("IO.Language", "en");
        config.options().copyDefaults(true);
        try {
            getConfig().save(confFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setupEconomy() {

        if (!config.getBoolean("Plugin.Economy")) {
            return;
        }

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().warning("Could not find Vault! Disabling economy support.");
            return;
        }

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
                .getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        if (economy == null) {
            getLogger().warning("Could not find an economy plugin! Disabling economy support.");
            return;
        }
        getLogger().info("Using " + economy.getName() + " for economy support.");
    }

    public void metricsInit(){
        if (getConfig().getBoolean("Plugin.Metrics")){
            try {
                metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }

        }
    }

    private void initLanguages() {
        Language en = new en(this);
        Language.addLanguage(en);
    }

    public static FastTravel getInstance() {
        return instance;
    }

    public IOManager getIOManger(){
        return io;
    }

    public File getDataDir(){
        return dataDir;
    }

    public static File getLangDir() {
        return langDir;
    }

    public Economy getEconomy(){
        return economy;
    }

    public DBType getDBHandler() {
        return dbHandler;
    }

}
