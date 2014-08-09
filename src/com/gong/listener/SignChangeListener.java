package com.gong.listener;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.gong.main.Main;
import com.gong.util.Functions;

public class SignChangeListener implements Listener {
	Main plugin = Main.getInstance();

	@EventHandler
	public void signChange(SignChangeEvent ev)
	{
		if(ev.getLines()[0].contains("[privatechest]"))
		{
			if(Functions.hasPermission(ev.getPlayer(), "privatechest.placesign"))
			{
				if(!Functions.privateChestNear((Sign)ev.getBlock().getState()).isEmpty())
				{

					ev.setLine(0, "[PrivateChest]");
					String id = UUID.randomUUID().toString();
					plugin.Config.set("sign."+id+".x", ev.getBlock().getX());
					plugin.Config.set("sign."+id+".y", ev.getBlock().getY());
					plugin.Config.set("sign."+id+".z", ev.getBlock().getZ());
					plugin.Config.set("sign."+id+".world", ev.getBlock().getWorld().getName());
					for(Block b : Functions.privateChestNear((Sign)ev.getBlock().getState()))
					{
						String uid = UUID.randomUUID().toString();
						plugin.Config.set("sign."+id+".chest."+uid+".x", b.getX());
						plugin.Config.set("sign."+id+".chest."+uid+".y", b.getY());
						plugin.Config.set("sign."+id+".chest."+uid+".z", b.getZ());
						plugin.Config.set("sign."+id+".chest."+uid+".world", b.getWorld().getName());
					}
					plugin.Config.saveConfig();
					ev.setLine(2, "Current Signs:");
					ev.setLine(3, Functions.getAmountRSigns()+"");
					Functions.updateSigns();
				} else {
					ev.getBlock().breakNaturally();
					ev.getPlayer().sendMessage(ChatColor.RED+"Chest not found!");
				}
			} else {
				ev.getBlock().breakNaturally();
				if(!ev.getPlayer().getGameMode().equals(GameMode.CREATIVE))
				{
					Functions.pushAwayEntity(ev.getPlayer(), 2.0, ev.getBlock(), 0.5D);
					ev.getPlayer().damage(0D);
					ev.getPlayer().sendMessage(ChatColor.YELLOW+"SLAP!");
				}
				ev.getPlayer().sendMessage(ChatColor.RED+"You do not have permission: privatechest.placesign");
			}
		}
	}
}
