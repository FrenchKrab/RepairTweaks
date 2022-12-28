package com.kraby.repairtweaks.utils;

import org.bukkit.plugin.Plugin;

public class MainConfig extends ConfigAccessor {

	private static final String MENDING_BEHAVIOUR_PREFIX = "mending_behaviour";

	public MainConfig(Plugin plugin, String fileName) {
		super(plugin, fileName);
	}

	public boolean isRepairDontIncreaseCost () {
		return config.getBoolean("repair_dont_increase_cost", false);
	}
	
	public boolean isMendingBehaviourEnabled() {
		return config.getBoolean(MENDING_BEHAVIOUR_PREFIX + ".enabled", false);
	}

	public MendingLevel getMendingLevel(int level)
	{
		return new MendingLevel(level, config.getConfigurationSection(MENDING_BEHAVIOUR_PREFIX + "." + String.valueOf(level)));
	}

}
