package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    public boolean maceEnabled;
    public boolean weaknessEnabled;
    public int maxStrengthLevel;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadConfigValues();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);

        getCommand("togglemace").setExecutor((sender, cmd, label, args) -> {
            maceEnabled = !maceEnabled;
            saveConfigValues();
            sender.sendMessage("§aMaces sind jetzt " + (maceEnabled ? "aktiviert" : "deaktiviert"));
            return true;
        });

        getCommand("toggleweakness").setExecutor((sender, cmd, label, args) -> {
            weaknessEnabled = !weaknessEnabled;
            saveConfigValues();
            sender.sendMessage("§aSchwäche ist jetzt " + (weaknessEnabled ? "aktiviert" : "deaktiviert"));
            return true;
        });

        getCommand("setstrength").setExecutor((sender, cmd, label, args) -> {
            if (args.length != 1) {
                sender.sendMessage("§c/setstrength <level>");
                return true;
            }

            try {
                int level = Integer.parseInt(args[0]);
                if (level < 1) {
                    sender.sendMessage("§cMinimum ist 1!");
                    return true;
                }

                maxStrengthLevel = level;
                saveConfigValues();
                sender.sendMessage("§aMaximale Stärke ist jetzt " + level);

            } catch (NumberFormatException e) {
                sender.sendMessage("§cBitte Zahl eingeben!");
            }

            return true;
        });
    }

    public void loadConfigValues() {
        maceEnabled = getConfig().getBoolean("mace-enabled");
        weaknessEnabled = getConfig().getBoolean("weakness-enabled");
        maxStrengthLevel = getConfig().getInt("max-strength-level");
    }

    public void saveConfigValues() {
        getConfig().set("mace-enabled", maceEnabled);
        getConfig().set("weakness-enabled", weaknessEnabled);
        getConfig().set("max-strength-level", maxStrengthLevel);
        saveConfig();
    }
}
