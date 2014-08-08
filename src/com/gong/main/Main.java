package com.gong.main;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import com.gong.listener.InventoryCloseListener;
import com.gong.listener.InventoryOpenListener;
import com.gong.listener.PlayerInteractListener;
import com.gong.listener.PlayerOpenPrivateChestListener;

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
		Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerOpenPrivateChestListener(), this);
		quickLog("["+this.getName()+"] Enabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(1).toUpperCase());
	}
	
	@Override
	public void onDisable()
	{
		quickLog("["+this.getName()+"] Disabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(1).toUpperCase());
	}
	
	public void quickLog(String arg0){Bukkit.getLogger().info(arg0);}
}
