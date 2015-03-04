package de.germanspacebuild.plugins.fasttravel.task;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneill011990 on 04.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class CheckPlayerTask implements Runnable {

    private FastTravel plugin;

    public CheckPlayerTask(FastTravel plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        List<Player> players = new ArrayList<>();
        players.addAll(plugin.getServer().getOnlinePlayers());

        for (Player player : players) {
            List<FastTravelSign> signs = FastTravelDB.getSignsFor(player.getUniqueId());
            for (FastTravelSign sign : signs) {
                if (sign.getSignLocation().distance(player.getLocation()) <= sign.getRange()){
                    sign.addPlayer(player.getUniqueId());
                }
            }
        }

    }
}
