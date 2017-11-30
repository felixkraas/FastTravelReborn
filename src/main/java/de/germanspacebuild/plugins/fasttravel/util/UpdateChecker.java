/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.util;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    private FastTravel plugin;
    private URL url;

    private String version;
    private String link;

    private boolean updateFound = false;

    public UpdateChecker(FastTravel plugin, String projectID) {
        this.plugin = plugin;

        try {
            this.url = new URL("https://api.curseforge.com/servermods/files?projectIds=" + projectID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void checkUpdate() {
        try {
            URLConnection connection = url.openConnection();

            connection.addRequestProperty("User-Agent", "CurseForge project updatechecker");

            System.out.println(connection.getURL());
            System.out.println(connection.toString());
            System.out.println(connection.getRequestProperties());

            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String response = reader.readLine();
            JSONArray array = (JSONArray) JSONValue.parse(response);

            if (array.size() > 0) {
                JSONObject latest = (JSONObject) array.get(array.size() - 1);

                if (!((String) latest.get("releaseType")).equalsIgnoreCase("release")) {
                    updateFound = false;
                    return;
                }

                this.version = ((String) latest.get("name")).replaceAll("[^0-9.]", "");
                this.link = (String) latest.get("downloadUrl");

                int versionInt = Integer.parseInt(version.replaceAll("[^0-9]", ""));
                int currVersion = Integer.parseInt(plugin.getDescription().getVersion().replaceAll("[^0-9]", ""));

                while (versionInt < 100) {
                    versionInt = versionInt * 10;
                }

                while (currVersion < 100) {
                    currVersion = currVersion * 10;
                }

                updateFound = currVersion < versionInt;

            } else {
                plugin.getIOManger().sendConsole(plugin.getIOManger().translate("Plugin.Update.Failed"));
                version = "failed";
            }

        } catch (IOException | NumberFormatException e) {
            plugin.getIOManger().sendConsole(plugin.getIOManger().translate("Plugin.Update.Failed"));
            version = "failed";
            if (FastTravel.BETA == true) {
                e.printStackTrace();
            }
        }
    }


    public boolean updateFound() {
        return updateFound;
    }

    public String getVersion() {
        return version;
    }

    public String getLink() {
        return link;
    }
}
