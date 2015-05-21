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
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand implements CommandExecutor {

	FastTravel plugin;
	IOManager io;

	public DeleteCommand(FastTravel plugin) {
		this.plugin = plugin;
		this.io = plugin.getIOManger();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			io.sendTranslation(sender, "Command.Player");
			return false;
		} else if (!sender.hasPermission(FastTravel.PERMS_BASE + "delete")) {
			io.sendTranslation(sender, "Perms.Not");
			return false;
		}



		if (args.length == 0) {
			io.sendTranslation(sender, "Command.NoSign");
		} else if (FastTravelDB.getSign(args[0]) == null) {
			io.send(sender, io.translate("Sign.ExistsNot").replaceAll("%sign", args[0]));
		} else {
			FastTravelSign sign = FastTravelDB.getSign(args[0]);
			Block block = sign.getSignLocation().getBlock();
			// Attempt to nuke the sign
			if (FastTravelUtil.isFTSign(block)) {
				block.setType(Material.AIR);
			}
			FastTravelDB.removeSign(args[0]);
			io.send(sender, io.translate("Sign.Removed").replaceAll("%sign", sign.getName()));
		}

		return true;
	}

}
