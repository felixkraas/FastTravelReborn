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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetpointCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public SetpointCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        if (!sender.hasPermission(FastTravel.PERMS_BASE + "setpoint")) {
            io.sendTranslation(sender, "Perms.Not");
            return false;
        }
        if (args.length == 0) {
            io.sendTranslation(sender, "Command.NoSign");
            return true;
        }

        FastTravelSign sign = FastTravelDB.getSign(args[0]);
        if (sign == null) {
            io.send(sender, io.translate("Sign.ExistsNot").replaceAll("%sign", args[0]));
        } else if (args.length == 1) {
            sign.setTPLocation(((Player) sender).getLocation());
            io.send(sender, io.translate("Command.SetPoint.Set").replaceAll("%sign", sign.getName()));
        } else if (args.length == 2 && args[1].equals("clear")) {
            sign.setTPLocation(sign.getSignLocation().clone());
            io.send(sender, io.translate("Command.SetPoint.Cleared").replaceAll("[%sign]", sign.getName()));
        }

        FastTravelDB.save();

        return true;
    }

}