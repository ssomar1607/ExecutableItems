package com.ssomar.executableitems.configs.ingame;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.util.StringConverter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI {
	
	public static StringConverter sc= new StringConverter();
	
	private Inventory inv;
	
	
	public GUI(String name, int size) {
		inv = Bukkit.createInventory(null, size, sc.coloredString(name));
	}
	
	public void createItem(Material material, int amount, int invSlot, String displayName, boolean glow, boolean haveEnchant, String... loreString) {

		ItemStack item= new ItemStack(material,amount);
		ItemMeta meta= item.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		
		if(glow || haveEnchant) {
			meta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.setDisplayName(sc.coloredString(displayName));

		for(String s : loreString) lore.add(sc.coloredString(s));

		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.setItem(invSlot, item);	

	}
	public void setItem(ItemStack item, int invSlot) {
		
		inv.setItem(invSlot, item);	
	}
	
	public ItemStack getByName(String name) {
		for(ItemStack item: inv.getContents()) {
			if(sc.decoloredString(item.getItemMeta().getDisplayName()).equals(sc.decoloredString(name))) return item;
		}
		return null;
	}
	
	public void openGUISync(Player player) {
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {		
				player.openInventory(inv);
			}
		};
		runnable.runTask(ExecutableItems.getPlugin());
	}

	public Inventory getInv() {
		return inv;
	}

	public void setInv(Inventory inv) {
		this.inv = inv;
	}
	
	

}
