package com.gong.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

public class OpenPrivateChestEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Block blockOpened;
	private boolean cancelled;
	private Player opener;
	private Inventory inventoryOpened;
	private boolean isDoubleChest;
	
	public OpenPrivateChestEvent(Block blockOpened, Player opener, Inventory inventoryOpened, boolean isDoubleChest)
	{
		this.setBlockOpened(blockOpened);
		this.setOpener(opener);
		this.setInventoryOpened(inventoryOpened);
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Block getBlockOpened() {
		return blockOpened;
	}

	public void setBlockOpened(Block blockOpened) {
		this.blockOpened = blockOpened;
	}

	public Player getOpener() {
		return opener;
	}

	public void setOpener(Player opener) {
		this.opener = opener;
	}

	public Inventory getInventoryOpened() {
		return inventoryOpened;
	}

	public void setInventoryOpened(Inventory inventoryOpened) {
		this.inventoryOpened = inventoryOpened;
	}

	public boolean isDoubleChest() {
		return isDoubleChest;
	}

	public void setDoubleChest(boolean isDoubleChest) {
		this.isDoubleChest = isDoubleChest;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
