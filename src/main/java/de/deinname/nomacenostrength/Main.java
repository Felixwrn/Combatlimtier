package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main instance;

    public boolean maceEnabled;
    public boolean weaknessEnabled;
    public boolean regenerationEnabled;
    public boolean netheriteEnabled;
    public int maxStrengthLevel;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadConfigValues();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);

        // GUI Command
        getCommand("combatlimiter").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player player)) return true;

            GUI.open(player);
            return true;
        });
    }

    public void loadConfigValues() {
        maceEnabled = getConfig().getBoolean("mace-enabled");
        weaknessEnabled = getConfig().getBoolean("weakness-enabled");
        regenerationEnabled = getConfig().getBoolean("regeneration-enabled");
        netheriteEnabled = getConfig().getBoolean("netherite-enabled");
        maxStrengthLevel = getConfig().getInt("max-strength-level");
    }

    public void saveConfigValues() {
        getConfig().set("mace-enabled", maceEnabled);
        getConfig().set("weakness-enabled", weaknessEnabled);
        getConfig().set("regeneration-enabled", regenerationEnabled);
        getConfig().set("netherite-enabled", netheriteEnabled);
        getConfig().set("max-strength-level", maxStrengthLevel);
        saveConfig();
    }
}
