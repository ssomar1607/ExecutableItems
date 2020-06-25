package com.ssomar.executableitems.events;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.util.Couple;
import com.ssomar.executableitems.util.StringConverter;

public class ItemThrow implements Listener {

	private static ItemThrow instance;

	private static StringConverter sc = new StringConverter();

	private Map<Player, Item> hasItemThrew= new HashMap<>();

	private Map<UUID, Couple<Player, Item>> validItemThrew= new HashMap<>();

	public ItemThrow() {
		instance=this;
	}

	@EventHandler
	public void onItemLaunch(ProjectileLaunchEvent e) {
		ProjectileSource s= e.getEntity().getShooter();
		if(!(s instanceof Player)) return;
		Player p = (Player) s;
		if(hasItemThrew.containsKey(p)) {
			validItemThrew.put(e.getEntity().getUniqueId(), new Couple<>(p, hasItemThrew.get(p)));
			hasItemThrew.remove(p);		
		}
	}


	@EventHandler
	public void onItemHit(ProjectileHitEvent e) {
		if(validItemThrew.containsKey(e.getEntity().getUniqueId())) {
			Player shooter = validItemThrew.get(e.getEntity().getUniqueId()).getElem1();
			Item item= validItemThrew.get(e.getEntity().getUniqueId()).getElem2();
			if(e.getHitEntity()!=null) {
				if(e.getHitEntity() instanceof Player) {
					Player hit =(Player) e.getHitEntity();
					shooter.sendMessage(sc.coloredString(sc.replaceVariable(MessageMain.getInstance().getValidHit(), hit.getName(), item.getName(), "", 0)));
					hit.sendMessage(sc.coloredString(sc.replaceVariable(MessageMain.getInstance().getReceivedHit(), shooter.getName(), item.getName(), "", 0)));
					CommandsExecutor.getInstance().runCommands(sc.replaceVariable(item.getCommands(), hit.getName(), item.getName(), "", 0), hit, item);
				}
				else {
					shooter.sendMessage(sc.coloredString(MessageMain.getInstance().getNoHit()));
				}
			}
			else {
				shooter.sendMessage(sc.coloredString(MessageMain.getInstance().getNoHit()));
			}
			validItemThrew.remove(e.getEntity().getUniqueId());

		}

	}


	public static ItemThrow getInstance() {
		if(instance==null)return new ItemThrow();
		return instance;
	}

	public Map<Player, Item> getHasItemThrew() {
		return hasItemThrew;
	}

	public void setHasItemThrew(HashMap<Player, Item> hasItemThrew) {
		this.hasItemThrew = hasItemThrew;
	}


}
