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

package de.germanspacebuild.plugins.fasttravel.thirdparty;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import java.util.List;

/**
 * Created by oneill011990 on 03.05.2016.
 */
public class DynmapHook extends PluginHook {

    DynmapAPI api;
    MarkerSet markers;

    public DynmapHook(FastTravel plugin, Plugin hook) {
        super(plugin, hook);
    }

    @Override
    public void init() {
        api = (DynmapAPI) hook;
        markers = api.getMarkerAPI().createMarkerSet("fasttravel", "FastTravelSigns", null, false);
    }

    public void processSigns() {
        List<FastTravelSign> signs = FastTravelDB.getAllSigns();

        for (FastTravelSign sign : signs) {
            if (sign.hasMarker()) {
                Location signLoc = sign.getSignLocation();
                markers.createMarker(sign.getName().toLowerCase(), sign.getName(), signLoc.getWorld().getName(),
                        signLoc.getX(), signLoc.getY(), signLoc.getZ(), api.getMarkerAPI().getMarkerIcon(MarkerIcon.SIGN),
                        false);
            }
        }
    }
}
