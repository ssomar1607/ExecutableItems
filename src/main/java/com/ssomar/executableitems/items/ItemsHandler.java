package com.ssomar.executableitems.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.util.StringConverter;


public class ItemsHandler extends ArrayList<Item>{

	private static final long serialVersionUID = 9008824876226705509L;

	private static StringConverter sc = new StringConverter();
	
	
	public boolean isExecutableItem(ItemStack item) {
		ItemMeta itemMeta;
		try {
			if(!item.hasItemMeta()) return false;
			itemMeta= item.getItemMeta();
		}catch(NullPointerException error){
			return false;
		}

		if(!itemMeta.hasLore()) return false ;
		List<String> lore = itemMeta.getLore();

		if(!itemMeta.hasDisplayName()) return false;
		String displayName = itemMeta.getDisplayName();

		Material material= item.getType();

		ItemsHandler itemsHandler = ConfigMain.getInstance().getItems();
		if(!itemsHandler.containsByNameLoreMaterial(displayName, lore, material)) return false;
		else return true;
	}
	
	public Item getExecutableItem(ItemStack item) {
		return this.getItemByNameLoreMaterial(item.getItemMeta().getDisplayName(), item.getItemMeta().getLore(), item.getType()).clone();
	}
	
	public boolean containsIdentification(String identification) {
		for(Item i : this) {
			if(i.getIdentification().equals(identification)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsByNameLoreMaterial(String name, List<String> lore, Material material) {
		for(Item i : this) {
			//Bukkit.broadcastMessage("register: name: "+i.getName()+" lore:"+ i.getLore().toString()+" material:"+ i.getMaterial().toString());
			//Bukkit.broadcastMessage("inagme: name: "+name+" lore:"+ lore.toString()+" material:"+ material.toString());
			if(lore.get(lore.size()-1).contains(MessageMain.getInstance().getUse())) {
				List<String> verifLore= sc.decoloredString(lore.subList(0, lore.size()-1));
				if(i.getName().equals(name) && sc.decoloredString(i.getLore()).equals(verifLore) && i.getMaterial().equals(material)) {
					return true;
				}
			}
			else {
				if(i.getName().equals(name) && sc.decoloredString(i.getLore()).equals(sc.decoloredString(lore)) && i.getMaterial().equals(material)) {
					return true;
				}
			}

		}
		return false;
	}

	public Item getItemByNameLoreMaterial(String name, List<String> lore, Material material) {
		for(Item i : this) {
			if(lore.get(lore.size()-1).contains(MessageMain.getInstance().getUse())) {
				List<String> verifLore= sc.decoloredString(lore.subList(0, lore.size()-1));
				if(i.getName().equals(name) && sc.decoloredString(i.getLore()).equals(verifLore) && i.getMaterial().equals(material)) {
					return i;
				}
			}
			else {
				if(i.getName().equals(name) && sc.decoloredString(i.getLore()).equals(sc.decoloredString(lore)) && i.getMaterial().equals(material)) {
					return i;
				}
			}
		}
		return null;
	}

	public Item getByIdentification(String identification) {
		for(Item i : this) {
			if(i.getIdentification().equals(identification)) {
				return i;
			}
		}
		return null;
	}

	public void showItems() {
		StringBuilder b = new StringBuilder( "Avaible item: identification,name,lore" );
		for(Item i : this) {
			b.append("\n");
			b.append("[");
			b.append(i.getIdentification());
			b.append(",");
			b.append(i.getName());
			b.append(",");

			for(String s : i.getLore()) {
				b.append(s);
				b.append("-");
			}
			b.append("]");

		}
		Bukkit.broadcastMessage(b.toString());
	}




}
