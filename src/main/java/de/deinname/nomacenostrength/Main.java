package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static boolean maceEnabled = false; // Default: Maces deaktiviert

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);
        this.getCommand("togglemace").setExecutor(new ToggleMaceCommand());
    }
}
