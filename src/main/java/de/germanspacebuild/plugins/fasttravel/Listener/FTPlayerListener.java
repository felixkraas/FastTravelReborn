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

package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelFoundEvent;
import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;

/**
 * Created by oneill011990 on 04.03.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class FTPlayerListener implements Listener {

    private FastTravel plugin;

    public FTPlayerListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!Arrays.asList(BlockUtil.signBlocks).contains(event.getClickedBlock().getType())) {
            return;
        } else if (!event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "use")) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Perms.Not");
            return;
        } else if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        } else if (!FastTravelUtil.isFTSign(event.getClickedBlock())) {
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (FastTravelDB.getSignsFor(event.getPlayer().getUniqueId()).contains(FastTravelDB.getSign(sign.getLine(1)))) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Found.Already".replaceAll("%sign",
                    sign.getLine(1)));
        } else {
            plugin.getServer().getPluginManager().callEvent(new FastTravelFoundEvent(event.getPlayer(),
                    FastTravelDB.getSign(sign.getLine(1))));
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractWand(PlayerInteractEvent event) {
        if (!Arrays.asList(BlockUtil.signBlocks).contains(event.getClickedBlock().getType())) {
        } else if (event.getItem() == null) {
        } else if (event.getItem().getType() != Material.BONE) {
        } else if (!FastTravelUtil.isMoveWand(event.getItem())) {
        } else {
            if (!(event.getClickedBlock().getState() instanceof Sign)) {
                return;
            }
            Sign signBlock = ((Sign) event.getClickedBlock().getState());
            for (String line : signBlock.getLines()) {
                if (!line.isEmpty()) {
                    plugin.getIOManger().sendTranslation(event.getPlayer(), "Command.Move.SignNotEmpty");
                    return;
                }
            }
            String signName = event.getItem().getItemMeta().getLore().get(0).replace("Sign: " + ChatColor.GOLD, "");
            FastTravelSign sign = FastTravelDB.getSign(signName);
            if (sign == null) {
                plugin.getIOManger().send(event.getPlayer(), plugin.getIOManger().translate("Sign.Exists.Not").
                        replaceAll("%sign", signName));
                return;
            }
            FastTravelUtil.formatSign(signBlock, sign.getName());
            Location loc = sign.getSignLocation();
            loc.getBlock().setType(Material.AIR);
            if (sign.getSignLocation() == sign.getTPLocation()) {
                sign.setTPLocation(signBlock.getLocation());
            }
            sign.setSignLocation(signBlock.getLocation());
            event.getPlayer().getInventory().remove(event.getItem());
            plugin.getIOManger().send(event.getPlayer(), plugin.getIOManger().translate("Command.Move.Success")
                    .replace("%sign", sign.getName()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "update") && FastTravel.getInstance().needUpdate) {
            plugin.getIOManger().send(event.getPlayer(), plugin.getIOManger().translate(
                    "Plugin.Update.Player").replace("%old", plugin.getDescription().getVersion())
                    .replaceAll("%new", plugin.getUpdateChecker().getVersion()).replaceAll("%link",
                            plugin.getUpdateChecker().getLink()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerItemDropEvent(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() != Material.BONE) {
            return;
        }
        if (FastTravelUtil.isMoveWand(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

}
