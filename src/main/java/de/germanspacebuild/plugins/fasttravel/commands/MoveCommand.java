/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Felix on 09.08.2016
 * for FastTravelReborn
 *
 * @author Felix
 */
public class MoveCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public MoveCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            io.sendTranslation(sender, "Command.Player");
            return true;
        } else if (!sender.hasPermission(FastTravel.PERMS_BASE + "move")) {
            io.sendTranslation(sender, "Perms.Not");
            return true;
        } else if (args.length != 1) {
            io.sendTranslation(sender, "Command.InvalidArgs");
        } else if (FastTravelDB.getSign(args[0]) == null) {
            io.send(sender, io.translate("Sign.Exists.Not").replaceAll("%sign", args[0]));
            return true;
        } else {
            FastTravelSign sign = FastTravelDB.getSign(args[0]);
            ItemStack wand = new ItemStack(Material.BONE, 1);
            ItemMeta meta = wand.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + "Sign Mover");

            List<String> lore = new ArrayList<>();
            lore.add("Sign: " + ChatColor.GOLD + sign.getName());
            lore.addAll(Arrays.asList(ChatColor.GRAY + "Right click new empty sign ",
                    ChatColor.GRAY + "with this to move Sign to ",
                    ChatColor.GRAY + "new location"));

            meta.setLore(lore);
            wand.setItemMeta(meta);

            ((Player) sender).getInventory().addItem(wand);
            return true;

        }

        return false;
    }
}
