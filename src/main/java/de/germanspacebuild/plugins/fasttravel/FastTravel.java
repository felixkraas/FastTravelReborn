package de.germanspacebuild.plugins.fasttravel;

import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.util.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.File;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class FastTravel extends JavaPlugin {

    FastTravel instance;
    Configuration config;
    File dataDir;
    Metrics metrics;
    Economy economy;
    UpdateChecker updateChecker;
    IOManager io;

    public boolean needUpdate;
    public String newVersion;


    @Override
    public void onEnable(){
        io = new IOManager(this);
        instance = this;
        config = this.getConfig();
        dataDir = this.getDataFolder();

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
        config.addDefault("IO.Language", "en");
    }

    public FastTravel getInstance() {
        return instance;
    }

    public IOManager getIOManger(){
        return io;
    }

}
