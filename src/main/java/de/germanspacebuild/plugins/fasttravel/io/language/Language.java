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
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Language {

    private static List<Language> languages = new ArrayList<Language>();
    protected YamlConfiguration lang;
    protected File langFile;
    protected static Configuration config;
    protected static FastTravel plugin;

    public Language(Configuration config, FastTravel plugin) {
        languages = new ArrayList<Language>();
        this.config = config;
        this.plugin = plugin;
        createLanguageFile();
        updateLanguage();
    }

    public abstract void createLanguageFile();

    public abstract void updateLanguage();

    public String translate(String key) {
        return lang.getString(key);
    }

    public File getFile() {
        return langFile;
    }

    public YamlConfiguration getKeys() {
        return lang;
    }

    public String getName() {
        return "LangEN";
    }

    public void set(String key, String value) {
        if (lang.get(key) == null) lang.set(key, value);
    }

    public static List<Language> getLanguages() {
        return languages;
    }

    public static void addKey(String langName, String key, String value) {
        Language lang = getLanguageByName(langName);
        if (lang != null) {
            lang.set(key, value);
            try {
                lang.getKeys().save(lang.getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //lang.save();
            languages.set(languages.indexOf(lang), lang);
        }
    }

    public static Language getLanguageByName(String name) {
        for (Language lang : languages) {
            if (lang.getName().equalsIgnoreCase(name)) return lang;
        }
        return null;
    }

    public void save() {
        try {
            if (lang == null) System.out.println("yml = null");
            if (langFile == null) System.out.println("file = null");
            lang.save(langFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLanguage(Language lang) {
        languages.add(lang);
    }
}
