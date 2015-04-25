/*
 * FastTravelSigns - The Simple Exploration and RPG-Friendly Teleportation Plugin
 *
 * Copyright (c) 2011-2015 craftycreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
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

/**
 * Created by oneill011990 on 03.09.2014.
 */
public class RemoveCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public RemoveCommand(FastTravel plugin){
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission(FastTravel.PERMS_BASE + "remove")){
            io.sendTranslation(sender, "Perms.Not");
        } else if (args.length == 0 || args[0] == null || args[1] == null) {
            io.sendTranslation(sender, "Command.InvalidArgs");
        } else {
            String sign = args[0];
            String player = args[1];

            FastTravelSign signRaw = FastTravelDB.getSign(sign);

            if (signRaw == null){
                io.send(sender, io.translate("Sign.ExistsNot").replaceAll("%sign", sign));
            } else if (plugin.getServer().getPlayer(player) == null) {
                io.send(sender, io.translate("Player.NotFound").replaceAll("%player", player));

            } else if (signRaw.isAutomatic() || !signRaw.foundBy(plugin.getServer().getPlayer(player).getUniqueId())){
                io.send(sender, io.translate("Command.Remove.CanNot"));
            }

            io.send(sender, io.translate("Command.Remove").replaceAll("%sign", signRaw.getName()));

            signRaw.removePlayer(plugin.getServer().getPlayer(player).getUniqueId());
        }

        return false;
    }
}
