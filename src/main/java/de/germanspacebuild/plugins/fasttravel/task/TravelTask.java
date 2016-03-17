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

package de.germanspacebuild.plugins.fasttravel.task;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import org.bukkit.*;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

/**
 * Created by oneill011990 on 04.03.2016.
 */
public class TravelTask implements Runnable {

    private FastTravel plugin;
    private UUID player;
    private FastTravelSign sign;

    public TravelTask(FastTravel plugin, UUID player, FastTravelSign sign) {
        this.plugin = plugin;
        this.player = player;
        this.sign = sign;
    }

    @Override
    public void run() {

        if (!plugin.getServer().getPlayer(player).isOnline())
            return;


        if (plugin.getConfig().getBoolean("Plugin.Economy") || plugin.getEconomy() != null) {

            if (!plugin.getServer().getPlayer(player).hasPermission(FastTravel.PERMS_BASE + "price")) {
                if (!plugin.getEconomy().has(plugin.getServer().getPlayer(player), sign.getPrice())) {

                    plugin.getIOManger().sendTranslation(plugin.getServer().getPlayer(player),
                            "Econ.MoneyLess".replaceAll("%cost", plugin.getEconomy().format(sign.getPrice())));
                    return;
                } else {

                    // Charge player
                    boolean success = plugin.getEconomy().withdrawPlayer(plugin.getServer().getPlayer(player),
                            sign.getPrice()).transactionSuccess();
                    if (success) {

                        plugin.getIOManger().sendTranslation(plugin.getServer().getPlayer(player),
                                "Econ.Charged".replaceAll("%cost", plugin.getEconomy().format(sign.getPrice())));
                    } else {
                        plugin.getIOManger().sendTranslation(plugin.getServer().getPlayer(player), "Econ.Error");
                    }
                }
            }
        }


        Location targ = sign.getTPLocation();
        if (!BlockUtil.safeLocation(targ)) {

            while (!BlockUtil.safeLocation(targ)) {

                for (int i = 0; i < 3 && !BlockUtil.safeLocation(targ); i++) {

                    for (int j = 0; j < 3 && !BlockUtil.safeLocation(targ); j++) {
                        targ.setX(targ.getBlockX() + 1);
                    }

                    targ.setZ(targ.getBlockZ() + 1);
                }

                targ.setY(targ.getBlockY() + 1);
            }
        }

        World targworld = sign.getTPLocation().getWorld();
        Chunk targChunk = targworld.getChunkAt(targ);
        if (!targChunk.isLoaded()) {
            targChunk.load();
        }


        plugin.getServer().getPlayer(player).getWorld().playSound(plugin.getServer().getPlayer(player).getLocation(),
                Sound.ENTITY_CHICKEN_EGG, 15, 1);
        plugin.getServer().getPlayer(player).getWorld().playEffect(plugin.getServer().getPlayer(player).getLocation(),
                Effect.SMOKE, 1);

        plugin.getServer().getPlayer(player).teleport(targ, PlayerTeleportEvent.TeleportCause.PLUGIN);
        plugin.getIOManger().send(plugin.getServer().getPlayer(player),
                plugin.getIOManger().translate("Travel.Success").replaceAll("%sign", sign.getName()));

    }

}
