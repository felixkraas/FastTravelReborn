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

package de.germanspacebuild.plugins.fasttravel.io.language;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by oneill011990 on 04.03.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class LangEN extends Language {

    private FastTravel plugin;

    public LangEN(FastTravel plugin) {
        super(plugin.getConfig(), plugin);
        this.plugin = plugin;
        createLanguageFile();
        updateLanguage();
    }

    @Override
    public void createLanguageFile() {
        langFile = new File(plugin.getLangDir(), "en.lang");
        if (!langFile.exists()) {
            try {
                super.langFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "en";
    }

    @Override
    public void updateLanguage() {

        lang = new YamlConfiguration();
        try {
            lang.load(langFile);

            //Plugin
            set("Plugin.Update.Player", "You are using &ev%old &fof FastTravelSigns. Latest version is &ev%new&f." +
                    " Get it here: &3%link");
            set("Plugin.Update.Console.Yes", "You are using v%old of FastTravelSigns. Latest version is v%new." +
                    " Get it here: %link");
            set("Plugin.Update.Console.No", "You are using the latest version of FastTravelSigns.");
            set("Plugin.InvalidDB", "Database you set in config is invalid. Falling back to YAML-File.");

            //Permissions
            set("Perms.Not", "&4You don't have permission to do that. Contact an admin if you think this is an error.");

            //Player
            set("Player.Removed", "You have been removed from&b %sign&f.");
            set("Player.NotFound", "Could not find player&b %player &f.");
            set("Player.HasZero", "You haven't found any signs yet.");
            set("Player.List", "You can travel to the following FastTravelSigns:");

            //Sign
            set("Sign.PlaceAbove.Not", "You can't place blocks above FastTravelSigns.");
            set("Sign.PlaceAbove.Is", "You can't place FastTravelSigns directly below blocks.");
            set("Sign.CantBreak", "You can't break FastTravelSigns.");
            set("Sign.Removed", "You have removed point &b%sign&f.");
            set("Sign.Found.Is", "You found &b%sign&f. now you can travel here.");
            set("Sign.Found.Already", "You already found &b%sign&f.");
            set("Sign.Found.Not", "You have not found &b%sign &fyet.");
            set("Sign.InvalidName", "You may not use anything but letters in sign names.");
            set("Sign.Exists.Already", "&b%sign &falready exists.");
            set("Sign.Created", "You successfully created FastTravelSign &b%sign&f.");
            set("Sign.Exists.Already.Not", "Sign&b %sign &fdoes not exist.");

            //Economy
            set("Econ.Disabled", "Economy support is disabled in the config.");
            set("Econ.MoneyLess", "You don't have the money to travel. It would cost &2%cost&f.");
            set("Econ.Charged", "You have been charged &2%cost&f.");
            set("Econ.Error", "Something is broken with economy. Please contact admin and/or plugin author." +
                    " You might travel anyway.");

            //Travel
            set("Travel.Success", "You traveled to&b %sign &f.");

            //Commands
            set("Command.Player", "This command has to be executed by a player.");
            set("Command.NoSign", "You need to specify a sign.");
            set("Command.InvalidArgs", "Invalid arguments.");

            set("Command.Auto.On", "Sign&b %sign&f is now available for all players.");
            set("Command.Auto.Off", "Sign&b %sign&f is no longer available for all players.");

            set("Command.Price.No", "You must provide a price (0 to charge nothing).");
            set("Command.Price.Invalid", "Invalid price given; expecting a number.");
            set("Command.Price.Set", "Traveling to&b %sign&f now costs&a %price&f.");

            set("Command.Range.Set", "Setting range of&b %sign&f to&a %range&f.");
            set("Command.Range.Get", "Range of &b %sign&f is: &a %range&f.");

            set("Command.SetPoint.Set", "Set destination of %sign to you current location.");
            set("Command.SetPoint.Cleared", "Destination of %sign has been reset.");

            set("Command.Save.Saved.Player", "You have saved the database.");
            set("Command.Save.Saved.Console", "Database was saved.");

            set("Command.ShowRange.Zero", "Range of &b%sign&f is &a0.");

            set("Command.Menu.Disabled", "Inventory menus are disabled.");

            set("Command.Convert.Invalid", "Invalid type of database, no changes performed.");
            set("Command.Convert.File", "Changed type of database to YAML-File.");
            set("Command.Convert.SQLite", "Changed type of database to SQLite.");
            set("Command.Convert.MySQL", "Changed type of database to MySQL.");

            //Warmup
            set("Warmup.Objective", "Warumup");

            //Hooks
            set("Hooks.Dynmap", "Dynmap found, hooking into it.");

            //Database
            set("DB.Empty", "No signs found in the database.");
            set("DB.EntryCount", "%count FastTravelSigns found in the database. Starting to load them.");
            set("DB.LoadedSigns", "Loaded %count FastTravelSigns from SQLite database.");
            set("DB.Saving", "Saving database.");
            set("DB.Saved", "Database saved.");

            save();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }
}
