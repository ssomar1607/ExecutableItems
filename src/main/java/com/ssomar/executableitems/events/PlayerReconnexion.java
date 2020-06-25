package com.ssomar.executableitems.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ssomar.executableitems.data.Database;

public class PlayerReconnexion implements Listener {
	
	
	@EventHandler(priority=EventPriority.HIGH)
	public void playerReconnexion(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		List<String> commands=Database.getInstance().selectCommandsForPlayer(p.getName());
		if(!commands.isEmpty()) {
			CommandsExecutor.getInstance().runCommands(commands,p, null);
			Database.getInstance().deleteCommandsForPlayer(p.getName());
		}
		if(CommandsExecutor.getInstance().getDisconnectedCommands().containsKey(p.getName())) {
			CommandsExecutor.getInstance().runDisconnectedCommands(p);				
		}
		else {
			
			return;
		}
	}
}
