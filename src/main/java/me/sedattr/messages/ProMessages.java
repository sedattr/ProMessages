package me.sedattr.messages;

import me.sedattr.messages.helpers.ActionBar;
import me.sedattr.messages.helpers.ActionBar1_8;
import me.sedattr.messages.helpers.ActionBar1_9;
import org.bstats.bukkit.MetricsLite;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class ProMessages extends JavaPlugin {
    private static ProMessages instance;

    public static ProMessages getInstance() {
        return instance;
    }

    public static ConfigurationSection settingsSection;
    public static ConfigurationSection messagesSection;
    public static ActionBar bar;
    public static boolean placeholderAPI = false;

    public void onEnable() {
        instance = this;
        placeholderAPI = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        saveDefaultConfig();

        String version = Bukkit.getVersion();
        if (version.contains("1.8")) bar = new ActionBar1_8();
        else bar = new ActionBar1_9();

        reloadFiles();
        PluginCommand command = getCommand("promessages");
        if (command != null)
            command.setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getConsoleSender().sendMessage("§8[§bProMessages§8] §eLoading messages...");

        new MetricsLite(this, 10232);
        try {
            new UpdateChecker();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadFiles() {
        settingsSection = getConfig().getConfigurationSection("settings");
        messagesSection = getConfig().getConfigurationSection("messages");
    }
}