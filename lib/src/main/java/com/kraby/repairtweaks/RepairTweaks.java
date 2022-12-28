/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.kraby.repairtweaks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.kraby.repairtweaks.commands.ReloadConfig;
import com.kraby.repairtweaks.listeners.MendingBehaviourChangesListener;
import com.kraby.repairtweaks.listeners.RepairCostKeeperListener;
import com.kraby.repairtweaks.utils.MainConfig;

public final class RepairTweaks extends JavaPlugin {
	public static RepairTweaks singleton;

	public MainConfig config;

    public void onEnable() {
		singleton = this;

		loadConfig();
		
		this.getServer().getPluginManager().registerEvents(new MendingBehaviourChangesListener(), this);
		this.getServer().getPluginManager().registerEvents(new RepairCostKeeperListener(), this);
		
		getCommand("rtreload").setExecutor(new ReloadConfig());

		Bukkit.getLogger().info("[RepairTweaks] Plugin loaded successfully");
    }


	public void loadConfig()
	{
		config = new MainConfig(this, "config.yml");
	}
}