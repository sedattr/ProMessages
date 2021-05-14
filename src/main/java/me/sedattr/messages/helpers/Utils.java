package me.sedattr.messages.helpers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.sedattr.messages.ProMessages;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public ProMessages plugin;
    public Utils(ProMessages plugin) { this.plugin = plugin; }

    private final static int CENTER_PX = 154;

    public static String replacePlaceholders(Player player, String message) {
        if (message == null || message.equals(""))
            return "";
        
        message = message
                .replace("%world%", player.getWorld().getName())
                .replace("%location_x%", String.valueOf(player.getLocation().getX()))
                .replace("%location_y%", String.valueOf(player.getLocation().getY()))
                .replace("%location_z%", String.valueOf(player.getLocation().getZ()))
                .replace("%xp%", String.valueOf(player.getExp()))
                .replace("%hunger%", String.valueOf(player.getFoodLevel()))
                .replace("%health%", String.valueOf(player.getHealth()))
                .replace("%online%", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("%player_name%", player.getName())
                .replace("%player%", player.getName())
                .replace("%name%", player.getName())
                .replace("%player_displayname%", player.getDisplayName())
                .replace("%display_name%", player.getDisplayName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%deaths%", String.valueOf(player.getStatistic(Statistic.DEATHS)));
        if (ProMessages.placeholderAPI)
            message = PlaceholderAPI.setPlaceholders(player, message);
        
        return colorizeRGB(message);
    }

    public static void sendCenteredMessage(Player player, String message){
        if (message == null || message.equals(""))
            return;
        
        message = colorizeRGB(message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
            }else if(previousCode){
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    public static String colorizeRGB(String s) {
        if (!Bukkit.getVersion().contains("1.16")) return colorize(s);

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher match = pattern.matcher(s);
        while (match.find()) {
            String hexColor = s.substring(match.start(), match.end());
            s = s.replace(hexColor, ChatColor.of(hexColor).toString());
            match = pattern.matcher(s);
        }

        return colorize(s);
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendActionBar(Player player, String message) {
        ProMessages.bar.sendActionbar(player, message);
    }

    public static void broadcastSound(Player player, String type) {
        ConfigurationSection soundSection = ProMessages.settingsSection.getConfigurationSection("sound." + type);
        if (soundSection != null && soundSection.getBoolean("enabled")) {
            if (type.equals("players")) {
                try {
                    for (Player p : Bukkit.getOnlinePlayers())
                        p.playSound(p.getLocation(), Sound.valueOf(soundSection.getString("value")), soundSection.getInt("volume"), soundSection.getInt("pitch"));
                } catch (NullPointerException ignored) {
                }
            } else
                player.playSound(player.getLocation(), Sound.valueOf(soundSection.getString("value")), soundSection.getInt("volume"), soundSection.getInt("pitch"));
        }
    }
}
