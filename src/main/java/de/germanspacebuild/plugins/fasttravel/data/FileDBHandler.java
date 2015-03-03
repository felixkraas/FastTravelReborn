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

    public static void load(File saveFile){

        FileDBHandler.saveFile = saveFile;

        YamlConfiguration signYAML = new YamlConfiguration();
        try {
            signYAML.load(saveFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        for (String signName : signYAML.getKeys(false)) {
            UUID creator = UUID.fromString(signYAML.getString(signName + ".creator"));
            int range = signYAML.getInt(signName + ".range");
            World locWorld = plugin.getServer().getWorld(
                    signYAML.getString(signName + ".signloc.world"));
            World tpLocWorld = plugin.getServer().getWorld(
                    signYAML.getString(signName + ".tploc.world"));
            filePlayers = signYAML.getStringList(signName + ".players");

            double price = signYAML.getDouble(signName + ".price", 0.0);

            Location location = new Location(locWorld, signYAML.getDouble(signName + ".signloc.x"),
                    signYAML.getDouble(signName + ".signloc.y"), signYAML.getDouble(signName
                    + ".signloc.z"));
            location.setYaw((float) signYAML.getDouble(signName + ".signloc.yaw"));

            Location tploc = new Location(tpLocWorld, signYAML.getDouble(signName + ".tploc.x"),
                    signYAML.getDouble(signName + ".tploc.y"), signYAML.getDouble(signName
                    + ".tploc.z"));
            tploc.setYaw((float) signYAML.getDouble(signName + ".tploc.yaw"));

            boolean automatic = signYAML.getBoolean(signName + ".automatic", false);

            if (!checkMissing(signName, creator, locWorld, tpLocWorld)){
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

    private static boolean checkMissing(String signName, UUID creator, World locWorld, World tplocWorld){

        if (Bukkit.getServer().getOfflinePlayer(creator) == null){
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
