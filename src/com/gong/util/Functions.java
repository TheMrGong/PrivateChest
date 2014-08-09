package com.gong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

import com.gong.main.Main;

public class Functions {
	Main plugin = Main.getInstance();
	public static List<Block> getSuroundingBlocksNY(Block arg0)
	{
		List<Block> ret = new ArrayList<Block>();
		for (int x = -(1); x <= 1; x++)
		{
			for (int y = -(1); y <= 1; y++)
			{
				for (int z = -(1); z <= 1; z++)
				{
					Block loc = arg0.getRelative(x, y, z);
					ret.add(loc);
				}
			}
		}
		return ret;
	}

	public static boolean hasPermission(Player p, String perm)
	{
		if(p.hasPermission(perm))
		{
			return true;
		}
		if(p.isOp())
		{
			return true;
		}
		if(p.hasPermission("privatechest.*"))
		{
			return true;
		}
		return false;
	}

	public static boolean hasPermission(CommandSender p, String perm)
	{
		if(p.hasPermission(perm))
		{
			return true;
		}
		if(p.isOp())
		{
			return true;
		}
		if(p.hasPermission("privatechest.*"))
		{
			return true;
		}
		return false;
	}

	public static Permission getPermissionFromString(String perm)
	{
		for(Permission b : Main.getInstance().getDescription().getPermissions())
		{
			if(b.getName().equalsIgnoreCase(perm))
			{
				return b;
			}
		}
		return null;
	}

	public static boolean hasPermission(Player p, Permission perm)
	{
		if(p.hasPermission(perm))
		{
			return true;
		}
		if(p.isOp())
		{
			return true;
		}
		if(p.hasPermission(getPermissionFromString("privatechest.*")))
		{
			return true;
		}
		return false;
	}

