package de.deinname.nomacenostrength;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class ListenerClass implements Listener {

    private final Main plugin = Main.instance;

    private boolean isNetherite(Material mat) {
        return mat.name().startsWith("NETHERITE");
    }

    // GUI Klicks
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

    // Mace & Netherite blocken (Inventar)
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

    // Aufheben
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        Material mat = e.getItem().getItemStack().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
            (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    // Benutzen
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

    // Angreifen
    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;

        Material mat = player.getInventory().getItemInMainHand().getType();

        if ((mat == Material.MACE && !plugin.maceEnabled) ||
            (isNetherite(mat) && !plugin.netheriteEnabled)) {

            e.setCancelled(true);
            player.getInventory().setItemInMainHand(null);
        }
    }

    // Tränke
    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (!(e.getItem().getItemMeta() instanceof PotionMeta meta)) return;

        String type = meta.getBasePotionData().getType().name();

        if (type.contains("WEAKNESS") && !plugin.weaknessEnabled) {
            e.setCancelled(true);
        }

        if (type.contains("REGENERATION") && !plugin.regenerationEnabled) {
            e.setCancelled(true);
        }

        if (type.contains("STRENGTH")) {
            int level = meta.getBasePotionData().isUpgraded() ? 2 : 1;

            if (level > plugin.maxStrengthLevel) {
                e.setCancelled(true);
            }
        }
    }

    // Effekte
    @EventHandler
    public void onEffect(EntityPotionEffectEvent e) {
        PotionEffect effect = e.getNewEffect();
        if (effect == null) return;

        String name = effect.getType().getName();

        if (name.equalsIgnoreCase("INCREASE_DAMAGE")) {
            int level = effect.getAmplifier() + 1;
            if (level > plugin.maxStrengthLevel) {
                e.setCancelled(true);
            }
        }

        if (name.equalsIgnoreCase("WEAKNESS") && !plugin.weaknessEnabled) {
            e.setCancelled(true);
        }

        if (name.equalsIgnoreCase("REGENERATION") && !plugin.regenerationEnabled) {
            e.setCancelled(true);
        }
    }
}
