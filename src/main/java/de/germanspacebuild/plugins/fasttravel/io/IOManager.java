package de.germanspacebuild.plugins.fasttravel.io;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
public class IOManager {

    private FastTravel plugin;
    private Configuration config;
    private Translator translator;

    private final String prefix = ChatColor.LIGHT_PURPLE + "[" + plugin.getDescription().getName() + "] " +
            ChatColor.WHITE;

    public IOManager(FastTravel plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        translator = new Translator(plugin);
        translator.init();
    }

    public void broadcast(String msg) {
        plugin.getServer().broadcastMessage(parseColor(prefix + msg));
    }

    public void broadcast(String msg, String perm) {
        plugin.getServer().broadcast(parseColor(prefix + msg), perm);
    }

    private String parseColor(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void sendConsole(String msg) {
        plugin.getServer().getConsoleSender().sendMessage(prefix + msg);
    }

    public void send(CommandSender sender, String msg) {
        sender.sendMessage(parseColor(prefix + msg));
    }

    public void sendTranslation(CommandSender sender, String key) {
        if (config.getBoolean("IO.Show-Prefix")) sender.sendMessage(parseColor(prefix + translate(key)));
        else sender.sendMessage(parseColor(translate(key)));
    }

    public void sendFewArgs(CommandSender sender, String usage) {
        if (config.getBoolean("IO.Show-Prefix")) {
            sender.sendMessage(parseColor(prefix + translate("Command.FewArgs")));
            sender.sendMessage(parseColor(prefix + translate("Command.Usage").replaceAll("%usage%", usage)));
        }else {
            sender.sendMessage(parseColor(translate("Command.FewArgs")));
            sender.sendMessage(parseColor(translate("Command.Usage").replaceAll("%usage%", usage)));
        }
    }

    public void sendManyArgs(CommandSender sender, String usage) {
        if (config.getBoolean("IO.Show-Prefix")) {
            sender.sendMessage(parseColor(prefix + translate("Command.ManyArgs")));
            sender.sendMessage(parseColor(prefix + translate("Command.Usage").replaceAll("%usage%", usage)));
        }else {
            sender.sendMessage(parseColor(translate("Command.ManyArgs")));
            sender.sendMessage(parseColor(translate("Command.Usage").replaceAll("%usage%", usage)));
        }
    }

    public String translate(String key) {
        return translator.getKey(key);
    }

    public Translator getTranslator() {
        return translator;
    }

}
