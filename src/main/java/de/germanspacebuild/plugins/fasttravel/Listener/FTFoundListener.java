package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelFoundEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FTFoundListener implements Listener {

    private FastTravel plugin;

    public FTFoundListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onSignFound(FastTravelFoundEvent event) {
        event.getSign().addPlayer(event.getPlayer().getUniqueId());
        plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Found".replaceAll("%sign",
                event.getSign().getName()));
    }



}
