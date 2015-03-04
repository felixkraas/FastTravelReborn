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

/**
 * Created by oneill011990 on 04.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class FTBlockListener implements Listener {

    private FastTravel plugin;

    public FTBlockListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (FastTravelUtil.isFTSign(event.getBlock().getWorld().getBlockAt(event.getBlock().getX(),
                event.getBlock().getY() - 1, event.getBlock().getZ()))) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.PlaceAbove");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        if (FastTravelUtil.isFTSign(event.getBlock()) && event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "break")) {
            event.setCancelled(true);
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Break");
        } else if (FastTravelUtil.isFTSign(event.getBlock()) && event.getPlayer().hasPermission(
                FastTravel.PERMS_BASE + "break")) {
            FastTravelDB.removeSign(((Sign) event.getBlock().getState()).getLine(1));
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Removed".replaceAll("%sign",
                    ((Sign) event.getBlock().getState()).getLine(1)));
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = false)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Block block = event.getBlock();
        if (FastTravelUtil.isFTSign(block)) {
            event.setCancelled(true);
        }
    }

}
