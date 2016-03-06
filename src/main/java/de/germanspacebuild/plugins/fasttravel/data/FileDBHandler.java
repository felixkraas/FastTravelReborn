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
import de.germanspacebuild.plugins.fasttravel.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oneill011990 on 03.03.2015.
 */
public class FileDBHandler {

    private static FastTravel plugin;

    private static List<String> filePlayers;

    private static File saveFile;

    static {
        plugin = FastTravel.getInstance();
        filePlayers = new ArrayList<>();
    }

    public static void load(File saveFile) {

        FileDBHandler.saveFile = saveFile;

        YamlConfiguration signYAML = new YamlConfiguration();

        try {
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            signYAML.load(saveFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for (String signName : signYAML.getKeys(false)) {
            //Creator
            UUID creator = UUID.fromString(signYAML.getString(signName + ".creator"));
            //Range
            int range = signYAML.getInt(signName + ".range");
            //Sign World
            World locWorld = plugin.getServer().getWorld(
                    signYAML.getString(signName + ".signloc.world"));
            //Destination World
            World tpLocWorld = plugin.getServer().getWorld(
                    signYAML.getString(signName + ".tploc.world"));
            //Player UUDIs
            filePlayers = signYAML.getStringList(signName + ".players");

            //Price
            double price = signYAML.getDouble(signName + ".price", 0.0);

            //Sign Location
            Location location = new Location(locWorld, signYAML.getDouble(signName + ".signloc.x"),
                    signYAML.getDouble(signName + ".signloc.y"), signYAML.getDouble(signName
                    + ".signloc.z"));
            location.setYaw((float) signYAML.getDouble(signName + ".signloc.yaw"));

            //Destination Location
            Location tploc = new Location(tpLocWorld, signYAML.getDouble(signName + ".tploc.x"),
                    signYAML.getDouble(signName + ".tploc.y"), signYAML.getDouble(signName
                    + ".tploc.z"));
            tploc.setYaw((float) signYAML.getDouble(signName + ".tploc.yaw"));

            //Automatic
            boolean automatic = signYAML.getBoolean(signName + ".automatic", false);

            if (!checkMissing(signName, creator, locWorld, tpLocWorld)) {
                continue;
            }

            FastTravelDB.addSign(new FastTravelSign(signName, creator, price, location, tploc,
                    automatic, range, UUIDUtil.stringToUUID(filePlayers)));
            filePlayers.clear();
        }

        plugin.getLogger().info("Loaded " + FastTravelDB.getAllSigns().size() + " FastTravelSigns from File.");
        save();
    }

    public static void save() {
        save(saveFile);
    }

    public static void save(File saveFile) {
        YamlConfiguration signYAML = new YamlConfiguration();
        for (String signName : FastTravelDB.getSignMap().keySet()) {
            FastTravelSign sign = FastTravelDB.getSignMap().get(signName);
            signName = sign.getName();
            signYAML.set(signName + ".creator", sign.getCreator().toString());

            signYAML.set(signName + ".signloc.world", sign.getSignLocation().getWorld().getName());
            signYAML.set(signName + ".signloc.x", sign.getSignLocation().getX());
            signYAML.set(signName + ".signloc.y", sign.getSignLocation().getY());
            signYAML.set(signName + ".signloc.z", sign.getSignLocation().getZ());
            signYAML.set(signName + ".signloc.yaw", (double) sign.getSignLocation().getYaw());

            signYAML.set(signName + ".tploc.world", sign.getTPLocation().getWorld().getName());
            signYAML.set(signName + ".tploc.x", sign.getTPLocation().getX());
            signYAML.set(signName + ".tploc.y", sign.getTPLocation().getY());
            signYAML.set(signName + ".tploc.z", sign.getTPLocation().getZ());
            signYAML.set(signName + ".tploc.yaw", (double) sign.getTPLocation().getYaw());

            signYAML.set(signName + ".automatic", sign.isAutomatic());

            signYAML.set(signName + ".players", UUIDUtil.uuidToString(sign.getPlayers()));

            signYAML.set(signName + ".price", sign.getPrice());
            signYAML.set(signName + ".range", sign.getRange());
        }

        try {
            signYAML.save(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkMissing(String signName, UUID creator, World locWorld, World tplocWorld) {

        if (Bukkit.getServer().getOfflinePlayer(creator) == null) {
            plugin.getLogger()
                    .warning("Could not load sign '" + signName + "' - missing creator!");
            return false;
        } else if (locWorld == null) {
            plugin.getLogger()
                    .warning("Could not load sign '" + signName + "' - missing world sign is in!");
            return false;
        } else if (tplocWorld == null) {
            plugin.getLogger()
                    .warning("Could not load sign '" + signName + "' - missing world to travel to!");
            return false;
        }
        return true;

    }
}
