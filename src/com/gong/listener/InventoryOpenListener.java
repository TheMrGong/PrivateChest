package com.gong.listener;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.gong.main.Main;
import com.gong.util.Functions;
import com.gong.util.OpenPrivateChestEvent;

public class InventoryOpenListener implements Listener {
	Main plugin = Main.getInstance();
	
	@EventHandler
	public void playerOpen(InventoryOpenEvent ev)
	{
		if(plugin.justOpenedPrivateChest.containsKey(ev.getPlayer().getUniqueId()))
		{
			Block opened = plugin.justOpenedPrivateChest.get(ev.getPlayer().getUniqueId());
			plugin.justOpenedPrivateChest.remove(ev.getPlayer().getUniqueId());
			OpenPrivateChestEvent open = new OpenPrivateChestEvent(opened, Functions.getPlayerFromUUID(ev.getPlayer().getUniqueId()), ev.getInventory(), Functions.isDoubleChest(opened), ev);
			Bukkit.getPluginManager().callEvent(open);
		}
	}
}
