package com.gong.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.gong.main.Main;
import com.gong.util.ItemSerialization;

public class InventoryCloseListener implements Listener {
	Main plugin = Main.getInstance();
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent ev)
	{
		if(ev.getInventory().getName().toLowerCase().contains("privatechest"))
		{
			String uuid = ev.getPlayer().getUniqueId().toString();
			String inventory = ItemSerialization.toBase64(ev.getInventory());
			plugin.Config.set("inv."+uuid, inventory);
			plugin.Config.saveConfig();
		}
	}
}
