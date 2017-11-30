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

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 01.04.2017
 * for FastTravelReborn
 *
 * @author Felix
 */
public class DebugCommand implements CommandExecutor {

    private static boolean debungEnabled = false;
    private FastTravel plugin;
    private IOManager io;

    public DebugCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission(FastTravel.PERMS_BASE + "debug")) {
            io.sendTranslation(sender, "Perms.Not");
            return true;
        }

        debungEnabled = !debungEnabled;

        if (debungEnabled) {
            io.send(sender, "Activating Debug-Mode");
            FastTravelDB.save();
            printPluginInfo(sender);
            printSignInfo(sender);
            showAllRanges();
            showAllTPs();
        } else if (!debungEnabled) {
            io.send(sender, "Deactivating Debug-Mode");
            plugin.getEffectManager().cancel(true);
        }

        return true;
    }

    private void printPluginInfo(CommandSender sender) {
        List<String> infos = new ArrayList<>();
        infos.add("Version: " + plugin.getDescription().getVersion());
        infos.add("Language: " + plugin.getConfig().get("IO.Language"));
        infos.add("Database: " + plugin.getConfig().get("Plugin.Database"));
        for (String info : infos) {
            io.send(sender, info);
        }
    }

    private void printSignInfo(CommandSender sender) {
        List<String> infos = new ArrayList<>();
        for (FastTravelSign sign : FastTravelDB.getAllSigns()) {
            String tmp = sign.toString();
            tmp = tmp + "\n\tLocation: " + sign.getSignLocation();
            tmp += "\n\tRadius: " + sign.getRange();
            tmp += "\n\tPrice: " + sign.getPrice();
            tmp += "\n\tAutomatic: " + sign.isAutomatic();
            infos.add(tmp);
        }
        io.send(sender, "Signs:");
        for (String info : infos) {
            io.send(sender, info);
        }
    }

    private void showAllRanges() {
        for (FastTravelSign sign : FastTravelDB.getAllSigns()) {
            SphereEffect effect = new SphereEffect(plugin.getEffectManager());
            effect.setDynamicOrigin(new DynamicLocation(sign.getSignLocation()));
            effect.radius = sign.getRange();
            effect.particle = ParticleEffect.FIREWORKS_SPARK;
            effect.color = Color.GREEN;
            effect.iterations = 100000 * 20;
            effect.particles = 2500;
            effect.start();
        }
    }

    private void showAllTPs() {
        for (FastTravelSign sign : FastTravelDB.getAllSigns()) {
            Location tpLoc = sign.getTPLocation().clone();
            tpLoc = tpLoc.getBlock().getLocation();
            LineEffect effect = new LineEffect(plugin.getEffectManager());
            effect.setDynamicOrigin(new DynamicLocation(tpLoc.subtract(0, tpLoc.getY(), 0)));
            effect.setDynamicTarget(new DynamicLocation(tpLoc.add(0, 255, 0)));
            effect.iterations = 100000 * 20;
            effect.particles = 250;
            effect.offset = new Vector(10, 0, 10);
            effect.particle = ParticleEffect.REDSTONE;
            effect.start();
        }
    }

}
