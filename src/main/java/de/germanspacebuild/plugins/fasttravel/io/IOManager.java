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

package de.germanspacebuild.plugins.fasttravel.io;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class IOManager {

    private final String prefix;
    private FastTravel plugin;
    private Configuration config;
    private Translator translator;

    public IOManager(FastTravel plugin) {
        this.plugin = plugin;
        this.config = this.plugin.getConfig();
        translator = new Translator(plugin);
        translator.init();
        prefix = ChatColor.LIGHT_PURPLE + "[" + plugin.getDescription().getName() + "] " +
                ChatColor.WHITE;
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

    public void send(Player player, String msg) {
        player.sendMessage(parseColor(prefix + msg));
    }

    public void sendTranslation(CommandSender sender, String key) {
        sender.sendMessage(parseColor(prefix + translate(key)));
    }

    public void sendTranslation(Player player, String key) {
        player.sendMessage(prefix + parseColor(translate(key)));
    }

    public String translate(String key) {
        return translator.getKey(key);
    }

    public Translator getTranslator() {
        return translator;
    }

}
