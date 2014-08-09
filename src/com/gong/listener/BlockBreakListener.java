package com.gong.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.gong.main.Main;
import com.gong.util.Functions;

public class BlockBreakListener implements Listener {
	Main plugin = Main.getInstance();

	@EventHandler
	public void blockBreak(BlockBreakEvent ev)
	{
		if(ev.getBlock().getType() == Material.SIGN || ev.getBlock().getType() == Material.SIGN_POST || ev.getBlock().getType() == Material.WALL_SIGN)
		{
			if(Functions.isRegisteredSign(ev.getBlock().getLocation())!= null)
			{
				if(Functions.hasPermission(ev.getPlayer(), Functions.getPermissionFromString("privatechest.breaksign")))
				{
					String id = Functions.isRegisteredSign(ev.getBlock().getLocation());
					if(plugin.Config.contains("sign."+id))
					{
						plugin.Config.removeKey("sign."+id);
						plugin.Config.saveConfig();
						Functions.updateSigns();
					}
				} else {
					ev.setCancelled(true);
					if(!ev.getPlayer().getGameMode().equals(GameMode.CREATIVE))
					{
						Functions.pushAwayEntity(ev.getPlayer(), 2.0, ev.getBlock(), 0.5D);
						ev.getPlayer().damage(0D);
						ev.getPlayer().sendMessage(ChatColor.YELLOW+"SLAP!");
					}
					ev.getPlayer().sendMessage(ChatColor.RED+"You do not have permission: privatechest.breaksign");
				}
			}
		} else if(ev.getBlock().getType() == Material.CHEST)
		{
			for(Block b : Functions.getSuroundingBlocksNY(ev.getBlock()))
			{
				if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
				{
					if(Functions.isRegisteredSign(b.getLocation()) != null)
					{
						if(Functions.hasPermission(ev.getPlayer(), Functions.getPermissionFromString("privatechest.breaksign")))
						{
							ev.setCancelled(true);
							ev.getPlayer().sendMessage(ChatColor.RED+"Please break all [PrivateChest] signs arround the chest before breaking the chest");
						} else {
							ev.setCancelled(true);
							if(!ev.getPlayer().getGameMode().equals(GameMode.CREATIVE))
							{
								Functions.pushAwayEntity(ev.getPlayer(), 2.0, ev.getBlock(), 0.5D);
								ev.getPlayer().damage(0D);
								ev.getPlayer().sendMessage(ChatColor.YELLOW+"SLAP!");
							}
							ev.getPlayer().sendMessage(ChatColor.RED+"You do not have permission: privatechest.breaksign");
						}
					}
				}
			}
		}
	}
}
