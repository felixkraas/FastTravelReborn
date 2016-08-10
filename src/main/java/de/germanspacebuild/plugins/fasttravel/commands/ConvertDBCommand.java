/*
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package de.germanspacebuild.plugins.fasttravel.commands;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.DBType;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Felix on 01.04.2016.
 *
 * @author Felix
 */
public class ConvertDBCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public ConvertDBCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission(FastTravel.PERMS_BASE + "convert")) {
            io.send(sender, io.translate("Perms.Not"));
            return true;
        }

        String newDBType = args[0];

        switch (newDBType.toLowerCase()) {
            case "file":
                DBType.setDBType(DBType.File);
                io.send(sender, io.translate("Command.Convert.File"));
                FastTravelDB.init(plugin, "signs.yml", false);
                plugin.getConfig().set("Plugin.Database", "File");
                break;
            case "sqlite":
                DBType.setDBType(DBType.SQLite);
                plugin.getConfig().set("Plugin.Database", "SQLite");
                io.send(sender, io.translate("Command.Convert.SQLite"));
                break;
            case "mysql":
                DBType.setDBType(DBType.MySQL);
                plugin.getConfig().set("Plugin.Database", "MySQL");
                io.send(sender, io.translate("Command.Convert.MySQL"));
            default:
                io.send(sender, io.translate("Command.Convert.Invalid"));
        }

        plugin.saveConfig();

        //Reloading database
        FastTravelDB.save();
        FastTravelDB.load();

        return true;
    }
}
