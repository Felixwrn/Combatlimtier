package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PvPGUI {

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8PvP Menü");

        int slot = 0;

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target == player) continue;

            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(target.getName());

            item.setItemMeta(meta);

            inv.setItem(slot++, item);
        }

        player.openInventory(inv);
    }
}
