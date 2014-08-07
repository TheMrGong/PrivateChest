package com.gong.main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	static Main plugin;
	
	public HashMap<UUID, Block> justOpenedPrivateChest = new HashMap<UUID, Block>();
	public static Main getInstance()
	{
		return plugin;
	}

	@Override
	public void onEnable()
	{
		plugin = this;
		quickLog("["+this.getName()+"] Enabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(0).toUpperCase());
	}
	
	@Override
	public void onDisable()
	{
		quickLog("["+this.getName()+"] Disabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(0).toUpperCase());
	}
	
	public void quickLog(String arg0){Bukkit.getLogger().info(arg0);}
}
