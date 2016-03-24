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
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class FTBlockListener implements Listener {

    private FastTravel plugin;

    public FTBlockListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (FastTravelUtil.isFTSign(event.getBlock().getWorld().getBlockAt(event.getBlock().getX(),
                event.getBlock().getY() - 1, event.getBlock().getZ()))) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.PlaceAbove");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (FastTravelUtil.isFTSign(event.getBlock()) && !event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "break")) {
            event.setCancelled(true);
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.CantBreak");
        } else if (FastTravelUtil.isFTSign(event.getBlock()) && event.getPlayer().hasPermission(
                FastTravel.PERMS_BASE + "break")) {
            FastTravelDB.removeSign(((Sign) event.getBlock().getState()).getLine(1));
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Removed".replaceAll("%sign",
                    ((Sign) event.getBlock().getState()).getLine(1)));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (FastTravelUtil.isFTSign(block)) {
            event.setCancelled(true);
        }
    }

}
