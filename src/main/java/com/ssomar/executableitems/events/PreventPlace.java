package com.ssomar.executableitems.events;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.ItemsHandler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PreventPlace  implements Listener{

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		
		ItemStack item =e.getItemInHand();
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

		if(itemsHandler.containsByNameLoreMaterial(displayName, lore, material)) e.setCancelled(true);;
	}
}
