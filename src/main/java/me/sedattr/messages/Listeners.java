package me.sedattr.messages;

import me.sedattr.messages.helpers.Title;
import me.sedattr.messages.helpers.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class Listeners implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Boolean joinedBefore = player.hasPlayedBefore();

        Utils.broadcastSound(player, "join");
        Utils.broadcastSound(player, "players");

        ConfigurationSection titleSection = ProMessages.settingsSection.getConfigurationSection("title");
        if (titleSection != null && titleSection.getBoolean("enabled")) {
            String titleText = Utils.replacePlaceholders(player, titleSection.getString("title"));
            String subtitleText = Utils.replacePlaceholders(player, titleSection.getString("subtitle"));

            Title.sendTitle(player, titleText, subtitleText, titleSection.getInt("fadeIn") * 20, titleSection.getInt("stay") * 20, titleSection.getInt("fadeOut") * 20);
        }

        String actionBar = ProMessages.settingsSection.getString("actionBar");
        if (actionBar != null && !actionBar.equals("")) {
            String actionBarMessage = Utils.replacePlaceholders(player, actionBar);
            
            Utils.sendActionBar(player,actionBarMessage);
        }

        String message = !joinedBefore ? ProMessages.settingsSection.getString("firstJoin") : ProMessages.settingsSection.getString("join");
        if (message != null && !message.equals("")) {
            String joinMessage = Utils.replacePlaceholders(player, message);
            e.setJoinMessage(joinMessage);
        }

        ConfigurationSection playerSection = ProMessages.settingsSection.getConfigurationSection("player");
        if (playerSection == null || !playerSection.getBoolean("enabled"))
            return;
        
            List<String> messages = !joinedBefore ? playerSection.getStringList("firstJoin") : playerSection.getStringList("join");
            if (messages.isEmpty())
                return;
            
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ProMessages.getInstance(), () -> {
                for (String text : messages) {
                    text = Utils.replacePlaceholders(player, text);

                    if (text.contains("%center")) Utils.sendCenteredMessage(player, text.replace("%center%", ""));
                    else player.sendMessage(text);
                }
            }, 20L * Math.max(playerSection.getInt("delay"), 1));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Utils.broadcastSound(player, "players");

        String message = ProMessages.settingsSection.getString("leave");
        if (message != null && !message.equals("")) {
            String leaveMessage = Utils.replacePlaceholders(player, message);
            e.setQuitMessage(leaveMessage);
        }
    }
}
