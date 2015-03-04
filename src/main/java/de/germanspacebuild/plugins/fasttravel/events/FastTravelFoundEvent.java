package de.germanspacebuild.plugins.fasttravel.events;

import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FastTravelFoundEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    Player player;
    FastTravelSign sign;

    public FastTravelFoundEvent(Player player, FastTravelSign sign){
        this.player = player;
        this.sign = sign;
    }

    public Player getPlayer() {
        return player;
    }

    public FastTravelSign getSign() {
        return sign;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
