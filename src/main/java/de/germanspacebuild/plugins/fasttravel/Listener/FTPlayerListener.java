package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FTPlayerListener implements Listener {

    private FastTravel plugin;

    public FTPlayerListener(FastTravel plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!Arrays.asList(BlockUtil.signBlocks).contains(event.getClickedBlock().getType())){
            return;
        } else if (event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "travel")) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Perms.Not");
            return;
        } else if (event.getAction() != Action.RIGHT_CLICK_AIR || event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        } else if (!FastTravelUtil.isFTSign(event.getClickedBlock())){
            return;
        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (FastTravelDB.getSignsFor(event.getPlayer().getUniqueId()).contains(FastTravelDB.getSign(sign.getLine(1)))){
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.FoundAlready".replaceAll("%sign",
                    sign.getLine(1)));
            return;
        } else {
            FastTravelDB.getSign(sign.getLine(1)).addPlayer(event.getPlayer().getUniqueId());
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Found".replaceAll("%sign", sign.getLine(1)));
        }

    }

}
