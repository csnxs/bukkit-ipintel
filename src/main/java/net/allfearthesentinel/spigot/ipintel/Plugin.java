package net.allfearthesentinel.spigot.ipintel;

import net.allfearthesentinel.spigot.ipintel.events.PlayerJoinHandler;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sean on 19/10/2016.
 */
public class Plugin extends JavaPlugin {
    public static Plugin instance;
    public Logger logger;
    public Configuration config;
    public BukkitTask tasks;

    public List<Player> playersToKick = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        config = getConfig();

        initConfig();

        tasks = new Tasks(this).runTaskTimer(this, 0, 1);

        getServer().getPluginManager().registerEvents(new PlayerJoinHandler(), this);
    }

    @Override
    public void onDisable() {

    }

    private void initConfig() {
        config.addDefault("enabled", false);
        config.addDefault("min_result", 1.0);
        config.addDefault("contact_email", "me@example.com");
        config.addDefault("ban_reason", "You have been banned due to suspicion of using a proxy.");

        config.options().copyDefaults(true);
        saveConfig();
    }
}
