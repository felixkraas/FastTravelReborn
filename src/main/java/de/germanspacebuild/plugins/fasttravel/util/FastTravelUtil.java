/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.util;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.germanspacebuild.plugins.fasttravel.menu.SignMenu;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by oneill011990 on 03.03.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class FastTravelUtil {

    private static IOManager io;

    static {
        FastTravel plugin = FastTravel.getInstance();
        io = plugin.getIOManger();
    }

    public static boolean isMoveWand(ItemStack item) {
        List<String> lore = item.getItemMeta().getLore();
        return lore != null && lore.size() > 0 && lore.get(0).contains("Sign");
    }

    /**
     * Checks if block is a FasTravelSign
     *
     * @param block Block to check
     * @return Is FastTravelSign or not
     */
    public static boolean isFTSign(Block block) {
        if (block == null)
            return false;
        if (!Arrays.asList(BlockUtil.signBlocks).contains(block.getType()))
            return false;
        String[] lines = ((Sign) block.getState()).getLines();
        String line1 = ChatColor.stripColor(lines[0]);
        return line1.equalsIgnoreCase("[fasttravel]") || line1.equalsIgnoreCase("[ft]");
    }


    /**
     * Checks if lines belong to a FastTravelSign
     *
     * @param lines Line to check
     * @return Belong to FastTravelSign or not
     */
    public static boolean isFTSign(String[] lines) {
        String line1 = ChatColor.stripColor(lines[0]);
        return line1.equalsIgnoreCase("[fasttravel]") || line1.equalsIgnoreCase("[ft]");
    }

    /**
     * Prints a list of signs to a player.
     *
     * @param sender Player the message will be sent to.
     * @param signs  Signs to sent.
     * @param econ   Is economy enabled?
     */
    public static void sendFTSignList(CommandSender sender, List<FastTravelSign> signs, boolean econ) {
        int counter = 0;
        String pointstr = "";
        for (FastTravelSign sign : signs) {
            counter++;
            if (counter != 1)
                pointstr = pointstr + ", ";
            if (sign.isAutomatic())
                // Special coloring for automatic signs
                pointstr += ChatColor.GREEN;
            else
                pointstr += ChatColor.AQUA;
            pointstr += sign.getName() + ChatColor.WHITE;
            if (econ && sign.getPrice() > 0)
                pointstr += " (" + sign.getPrice() + ")";
            if (counter == 4) {
                io.send(sender, pointstr);
                counter = 0;
                pointstr = "";
            }
        }
        if (counter != 0)
            io.send(sender, pointstr);
    }

    /**
     * Creates a list of string with the names of the given signs.
     *
     * @param signs Signs to get names from
     * @return Names of the signs.
     */
    public static List<String> sendSignNames(List<FastTravelSign> signs) {

        return signs.stream().map(FastTravelSign::getName).collect(Collectors.toList());
    }

    /**
     * Checks if inventory is a menu from FastTravel
     *
     * @param inv Inventory to check
     * @return true if inventory is a menu from FastTravel
     */
    public static boolean isFastTravelMenu(Inventory inv) {
        List<SignMenu> menus = SignMenu.getMenus();
        List<Inventory> inventories = new ArrayList<>();
        boolean isTravelInv = false;

        for (SignMenu m : menus) {
            inventories.addAll(m.getInventories());
            if (inventories.contains(inv)) {
                isTravelInv = true;
                inventories.clear();
                break;
            }
            inventories.clear();
        }

        return isTravelInv;
    }

    public static void formatSign(Sign sign, String name) {
        // Colorize sign
        sign.setLine(0, ChatColor.DARK_PURPLE + "[FastTravel]");
        sign.setLine(1, ChatColor.DARK_BLUE + name);
        sign.update();

    }
}
