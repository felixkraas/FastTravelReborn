/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2015 CraftyCreeper, minebot.net, oneill011990
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
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelFoundEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
            if (!player.hasPermission(FastTravel.PERMS_BASE + "use")) {
                return;
            }
            List<FastTravelSign> signs = FastTravelDB.getSignsFor(player.getUniqueId());
            for (FastTravelSign sign : signs) {
                if (player.getWorld() != sign.getSignLocation().getWorld()) {
                    return;
                }
                if (sign.getSignLocation().distance(player.getLocation()) <= sign.getRange()){
                    plugin.getServer().getPluginManager().callEvent(new FastTravelFoundEvent(player, sign));
                    try {
                        wait((long) 1000.0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
