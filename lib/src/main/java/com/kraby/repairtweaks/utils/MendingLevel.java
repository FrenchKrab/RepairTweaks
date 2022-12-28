package com.kraby.repairtweaks.utils;

import org.bukkit.configuration.ConfigurationSection;

public class MendingLevel {
    
    public enum EffectType {
        MENDING,
        REPAIR_COST
    }

    private final ConfigurationSection _cfg;

    private final int _level;

    public MendingLevel(int level, ConfigurationSection cfg) {
        this._level = level;
        this._cfg = cfg;
    }

    public EffectType getEffect() {
        String s = _cfg.getString("effect", EffectType.MENDING.toString());
        EffectType t = EffectType.valueOf(s);
        return t;
    }

    public int getRepairCostLevels() {
        return _cfg.getInt("levels", 1);
    }

    public double getDurabilityPerXp()
    {
        return _cfg.getDouble("durability_per_xp", 2.0);
    }

    public int getLevel()
    {
        return _level;
    }
}
