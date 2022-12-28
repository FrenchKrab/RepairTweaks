package com.kraby.repairtweaks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.kraby.repairtweaks.RepairTweaks;

public class ReloadConfig implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		RepairTweaks.singleton.loadConfig();
		sender.sendMessage("Reloaded RepairTweaks configuration");
		return true;
	}
	
}
