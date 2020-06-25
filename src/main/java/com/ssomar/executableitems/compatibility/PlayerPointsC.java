/*package com.ssomar.executableitems.compatibility;

import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ssomar.executableitems.ExecutableItems;

public class PlayerPointsC {

	private static PlayerPointsC instance;
	
	private PlayerPoints playerPoints;
	
	@SuppressWarnings("unused")
	private ExecutableItems main;

	public void setup(ExecutableItems main) {

		this.main=main;
		instance=this;
		final Plugin plugin = main.getServer().getPluginManager().getPlugin("PlayerPoints");
		playerPoints = PlayerPoints.class.cast(plugin);
	}
	
	
	
	public void addPoints(Player player, int amount) {
	   playerPoints.getAPI().give(player.getUniqueId(), amount);
	}
	
	public static PlayerPointsC getInstance() {
		if(instance==null) return new PlayerPointsC();
		return instance;
	}
}
*/