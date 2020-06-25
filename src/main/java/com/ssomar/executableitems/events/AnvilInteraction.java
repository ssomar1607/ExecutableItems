package com.ssomar.executableitems.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.ItemsHandler;

public class AnvilInteraction implements Listener{

	@EventHandler
	public void onItemMoveInventory(InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		
		ItemStack item = e.getClick() == ClickType.NUMBER_KEY ? p.getInventory().getItem(e.getHotbarButton()) : e.getCurrentItem();

		ItemMeta itemMeta;
		try {
			if(!item.hasItemMeta()) return;
			itemMeta= item.getItemMeta();
		}catch(NullPointerException error){
			return;
		}

		if(!itemMeta.hasLore()) return;
		List<String> lore = itemMeta.getLore();

		if(!itemMeta.hasDisplayName()) return;
		String displayName = itemMeta.getDisplayName();

		Material material= item.getType();

		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();
		if(!itemsHandler.containsByNameLoreMaterial(displayName, lore, material)) return;

		if(e.getView().getType()==InventoryType.ANVIL) {
			e.setCancelled(true);
			return;
		}

	}

	//We cant move item hopper-> anvil so it is probably useless but to prevent :D
	@EventHandler
	public void onItemMoveInventoryHopper(InventoryMoveItemEvent e) {
		ItemStack item =e.getItem();
		ItemMeta itemMeta;
		try {
			if(!item.hasItemMeta()) return;
			itemMeta= item.getItemMeta();
		}catch(NullPointerException error){
			return;
		}

		if(!itemMeta.hasLore()) return;
		List<String> lore = itemMeta.getLore();

		if(!itemMeta.hasDisplayName()) return;
		String displayName = itemMeta.getDisplayName();

		Material material= item.getType();

		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();
		if(!itemsHandler.containsByNameLoreMaterial(displayName, lore, material)) return;
		if(e.getDestination().getType()==InventoryType.ANVIL) {
			e.setCancelled(true);
			return;
		}
	}
}
