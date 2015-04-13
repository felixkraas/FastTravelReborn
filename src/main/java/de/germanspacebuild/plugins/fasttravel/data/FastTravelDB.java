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

        if (plugin.getDBHandler() == DBType.File) {
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