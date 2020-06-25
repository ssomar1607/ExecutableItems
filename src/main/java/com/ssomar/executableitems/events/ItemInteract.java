package com.ssomar.executableitems.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ssomar.executableitems.commands.GiveCommand;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.cooldowns.MapCooldowns;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.items.ItemsHandler;
import com.ssomar.executableitems.util.Couple;
import com.ssomar.executableitems.util.StringConverter;

public class ItemInteract implements Listener {

	private static StringConverter sc = new StringConverter();

	private MapCooldowns cooldowns= new MapCooldowns();

	//when the drop item is cancel the left_click_ai is invoke so i cancel that
	private ArrayList<Player> cancelInteract= new ArrayList<>();

	private HashMap<Player,Item> needConfirm= new HashMap<>();

	@EventHandler
	public void onItemInteract(PlayerInteractEvent e) {

		ItemStack item= e.getItem();	
		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();

		if(!itemsHandler.isExecutableItem(item)) return;

		Player p = e.getPlayer();
		Item infoItem= itemsHandler.getExecutableItem(item);

		if(!this.hasItemPerm(p, infoItem)) return;

		if(Bukkit.getServer().getVersion().contains("1.12")) {
			if(!infoItem.getClick().equals("projectile") && (item.getType().toString().contains("EXP_BOTTLE") || item.getType().equals(Material.SPLASH_POTION))) e.setCancelled(true);
		}
		else {
			if (!infoItem.getClick().equals("projectile") && (item.getType().equals(Material.EXPERIENCE_BOTTLE) || item.getType().equals(Material.SPLASH_POTION))) {
				e.setCancelled(true);
			}
			if(!(Bukkit.getServer().getVersion().contains("1.13") || Bukkit.getServer().getVersion().contains("1.14"))) {
				if(cancelInteract.contains(p)) {
					cancelInteract.remove(p);
					e.setCancelled(true);
					return;
				}
			}
		}

		if(!((e.getAction().toString().toLowerCase().contains(infoItem.getClick()) || infoItem.getClick().equals("all") || infoItem.getClick().equals("projectile")) && !infoItem.getClick().equals("consume")) ) return;

		//cancel the double interaction
		if( infoItem.getClick().equals("all")) {
			if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
				e.setCancelled(true);
				return;
			}
		}

		if(infoItem.isSneaking()==true && p.isSneaking()==false) return;

		String identification = infoItem.getIdentification();
		Couple<String, String> key= new Couple<>(p.getName(),identification);

		if(cooldowns.containsKey(key)) {

			int cooldown= infoItem.getCooldown();
			long timeleft= ((cooldowns.getValue(key))/1000)  - ((System.currentTimeMillis())/1000) +  cooldown;

			if(timeleft>=1) {

				p.sendMessage(sc.replaceVariable(MessageMain.getInstance().getTimeLeft(), p.getName(), item.getItemMeta().getDisplayName(), "", (int) timeleft));
				e.setCancelled(true);
				return;

			}
		}

		boolean sendMessage=true;
		if(infoItem.isNeedConfirmBeforeUse()) {
			if(needConfirm.containsKey(p)) {
				if(infoItem.getIdentification().equals(needConfirm.get(p).getIdentification())) {
					sendMessage=false;
					needConfirm.remove(p);
				}
			}
			if(sendMessage) {
				needConfirm.put(p, infoItem);

				p.sendMessage(sc.coloredString(MessageMain.getInstance().getConfirmMessage()));
				e.setCancelled(true);
				return;
			}
		}

		cooldowns.replaceKey(key, System.currentTimeMillis());
		this.setOtherCooldown(p, infoItem);
		this.updateLore(p, item, infoItem);

