package de.germanspacebuild.plugins.fasttravel.io.language;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by oneill011990 on 06.03.2016.
 *
 * @author oneill011990
 */
public class LangDE extends Language {


    private FastTravel plugin;

    public LangDE(FastTravel plugin) {
        super(plugin.getConfig(), plugin);
        this.plugin = plugin;
    }

    @Override
    public void createLanguageFile() {
        langFile = new File(FastTravel.getLangDir(), "de.lang");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getName() {
        return "de";
    }

    @Override
    public void updateLanguage() {

        lang = new YamlConfiguration();
        try {
            lang.load(langFile);

            //Plugin
            set("Plugin.Update.Player", "Du benutzt FastTravelSigns &ev%old &F. Die neuste Version ist &ev%new&f." +
                    " Hols dir hier: &3%link");
            set("Plugin.Update.Console.Yes", "Du benutzt FastTravelSigns &ev%old &F. Die neuste Version ist &ev%new&f." +
                    " Hols dir hier: &3%link");
            set("Plugin.Update.Console.No", "Du benutzt die neuste Version von FastTravelSigns.");
            set("Plugin.InvalidDB", "Die gewählte Datenbak ist ungültig. Nutze YAML stattdessen.");

            //Permissions
            set("Perms.Not", "&4Du hast nicht die Berechtigung dies zu tun. " +
                    "Benachrichtige einen Admin wenn du dies für einen Fehler hältst.");

            //Player
            set("Player.Removed", "Du wurdest vom Schild &b %sign&f entfernt.");
            set("Player.NotFound", "Konnte Spiler &b %player &fnicht finden.");
            set("Player.HasZero", "Du hast noch keine Schilder gefunden.");

            //Sign
            set("Sign.PlaceAbove", "Du kannst über FastTravelSigns keine Blöcke platzieren.");
            set("Sign.PlaceAbove.Is", "Du kannst FastTravelSigns nicht direkt unter Blöcken platzieren.");
            set("Sign.Break", "Du kannst FastTravelSigns nicht zerstören.");
            set("Sign.Removed", "Du hast &b%sign&f gelöscht.");
            set("Sign.Found", "Du hast &b%sign&f gefunden. Du kannst jetzt hier her reisen.");
            set("Sign.Found.Already", "Du hast &b%sign&f schon gefunden.");
            set("Sign.Found.Not", "Du hast &b%sign &fnoch nicht gefunden.");
            set("Sign.InvalidName", "Du darfst nur Buchstaben in FastTravelSigns benutzen.");
            set("Sign.Exists", "&b%sign &fexistiert bereits.");
            set("Sign.Created", "Du hast das FastTravelSign &b%sign&ferfolgreich erstellt.");
            set("Sign.ExistsNot", "FastTravelSign&b %sign &fexistiert nicht.");

            //Economy
            set("Econ.Disabled", "Economy unterstützung ist in der Konfiguration deaktiviert.");
            set("Econ.MoneyLess", "Du hast nicht genug Geld um zu reisen. Es kostet &2%cost&f.");
            set("Econ.Charged", "Du hast &2%cost&f bezahlt.");
            set("Econ.Error", "Es gab einen Fehler bei der Economy unterstützung. Bitte benachrichtige einen Admin." +
                    " Du darfst trotzdem reisen.");

            //Travel
            set("Travel.Success", "Du bist nach&b %sign &fgereist.");

            //Commands
            set("Command.Player", "Dieser Befehl muss von einem Spieler ausgeführt werden.");
            set("Command.NoSign", "Du musst ein FastTravelSign angeben.");
            set("Command.InvalidArgs", "Falsche Parameter.");

            set("Command.Auto.On", "FastTravelSign&b %sign&f kann nun von allen Spielern genutzt werden.");
            set("Command.Auto.Off",
                    "FastTravelSign&b %sign&f kann jetzt nicht mehr von allen Spielern genutzt werden.");

            set("Command.Price.No", "Du musst einen Preis angeben (0 tfür umsonst).");
            set("Command.Price.Invalid", "Ungültiger Preis; Es wurde eine zahl erwartet.");
            set("Command.Price.Set", "Die Reise nach&b %sign&f Kostet nun&a %price&f.");

            set("Command.Range.Set", "Reichweite von&b %sign&f ist nun&a %range&f.");

            set("Command.Save.Saved", "Datnbank wurde gespeichert.");

            //Warmup
            set("Warmup.Objective", "Aufladen");

            //Hooks
            set("Hooks.Dynmap", "Dynmap gefunden, aktiviere Kompatibilität..");

            save();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

}
