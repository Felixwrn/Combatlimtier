package de.deinname.nomacenostrength;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;

public class ListenerClass implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getCurrentItem()==null) return;
        if(e.getCurrentItem().getType()==Material.MACE){
            e.setCancelled(true);
            e.setCurrentItem(null);
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        if(e.getItem().getItemStack().getType()==Material.MACE){
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent e){
        if(e.getItem().getItemMeta() instanceof PotionMeta meta){
            if(meta.getBasePotionData().getType().name().contains("STRENGTH")){
                e.setCancelled(true);
            }
        }
    }
}
