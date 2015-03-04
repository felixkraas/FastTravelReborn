package de.germanspacebuild.plugins.fasttravel.tabcomplete;

import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by oneill011990 on 03.03.2015.
 */
public class FtTabComplete implements TabCompleter{

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!command.getName().equalsIgnoreCase("ft") || args.length != 1)
            return null;

        if (!(sender instanceof Player))
            return null;

        Player player = ((Player) sender);

        List<FastTravelSign> signs = FastTravelDB.getSignsFor(player);

        return FastTravelUtil.sendSignNames(signs);
    }
}
