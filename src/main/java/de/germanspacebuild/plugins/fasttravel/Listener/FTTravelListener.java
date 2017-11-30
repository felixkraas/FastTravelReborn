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
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.events.FastTravelEvent;
import de.germanspacebuild.plugins.fasttravel.task.TravelTask;
import de.germanspacebuild.plugins.fasttravel.task.WarmupPlayerTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by oneill011990 on 05.03.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class FTTravelListener implements Listener {

    private FastTravel plugin;
    private Map<UUID, Long> cooldowns;

    public FTTravelListener(FastTravel plugin) {
        cooldowns = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFastTravel(FastTravelEvent event) {

        Player player = event.getPlayer();
        FastTravelSign sign = event.getSign();

        if (cooldowns.containsKey(player.getUniqueId()) && !player.hasPermission(FastTravel.PERMS_BASE +
                "overrides.cooldown")) {
            long curTime = System.currentTimeMillis();
            int cooldown = plugin.getConfig().getInt("Travel.Cooldown");

            if (cooldown > 0) {
                if ((curTime - cooldowns.get(player.getUniqueId())) < (cooldown * 1000)) {
                    plugin.getIOManger().send(player, plugin.getIOManger().translate("Travel.Cooldown")
                            .replaceAll("%time", String.valueOf(plugin.getConfig().getInt("Travel.Cooldown"))));
                    return;
                }
            }
        }

        if (player.hasPermission(FastTravel.PERMS_BASE + "overrides.warmup") ||
                plugin.getConfig().getLong("Travel.Warmup") == 0) {
            plugin.getServer().getScheduler().runTask(plugin, new TravelTask(plugin, player.getUniqueId(), sign));
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        } else {
            plugin.getIOManger().sendTranslation(player, "Travel.WarmingUp".replaceAll("%time",
                    String.valueOf(plugin.getConfig().getLong("Travel.Warmup"))));
            plugin.getServer().getScheduler().runTask(plugin, new WarmupPlayerTask(plugin,
                    event.getPlayer().getUniqueId(), plugin.getConfig().getLong("Travel.Warmup")));
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new TravelTask(plugin,
                    player.getUniqueId(), sign), plugin.getConfig().getLong("Travel.Warmup") * 20);
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }

    }

}
