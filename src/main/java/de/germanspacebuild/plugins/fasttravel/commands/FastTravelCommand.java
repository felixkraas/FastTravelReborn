package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelEvent;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by oneill011990 on 10.03.15.
 *
 * @author oneill011990
 */
public class FastTravelCommand implements CommandExecutor {
    FastTravel plugin;

    public FastTravelCommand(FastTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission(FastTravel.PERMS_BASE + "travel")) {
            plugin.getIOManger().sendTranslation(sender, "Perms.Not");
            return false;
        }

        if (args.length == 0) {

            // Send a list
            plugin.getIOManger().sendTranslation(sender, "Player.List");
            List<FastTravelSign> usigns = FastTravelDB.getSignsFor(((Player) sender).getUniqueId());
            if (usigns == null || usigns.size() == 0) {
                plugin.getIOManger().sendTranslation(sender, "Player.HasZero");
            } else
                FastTravelUtil.sendFTSignList(sender, usigns, (plugin.getEconomy() != null));
        }

        else if (args.length == 1) {

            // Time to travel. Check if the requested sign exists.
            FastTravelSign ftsign = FastTravelDB.getSign(args[0]);

            if (ftsign == null) {
                plugin.getIOManger().sendTranslation(sender, "Sign.NotExists");
                return true;
            }

            boolean allPoints = sender.hasPermission("fasttravelsigns.overrides.allpoints");
            if (!(ftsign.isAutomatic() || ftsign.foundBy(((Player) sender).getUniqueId())) && !allPoints) {
                plugin.getIOManger().sendTranslation(sender, "Sign.NotFound");
                return true;
            }
            // Check if world exists
            World targworld = ftsign.getTPLocation().getWorld();
            if (!allPoints && !targworld.equals(((Player) sender).getWorld())
                    && !sender.hasPermission("fasttravelsigns.multiworld")) {
                plugin.getIOManger().sendTranslation(sender, "Perms.Not.Multiworld");
                return true;
            }

            if (plugin.getEconomy() == null  || !plugin.getConfig().getBoolean("economy.enabled")) {
                plugin.getServer().getPluginManager().callEvent(new FastTravelEvent((Player) sender, ftsign));
                return true;
            }
            plugin.getServer().getPluginManager().callEvent(new FastTravelEvent((Player) sender, ftsign));
        }
        return true;
    }
}
