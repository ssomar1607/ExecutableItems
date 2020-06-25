package com.ssomar.executableitems.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.Item;

public class PlayerFirstJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p=e.getPlayer();
		if(!p.hasPlayedBefore()) {
			for(Item i: ConfigMain.getInstance().getItems()) {
				if(i.isGiveFirstJoin()) {
					if(i.getGiveSlot()!=0) {
						p.getInventory().setItem(i.getGiveSlot()-1, i.formItem(1));
					}
					else {
						p.getInventory().addItem(i.formItem(1));
					}
				}
			}
		}
	}
	
}
