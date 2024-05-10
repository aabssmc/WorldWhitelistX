package cc.aabss.worldwhitelistx.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static cc.aabss.worldwhitelistx.WorldWhitelistX.*;

public class WorldUtil {
    public static void addWhitelist(OfflinePlayer p, World world, CommandSender audience){
        if (config.contains(world.getName() + ".players")) {
            List<String> players = config.getStringList(world.getName() + ".players");
            if (players.contains(p.getUniqueId().toString())) {
                mini("<color:#ff0000>"+p.getName()+" is already on the whitelist!", audience);
            } else {
                players.add(p.getUniqueId().toString());
                config.set(world.getName() + ".players", players);
                mini("<color:#00ff00>Added "+p.getName()+" to the whitelist!", audience);
                instance.saveConfig();
                instance.reloadConfig();
            }
        } else {
            List<String> players = new ArrayList<>();
            players.add(p.getUniqueId().toString());
            config.set(world.getName() + ".players", players);
            mini("<color:#00ff00>Added "+p.getName()+" to the whitelist!", audience);
            instance.saveConfig();
            instance.reloadConfig();
        }
    }

    public static void removeWhitelist(OfflinePlayer p, World world, CommandSender audience){
        if (config.contains(world.getName() + ".players")) {
            List<String> players = config.getStringList(world.getName() + ".players");
            if (players.contains(p.getUniqueId().toString())) {
                players.remove(p.getUniqueId().toString());
                config.set(world.getName() + ".players", players);
                mini("<color:#00ff00>Removed "+p.getName()+" from the whitelist!", audience);
                instance.saveConfig();
                instance.reloadConfig();
            } else {
                mini("<color:#00ff00>"+p.getName()+" is not in the whitelist!", audience);
            }
        } else {
            mini("<color:#ff0000>"+p.getName()+" is not in the whitelist!", audience);
        }
    }

    public static void whitelistOn(World world, CommandSender audience){
        if (config.contains(world.getName() + ".enabled")) {
            if (!config.getBoolean(world.getName() + ".enabled")) {
                mini("<color:#00ff00>Enabled whitelist for "+world.getName(), audience);
                config.set((world.getName() + ".enabled"), true);
                instance.saveConfig();
                instance.reloadConfig();
            } else {
                mini("<color:#ff0000>Whitelist is already enabled for "+world.getName(), audience);
            }
        } else {
            mini("<color:#00ff00>Enabled whitelist for "+world.getName(), audience);
            config.set((world.getName() + ".enabled"), true);
            instance.saveConfig();
            instance.reloadConfig();
        }
    }

    public static void whitelistOff(World world, CommandSender audience){
        if (config.contains(world.getName() + ".enabled")) {
            if (config.getBoolean(world.getName() + ".enabled")) {
                mini("<color:#00ff00>Disabled whitelist for "+world.getName(), audience);
                config.set((world.getName() + ".enabled"), false);
                instance.saveConfig();
                instance.reloadConfig();
            } else {
                mini("<color:#ff0000>Whitelist is already disabled for "+world.getName(), audience);
            }
        } else {
            mini("<color:#ff0000>Disabled whitelist for "+world.getName(), audience);
            config.set((world.getName() + ".enabled"), false);
            instance.saveConfig();
            instance.reloadConfig();
        }
    }
}
