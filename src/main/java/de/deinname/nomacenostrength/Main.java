package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new ListenerClass(), this);
    }

    public Location getArenaSpawn(int number) {
        World world = Bukkit.getWorld(getConfig().getString("arena.world", "world"));
        if (world == null) {
            throw new IllegalStateException("The configured arena world is not loaded");
        }
        String path = "arena.spawn" + number;
        return new Location(world, getConfig().getDouble(path + ".x"),
                getConfig().getDouble(path + ".y"), getConfig().getDouble(path + ".z"));
    }

    public int getWins(UUID player) {
        return getConfig().getInt("stats." + player + ".wins");
    }

    public int getLosses(UUID player) {
        return getConfig().getInt("stats." + player + ".losses");
    }

    public void addWin(UUID player) {
        getConfig().set("stats." + player + ".wins", getWins(player) + 1);
        saveConfig();
    }

    public void addLoss(UUID player) {
        getConfig().set("stats." + player + ".losses", getLosses(player) + 1);
        saveConfig();
    }
}
