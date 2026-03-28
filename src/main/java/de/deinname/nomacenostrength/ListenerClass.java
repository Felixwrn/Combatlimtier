package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class ListenerClass implements Listener {

    private final Main plugin = Main.instance;

    private boolean isNetherite(Material mat) {
        return mat.name().startsWith("NETHERITE");
    }

    // ===== PvP GUI =====
    @EventHandler
    public void onPvPGUI(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("§8PvP Menü")) return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player player)) return;

        ItemStack item = e.getCurrentItem();
        if (item == null || item.getItemMeta() == null) return;

        String name = org.bukkit.ChatColor.stripColor(item.getItemMeta().getDisplayName());
        Player target = Bukkit.getPlayer(name);

        if (target != null) {
            PVPManager.sendRequest(player, target);
            player.closeInventory();
        }
    }

    // ===== Death =====
    @EventHandler
public void onDeath(PlayerDeathEvent e) {
    Player player = e.getEntity();

    player.sendMessage("§cDEBUG: Death Event wurde ausgelöst");

    if (PVPManager.isInFight(player)) {
        player.sendMessage("§aDEBUG: Spieler ist im Fight");

        e.setKeepInventory(true);
        e.getDrops().clear();

        e.setKeepLevel(true);
        e.setDroppedExp(0);

        PVPManager.endFight(player);

    }
}

    // ===== Inventory Block =====
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        Material mat = e.getCurrentItem().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
                (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            e.setCurrentItem(null);
        }
    }

    // ===== Pickup =====
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        Material mat = e.getItem().getItemStack().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
                (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    // ===== Use Item =====
    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        Material mat = e.getItem().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
                (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            e.getPlayer().getInventory().setItemInMainHand(null);
        }
    }

    // ===== Drink =====
    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (!(e.getItem().getItemMeta() instanceof PotionMeta meta)) return;

        String type = meta.getBasePotionData().getType().name();

        if (type.contains("WEAKNESS") && !plugin.weaknessEnabled) e.setCancelled(true);
        if (type.contains("REGENERATION") && !plugin.regenerationEnabled) e.setCancelled(true);

        if (type.contains("STRENGTH")) {
            int level = meta.getBasePotionData().isUpgraded() ? 2 : 1;
            if (level > plugin.maxStrengthLevel) e.setCancelled(true);
        }
    }

    // ===== Effects =====
    @EventHandler
    public void onEffect(EntityPotionEffectEvent e) {
        if (e.getNewEffect() == null) return;

        String name = e.getNewEffect().getType().getName();

        if (name.equalsIgnoreCase("INCREASE_DAMAGE")) {
            int level = e.getNewEffect().getAmplifier() + 1;
            if (level > plugin.maxStrengthLevel) e.setCancelled(true);
        }

        if (name.equalsIgnoreCase("WEAKNESS") && !plugin.weaknessEnabled) e.setCancelled(true);
        if (name.equalsIgnoreCase("REGENERATION") && !plugin.regenerationEnabled) e.setCancelled(true);
    }
}
