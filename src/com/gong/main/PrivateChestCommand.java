package com.gong.main;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.gong.util.Functions;
import com.gong.util.ItemSerialization;
import com.gong.util.UUIDFetcher;

public class PrivateChestCommand implements CommandExecutor {
	Main plugin = Main.getInstance();

	String[] usage = {
			ChatColor.STRIKETHROUGH+""+ChatColor.GOLD+"------------["+ChatColor.RESET+ChatColor.DARK_GREEN+"Private"+ChatColor.AQUA+"Chest"+ChatColor.STRIKETHROUGH+""+ChatColor.GOLD+"]------------",
			ChatColor.RESET+""+ChatColor.DARK_AQUA+"Commands: ",
			ChatColor.GREEN+"/pr help"+ChatColor.GRAY+" - Shows you this menu",
			ChatColor.GREEN+"/pr"+ChatColor.GRAY+" - Shows you this menu",
			ChatColor.GREEN+"/pr open"+ChatColor.GRAY+" - Opens own private chest if exists or creates one",
			ChatColor.GREEN+"/pr open <Player>"+ChatColor.GRAY+" - Opens a players private chest if it exists"
	};
	String[] usageCMD = {
			"------------[PrivateChest]------------",
			"Commands: ",
			"/pr help - Shows you this menu",
			"/pr - Shows you this menu",
			"/pr open - Opens own private chest if exists or creates one",
			"/pr open <Player> - Opens a players private chest if it exists"
	};

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length == 2)
		{
			if(args[0].equalsIgnoreCase("open"))
			{
				if(Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.*")) || Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.open.other")))
				{
					if(sender instanceof Player)
					{
						Player p = (Player) sender;
						UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(args[1]));
						Map<String, UUID> response = null;
						try {
							response = fetcher.call();
						} catch (Exception e) {
							Bukkit.getLogger().warning("[PrivateChest] Exception while running UUIDFetcher");
							e.printStackTrace();
						}
						UUID id = response.get(args[1]);
						if(id != null)
						{
							if(Functions.hasPrivateChest(id.toString()))
							{
								Inventory chest = ItemSerialization.fromBase64(plugin.Config.getString("inv."+id.toString()), args[1]);
								p.openInventory(chest);
							} else {
								p.sendMessage(ChatColor.RED+args[1]+" dosen't have a PrivateChest");
							}
						} else p.sendMessage(ChatColor.RED+"Player not found. Name has to be specific and caps sensitive.");
					} else sender.sendMessage(ChatColor.RED+"Consoles can't open inventorys.");
				} else sender.sendMessage(ChatColor.RED+"You don't have permission: privatechest.cmd.open.other");
			}
		} else if(args.length == 1)
		{
			if(args[0].equalsIgnoreCase("help"))
			{
				if(Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.*")) || Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.help")))
				{
					if(sender instanceof Player)
					{
						sender.sendMessage(usage);
					} else {
						sender.sendMessage(usageCMD);
					}
				} else sender.sendMessage(ChatColor.RED+"You don't have permission: privatechest.cmd.help");
			} else if(args[0].equalsIgnoreCase("open"))
			{
				if(Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.*")) || Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.open")))
				{
					if(sender instanceof Player)
					{
						Player p = (Player) sender;
						if(Functions.hasPrivateChest(p))
						{
							Inventory chest = ItemSerialization.fromBase64(plugin.Config.getString("inv."+p.getUniqueId().toString()), p.getName());
							p.openInventory(chest);
						} else {
							p.sendMessage(ChatColor.GREEN+"PrivateChest not found, generating new one");
							Inventory defaultPrivateChest = Bukkit.createInventory(null, 27, ChatColor.BLACK+"["+ChatColor.AQUA+"PrivateChest"+ChatColor.BLACK+"] ");
							plugin.Config.set("inv."+p.getUniqueId().toString(), ItemSerialization.toBase64(defaultPrivateChest));
							plugin.Config.saveConfig();
							p.openInventory(defaultPrivateChest);
						}
					} else sender.sendMessage(ChatColor.RED+"Consoles don't have a PrivateChest. ");
				} else sender.sendMessage(ChatColor.RED+"You don't have permission: privatechest.cmd.open");
			}
		} else if(args.length == 0)
		{
			if(Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.*")) || Functions.hasPermission(sender, Functions.getPermissionFromString("privatechest.cmd.help")))
			{
				if(sender instanceof Player)
				{
					sender.sendMessage(usage);
				} else {
					sender.sendMessage(usageCMD);
				}
			} else sender.sendMessage(ChatColor.RED+"You don't have permission: privatechest.cmd.help");
		}
		return true;
	}
}
