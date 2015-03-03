package de.germanspacebuild.plugins.fasttravel.io.language;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
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
        return "en";
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

    public static void addLanguage(Language lang){
        languages.add(lang);
    }
}
