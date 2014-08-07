package com.gong.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.gong.main.Main;

public class Functions {
	Main plugin = Main.getInstance();
	
	public static List<Block> getSuroundingBlocks(Block arg0)
	{
		List<Block> ret = new ArrayList<Block>();
		
		Location pos1 = arg0.getLocation().add(-1, -1, -1);
		Location pos2 = arg0.getLocation().add(1, 1, 1);
		ret = getBlocks(pos1, pos2);
		for(Block b : ret)
		{
			if(b.getLocation().equals(arg0.getLocation()))
			{
				ret.remove(b);
			}
		}
		return ret;
	}
	public static List<Block> getSuroundingBlocksNY(Block arg0)
	{
		List<Block> ret = new ArrayList<Block>();
		
		Location pos1 = arg0.getLocation().add(-1, 0, -1);
		Location pos2 = arg0.getLocation().add(1, 0, 1);
		ret = getBlocks(pos1, pos2);
		for(Block b : ret)
		{
			if(b.getLocation().equals(arg0.getLocation()))
			{
				ret.remove(b);
			}
		}
		return ret;
	}
	public static boolean isPrivateChest(Block arg0)
	{
		boolean isPrivateChest = false;
		for(Block b : Functions.getSuroundingBlocks(arg0))
		{
			if(b.equals(Material.SIGN) || b.equals(Material.SIGN_POST) || b.equals(Material.WALL_SIGN))
			{
				Sign sign = (Sign)b;
				sign.getLines()[0].toLowerCase().contains("privatechest");
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
