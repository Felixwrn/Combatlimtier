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

    // ================= CombatLimiter GUI =================
    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("§8CombatLimiter")) return;

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player player)) return;

        switch (e.getSlot()) {
            case 0 -> plugin.maceEnabled = !plugin.maceEnabled;
            case 1 -> {
                plugin.maxStrengthLevel++;
                if (plugin.maxStrengthLevel > 2) plugin.maxStrengthLevel = 1;
            }
            case 2 -> plugin.weaknessEnabled = !plugin.weaknessEnabled;
            case 3 -> plugin.regenerationEnabled = !plugin.regenerationEnabled;
            case 4 -> plugin.netheriteEnabled = !plugin.netheriteEnabled;
        }

        plugin.saveConfigValues();
        Bukkit.getScheduler().runTask(plugin, () -> GUI.open(player));
    }

    // ================= PvP GUI =================
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

    // ================= PvP Tod =================
@EventHandler
public void onDeath(PlayerDeathEvent e) {
    Player player = e.getEntity();

    // Nur im PvP Fight
    if (PVPManager.isInFight(player)) {

        // Items behalten
        e.setKeepInventory(true);
        e.getDrops().clear();

        // EXP behalten
        e.setKeepLevel(true);
        e.setDroppedExp(0);

        // Fight beenden
        PVPManager.endFight(player.teleport(Bukkit.getWorld("world").getSpawnLocation()););
    }
}

    // ================= CombatLimiter =================
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

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        Material mat = e.getItem().getItemStack().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
                (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            e.getItem().remove();
        }
    }

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
