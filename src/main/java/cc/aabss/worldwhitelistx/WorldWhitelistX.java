package cc.aabss.worldwhitelistx;

import cc.aabss.worldwhitelistx.util.Metrics;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static cc.aabss.worldwhitelistx.util.SimpleCommand.register;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class WorldWhitelistX extends JavaPlugin implements Listener {

    public static FileConfiguration config = null;
    public static WorldWhitelistX instance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = this.getConfig();
        instance = this;
        register(new WorldWhitelistCommand(), "worldwhitelistx");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN+"WorldWhitelistX has been enabled!");
        for (World world : Bukkit.getWorlds()){
            if (Bukkit.getPluginManager().getPermission("worldwhitelistx."+world.getName()) == null){
                Bukkit.getPluginManager().addPermission(new Permission("worldwhitelistx."+world.getName()));
            }
        }
        Metrics metrics = new Metrics(this, 21834);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED+"WorldWhitelistX has been disabled!");
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getFrom().getWorld() == e.getTo().getWorld()) {
            return;
        }
        boolean d = config.getBoolean("debug");
        if (d) getLogger().warning("TELEPORT START "+e.getTo().getWorld().getName());
        String name = e.getTo().getWorld().getName();
        if (config.getBoolean(name + ".enabled")) {
            if (d)getLogger().warning("WHITELIST IS ON "+e.getTo().getWorld().getName());
            if (config.getList(name + ".players", List.of("")).contains(e.getPlayer().getUniqueId().toString())){
                if (d) getLogger().warning("CONTAINS PLAYER "+e.getTo().getWorld().getName());
            } else if (e.getPlayer().hasPermission("worldwhitelistx.bypass")){
                if (d) getLogger().warning("HAS BYPASS PERMISSION "+e.getTo().getWorld().getName());
            } else if (e.getPlayer().hasPermission("worldwhitelistx." + name)) {
                if (d) getLogger().warning("HAS WORLD WHITELIST PERMISSION " + e.getTo().getWorld().getName());
            } else {
                e.setCancelled(true);
                mini("<color:#ff0000>You are not whitelisted on this world!", e.getPlayer());
                if (d) getLogger().warning("DOESNT CONTAIN PLAYER OR BYPASS PERMISSION OR WORLD WHITELIST "+e.getTo().getWorld().getName());
            }
        } else{
            if (d) getLogger().warning("WHITELIST IS OFF " + e.getTo().getWorld().getName());
        }

    }

    public static Component get(String path){
        return miniMessage().deserialize(config.getString(path, "null"));
    }

    public static void mini(String string, Audience audience){
        audience.sendMessage(miniMessage().deserialize(string));
    }

}