	public static boolean hasPrivateChest(Player arg0)
	{
		String id = arg0.getUniqueId().toString();
		for(Object b : Main.getInstance().Config.getConfigurationSection("inv").getKeys(false))
		{
			String s = (String) b;
			if(s.equalsIgnoreCase(id))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean hasPrivateChest(String arg0)
	{
		String id = arg0;
		for(Object b : Main.getInstance().Config.getConfigurationSection("inv").getKeys(false))
		{
			String s = (String) b;
			if(s.equalsIgnoreCase(id))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean hasPrivateChest(UUID arg0)
	{
		String id = arg0.toString();
		for(Object b : Main.getInstance().Config.getConfigurationSection("inv").getKeys(false))
		{
			String s = (String) b;
			if(s.equalsIgnoreCase(id))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean hasPermission(CommandSender p, Permission perm)
	{
		if(p.hasPermission(perm))
		{
			return true;
		}
		if(p.isOp())
		{
			return true;
		}
		if(p.hasPermission(getPermissionFromString("privatechest.*")))
		{
			return true;
		}
		return false;
	}

	public static List<Block> privateChestNear(Sign b)
	{
		List<Block> ret = new ArrayList<Block>();
		for(Block i : getSuroundingBlocksNY(b.getLocation().getBlock()))
		{
			if(i.getType().equals(Material.CHEST))
			{
				ret.add(i);
			}
		}
		return ret;
	}

	public static List<Block> getNearbySigns(Block b)
	{
		List<Block> ret = new ArrayList<Block>();
		for(Block i : getSuroundingBlocksNY(b))
		{
			if(i.getType().equals(Material.WALL_SIGN) || i.getType().equals(Material.SIGN) || i.getType().equals(Material.SIGN_POST))
			{
				ret.add(i);
			}
		}
		return ret;
	}

	public static void pushAwayEntity(Player entity, double speed, Block block) {
		// Ignore if the player isn't online:
		// Get velocity unit vector:
		Vector unitVector = entity.getLocation().toVector().subtract(block.getLocation().toVector()).normalize();
		// Set speed and push entity:
		entity.setVelocity(unitVector.multiply(speed));
	}

	public static void pushAwayEntity(Player entity, double speed, Block block, double up) {
		// Ignore if the player isn't online:
		// Get velocity unit vector:
		Vector unitVector = entity.getLocation().toVector().subtract(block.getLocation().toVector()).normalize();
		// Set speed and push entity:
		Vector adding = new Vector(0, up, 0);
		unitVector.add(adding);
		entity.setVelocity(unitVector.multiply(speed));
	}

	public static String isRegisteredSign(Location arg0)
	{
		for(Object f : Main.getInstance().Config.getConfigurationSection("sign").getKeys(false))
		{
			String g = (String) f;
			Location b = new Location(Bukkit.getWorld(Main.getInstance().Config.getString("sign."+g+".world")), Main.getInstance().Config.getInt("sign."+g+".x"), Main.getInstance().Config.getInt("sign."+g+".y"), Main.getInstance().Config.getInt("sign."+g+".z"));
			if(arg0.getBlockX() == b.getBlockX())
			{
				if(arg0.getBlockY() == b.getBlockY())
				{
					if(arg0.getBlockZ() == b.getBlockZ())
					{
						return g;
					}
				}
			}
		}
		return null;
	}


	/**
	 * 
	 * @param sign
	 * @param block
	 * @return If "block" is a registered block of "sign"
	 */
	public static String isRegisteredChest(String sign, Location block)
	{
		if(Main.getInstance().Config.contains("sign."+sign))
		{
			ConfigurationSection conf = Main.getInstance().Config.getConfigurationSection("sign."+sign+".chest");
			for(Object f : conf.getKeys(false))
			{
				String g = (String) f;
				Location chest = new Location(Bukkit.getWorld(conf.getString(g+".world")), conf.getInt(g+".x"), conf.getInt(g+".y"), conf.getInt(g+".z"));
				if(chest.getBlockX() == block.getBlockX())
				{
					if(chest.getBlockY() == block.getBlockY())
					{
						if(chest.getBlockZ() == block.getBlockZ())
						{
							return g;
						}
					}
				}
			}
		}
		return null;
	}

	public static List<String> getBelongingSigns(Block chestB)
	{
		List<String> signs = new ArrayList<String>();
		for(Object f : Main.getInstance().Config.getConfigurationSection("sign").getKeys(false))
		{
			String sign = (String) f;
			for(Object v : Main.getInstance().Config.getConfigurationSection("sign."+sign+".chest").getKeys(false))
			{
				String chest = (String) v;
				ConfigurationSection conf = Main.getInstance().Config.getConfigurationSection("sign."+sign+".chest."+chest);
				Block h = new Location(Bukkit.getWorld(conf.getString("world")), conf.getInt("x"), conf.getInt("y"), conf.getInt("z")).getBlock();
				if(h.getX() == chestB.getX())
				{
					if(h.getY() == chestB.getY())
					{
						if(h.getZ() == chestB.getZ())
						{
							signs.add(sign);
						}
					}
				}
			}
		}
		return signs;
	}

	public static String getChestID(Block chest)
	{
		if(!Functions.getBelongingSigns(chest).isEmpty())
		{
			for(String sign : Functions.getBelongingSigns(chest))
			{
				for(Object b : Main.getInstance().Config.getConfigurationSection("sign."+sign+".chest").getKeys(false))
				{
					String chestb = (String) b;
					Block x = new Location(Bukkit.getWorld(Main.getInstance().Config.getString("sign."+sign+".chest."+b+".world")), Main.getInstance().Config.getInt("sign."+sign+".chest."+b+".x"), Main.getInstance().Config.getInt("sign."+sign+".chest."+b+".y"), Main.getInstance().Config.getInt("sign."+sign+".chest."+b+".z")).getBlock();
					if(x.getX() == chest.getX())
					{
						if(x.getY() == chest.getY())
						{
							if(x.getZ() == chest.getZ())
							{
								return chestb;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static List<Sign> getRegisteredSigns()
	{
		List<Sign> ret = new ArrayList<Sign>();
		if(Main.getInstance().Config.getConfigurationSection("sign").getKeys(false) != null)
		{
			for(Object f : Main.getInstance().Config.getConfigurationSection("sign").getKeys(false))
			{
				String g = (String) f;
				ConfigurationSection sec = Main.getInstance().Config.getConfigurationSection("sign."+g);
				Block b = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z")).getBlock();
				if(b.getState() instanceof Sign)
				{
					ret.add((Sign) b.getState());
				}
			}
		}
		return ret;
	}

	@SuppressWarnings("unused")
	public static int getAmountRSigns()
	{
		int ret = 0;
		if(getRegisteredSigns() != null)
		{
			for(Object f : Main.getInstance().Config.getConfigurationSection("sign").getKeys(false))
			{
				ret += 1;
			}
		}
		return ret;
	}

	public static int getAmountInventories()
	{
		int ret = 0;
		for(@SuppressWarnings("unused") Object f : Main.getInstance().Config.getConfigurationSection("inv").getKeys(false))
		{
			ret += 1;
		}
		return ret;
	}

	public static void updateSigns()
	{
		for(Object f : Main.getInstance().Config.getConfigurationSection("sign").getKeys(false))
		{
			String g = (String) f;
			ConfigurationSection sec = Main.getInstance().Config.getConfigurationSection("sign."+g);
			Block b = new Location(Bukkit.getWorld(sec.getString("world")), sec.getInt("x"), sec.getInt("y"), sec.getInt("z")).getBlock();
			if(b.getState() instanceof Sign)
			{
				Sign b2 = (Sign) b.getState();
				b2.setLine(0, "[PrivateChest]");
				b2.setLine(1, "");
				b2.setLine(2, "Current Signs:");
				b2.setLine(3, getAmountRSigns()+"");
				b2.update();
			}
		}
	}

	public static boolean isDoubleChest(Block arg0)
	{
		boolean ret = false;
		for(Block t : getSuroundingBlocksNY(arg0))
		{
			if(t.getType().equals(Material.CHEST))
			{
				return true;
			}
		}
		return ret;
	}
	public static boolean isPrivateChest(Block arg0)
	{
		boolean isPrivateChest = false;
		for(Block b : Functions.getSuroundingBlocksNY(arg0))
		{
			if(b.getType().equals(Material.SIGN) || b.getType().equals(Material.SIGN_POST) || b.getType().equals(Material.WALL_SIGN))
			{
				Sign sign = (Sign)b.getState();
				if(sign.getLines()[0].toLowerCase().contains("[privatechest]"))
				{
					isPrivateChest = true;
				}
			}
		}
		return isPrivateChest;
	}

	public static Player getPlayerFromUUID(UUID arg0)
	{
		for(Player t : Bukkit.getOnlinePlayers())
		{
			if(t.getUniqueId() == arg0)
			{
				return t;
			}
		}
		return null;
	}
	public static List<Block> getBlocks(Location pos1, Location pos2)
	{
		List<Block> blocks = new ArrayList<Block>();

		int topBlockX = (pos1.getBlockX() < pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());
		int bottomBlockX = (pos1.getBlockX() > pos2.getBlockX() ? pos2.getBlockX() : pos1.getBlockX());

		int topBlockY = (pos1.getBlockY() < pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());
		int bottomBlockY = (pos1.getBlockY() > pos2.getBlockY() ? pos2.getBlockY() : pos1.getBlockY());

		int topBlockZ = (pos1.getBlockZ() < pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());
		int bottomBlockZ = (pos1.getBlockZ() > pos2.getBlockZ() ? pos2.getBlockZ() : pos1.getBlockZ());

		for(int x = bottomBlockX; x <= topBlockX; x++)
		{
			for(int z = bottomBlockZ; z <= topBlockZ; z++)
			{
				for(int y = bottomBlockY; y <= topBlockY; y++)
				{
					Block block = pos1.getWorld().getBlockAt(x, y, z);

					blocks.add(block);
				}
			}
		}

		return blocks;
	}
}
