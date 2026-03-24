package de.deinname.nomacenostrength;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ListenerClass implements Listener {

    // Maces verhindern
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.MACE && !Main.maceEnabled) {
            e.setCancelled(true);
            e.setCurrentItem(null);
            e.getWhoClicked().sendMessage("§cMaces sind deaktiviert!");
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.MACE && !Main.maceEnabled) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    // Tränke trinken
    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e) {
        if (e.getItem().getItemMeta() instanceof PotionMeta meta) {
            String typeName = meta.getBasePotionData().getType().name();

            // Schwäche blockieren
            if (typeName.contains("WEAKNESS")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cSchwäche-Tränke sind deaktiviert!");
            }

            // Stärke-Trank prüfen
            if (typeName.contains("STRENGTH")) {
                // Stärke-Level direkt vom Trank bestimmen
                int amplifier = meta.getBasePotionData().isUpgraded() ? 1 : 0; // false = Stärke I, true = Stärke II+
                if (amplifier > 0) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage("§cNur Stärke I ist erlaubt! Stärke II+ darf nicht getrunken werden!");
                }
            }
        }
    }

    // Effekte blockieren: Stärke II+ und Schwäche
    @EventHandler
    public void onEffect(EntityPotionEffectEvent e) {
        PotionEffect effect = e.getNewEffect();
        if (effect != null) {
            PotionEffectType strength = PotionEffectType.getByName("INCREASE_DAMAGE");
            if ((strength != null && effect.getType() == strength && effect.getAmplifier() > 0)
                    || effect.getType() == PotionEffectType.WEAKNESS) {
                e.setCancelled(true);
            }
        }
    }
}
