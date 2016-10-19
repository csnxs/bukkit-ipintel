package net.allfearthesentinel.spigot.ipintel;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 19/10/16.
 */
public class Tasks extends BukkitRunnable {
    public final Plugin plugin;

    public Tasks(Plugin plugin) {
        this.plugin = plugin;
    }

    public void run() {
        for (int i = 0; i < plugin.playersToKick.size(); i++) {
            Player player = plugin.playersToKick.get(i);

            player.kickPlayer(plugin.config.getString("ban_reason"));

            plugin.playersToKick.remove(i);
        }
    }
}