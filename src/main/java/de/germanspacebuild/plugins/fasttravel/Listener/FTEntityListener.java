package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FTEntityListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onEntityExplode(EntityExplodeEvent event){
        if (event.blockList().isEmpty()){
            return;
        }

        for (FastTravelSign sign : FastTravelDB.getAllSigns()){
            if (event.blockList().contains(sign.getSignLocation().getBlock())){
                List<Block> signBlocks = new ArrayList<>();

                Location  blockBelow = sign.getSignLocation().getWorld().getBlockAt(sign.getSignLocation().getBlockX() - 1,
                        sign.getSignLocation().getBlockY() - 1, sign.getSignLocation().getBlockZ() - 1).getLocation();

                for (int i = 0; i <= 2; i++){
                    for (int j = 0; j <= 2; j++){
                        for (int k = 0; k <= 2; k++){
                            signBlocks.add(sign.getSignLocation().getWorld().getBlockAt(blockBelow.getBlockX() + i,
                                    blockBelow.getBlockY() + j, blockBelow.getBlockZ() + k));
                        }
                    }
                }
                event.blockList().removeAll(signBlocks);
            }
        }

    }

}

