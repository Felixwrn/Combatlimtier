package de.deinname.nomacenostrength;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class ListenerClass implements Listener {

    private final Main plugin = Main.instance;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        if (e.getCurrentItem().getType() == Material.MACE && !plugin.maceEnabled) {
            e.setCancelled(true);
            e.setCurrentItem(null);
            e.getWhoClicked().sendMessage("§cMaces sind deaktiviert!");
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.MACE && !plugin.maceEnabled) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        if (e.getItem() == null) return;

        if (e.getItem().getType() == Material.MACE && !plugin.maceEnabled) {
            e.setCancelled(true);
            e.getPlayer().getInventory().setItemInMainHand(null);
            e.getPlayer().sendMessage("§cMaces sind deaktiviert und wurden gelöscht!");
        }
    }
    @EventHandler
public void onAttack(org.bukkit.event.entity.EntityDamageByEntityEvent e) {
    if (!(e.getDamager() instanceof org.bukkit.entity.Player player)) return;

    if (player.getInventory().getItemInMainHand().getType() == Material.MACE 
            && !plugin.maceEnabled) {

        e.setCancelled(true);

        // Mace löschen
        player.getInventory().setItemInMainHand(null);

        player.sendMessage("§cMaces sind deaktiviert und wurden gelöscht!");
    }
}

    

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (e.getItem().getItemMeta() instanceof PotionMeta meta) {

            String typeName = meta.getBasePotionData().getType().name();

            if (typeName.contains("WEAKNESS") && !plugin.weaknessEnabled) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cSchwäche ist deaktiviert!");
            }

            if (typeName.contains("STRENGTH")) {
                int level = meta.getBasePotionData().isUpgraded() ? 2 : 1;

                if (level > plugin.maxStrengthLevel) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§cMaximale Stärke ist " + plugin.maxStrengthLevel);
                }
            }
        }
    }

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
    }
}

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (e.getItem().getItemMeta() instanceof PotionMeta meta) {

            String typeName = meta.getBasePotionData().getType().name();

            if (typeName.contains("REGENERATION") && !plugin.regenerationEnabled) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cregeneration ist deaktiviert!");
            }
