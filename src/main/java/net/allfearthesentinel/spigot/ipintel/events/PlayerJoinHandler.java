package net.allfearthesentinel.spigot.ipintel.events;

import net.allfearthesentinel.spigot.ipintel.Plugin;
import net.allfearthesentinel.spigot.ipintel.RequestThread;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Created by Sean on 19/10/2016.
 */
public class PlayerJoinHandler implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        final Configuration config = Plugin.instance.config;

        if (config.getBoolean("enabled")) {
            RequestThread requestThread = new RequestThread(player);
            Thread thread = new Thread(requestThread);
            thread.setName("IPIntel Request Thread for " + player.getAddress().getHostString());
            thread.start();
        }
    }
}
