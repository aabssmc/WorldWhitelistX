package cc.aabss.worldwhitelistx;

import cc.aabss.worldwhitelistx.util.WorldUtil;
import cc.aabss.worldwhitelistx.util.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static cc.aabss.worldwhitelistx.WorldWhitelistX.instance;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class WorldWhitelistCommand implements SimpleCommand {

    @Override
    public boolean run(CommandSender sender, Command command, String[] args) {
        if (args.length == 0 || args[0].equals("help")){
            return help(sender);
        }
        return switch (args[0]) {
            case "add" -> add(sender, args);
            case "remove" -> remove(sender, args);
            case "on" -> on(sender, args);
            case "off" -> off(sender, args);
            case "reload" -> reload(sender);
            default -> help(sender);
        };
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        if (args.length == 1){
            if (sender.hasPermission("worldwhitelistx.command")) {
                return List.of("on", "off", "add", "remove", "help", "reload");
            }
        } else if (args.length == 2){
            if (args[0].equals("add") || args[0].equals("remove")){
                if (sender.hasPermission("worldwhitelistx.add") ||sender.hasPermission("worldwhitelistx.remove")) {
                    List<String> list = new ArrayList<>();
                    Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
                    return list;
                }
            } else if (args[0].equals("on") || args[0].equals("off")){
                if (sender.hasPermission("worldwhitelistx.on") || sender.hasPermission("worldwhitelistx.off")) {
                    List<String> list = new ArrayList<>();
                    Bukkit.getWorlds().forEach(w -> list.add(w.getName()));
                    return list;
                }
            }
        } else if (args.length == 3){
            if (args[0].equals("add") || args[0].equals("remove")){
                if (sender.hasPermission("worldwhitelistx.add") || sender.hasPermission("worldwhitelistx.remove")) {
                    List<String> list = new ArrayList<>();
                    Bukkit.getWorlds().forEach(w -> list.add(w.getName()));
                    return list;
                }
            }
        }
        return List.of();
    }

    public boolean add(CommandSender sender, String[] args){
        if (sender.hasPermission("worldwhitelistx.add")) {
            if (args.length == 1 || args.length == 2) {
                sender.sendMessage(miniMessage().deserialize(
                        "<gold>/worldwhitelistx</gold> <yellow>add <player> <world></yellow>"
                ));
                return true;
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
            World w = Bukkit.getWorld(args[2]);
            if (w == null) {
                sender.sendMessage(miniMessage().deserialize(
                        "<color:#ff0000>Invalid world."
                ));
                return true;
            }
            WorldUtil.addWhitelist(p, w, sender);
        }
        return true;
    }

    public boolean remove(CommandSender sender, String[] args){
        if (sender.hasPermission("worldwhitelistx.remove")) {
            if (args.length == 1 || args.length == 2) {
                sender.sendMessage(miniMessage().deserialize(
                        "<gold>/worldwhitelistx</gold> <yellow>remove <player> <world></yellow>"
                ));
                return true;
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
            World w = Bukkit.getWorld(args[2]);
            if (w == null) {
                sender.sendMessage(miniMessage().deserialize(
                        "<color:#ff0000>Invalid world."
                ));
                return true;
            }
            WorldUtil.removeWhitelist(p, w, sender);
        }
        return true;
    }

    public boolean on(CommandSender sender, String[] args){
        if (sender.hasPermission("worldwhitelistx.on")) {
            if (args.length == 1) {
                sender.sendMessage(miniMessage().deserialize(
                        "<gold>/worldwhitelistx</gold> <yellow>on <world></yellow>"
                ));
                return true;
            }
            World w = Bukkit.getWorld(args[1]);
            if (w == null) {
                sender.sendMessage(miniMessage().deserialize(
                        "<color:#ff0000>Invalid world."
                ));
                return true;
            }
            WorldUtil.whitelistOn(w, sender);
        }
        return true;
    }

    public boolean off(CommandSender sender, String[] args){
        if (sender.hasPermission("worldwhitelistx.off")) {
            if (args.length == 1) {
                sender.sendMessage(miniMessage().deserialize(
                        "<gold>/worldwhitelistx</gold> <yellow>off <world></yellow>"
                ));
                return true;
            }
            World w = Bukkit.getWorld(args[1]);
            if (w == null) {
                sender.sendMessage(miniMessage().deserialize(
                        "<color:#ff0000>Invalid world."
                ));
                return true;
            }
            WorldUtil.whitelistOff(w, sender);
        } else{
            sender.sendMessage(permissionMessage());
        }
        return true;
    }
    
    public boolean reload(CommandSender sender){
        if (sender.hasPermission("worldwhitelistx.reload")) {
            instance.saveConfig();
            instance.reloadConfig();
            sender.sendMessage(miniMessage().deserialize(
                    "<color:#00ff00>Reloaded config."
            ));
        } else{
            sender.sendMessage(permissionMessage());
        }
        return true;
    }

    public boolean help(CommandSender sender){
        sender.sendMessage(miniMessage().deserialize(
                "<br><gold>/worldwhitelistx</gold> <yellow><add | remove> <player> <world></yellow><br>" +
                "<gold>/worldwhitelistx</gold> <yellow><on | off> <world></yellow><br>")
        );
        return true;
    }


}
