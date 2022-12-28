package com.kraby.repairtweaks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.kraby.repairtweaks.RepairTweaks;

public class RepairCostKeeperListener implements Listener {
    
    @EventHandler
    public void keepCostOnRepair(PrepareAnvilEvent e) {
        if (!RepairTweaks.singleton.config.isRepairDontIncreaseCost())
            return;

        AnvilInventory inv = e.getInventory();

        if (!(
            inv.getFirstItem() != null
            && inv.getSecondItem() != null
            && e.getResult() != null
            && inv.getFirstItem().getItemMeta() instanceof Repairable))
        {
            return;
        }

        // only keep going if the second item does not have any enchants
        ItemMeta secondItemMeta = inv.getSecondItem().getItemMeta();
        if (secondItemMeta.hasEnchants() 
        || (secondItemMeta instanceof EnchantmentStorageMeta && ((EnchantmentStorageMeta)secondItemMeta).hasStoredEnchants())) {
            return;
        }

        ItemStack result = e.getResult();
        Repairable resultMeta = (Repairable)result.getItemMeta();
        resultMeta.setRepairCost(((Repairable)inv.getFirstItem().getItemMeta()).getRepairCost());
        result.setItemMeta(resultMeta);
        e.setResult(result);
    }

}
