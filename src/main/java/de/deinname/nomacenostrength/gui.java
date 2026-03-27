package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUI {

    public static void open(Player player) {
        Main plugin = Main.instance;

        Inventory inv = Bukkit.createInventory(null, 9, "§8CombatLimiter");

        inv.setItem(0, createItem(Material.MACE, "§eMace: " + status(plugin.maceEnabled)));
        inv.setItem(1, createItem(Material.BLAZE_POWDER, "§eStärke: §f" + plugin.maxStrengthLevel));
        inv.setItem(2, createItem(Material.FERMENTED_SPIDER_EYE, "§eWeakness: " + status(plugin.weaknessEnabled)));
        inv.setItem(3, createItem(Material.GHAST_TEAR, "§eRegen: " + status(plugin.regenerationEnabled)));
        inv.setItem(4, createItem(Material.NETHERITE_SWORD, "§eNetherite: " + status(plugin.netheriteEnabled)));

        player.openInventory(inv);
    }

    private static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static String status(boolean b) {
        return b ? "§aAN" : "§cAUS";
    }
}
