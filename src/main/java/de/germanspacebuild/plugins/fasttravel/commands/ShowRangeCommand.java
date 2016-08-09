package de.germanspacebuild.plugins.fasttravel.commands;


import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.io.IOManager;
import de.slikey.effectlib.effect.SphereEffect;
import de.slikey.effectlib.util.DynamicLocation;
import de.slikey.effectlib.util.ParticleEffect;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by oneill011990 on 02.04.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class ShowRangeCommand implements CommandExecutor {

    private FastTravel plugin;
    private IOManager io;

    public ShowRangeCommand(FastTravel plugin) {
        this.plugin = plugin;
        this.io = plugin.getIOManger();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission(FastTravel.PERMS_BASE + "show")) {
            io.sendTranslation(sender, "Perms.Not");
            return true;
        } else if (!(sender instanceof Player)) {
            io.sendTranslation(sender, "Command.Player");
            return true;
        } else if (args.length == 0) {
            io.sendTranslation(sender, "Command.InvalidArgs");
            return true;
        }

        FastTravelSign sign = FastTravelDB.getSign(args[0]);

        if (sign == null) {
            io.send(sender, io.translate("Sign.Exists.Not").replaceAll("%sign", args[0]));
            return true;
        } else if (sign.getRange() == 0) {
            io.send(sender, io.translate("Command.ShowRange.Zero").replaceAll("%sign", sign.getName()));
            return true;
        }

        SphereEffect effect = new SphereEffect(plugin.getEffectManager());
        effect.setDynamicOrigin(new DynamicLocation(sign.getSignLocation()));
        effect.radius = sign.getRange();
        effect.particle = ParticleEffect.FIREWORKS_SPARK;
        effect.color = Color.GREEN;
        effect.iterations = 10 * 20;
        effect.particles = 2500;
        effect.start();

        return true;
    }
}
