package com.gong.listener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gong.main.Main;
import com.gong.util.Functions;

public class TestCommand implements CommandExecutor {
	Main plugin = Main.getInstance();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length <= 0)
		{
		sender.sendMessage("Signs: "+Functions.getAmountRSigns());
		} else {
			sender.sendMessage("Upadating");
			Functions.updateSigns();
		}
		return true;
	}

}
