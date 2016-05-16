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

package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by oneill011990 on 04.03.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class FTSignListener implements Listener {

    private FastTravel plugin;

    public FTSignListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onSignChange(SignChangeEvent event) {

        String lines[] = event.getLines();

        if (!FastTravelUtil.isFTSign(lines))
            return;

        if (!event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "create")) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Perms.Not");
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            }
            return;
        }

        // Check for valid name
        Pattern an = Pattern.compile("^[a-zA-Z0-9_-]+$");
        if (!an.matcher(lines[1]).find()) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            }
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.InvalidName");
            return;
        }

        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1,
                event.getBlock().getZ());
        if (!Arrays.asList(BlockUtil.safeBlocks).contains(blockAbove.getType())) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            }
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.PlaceAbove.Is");
            return;
        }

        if (FastTravelDB.getSign(lines[1]) != null) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            }
            plugin.getIOManger().send(event.getPlayer(), plugin.getIOManger().translate("Sign.Exists.Already")
                    .replaceAll("%sign", lines[1]));
            return;
        } else {
            FastTravelSign newFTSign = new FastTravelSign(lines[1], event.getPlayer().getUniqueId(), event.getBlock());

            // Economy support - set default price
            if (plugin.getEconomy() != null) {
                float defPrice = (float) plugin.getConfig().getDouble("Travel.Price");
                if (defPrice > 0)
                    event.setLine(2, "Price: " + defPrice);
                newFTSign.setPrice(defPrice);
            }

            FastTravelDB.addSign(newFTSign);

            plugin.getIOManger().send(event.getPlayer(), plugin.getIOManger().translate("Sign.Created")
                    .replaceAll("%sign", newFTSign.getName()));

            newFTSign.addPlayer(event.getPlayer().getUniqueId());

            // Colorize sign
            event.setLine(0, ChatColor.DARK_PURPLE + "[FastTravel]");
            event.setLine(1, ChatColor.DARK_BLUE + lines[1]);

            FastTravelDB.save();

        }


    }

}
