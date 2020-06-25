package com.ssomar.executableitems.actionbar;

import com.ssomar.executableitems.items.Item;
import org.bukkit.scheduler.BukkitTask;

public class InfoActionbar {
	
	private String playerName;
	
	private Item item;
	
	private boolean Active;
	
	private BukkitTask task;

	

	public InfoActionbar(String playerName, Item item, boolean active, BukkitTask task) {
		super();
		this.playerName = playerName;
		this.item = item;
		Active = active;
		this.task = task;
	}
	

	public BukkitTask getTask() {
		return task;
	}
	
	public void setTask(BukkitTask task) {
		this.task = task;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public boolean isActive() {
		return Active;
	}

	public void setActive(boolean active) {
		Active = active;
	}
}
