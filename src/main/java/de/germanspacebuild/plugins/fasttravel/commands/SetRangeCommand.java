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

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by oneill011990 on 03.09.2014.
 */
public class SetRangeCommand implements CommandExecutor {

    private static int range;

    private FastTravel plugin;
    private IOManager io;

    public SetRangeCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)){
            return false;
        } else if (!sender.hasPermission(FastTravel.PERMS_BASE + "range")){
            io.sendTranslation(sender, "Perms.Not");
            return false;
        } else if (args.length == 0){
            io.sendTranslation(sender, "Command.InvalidArgs");
            return false;
        }

        FastTravelSign sign = FastTravelDB.getSign(args[0]);
        if (sign == null) {
            io.send(sender, io.translate("Sign.ExistsNot").replaceAll("%sign", args[0]));
        } else if (args.length == 2){
            try {
                range = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                e.printStackTrace();
            }
            sign.setRange(range);
            io.send(sender, io.translate("Command.Range.Set").replaceAll("%sign", sign.getName()).replaceAll("%range",
                    String.valueOf(range)));

            return true;
        } else if (args.length == 1) {
            io.send(sender, io.translate("Command.Range.Get").replaceAll("%sign", args[0]).replaceAll("%range",
                    String.valueOf(sign.getRange())));
            return true;
        }

        return true;
    }

}
