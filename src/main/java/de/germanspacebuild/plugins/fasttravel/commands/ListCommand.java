package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by oneill011990 on 11.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class ListCommand implements CommandExecutor {
    private FastTravel plugin;

    public ListCommand(FastTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(FastTravel.PERMS_BASE + "list")){
            plugin.getIOManger().sendTranslation(sender, "Perms.Not");
            return false;
        }

        if (FastTravelDB.getAllSigns().size() == 0) {
            plugin.getIOManger().sendTranslation(sender, "DB.Empty");
        } else {
            plugin.getIOManger().sendTranslation(sender, "Sign.List");
            FastTravelUtil.sendFTSignList(sender, FastTravelDB.getAllSigns(),
                    plugin.getConfig().getBoolean("Plugin.Economy"));
        }

        return true;
    }
}
