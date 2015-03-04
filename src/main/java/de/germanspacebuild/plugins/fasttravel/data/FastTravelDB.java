package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.util.DBType;

import java.io.File;
import java.util.*;

/**
 * Created by oneill011990 on 03.03.2015.
 */
public class FastTravelDB {

    private static FastTravel plugin;

    private static Map<String, FastTravelSign> signs;

    private static File saveFile;

    public static void init(FastTravel plugin, String saveFile, boolean load) {
        FastTravelDB.plugin = plugin;
        FastTravelDB.saveFile = new File(plugin.getDataDir(), saveFile);

        signs = new HashMap<>();

        if (load)
            load();
    }

    public static void init(FastTravel plugin, boolean load){
        FastTravelDB.plugin = plugin;

        signs = new HashMap<>();

        if (load)
            load();
    }

    public static void load(){

        if (plugin.getDBHandler() == DBType.File){
            FileDBHandler.load(saveFile);
        }

    }

    public static void save(){

        if (plugin.getDBHandler() == DBType.File){
            FileDBHandler.save(saveFile);
        }

    }

    public static void removeSign(String name) {
        if (signs.containsKey(name.toLowerCase()))
            signs.remove(name.toLowerCase());

        if (plugin.getDBHandler() == DBType.File){
            save();
        }
    }

    public static FastTravelSign getSign(String name) {
        if (signs.containsKey(name.toLowerCase()))
            return signs.get(name.toLowerCase());
        else
            return null;
    }

    public static List<FastTravelSign> getSignsFor(UUID player) {
        List<FastTravelSign> playerSigns = new ArrayList<>();
        for (FastTravelSign sign : signs.values()) {
            if (sign.isAutomatic() || sign.foundBy(player))
                playerSigns.add(sign);
        }
        Collections.sort(playerSigns);
        return playerSigns;
    }

    public static List<FastTravelSign> getAllSigns() {
        List<FastTravelSign> allSigns = new ArrayList<>();
        allSigns.addAll(signs.values());
        Collections.sort(allSigns);
        return allSigns;
    }

    public static Map<String, FastTravelSign> getSignMap(){
        return signs;
    }

    public static void addSign(FastTravelSign sign) {
        if (!signs.containsKey(sign.getName().toLowerCase()))
            signs.put(sign.getName().toLowerCase(), sign);
        save();
    }
}