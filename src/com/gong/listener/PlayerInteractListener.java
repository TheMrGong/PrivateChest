package com.gong.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
			if(ev.getClickedBlock().getType().equals(Material.CHEST))
			{
				if(Functions.isPrivateChest(ev.getClickedBlock()))
				{
					if(Functions.hasPermission(ev.getPlayer(), "privatechest.openchest"))
					{
						plugin.justOpenedPrivateChest.put(ev.getPlayer().getUniqueId(), ev.getClickedBlock());
					} else {
						ev.setCancelled(true);
						if(!ev.getPlayer().getGameMode().equals(GameMode.CREATIVE))
						{
							Functions.pushAwayEntity(ev.getPlayer(), 2.0, ev.getClickedBlock(), 0.5D);
							ev.getPlayer().damage(0D);
							ev.getPlayer().sendMessage(ChatColor.YELLOW+"SLAP!");
						}
						ev.getPlayer().sendMessage(ChatColor.RED+"You do not have permission: privatechest.placesign");
					}
				}
			}
		}
	}
}
