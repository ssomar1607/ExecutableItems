package com.ssomar.executableitems.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.ssomar.executableitems.configs.ingame.ConfigGUIManager;
import com.ssomar.executableitems.configs.ingame.ShowGUI;
import com.ssomar.executableitems.configs.ingame.TestGUI;
import com.ssomar.executableitems.util.StringConverter;

public class InteractionGUI implements Listener{

	private static StringConverter sc= new StringConverter();

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {

		if(!(e.getWhoClicked() instanceof Player)) return;	

		String title= e.getView().getTitle();
		Player player = (Player) e.getWhoClicked();
		if(e.getClickedInventory().getType()==InventoryType.PLAYER) return;



		if(title.equals(sc.coloredString("&8&lEI Creator")) || title.contains(sc.coloredString("&8&lEI Editor"))) {
			e.setCancelled(true);
			if(e.getView().getItem(e.getSlot())!=null)
				ConfigGUIManager.getInstance().clicked(player, e.getView().getItem(e.getSlot()));

		}
		else if(title.contains(sc.coloredString("&8&lEI Show"))) {
			e.setCancelled(true);
			if(e.getClick()==ClickType.SHIFT_LEFT) {
				//no item select if catch
				try {
					if(e.getView().getItem(e.getSlot())!=null)
						new ShowGUI(player).shiftLeftClicked(player, e.getView().getItem(e.getSlot()).getItemMeta().getDisplayName());	
				}catch (NullPointerException error) {}
			}
			else {
				//no item select if catch
				try {
					if(e.getView().getItem(e.getSlot())!=null)
						new ShowGUI(player).clicked(player, e.getView().getItem(e.getSlot()).getItemMeta().getDisplayName(), e.getView().getTitle());	
				}catch (NullPointerException error) {}
			}	
		}
		else if(title.contains(sc.coloredString("&8&lEI Test"))) {
			e.setCancelled(true);

			//no item select if catch
			try {
				if(e.getView().getItem(e.getSlot())!=null)
					new TestGUI(player).clicked(player, e.getView().getItem(e.getSlot()).getItemMeta().getDisplayName(), e.getView().getTitle());	
			}catch (NullPointerException error) {}

		}
	}



	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent e) {

		Player p = e.getPlayer();
		if(ConfigGUIManager.getInstance().getRequestWriting().containsKey(p)) {
			e.setCancelled(true);
			ConfigGUIManager.getInstance().receivedMessage(p, e.getMessage());
		}
	}	
}
