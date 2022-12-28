package com.kraby.repairtweaks.listeners;

import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.kraby.repairtweaks.RepairTweaks;
import com.kraby.repairtweaks.utils.MainConfig;
import com.kraby.repairtweaks.utils.MendingLevel;
import com.kraby.repairtweaks.utils.MendingLevel.EffectType;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemMendEvent;

public class MendingBehaviourChangesListener implements Listener {

    @EventHandler
    public void onMending(PlayerItemMendEvent e) {
        MainConfig config = RepairTweaks.singleton.config;

        if (!config.isMendingBehaviourEnabled())
            return;

        int mendingEnchantLevel = e.getItem().getEnchantmentLevel(Enchantment.MENDING);
        MendingLevel mendingCfg = config.getMendingLevel(mendingEnchantLevel);

        if (mendingCfg.getEffect() != EffectType.MENDING)
        {
            e.setCancelled(true);
            return;
        }
        
        Damageable repairableMeta = (Damageable)e.getItem().getItemMeta();

        int xp = e.getExperienceOrb().getExperience();
        int repairNeeded = repairableMeta.getDamage();
        int repairAvailable = (int)Math.ceil(xp * mendingCfg.getDurabilityPerXp());
        int repairDone = Math.min(repairNeeded, repairAvailable);
        int xpConsumed = (int) Math.round(repairDone / mendingCfg.getDurabilityPerXp());

        e.setRepairAmount(0);   // get rid of vanilla behaviour (durability per xp = 2.0)
        e.getExperienceOrb().setExperience(xp - xpConsumed);
        repairableMeta.setDamage(repairableMeta.getDamage() - repairDone);
        e.getItem().setItemMeta(repairableMeta);
        Bukkit.getLogger().info("needed="+repairNeeded+";availabled="+repairAvailable+";done="+repairDone+";final damage="+repairableMeta.getDamage());
        e.setCancelled(true);
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        MainConfig config = RepairTweaks.singleton.config;
        
        if (!config.isMendingBehaviourEnabled())
            return;

        AnvilInventory inv = e.getInventory();

        // only keep going if we're dealing with a mending book
        if (!(
            inv.getFirstItem() != null
            && inv.getSecondItem() != null
            && e.getResult() != null
            && inv.getSecondItem().getType() == Material.ENCHANTED_BOOK
            && inv.getSecondItem().getItemMeta() instanceof EnchantmentStorageMeta
            && ((EnchantmentStorageMeta)inv.getSecondItem().getItemMeta()).hasStoredEnchant(Enchantment.MENDING))) 
        {
            return;
        }

        EnchantmentStorageMeta bookEnchantsMeta = (EnchantmentStorageMeta)inv.getSecondItem().getItemMeta();

        MendingLevel mendingCfg = config.getMendingLevel(bookEnchantsMeta.getStoredEnchantLevel(Enchantment.MENDING));
        if (mendingCfg == null) {
            return;
        }
        // Remove mending from item if it's a "repair cost lowering"-mending
        int previousMendingLevel = inv.getFirstItem().getItemMeta().getEnchantLevel(Enchantment.MENDING);
        if (previousMendingLevel > 0) {
            MendingLevel previousMendingCfg = config.getMendingLevel(previousMendingLevel);
            if (previousMendingCfg.getEffect() != EffectType.MENDING) {
                ItemStack resultWithoutMending = e.getResult();
                ItemMeta metaWithoutMending = resultWithoutMending.getItemMeta();
                metaWithoutMending.removeEnchant(Enchantment.MENDING);
                resultWithoutMending.setItemMeta(metaWithoutMending);
                e.setResult(resultWithoutMending);
            }
        }

        // Apply book's mending effect to result
        applyMendingLevel(e, mendingCfg);
        Bukkit.getLogger().info("ok3");
    }


    private void applyMendingLevel(PrepareAnvilEvent e, MendingLevel mending)
    {
        AnvilInventory inv = e.getInventory();
        ItemStack result = e.getResult();

        if (mending.getEffect() == EffectType.REPAIR_COST)
        {
            inv.setRepairCost(0);
        
            
            if (result.getItemMeta() instanceof Repairable)
            {
                Repairable originalRepairableMeta = ((Repairable)inv.getFirstItem().getItemMeta());
                Repairable resultRepairableMeta = ((Repairable)result.getItemMeta());
                resultRepairableMeta.setRepairCost(originalRepairableMeta.getRepairCost() - mending.getRepairCostLevels());
                resultRepairableMeta.removeEnchant(Enchantment.MENDING);

                result.setItemMeta(resultRepairableMeta);
            }
        }
        else if (mending.getEffect() == EffectType.MENDING)
        {
            Bukkit.getLogger().info("mending " + mending.getLevel());
            ItemMeta meta = inv.getFirstItem().getItemMeta();
            meta.removeEnchant(Enchantment.MENDING);
            meta.addEnchant(Enchantment.MENDING, mending.getLevel(), true);
            result.setItemMeta(meta);
        }

        e.setResult(result);
    }
}
