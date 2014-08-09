package com.gong.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.plugin.java.JavaPlugin;

import com.gong.config.MyConfig;
import com.gong.config.MyConfigManager;
import com.gong.listener.BlockBreakListener;
import com.gong.listener.InventoryCloseListener;
import com.gong.listener.InventoryOpenListener;
import com.gong.listener.PlayerInteractListener;
import com.gong.listener.PlayerOpenPrivateChestListener;
import com.gong.listener.SignChangeListener;
import com.gong.main.Metrics.Graph;
import com.gong.util.Functions;
import com.gong.util.Updater;

public class Main extends JavaPlugin {
	static Main plugin;
	
	public HashMap<UUID, Block> justOpenedPrivateChest = new HashMap<UUID, Block>();
	public MyConfig Config;
	public MyConfigManager manager;
	public static Main getInstance()
	{
		return plugin;
	}
	
	public static void closeInventory(Chest b)
	{
		b.getBlock().setType(Material.AIR);
		b.getBlock().setType(Material.CHEST);
	}
	
	public Main addDefault(String path)
	{
		if(!Config.contains(path))
		{
			Config.createSection(path);
			Config.saveConfig();
		}
		return this;
	}
	
	public Main addDefault(String path, Object value)
	{
		if(!Config.contains(path))
		{
			Config.set(path, value);
			Config.saveConfig();
		}
		return this;
	}
	
	public Main addDefault(String path, Object value, String header)
	{
		if(!Config.contains(path))
		{
			Config.set(path, value, header);
			Config.saveConfig();
		}
		return this;
	}
	
	@Override
	public void onEnable()
	{
		if(Bukkit.getServer().getOnlineMode() == false)
		{
			Bukkit.getLogger().warning("[PrivateChest] Plugin cannot function with online-mode false.");
			Bukkit.getLogger().warning("[PrivateChest] Change to true and restart the server for this plugin to work.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		plugin = this;
		manager = new MyConfigManager(this);
		Config = manager.getNewConfig("config.yml", new String[]{"Config for", "PrivateChest"});
		addDefault("opt-out", false, "Set to \"true\" if you dont want metrics on!").addDefault("auto-update", "update", "Set to \"check\" to check for an update and log into console if a update is found. Set to \"update\" to automaticly download an update when it is found. Set to \"none\" to do nothing when an update is found.").addDefault("sign").addDefault("inv");
		Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
		Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerOpenPrivateChestListener(), this);
		Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
		Bukkit.getPluginManager().registerEvents(new BlockBreakListener(), this);
		
		this.getCommand("privatechest").setExecutor(new PrivateChestCommand());
		
		if(!Config.getBoolean("opt-out"))
		{
			 try {
			        Metrics metrics = new Metrics(this);
			        Graph inventorysCreated = metrics.createGraph("Saved in yml");
			        inventorysCreated.addPlotter(new Metrics.Plotter("Inventories Created") {
						@Override
						public int getValue() {
							return Functions.getAmountInventories();
						}
					});
			        inventorysCreated.addPlotter(new Metrics.Plotter("Signs registered") {
						@Override
						public int getValue() {
							return Functions.getAmountRSigns();
						}
					});
			        metrics.start();
			    } catch (IOException e) {
			        // Failed to submit the stats :-(
			    }
		}
		String type = Config.getString("auto-update");
		if(type.equalsIgnoreCase("update"))
		{
			Bukkit.getLogger().info("[PrivateChest] Checking for updates...");
			Updater updater = new Updater(this, 83724, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if(updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
			{
				Bukkit.getLogger().info("[PrivateChest] Update found, downloading update...");
				Updater updater2 = new Updater(this, 83724, this.getFile(), Updater.UpdateType.DEFAULT, true);
				if(updater2.getResult() == Updater.UpdateResult.FAIL_APIKEY)
				{
					Bukkit.getLogger().severe("[PrivateChest] The server administrator has improperly configured their API key in the configuration.");
				} else if(updater2.getResult() == Updater.UpdateResult.FAIL_BADID)
				{
					Bukkit.getLogger().severe("[PrivateChest] Invalid project ID, please contact the developer.");
				} else if(updater2.getResult() == Updater.UpdateResult.FAIL_DBO)
				{
					Bukkit.getLogger().severe("[PrivateChest] Failed to connect to dev.bukkit.org");
				} else if(updater2.getResult() == Updater.UpdateResult.FAIL_DOWNLOAD)
				{
					Bukkit.getLogger().severe("[PrivateChest] Unable to download file");
				} else if(updater2.getResult() == Updater.UpdateResult.FAIL_NOVERSION)
				{
					Bukkit.getLogger().severe("[PrivateChest] File did not contain recognizable version");
				} else if(updater2.getResult() == Updater.UpdateResult.NO_UPDATE)
				{
					Bukkit.getLogger().info("[PrivateChest] No update found");
				} else if(updater2.getResult() == Updater.UpdateResult.SUCCESS)
				{
					Bukkit.getLogger().info("[PrivateChest] Succesfully downloaded version "+updater2.getLatestName().split("v.")[2]);
				}
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_BADID)
			{
				Bukkit.getLogger().severe("[PrivateChest] Invalid project ID, please contact the developer.");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_DBO)
			{
				Bukkit.getLogger().severe("[PrivateChest] Failed to connect to dev.bukkit.org");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_DOWNLOAD)
			{
				Bukkit.getLogger().severe("[PrivateChest] Unable to download file");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_NOVERSION)
			{
				Bukkit.getLogger().severe("[PrivateChest] File did not contain recognizable version");
			} else if(updater.getResult() == Updater.UpdateResult.NO_UPDATE)
			{
				Bukkit.getLogger().info("[PrivateChest] No update found");
			}
		} else if(type.equalsIgnoreCase("check"))
		{
			Updater updater = new Updater(this, 83724, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			if(updater.getResult() == Updater.UpdateResult.SUCCESS)
			{
				Bukkit.getLogger().info("[PrivateChest] Update found, download at "+updater.getLatestFileLink());
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_BADID)
			{
				Bukkit.getLogger().severe("[PrivateChest] Invalid project ID, please contact the developer.");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_DBO)
			{
				Bukkit.getLogger().severe("[PrivateChest] Failed to connect to dev.bukkit.org");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_DOWNLOAD)
			{
				Bukkit.getLogger().severe("[PrivateChest] Unable to download file");
			} else if(updater.getResult() == Updater.UpdateResult.FAIL_NOVERSION)
			{
				Bukkit.getLogger().severe("[PrivateChest] File did not contain recognizable version");
			} else if(updater.getResult() == Updater.UpdateResult.NO_UPDATE)
			{
				Bukkit.getLogger().info("[PrivateChest] No update found");
			}
		} else if(type.equalsIgnoreCase("none"))
		{
			Bukkit.getLogger().info("[PrivateChest] Updates disabled.");
		}
		quickLog("["+this.getName()+"] Enabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(1).toUpperCase());
	}
	
	@Override
	public void onDisable()
	{
		quickLog("["+this.getName()+"] Disabled v."+this.getDescription().getVersion()+" "+this.getDescription().getAuthors().get(1).toUpperCase());
	}
	
	public void quickLog(String arg0){Bukkit.getLogger().info(arg0);}
}
