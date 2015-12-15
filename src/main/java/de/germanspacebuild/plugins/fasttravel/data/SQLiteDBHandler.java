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

package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oneill011990 on 30.07.2015.
 */
public class SQLiteDBHandler {

    private static Database db;
    private static FastTravel plugin;
    private static List<String> filePlayers;
    private static int entries;

    static {
        db = Database.getDatabaseBySystem(DBType.SQLite);
        plugin = FastTravel.getInstance();
        filePlayers = new ArrayList<>();
    }

    public static void load() {
        db.init();

        try {
           /* entries = db.query("COUNT (*) FROM FastTravelSigns;").getInt(1);

            if (entries == 0){
                plugin.getLogger().info("No signs found in the database");
                return;
            } else {
                plugin.getLogger().info(entries + " FastTravelSigns found in the database. Starting to load them.");
            }*/

            ResultSet rs = db.query("SELECT * From FastTravelSigns");

            while (rs.next()) {
                String name = rs.getString(1);
                UUID creator = UUID.fromString(rs.getString(2));
                World signloc_World = plugin.getServer().getWorld(rs.getString(3));
                int signloc_X = rs.getInt(4);
                int signloc_Y = rs.getInt(5);
                int signloc_Z = rs.getInt(6);
                float signloc_Yaw = rs.getFloat(7);
                World tploc_World = plugin.getServer().getWorld(rs.getString(8));
                int tploc_X = rs.getInt(9);
                int tploc_Y = rs.getInt(10);
                int tploc_Z = rs.getInt(11);
                float tploc_Yaw = rs.getFloat(12);
                boolean automatic = db.parseBoolean(rs.getInt(13));
                float price = rs.getFloat(14);
                int range = rs.getInt(15);

                List<UUID> players = null;

                players = db.getList(rs.getBytes(16));

                if (!players.contains(creator)){
                    players.add(creator);
                }

                Location tpLoc = null;
                Location signLoc = null;

                signLoc = new Location(signloc_World, signloc_X, signloc_Y, signloc_Z);
                signLoc.setYaw(signloc_Yaw);

                tpLoc = new Location(tploc_World, tploc_X, tploc_Y, tploc_Z);
                tpLoc.setYaw(tploc_Yaw);

                FastTravelSign sign = null;

                sign = new FastTravelSign(1, name, creator, price, signLoc, tpLoc, automatic, range, players);

                if (plugin.getConfig().getBoolean("DevMode")){
                    plugin.getLogger().info("Loaded sign: " + sign.getName());
                }

                FastTravelDB.addSign(sign);

            }

            rs.close();
            plugin.getLogger().info("Loaded " + FastTravelDB.getAllSigns().size() + " FastTravelSigns from SQLite" +
                    " database.");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() throws SQLException {
        for (String signName : FastTravelDB.getSignMap().keySet()) {

            FastTravelSign sign = FastTravelDB.getSign(signName);
            if (!db.tableContains("name", signName)){
                PreparedStatement preparedStatement = db.dbConn.prepareStatement(
                        "INSERT INTO FastTravelSigns VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
                );

                //Basic Information
                preparedStatement.setString(1, sign.getName());
                preparedStatement.setString(2, sign.getCreator().toString());
                //Sign Location
                preparedStatement.setString(3, sign.getSignLocation().getWorld().getName());
                preparedStatement.setInt(4, sign.getSignLocation().getBlockX());
                preparedStatement.setInt(5, sign.getSignLocation().getBlockY());
                preparedStatement.setInt(6, sign.getSignLocation().getBlockZ());
                preparedStatement.setFloat(7, sign.getSignLocation().getYaw());
                //TP Location
                preparedStatement.setString(8, sign.getTPLocation().getWorld().getName());
                preparedStatement.setInt(9, sign.getTPLocation().getBlockX());
                preparedStatement.setInt(10, sign.getTPLocation().getBlockY());
                preparedStatement.setInt(11, sign.getTPLocation().getBlockZ());
                preparedStatement.setFloat(12, sign.getTPLocation().getYaw());
                //more Information
                preparedStatement.setBoolean(13, sign.isAutomatic());
                preparedStatement.setDouble(14, sign.getPrice());
                preparedStatement.setInt(15, sign.getRange());
                preparedStatement.setBlob(16, new SerialBlob(db.updateList(sign.getPlayers())));

                preparedStatement.executeUpdate();

            }

        }
    }

}
