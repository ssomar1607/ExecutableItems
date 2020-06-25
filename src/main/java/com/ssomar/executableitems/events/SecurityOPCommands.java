package com.ssomar.executableitems.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SecurityOPCommands implements Listener{

	@EventHandler
	public void onPerformCommandAsOP(PlayerCommandPreprocessEvent e){
		
		Player p = e.getPlayer();
		if(CommandsExecutor.getInstance().getSecurityOP().containsKey(p)) {
			Bukkit.getLogger().severe("[ExecutableItems] WARNING THE COMMAND "+e.getMessage()+" HAS BEEN BLOCKED WHEN SUDOOP "+p.getName()+ " PROBABLY USE HACKED CLIENT");
			e.setCancelled(true);
		}
	}
}
