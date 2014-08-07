package com.gong.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import com.gong.main.Main;
import com.gong.util.ItemSerialization;
import com.gong.util.OpenPrivateChestEvent;

public class PlayerOpenPrivateChestListener implements Listener {
	Main plugin = Main.getInstance();
	
	@EventHandler
	public void openInv(OpenPrivateChestEvent ev)
	{
		ev.getOpener().closeInventory();
		if(plugin.getConfig().contains("inv."+ev.getOpener().getUniqueId().toString()))
		{
			Inventory get = ItemSerialization.fromBase64(plugin.getConfig().getString("inv."+ev.getOpener().getUniqueId().toString()), ev.getOpener().getName());
			ev.getOpener().openInventory(get);
		} else {
			if(ev.isDoubleChest() == true)
			{
				Inventory defaultPrivateChest = Bukkit.createInventory(null, 54, ChatColor.BLACK+"["+ChatColor.AQUA+"PrivateInventory"+ChatColor.BLACK+"] "+ChatColor.GOLD+ev.getOpener().getName());
				plugin.getConfig().set("inv."+ev.getOpener().getUniqueId().toString(), ItemSerialization.toBase64(defaultPrivateChest));
				ev.getOpener().openInventory(defaultPrivateChest);
			} else {
				Inventory defaultPrivateChest = Bukkit.createInventory(null, 27, ChatColor.BLACK+"["+ChatColor.AQUA+"PrivateInventory"+ChatColor.BLACK+"] "+ChatColor.GOLD+ev.getOpener().getName());
				plugin.getConfig().set("inv."+ev.getOpener().getUniqueId().toString(), ItemSerialization.toBase64(defaultPrivateChest));
				ev.getOpener().openInventory(defaultPrivateChest);
			}
		}
	}
}
