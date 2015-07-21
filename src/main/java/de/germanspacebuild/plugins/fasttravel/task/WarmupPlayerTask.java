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
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.lang.management.BufferPoolMXBean;
import java.util.UUID;

/**
 * Created by oneill011990 on 01.06.2015.
 */
public class WarmupPlayerTask extends Thread {

    private FastTravel plugin;
    private long warmup;
    private UUID player;
    private ScoreboardManager sm;
    private IOManager io;

    public WarmupPlayerTask(FastTravel plugin, UUID player, long warmup) {
        this.plugin = plugin;
        this.sm = plugin.getScoreboardManager();
        this.warmup = warmup;
        this.player = player;
        this.io = plugin.getIOManger();
    }

    @Override
    public void run() {
        long timestamp = System.currentTimeMillis();
        Scoreboard board = sm.getNewScoreboard();
        Team team = board.getTeam("warmup-" + this.player.toString());
        team.setDisplayName(ChatColor.DARK_PURPLE + plugin.getName());
        //TODO get the whole team shit to work!
        Player playerRaw = plugin.getServer().getPlayer(player);
        team.addPlayer(playerRaw);

        Objective timer = board.registerNewObjective("timer", "dummy");
        timer.setDisplaySlot(DisplaySlot.SIDEBAR);
        timer.setDisplayName(ChatColor.DARK_PURPLE + plugin.getName());

        Score timerCount = timer.getScore(ChatColor.GREEN + io.translate("Warmup.Objective"));
        timerCount.setScore((int) warmup);

        for (int i = (int) warmup; i > 0; i--) {
            while (!((System.currentTimeMillis() - timestamp) < 1000L)) {
                timerCount.setScore(i);
                timestamp = System.currentTimeMillis();
            }
        }

        board.clearSlot(DisplaySlot.SIDEBAR);
        team.removePlayer(playerRaw);


    }
}