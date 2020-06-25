package com.ssomar.executableitems.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.ItemsHandler;

public class PlayerDeath implements Listener{

	private Map<String, List<ItemStack>> savedItems= new HashMap<>();

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();
		List<ItemStack> remove= new ArrayList<>();
		for(ItemStack item :e.getDrops()) {

			ItemMeta itemMeta;
			try {
				if(!item.hasItemMeta()) continue;
				itemMeta= item.getItemMeta();
			}catch(NullPointerException error){
				continue;
			}

			if(!itemMeta.hasLore()) continue;
			List<String> lore = itemMeta.getLore();

			if(!itemMeta.hasDisplayName()) continue;
			String displayName = itemMeta.getDisplayName();

			Material material= item.getType();

			if(!itemsHandler.containsByNameLoreMaterial(displayName, lore, material)) continue;

			if(itemsHandler.getItemByNameLoreMaterial(displayName, lore, material).isKeepItemOnDeath()) remove.add(item);
		}
		for(ItemStack item :remove) {
			e.getDrops().remove(item);	
		}
		savedItems.put(p.getName(), remove);

	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent e) {
		Player p=e.getPlayer();

		if(savedItems.containsKey(p.getName())) {
			for(ItemStack is: savedItems.get(p.getName())) {
				p.getInventory().addItem(is);		
			}
			savedItems.remove(p.getName());
		}

	} 
}
