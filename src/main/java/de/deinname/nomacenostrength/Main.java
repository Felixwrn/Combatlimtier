package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    public static Main instance;

    public boolean maceEnabled;
    public boolean weaknessEnabled;
    public boolean regenerationEnabled;
    public boolean netheriteEnabled;
    public int maxStrengthLevel;

    public Location arenaSpawn1;
    public Location arenaSpawn2;

    public HashMap<UUID, Integer> wins = new HashMap<>();
    public HashMap<UUID, Integer> losses = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        loadConfigValues();

        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);

        // GUI öffnen
        getCommand("combatlimiter").setExecutor((sender, cmd, label, args) -> {
            if (sender instanceof org.bukkit.entity.Player player) {
                GUI.open(player);
            }
            return true;
        });

        // PvP GUI
        getCommand("pvp").setExecutor((sender, cmd, label, args) -> {
            if (sender instanceof org.bukkit.entity.Player player) {
                PvPGUI.open(player);
            }
            return true;
        });

        getCommand("accept").setExecutor((sender, cmd, label, args) -> {
            if (!(sender instanceof org.bukkit.entity.Player player)) return true;

    PVPManager.accept(player);
    return true;
});
    }

    public void loadConfigValues() {
        maceEnabled = getConfig().getBoolean("mace-enabled");
        weaknessEnabled = getConfig().getBoolean("weakness-enabled");
        regenerationEnabled = getConfig().getBoolean("regeneration-enabled");
        netheriteEnabled = getConfig().getBoolean("netherite-enabled");
        maxStrengthLevel = getConfig().getInt("max-strength-level");

        String world = getConfig().getString("arena.world");

        arenaSpawn1 = new Location(
                Bukkit.getWorld(world),
                getConfig().getDouble("arena.spawn1.x"),
                getConfig().getDouble("arena.spawn1.y"),
                getConfig().getDouble("arena.spawn1.z")
        );

        arenaSpawn2 = new Location(
                Bukkit.getWorld(world),
                getConfig().getDouble("arena.spawn2.x"),
                getConfig().getDouble("arena.spawn2.y"),
                getConfig().getDouble("arena.spawn2.z")
        );
    }

    public void saveConfigValues() {
        getConfig().set("mace-enabled", maceEnabled);
        getConfig().set("weakness-enabled", weaknessEnabled);
        getConfig().set("regeneration-enabled", regenerationEnabled);
        getConfig().set("netherite-enabled", netheriteEnabled);
        getConfig().set("max-strength-level", maxStrengthLevel);
        saveConfig();
    }

    public void addWin(UUID uuid) {
        wins.put(uuid, wins.getOrDefault(uuid, 0) + 1);
    }

    public void addLoss(UUID uuid) {
        losses.put(uuid, losses.getOrDefault(uuid, 0) + 1);
    }

    public int getWins(UUID uuid) {
        return wins.getOrDefault(uuid, 0);
    }

    public int getLosses(UUID uuid) {
        return losses.getOrDefault(uuid, 0);
    }
}
