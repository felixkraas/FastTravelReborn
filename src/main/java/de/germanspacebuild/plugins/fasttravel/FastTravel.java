/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
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
import de.germanspacebuild.plugins.fasttravel.data.*;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.io.language.LangEN;
import de.germanspacebuild.plugins.fasttravel.io.language.Language;
import de.germanspacebuild.plugins.fasttravel.tabcomplete.FtTabComplete;
import de.germanspacebuild.plugins.fasttravel.task.CheckPlayerTask;
import de.germanspacebuild.plugins.fasttravel.thirdparty.DynmapHook;
import de.germanspacebuild.plugins.fasttravel.thirdparty.PluginHook;
import de.germanspacebuild.plugins.fasttravel.util.UpdateChecker;
import de.slikey.effectlib.EffectManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FastTravel extends JavaPlugin {

    public static final boolean BETA = false;

    public static final String PERMS_BASE = "fasttravelsigns.";
    private static FastTravel instance;
    private static File langDir;
    public boolean needUpdate;
    public String newVersion;
    private EffectManager em;
    private Map<String, PluginHook> hooks = new HashMap<>();
    private Configuration config;
    private File dataDir;
    private Metrics metrics;
    private Economy economy;
    private UpdateChecker updateChecker;
    private IOManager io;

    public static FastTravel getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        //private variables
        instance = this;
        this.config = this.getConfig();
        this.dataDir = this.getDataFolder();
        langDir = new File(dataDir, "lang");

        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        if (!langDir.exists()) {
            langDir.mkdir();
        }

        initLanguages();
        //Init language
        io = new IOManager(this);

        setupConfig();

        setupEconomy();

        metricsInit();

        initDB();

        this.em = new EffectManager(this);
        PluginManager pm = getServer().getPluginManager();

        //Listener
        pm.registerEvents(new FTPlayerListener(this), this);
        pm.registerEvents(new FTBlockListener(this), this);
        pm.registerEvents(new FTSignListener(this), this);
        pm.registerEvents(new FTEntityListener(), this);
        pm.registerEvents(new FTInventoryListener(this), this);
        pm.registerEvents(new FTFoundListener(this), this);
        pm.registerEvents(new FTTravelListener(this), this);

        //Commands
        getCommand("ft").setExecutor(new FastTravelCommand(this));
        getCommand("ftlist").setExecutor(new ListCommand(this));
        getCommand("ftclear").setExecutor(new ClearCommand(this));
        getCommand("ftauto").setExecutor(new AutoCommand(this));
        getCommand("ftdelete").setExecutor(new DeleteCommand(this));
        getCommand("ftmenu").setExecutor(new MenuCommand(this));
        getCommand("ftprice").setExecutor(new PriceCommand(this));
        getCommand("ftrange").setExecutor(new SetRangeCommand(this));
        getCommand("ftremove").setExecutor(new RemoveCommand(this));
        getCommand("ftreload").setExecutor(new ReloadCommand(this));
        getCommand("ftsave").setExecutor(new SaveCommand(this));
        getCommand("ftsetpoint").setExecutor(new SetpointCommand(this));
        getCommand("ftshow").setExecutor(new ShowTPCommand(this));
        getCommand("ftconvert").setExecutor(new ConvertDBCommand(this));
        getCommand("ftshowrange").setExecutor(new ShowRangeCommand(this));
        getCommand("ftmove").setExecutor(new MoveCommand(this));

        //Tab-Completer
        getCommand("ft").setTabCompleter(new FtTabComplete());

        //Updatecheck
        if (!BETA) {
            updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/fasttravel/files.rss");

            if (updateChecker.updateFound()) {
                io.sendConsole(io.translate("Plugin.Update.Console.Yes").replace("%old", this.getDescription().getVersion())
                        .replaceAll("%new", updateChecker.getVersion()).replaceAll("%link", updateChecker.getLink()));
                needUpdate = true;
                newVersion = updateChecker.getLink();
            } else {
                io.sendConsole(io.translate("Plugin.Update.Console.No"));
            }
        }

        //Database
        if (config.getString("Plugin.Database").equalsIgnoreCase("FILE")) {
            DBType.setDBType(DBType.File);
            FastTravelDB.init(this, "signs.yml", true);
        } else if (config.getString("Plugin.Database").equalsIgnoreCase("MySQL")) {
            DBType.setDBType(DBType.MySQL);
            SQLDBHandler.load();
        } else if (config.getString("Plugin.Database").equalsIgnoreCase("SQLite")) {
            DBType.setDBType(DBType.SQLite);
            SQLDBHandler.load();
        } else {
            io.sendConsole(io.translate("Plugin.InvalidDB"));
        }

        checkHooks();

        getServer().getScheduler().runTaskTimer(this, new CheckPlayerTask(this), 5 * 20, 1 * 20);

        if (BETA) {
            io.sendConsole(ChatColor.RED + "This is a beta release of FastTravelSigns. Please report any bug to the" +
                    " author.");
        }

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        io.sendConsole(io.translate("DB.Saving"));
        FastTravelDB.save();
        if (DBType.getDBType() == DBType.SQLite) {
            SQLDBHandler.shutdown();
        }
        io.sendConsole(io.translate("DB.Saved"));
        em.dispose();
    }

    public void setupConfig() {
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
        config.addDefault("Plugin.Menus", true);
        config.addDefault("Plugin.Database", "FILE");
        config.addDefault("Travel.Cooldown", 0);
        config.addDefault("Travel.Warmup", 0);
        config.addDefault("Travel.Price", 0);
        config.addDefault("Travel.Range", true);
        config.addDefault("IO.Language", "en");
        config.addDefault("Hooks.Dynmap", false);
        config.addDefault("MySQL.Host", "localhost");
        config.addDefault("MySQL.Port", 3306);
        config.addDefault("MySQL.Name", "FastTravel");
        config.addDefault("MySQL.Username", "username");
        config.addDefault("MySQL.Password", "password");
        config.options().copyDefaults(true);
        try {
            getConfig().save(confFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkHooks() {
        PluginManager pm = this.getServer().getPluginManager();
        if (config.getBoolean("Hooks.Dynmap") && pm.getPlugin("dynmap").isEnabled()) {
            hooks.put("dynmap", new DynmapHook(this, pm.getPlugin("dynmap")));
            io.sendConsole(io.translate("Hooks.Dynmap"));
            hooks.get("dynmap").init();
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

    public void metricsInit() {
        if (getConfig().getBoolean("Plugin.Metrics")) {
            try {
                metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }

        }
    }

    private void initLanguages() {
        Language en = new LangEN(this);
        Language.addLanguage(en);
    }

    private void initDB() {
        Database.registerDatabaseSystem(DBType.SQLite, new SQLite(this));
        Database.registerDatabaseSystem(DBType.MySQL, new MySQL(this));
    }

    public IOManager getIOManger() {
        return io;
    }

    public File getLangDir() {
        return langDir;
    }

    public File getDataDir() {
        return dataDir;
    }

    public Economy getEconomy() {
        return economy;
    }

    public EffectManager getEffectManager() {
        return this.em;
    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public PluginHook getHook(String hook) {
        return hooks.get(hook);
    }
}
