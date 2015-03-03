package de.germanspacebuild.plugins.fasttravel;

import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.util.DBType;
import de.germanspacebuild.plugins.fasttravel.util.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;
import java.io.IOException;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class FastTravel extends JavaPlugin {

    static FastTravel instance;
    Configuration config;
    File dataDir;
    Metrics metrics;
    Economy economy;
    UpdateChecker updateChecker;
    IOManager io;
    DBType dbHandler;

    public boolean needUpdate;
    public String newVersion;


    @Override
    public void onEnable(){
        io = new IOManager(this);
        instance = this;
        config = this.getConfig();
        dataDir = this.getDataFolder();

        setupConfig();

        setupEconomy();

        metricsInit();

        PluginManager pm = getServer().getPluginManager();

        //Updatecheck
        updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/fasttravel/files.rss");

        if (updateChecker.updateFound()){
            io.sendConsole(io.translate("Plugin.Update").replace("%old", this.getDescription().getVersion())
                    .replaceAll("%new", updateChecker.getVersion()).replaceAll("%link", updateChecker.getLink()));
            needUpdate = true;
            newVersion = updateChecker.getLink();
        }
    }

    @Override
    public void onDisable() {

    }

    public void setupConfig(){
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

    public static FastTravel getInstance() {
        return instance;
    }

    public IOManager getIOManger(){
        return io;
    }

    public String getDataDir(){
        return dataDir.getPath();
    }

    public DBType getDBHandler() {
        return dbHandler;
    }

}
