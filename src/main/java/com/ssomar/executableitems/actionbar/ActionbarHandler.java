package com.ssomar.executableitems.actionbar;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.util.StringConverter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;


public class ActionbarHandler {

	private static StringConverter sc = new StringConverter();

	private static ActionbarHandler instance;

	private ArrayList<String> listPlayerActionbarOff= new ArrayList<>();

	private ArrayList<InfoActionbar> listActionbar= new ArrayList<>();

	ExecutableItems main;


	public void setup(ExecutableItems main) {
		this.main=main;
	}


	public void startActionbar(Player p, Item i, int delay) {
		BukkitTask task;
		BukkitRunnable runnable = new BukkitRunnable() {
			int timer = delay;

			@Override
			public void run() {
				if (timer == 0) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(sc.replaceVariable(MessageMain.getInstance().getActionbarEnd(),p.getName(), i.getName(), "", timer)));
					stopActionbar(p, i);
					return;
				}
				if(p.isOnline()) {
					if(isActiveActionbar(p, i) && !haveActionbarOff(p.getName()) ) {

						Bukkit.getServer().getPlayer(p.getName()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(sc.replaceVariable(MessageMain.getInstance().getActionbarMessage(),p.getName(), i.getName(), "", timer)));
					}
				}
				timer--;
			}
		};
		task = runnable.runTaskTimer(main, 0L, 20L);
		desactivePreviousActiveActionbar(p);
		listActionbar.add(new InfoActionbar(p.getName(), i, true, task));


	}

	public boolean haveActionbarOff(String playerName) {

		for(String player: listPlayerActionbarOff) {
			if(player.equals(playerName)) {
				return true;
			}
		}
		return false;
	}

	public void desactivePreviousActiveActionbar(Player p) {
		if(!playerHaveActiveActionbar(p)) return;

		getActiveActionbar(p).setActive(false);

	}

	public boolean playerHaveActiveActionbar(Player p) {
		for(InfoActionbar iA: listActionbar ) {
			if(iA.getPlayerName().equals(p.getName())) {
				return true;
			}
		}
		return false;
	}

	public InfoActionbar getActiveActionbar(Player p) {
		for(InfoActionbar iA: listActionbar ) {
			if(p.getName().equals(iA.getPlayerName()) && iA.isActive()) {
				return iA;
			}
		}
		return null;
	}

	public InfoActionbar getActiveActionbar(Player p, Item i) {
		for(InfoActionbar iA: listActionbar ) {
			if(p.getName().equals(iA.getPlayerName()) && i==iA.getItem() && iA.isActive()) {
				return iA;
			}
		}
		return null;
	}

	public boolean isActiveActionbar(Player p, Item i) {
		for(InfoActionbar iA: listActionbar ) {
			if(p.getName().equals(iA.getPlayerName()) && i==iA.getItem() && iA.isActive()) {
				return true;
			}
		}
		return false;
	}

	public void stopActionbar(Player p, Item i) {

		InfoActionbar toRemove=null;

		for(InfoActionbar iA: listActionbar ) {
			if(p.getName().equals(iA.getPlayerName()) && i==iA.getItem()) {
				iA.getTask().cancel();
				toRemove=iA;
				break;
			}
		}
		if(toRemove!=null){
			listActionbar.remove(toRemove);
		}
		else {
			Bukkit.broadcastMessage("Une errur s'est produite , aucun actionbar ne correspond");
		}


	}

	public static ActionbarHandler getInstance() {
		if (instance == null)
			instance = new ActionbarHandler(); 
		return instance;
	}

	public ArrayList<String> getListPlayerActionbarOff() {
		return listPlayerActionbarOff;
	}

	public void setListPlayerActionbarOff(ArrayList<String> listPlayerActionbarOff) {
		this.listPlayerActionbarOff = listPlayerActionbarOff;
	}


}
