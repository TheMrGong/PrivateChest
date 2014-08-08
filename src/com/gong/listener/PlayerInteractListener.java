package com.gong.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gong.main.Main;
import com.gong.util.Functions;

public class PlayerInteractListener implements Listener {
	Main plugin = Main.getInstance();
	
	@EventHandler
	public void interactEvent(PlayerInteractEvent ev)
	{
		if(ev.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if(ev.getClickedBlock().equals(Material.CHEST))
			{
				if(Functions.isPrivateChest(ev.getClickedBlock()))
				{
					plugin.justOpenedPrivateChest.put(ev.getPlayer().getUniqueId(), ev.getClickedBlock());
				}
			}
		}
	}
}
