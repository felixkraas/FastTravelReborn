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

package de.germanspacebuild.plugins.fasttravel.io.language;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class en extends Language {

    private FastTravel plugin;

    public en(FastTravel plugin) {
        super(plugin.getConfig(), plugin);
        this.plugin = plugin;
    }

    @Override
    public void createLanguageFile() {
        langFile = new File(plugin.getLangDir() + "en.lang");
        if (!langFile.exists()){
            try {
                langFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateLanguage() {

        lang = new YamlConfiguration();
        try {
            lang.load(langFile);

            //Plugin
            set("Plugin.Update", "You are using &ev%old &fof FastTravelSigns. Latest version is &ev%new&f." +
                    " Get it here: &3%link");

            //Permissions
            set("Perms.Not", "&4You don't have permission to do that. Contact an admin if you think this is an error.");

            //Sign
            set("Sign.PlaceAbove", "You can't place blocks above FastTravelSigns.");
            set("Sign.PlaceAbove.Is", "You can't place FastTravelSigns directly below blocks.");
            set("Sign.Break", "You can't break FastTravelSigns.");
            set("Sign.Removed", "You have removed point &b%sign&f.");
            set("Sign.Found", "You found &b%sign&f. now you can travel here.");
            set("Sign.FoundAlready", "You already found &b%sign&f.");
            set("Sign.InvalidName", "You may not use anything but letters in sign names.");
            set("Sign.Exists", "&b%sign &falready exists.");
            set("Sign.Created", "You successfully created FastTravelSign &b%sign&f.");

            //Economy
            set("Econ.MoneyLess", "You don't have the money to travel. It would cost &2%cost&f.");
            set("Econ.Charged", "You have been charged &2%cost&f.");
            set("Econ.Error", "Something is broken with economy. Please contact admin or plugin author." +
                    " You might travel anyway");

            //Travel
            set("Travel.Success", "You traveled to &b%sign&f.");



        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
