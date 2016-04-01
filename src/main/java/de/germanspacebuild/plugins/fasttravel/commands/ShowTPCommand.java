/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
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

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.LineEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

/**
 * Created by oneill011990 on 03.06.2016.
 */
public class ShowTPCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public ShowTPCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission(FastTravel.PERMS_BASE + "show")) {
            io.sendTranslation(sender, "Perms.Not");
            return false;
        }

        if (args.length == 0) {
            io.sendTranslation(sender, "Command.InvalidArgs");
            return true;
        }

        FastTravelSign sign = FastTravelDB.getSign(args[0]);

        if (sign == null) {
            io.send(sender, io.translate("Sign.ExistsNot").replaceAll("%sign", args[0]));
            return false;
        }
        Location tpLoc = sign.getTPLocation().clone();
        EffectManager em = FastTravel.getEffectManager();
        LineEffect effect = new LineEffect(em);
        effect.setDynamicOrigin(new DynamicLocation(tpLoc.subtract(0, tpLoc.getY(), 0)));
        effect.setDynamicTarget(new DynamicLocation(tpLoc.add(0, 255, 0)));
        effect.iterations = 10 * 20;
        effect.particles = 250;
        effect.offset = new Vector(1, 0, 1);
        effect.particle = ParticleEffect.REDSTONE;
        effect.start();

        return true;
    }
}
