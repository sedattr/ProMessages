package me.sedattr.messages;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {
    public UpdateChecker() throws IOException {
        int projectID = 61694;

        URLConnection con = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID).openConnection();
        String oldVersion = ProMessages.getInstance().getDescription().getVersion();
        String newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        if (!oldVersion.equals(newVersion))
            Bukkit.getConsoleSender().sendMessage("§8[§bProMessages§8] §eNew version is found! Old/New Version: " + oldVersion + "/" + newVersion);
        else
            Bukkit.getConsoleSender().sendMessage("§8[§bProMessages§8] §ePlugin is up to date!");
    }
}