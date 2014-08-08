package com.gong.listener;

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
		if(Functions.isRegisteredSign(ev.getBlock().getLocation())!= null)
		{
			String id = Functions.isRegisteredSign(ev.getBlock().getLocation());
			if(plugin.Config.contains("sign."+id))
			{
				plugin.Config.removeKey("sign."+id);
				plugin.Config.saveConfig();
			}
		}
	}
}
