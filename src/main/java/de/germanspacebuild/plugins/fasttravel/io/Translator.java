package de.germanspacebuild.plugins.fasttravel.io;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.io.language.Language;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class Translator {

    private static FastTravel plugin;
    private static HashMap<String, YamlConfiguration> languages;
    private static Configuration config;
    private static String language;
    private static IOManager io;

    public Translator(FastTravel plugin) {
        this.plugin = plugin;
    }

    public void init() {
        config = plugin.getConfig();
        io = plugin.getIOManger();
        languages = new HashMap<String, YamlConfiguration>();
        language = config.getString("IO.Language", "en");
        List<Language> langs = Language.getLanguages();
        List<File> loadedFiles = new ArrayList<File>();
        for (int i = 0; i < langs.size(); i++) {
            langs.get(i).createLanguageFile();
            langs.get(i).updateLanguage();
            loadedFiles.add(langs.get(i).getFile());
            languages.put(langs.get(i).getName(), langs.get(i).getKeys());
        }
        File[] langFiles = new File(plugin.getInstance().getDataFolder(), "lang").listFiles();
        for (int i = 0; i < langFiles.length; i++) {
            if (loadedFiles.indexOf(langFiles[i]) == -1)  loadLanguageFile(langFiles[i]);
        }
        if (languages.get(language) == null) {
            io.sendConsole("Language " + language + " doesn't seem to exist. Forcing to en!");
            language = "en";
            config.set("IO.Language", "en");
        }
    }

    private void loadLanguageFile(File languageFile) {
        YamlConfiguration lang = new YamlConfiguration();
        try {
            lang.load(languageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        languages.put(languageFile.getName().split(".lang")[0], lang);
    }

    public String getLanguage() {
        return language;
    }

    public HashMap<String, YamlConfiguration> getLanguages() {
        return languages;
    }

    public String getKey(String key) {
        if (languages.get(language) == null) return "Language " + language + " doesn't seem to exist. Please change it in the config.yml!";
        if (languages.get(language).getString(key) != null) return languages.get(language).getString(key);
        else return "Key " + key + " couldn't be found. Please check your lang Files!";
    }
}
