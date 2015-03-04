package de.germanspacebuild.plugins.fasttravel.events;

import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FastTravelEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;

    Player player;
    FastTravelSign sign;
    int price;

    public FastTravelEvent(Player player, FastTravelSign sign){
        this.player = player;
        this.sign = sign;
    }

    public FastTravelEvent(Player player, FastTravelSign sign, int price) {
        this.player = player;
        this.sign = sign;
        this.price = price;
    }

    public Player getPlayer(){
        return player;
    }

    public FastTravelSign getSign(){
        return  sign;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
