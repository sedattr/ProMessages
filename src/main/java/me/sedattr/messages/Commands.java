package me.sedattr.messages;

import me.sedattr.messages.helpers.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Commands implements CommandExecutor, TabCompleter, Listener {
    public boolean noPermission(CommandSender player, String text) {
        String permission = ProMessages.settingsSection.getString("permissions." + text);
        if (permission == null || permission.equals("")) return false;

        return !player.hasPermission(permission);
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (noPermission(commandSender, "command")) return null;

        ArrayList<String> complete = new ArrayList<>();
        if (!noPermission(commandSender, "reload")) complete.add("reload");
        if (!noPermission(commandSender, "info")) complete.add("info");

        if (args.length == 1) return complete;
        return null;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (noPermission(commandSender, "command")) {
            commandSender.sendMessage(Utils.colorizeRGB(ProMessages.messagesSection.getString("noPermission")));
            return false;
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "reload":
                    if (noPermission(commandSender, "reload")) {
                        commandSender.sendMessage(Utils.colorizeRGB(ProMessages.messagesSection.getString("noPermission")));
                        return false;
                    }

                    ProMessages.getInstance().reloadConfig();
                    ProMessages.getInstance().reloadFiles();
                    commandSender.sendMessage(Utils.colorizeRGB(ProMessages.messagesSection.getString("reloaded")));
                    return true;
                case "info":
                    if (noPermission(commandSender, "info")) {
                        commandSender.sendMessage(Utils.colorizeRGB(ProMessages.messagesSection.getString("noPermission")));
                        return false;
                    }

                    List<String> info = ProMessages.messagesSection.getStringList("info");
                    if (info.isEmpty())
                        return false;

                    for (String text : info) {
                        commandSender.sendMessage(Utils.colorizeRGB(text
                                .replace("%join_sound%", ProMessages.settingsSection.getString("sound.join.enabled").replace("t", "T").replace("f", "F"))
                                .replace("%players_sound%", ProMessages.settingsSection.getString("sound.players.enabled").replace("t", "T").replace("f", "F"))
                                .replace("%player_message%", ProMessages.settingsSection.getString("player.enabled").replace("t", "T").replace("f", "F"))
                                .replace("%join_message%", ProMessages.settingsSection.getString("join").equals("") ? "False" : "True")
                                .replace("%leave_message%", ProMessages.settingsSection.getString("leave").equals("") ? "False" : "True")
                                .replace("%action_bar%", ProMessages.settingsSection.getString("actionBar").equals("") ? "False" : "True")
                                .replace("%title%", ProMessages.settingsSection.getString("title.enabled").replace("t", "T").replace("f", "F"))));
                    }
                    return true;
            }
        }

        List<String> usage = ProMessages.messagesSection.getStringList("usage");
        if (usage.isEmpty())
            return false;
        for (String message : usage)
            commandSender.sendMessage(Utils.colorizeRGB(message));

        return false;
    }
}