		if(infoItem.getClick().equals("projectile")) {
			ItemThrow.getInstance().getHasItemThrew().put(p, infoItem);		
			return;
		}
		CommandsExecutor.getInstance().runCommands(infoItem.getCommands(), p, infoItem);

	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent e) {

		ItemStack item= e.getItem();	
		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();

		if(!itemsHandler.isExecutableItem(item)) return;

		Player p = e.getPlayer();
		Item infoItem= itemsHandler.getExecutableItem(item);

		if(infoItem.isSneaking()==true && p.isSneaking()==false) return;

		String identification = infoItem.getIdentification();

		this.hasItemPerm(p, infoItem);

		Couple<String, String> key= new Couple<>(p.getName(),identification);

		if(cooldowns.containsKey(key)) {

			int cooldown= infoItem.getCooldown();
			long timeleft= ((cooldowns.getValue(key))/1000)  - ((System.currentTimeMillis())/1000) +  cooldown;

			if(timeleft>=1) {

				p.sendMessage(sc.replaceVariable(MessageMain.getInstance().getTimeLeft(), p.getName(), infoItem.getName(), "", (int) timeleft));
				e.setCancelled(true);
				return;
			}
		}

		boolean sendMessage=true;
		if(infoItem.isNeedConfirmBeforeUse()) {
			if(needConfirm.containsKey(p)) {
				if(infoItem.getIdentification().equals(needConfirm.get(p).getIdentification())) {
					sendMessage=false;
					needConfirm.remove(p);
				}
			}
			if(sendMessage) {
				needConfirm.put(p, infoItem);

				p.sendMessage(sc.coloredString(MessageMain.getInstance().getConfirmMessage()));
				e.setCancelled(true);
				return;
			}
		}

		cooldowns.replaceKey(key, System.currentTimeMillis());	
		this.setOtherCooldown(p, infoItem);

		ItemMeta itemMeta=item.getItemMeta();
		List<String> lore= itemMeta.getLore();

		if(lore.get(lore.size()-1).contains(MessageMain.getInstance().getUse())) {
			int use= Integer.valueOf(lore.get(lore.size()-1).split(MessageMain.getInstance().getUse())[1]);
			if(use>1) {
				use--;
				if(item.getAmount()>1) {
					e.setCancelled(true);
					item.setAmount(item.getAmount()-1);
					p.getInventory().setItemInMainHand(item);
					p.updateInventory();
					GiveCommand gc= new GiveCommand();
					infoItem.setUse(use);
					gc.simpleGive(p, infoItem);
				}
				else {
					e.setCancelled(true);
					item.setAmount(0);
					lore.set(lore.size()-1, MessageMain.getInstance().getUse()+ String.valueOf(use));
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
					item.setAmount(1);
					p.getInventory().setItemInMainHand(item);
					p.updateInventory();
				}
			}
			else; //nothing item is consume	
		}
		else if(infoItem.getUse()==-1) e.setCancelled(true);

		CommandsExecutor.getInstance().runCommands(sc.replaceVariable(infoItem.getCommands(), p.getName(), infoItem.getName(), "", 0), p, infoItem);
	}


	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {

		ItemStack item= e.getItemDrop().getItemStack();	
		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();

		if(!itemsHandler.isExecutableItem(item)) return;

		Player p = e.getPlayer();
		Item infoItem= itemsHandler.getExecutableItem(item);

		if(infoItem.isCancelDrop()) {
			if(!(Bukkit.getServer().getVersion().contains("1.12") || Bukkit.getServer().getVersion().contains("1.13") || Bukkit.getServer().getVersion().contains("1.14"))) if(!cancelInteract.contains(p)) cancelInteract.add(p);
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onItemInteractonPlayer(PlayerInteractEntityEvent e) {

		Player p = e.getPlayer();

		ItemStack item =p.getInventory().getItemInMainHand();
		if(e.getHand()==EquipmentSlot.OFF_HAND)item =p.getInventory().getItemInOffHand();	
		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();

		if(!itemsHandler.isExecutableItem(item)) return;

		Item infoItem= itemsHandler.getExecutableItem(item);
		String identification = infoItem.getIdentification();

		if(infoItem.isSneaking()==true && p.isSneaking()==false) return;
		this.hasItemPerm(p, infoItem);



		if(!(e.getRightClicked() instanceof Player)) {
			p.sendMessage(sc.coloredString(MessageMain.getInstance().getNoHit()));
			return;
		}

		Couple<String, String> key= new Couple<>(p.getName(),identification);

		if(cooldowns.containsKey(key)) {

			int cooldown= infoItem.getCooldown();
			long timeleft= ((cooldowns.getValue(key))/1000)  - ((System.currentTimeMillis())/1000) +  cooldown;

			if(timeleft>=1) {

				p.sendMessage(sc.replaceVariable(MessageMain.getInstance().getTimeLeft(), p.getName(), infoItem.getName(), "", (int) timeleft));
				e.setCancelled(true);
				return;

			}
		}

		boolean sendMessage=true;
		if(infoItem.isNeedConfirmBeforeUse()) {
			if(needConfirm.containsKey(p)) {
				if(infoItem.getIdentification().equals(needConfirm.get(p).getIdentification())) {
					sendMessage=false;
					needConfirm.remove(p);
				}
			}
			if(sendMessage) {
				needConfirm.put(p, infoItem);

				p.sendMessage(sc.coloredString(MessageMain.getInstance().getConfirmMessage()));
				e.setCancelled(true);
				return;
			}
		}

		cooldowns.replaceKey(key, System.currentTimeMillis());		
		this.setOtherCooldown(p, infoItem);

		ItemMeta itemMeta=item.getItemMeta();
		List<String> lore= itemMeta.getLore();

		if(lore.get(lore.size()-1).contains(MessageMain.getInstance().getUse())) {
			int use= Integer.valueOf(lore.get(lore.size()-1).split(MessageMain.getInstance().getUse())[1]);
			if(use>1) {
				use--;
				if(item.getAmount()>1) {
					item.setAmount(item.getAmount()-1);
				}
				else {
					lore.set(lore.size()-1, MessageMain.getInstance().getUse()+ String.valueOf(use));
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
				}
			}
			else item.setAmount(item.getAmount()-1);
		}

		else if(infoItem.getUse()!=-1) item.setAmount(item.getAmount()-1);
		
		Player target = (Player) e.getRightClicked();
		p.sendMessage(sc.replaceVariable(sc.coloredString(MessageMain.getInstance().getValidHit()), target.getName(), infoItem.getName(), "", 0));
		target.sendMessage(sc.replaceVariable(sc.coloredString(MessageMain.getInstance().getReceivedHit()), p.getName(), infoItem.getName(), "", 0));
		
		if(infoItem.getClick().toLowerCase().equals("target")) {
			CommandsExecutor.getInstance().runCommands(infoItem.getCommands(), target, infoItem);
		}
		else if(infoItem.getClick().toLowerCase().equals("grabplayername")) {

			List<String> commands= new ArrayList<String>();
			for(String s: infoItem.getCommands()) {
				commands.add(s.replaceAll("%target%", target.getName()));
			}
			CommandsExecutor.getInstance().runCommands(commands, p, infoItem);
		}
	}


	public boolean hasItemPerm(Player p, Item infoItem) {

		String id=infoItem.getIdentification();
		String displayName= infoItem.getName();

		if(!(p.hasPermission("ExecutableItems.item."+id) || p.hasPermission("ei.item."+id) || p.hasPermission("ExecutableItems.item.*") || p.hasPermission("ei.item.*"))) {
			p.sendMessage(sc.replaceVariable(MessageMain.getInstance().getRequirePermission(), p.getName(), displayName, "", 0));
			return false;
		}
		return true;
	}

	public void setOtherCooldown(Player p, Item infoItem) {

		ItemsHandler items = ConfigMain.getInstance().getItems();

		for(String s: infoItem.getOtherCooldown().keySet()) {
			if(items.containsIdentification(s)) {
				Couple<String, String> keyO= new Couple<>(p.getName(),s);
				if(cooldowns.containsKey(keyO)) {
					if(cooldowns.get(keyO)!=null) {
						Bukkit.broadcastMessage("current: "+cooldowns.get(keyO)+ " set: "+infoItem.getOtherCooldown().get(s));
						if(Integer.valueOf(cooldowns.get(keyO).toString())<infoItem.getOtherCooldown().get(s)) {
							cooldowns.replaceKey(keyO, System.currentTimeMillis()+infoItem.getOtherCooldown().get(s)*1000);
						}
					}
					else  {
						cooldowns.put(keyO, System.currentTimeMillis()+(infoItem.getOtherCooldown().get(s)-items.getByIdentification(s).getCooldown())*1000);
						//Bukkit.broadcastMessage(" set: "+infoItem.getOtherCooldown().get(s));
						//Bukkit.broadcastMessage(System.currentTimeMillis()+" a / s "+ cooldowns.get(keyO));
					}
				}
				else cooldowns.put(keyO, System.currentTimeMillis()+(infoItem.getOtherCooldown().get(s)-items.getByIdentification(s).getCooldown())*1000);
			}
		}
	}

	public void updateLore(Player p, ItemStack item, Item infoItem) {
		ItemMeta itemMeta=item.getItemMeta();
		List<String> lore= itemMeta.getLore();

		if(lore.get(lore.size()-1).contains(MessageMain.getInstance().getUse())) {
			int use= Integer.valueOf(lore.get(lore.size()-1).split(MessageMain.getInstance().getUse())[1]);
			if(use>1) {
				use--;
				if(item.getAmount()>1) {
					if(!infoItem.getClick().equals("projectile")) item.setAmount(item.getAmount()-1);
					GiveCommand gc= new GiveCommand();
					infoItem.setUse(use);
					gc.simpleGive(p, infoItem);
				}
				else {
					if(infoItem.getClick().equals("projectile")) item.setAmount(item.getAmount()+1);
					lore.set(lore.size()-1, MessageMain.getInstance().getUse()+ String.valueOf(use));
					itemMeta.setLore(lore);
					item.setItemMeta(itemMeta);
				}
			}
			else if(!infoItem.getClick().equals("projectile")) item.setAmount(item.getAmount()-1);
		}

		else if(infoItem.getUse()!=-1) if(!infoItem.getClick().equals("projectile")) item.setAmount(item.getAmount()-1);
		else if(infoItem.getClick().equals("projectile")) item.setAmount(item.getAmount()+1);
	}
}
