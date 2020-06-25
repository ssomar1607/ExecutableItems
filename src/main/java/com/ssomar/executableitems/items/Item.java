package com.ssomar.executableitems.items;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.ssomar.executableitems.configs.MessageMain;

public class Item {

	private String identification;

	private String name;

	private List<String> lore;

	private Material material;
	
	private String headValue;

	private boolean glow;

	private int cooldown;
	
	private Map<String,Integer> otherCooldown;

	private String click;

	private boolean sneaking;
	
	private boolean cancelDrop;
	
	private boolean keepItemOnDeath;
	
	private boolean giveFirstJoin;
	
	private int giveSlot;
	
	private boolean needConfirmBeforeUse;

	private int use;
	
	private Map<Enchantment,Integer> enchants;

	private List<String> commands;

	public Item(String identification, String name, List<String> lore, Material material, String headValue, boolean glow, int cooldown, Map<String,Integer> otherCooldown, String click, boolean sneaking, boolean cancelDrop, boolean keepItemOnDeath, boolean giveFirstJoin, int giveSlot, boolean needConfirmBeforeUse, int use, Map<Enchantment,Integer> enchants, List<String> commands) {
		this.identification=identification;
		this.name = name;
		this.lore = lore;
		this.material=material;
		this.setHeadValue(headValue);
		this.glow = glow;
		this.cooldown=cooldown;
		this.otherCooldown=otherCooldown;
		this.click=click;
		this.sneaking=sneaking;
		this.cancelDrop=cancelDrop;
		this.keepItemOnDeath=keepItemOnDeath;
		this.giveFirstJoin= giveFirstJoin;
		this.giveSlot=giveSlot;
		this.needConfirmBeforeUse= needConfirmBeforeUse;
		this.use=use;
		this.enchants=enchants;
		this.commands = commands;
	}

	public ItemStack formItem(int quantity) {
		ItemStack item = new ItemStack(material, quantity);
		if(Bukkit.getServer().getVersion().contains("1.12")) {
			ItemMeta itemMeta=item.getItemMeta();
			itemMeta.setDisplayName(name);
			itemMeta.setLore(lore);
			if(use>0) {
				List<String> complementUse= itemMeta.getLore();
				complementUse.add(MessageMain.getInstance().getUse()+ String.valueOf(use));
				itemMeta.setLore(complementUse);
			}
			//itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			if(enchants.size()==0) {
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				if(glow) {
					itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
				}
			}
			else {
				for(Enchantment enchantment : enchants.keySet()) {
					itemMeta.addEnchant(enchantment, enchants.get(enchantment), true);
				}
			}
			itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
			item.setItemMeta(itemMeta);
			
			 //NBTItem nbti = new NBTItem(item);
			 //nbti.setString("Stringtest", "Teststring");
			 //item = nbti.getItem();
			
			
			
			return item;
		}
		else {
			if(Material.PLAYER_HEAD.equals(material)) {

				SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
				GameProfile profile = getGameProfile(headValue);
				Field profileField = null;
				try {
					profileField = itemMeta.getClass().getDeclaredField("profile");
				} catch (NoSuchFieldException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
				profileField.setAccessible(true);
				try {
					profileField.set(itemMeta, profile);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				item.setItemMeta((ItemMeta) itemMeta);
				itemMeta.setDisplayName(name);
				itemMeta.setLore(lore);
				if(use>0) {
					List<String> complementUse= itemMeta.getLore();
					complementUse.add(MessageMain.getInstance().getUse()+ String.valueOf(use));
					itemMeta.setLore(complementUse);
				}
				
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				if(enchants.size()==0) {
					itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					if(glow) {
						itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
					}
				}
				else {
					for(Enchantment enchantment : enchants.keySet()) {
						itemMeta.addEnchant(enchantment, enchants.get(enchantment), true);
					}
				}
				itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				item.setItemMeta(itemMeta);
				return item;
			}
			else {
				ItemMeta itemMeta=item.getItemMeta();
				itemMeta.setDisplayName(name);
				itemMeta.setLore(lore);
				if(use>0) {
					List<String> complementUse= itemMeta.getLore();
					complementUse.add(MessageMain.getInstance().getUse()+ String.valueOf(use));
					itemMeta.setLore(complementUse);
				}
				
				itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				if(enchants.size()==0) {
					itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					if(glow) {
						itemMeta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
					}
				}
				else {
					for(Enchantment enchantment : enchants.keySet()) {
						itemMeta.addEnchant(enchantment, enchants.get(enchantment), true);
					}
				}
				itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				item.setItemMeta(itemMeta);
				return item;
			}
		}
	

	}
	public GameProfile getGameProfile(String input) {
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    profile.getProperties().put("textures", new Property("textures", input));
	    return profile;
	  }

	public Item clone() {
		return new Item(identification, name, lore, material, headValue, glow, cooldown, otherCooldown, click, sneaking, cancelDrop, keepItemOnDeath, giveFirstJoin, giveSlot, needConfirmBeforeUse, use, enchants, commands);
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public boolean isGlow() {
		return glow;
	}

	public void setGlow(boolean glow) {
		this.glow = glow;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public boolean isSneaking() {
		return sneaking;
	}

	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}

	public int getUse() {
		return use;
	}

	public void setUse(int use) {
		this.use = use;
	}

	public String getHeadValue() {
		return headValue;
	}

	public void setHeadValue(String headValue) {
		this.headValue = headValue;
	}

	public boolean isCancelDrop() {
		return cancelDrop;
	}

	public void setCancelDrop(boolean cancelDrop) {
		this.cancelDrop = cancelDrop;
	}

	public boolean isGiveFirstJoin() {
		return giveFirstJoin;
	}

	public void setGiveFirstJoin(boolean giveFirstJoin) {
		this.giveFirstJoin = giveFirstJoin;
	}

	public int getGiveSlot() {
		return giveSlot;
	}

	public void setGiveSlot(int giveSlot) {
		this.giveSlot = giveSlot;
	}

	public boolean isNeedConfirmBeforeUse() {
		return needConfirmBeforeUse;
	}

	public void setNeedConfirmBeforeUse(boolean needConfirmBeforeUse) {
		this.needConfirmBeforeUse = needConfirmBeforeUse;
	}

	public Map<Enchantment, Integer> getEnchants() {
		return enchants;
	}

	public void setEnchants(Map<Enchantment, Integer> enchants) {
		this.enchants = enchants;
	}
	
	public boolean haveEnchant() {
		return this.enchants.size()>0;
	}

	public boolean isKeepItemOnDeath() {
		return keepItemOnDeath;
	}

	public void setKeepItemOnDeath(boolean keepItemOnDeath) {
		this.keepItemOnDeath = keepItemOnDeath;
	}

	public Map<String, Integer> getOtherCooldown() {
		return otherCooldown;
	}

	public void setOtherCooldown(Map<String, Integer> otherCooldown) {
		this.otherCooldown = otherCooldown;
	}

	












}
