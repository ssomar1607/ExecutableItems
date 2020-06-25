package com.ssomar.executableitems.configs.ingame;


import com.ssomar.executableitems.items.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConfigGUIv2 extends GUI {

	private boolean newItem;


	public ConfigGUIv2() {
		super("&8&lEI Creator", 4*9);
		newItem=true;
		//Main Options
		createItem(Material.DIAMOND, 					1 , 0, 	"&e&lMaterial", 	false,	false, "", "&aClick here to change", "&7actually: &eDIAMOND" );
		createItem(Material.NAME_TAG, 					1 , 1, 	"&e&lDisplayName", 	false,	false, "", "&aClick here to change", "&7actually: &e&lDefault Name" );
		createItem(Material.PAPER, 						1 , 2, 	"&e&lLore", 		false,	false, "", "&aClick here to change", "&7actually: ", "",  "&bDefault Lore" );
		createItem(Material.BEACON, 					1 , 3, 	"&e&lGlow", 		true,	false,  "", "&aClick here to change", "&7actually: &aTrue" );
		createItem(Material.REDSTONE, 					1 , 4, 	"&e&lItem cooldown", 	false,	false, "", "&aClick here to change", "&7(in seconds)", "&7actually: &e30" );
		createItem(Material.REDSTONE, 					1 , 5, 	"&e&lOther items cooldown", 	false,	false, "", "&aClick here to change", "&7actually: " );
		createItem(Material.STICK, 						1 , 6, 	"&e&lClick", 		false,	false, "", "&aClick here to change", "&7actually: &eALL" );
		createItem(Material.BUCKET, 					1 , 7, 	"&e&lUsage", 		false,	false, "", "&aClick here to change", "&7actually: &e1" );
		createItem(Material.ARMOR_STAND,				1 , 8, 	"&e&lSneaking", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.BOOK,						1 , 9, 	"&e&lCommands", 	false,	false, "", "&aClick here to change", "&7actually: ", "&bNo command" );
		createItem(Material.HOPPER,						1 , 10, 	"&e&lCancel drop", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		createItem(Material.BONE,						1 , 11, 	"&e&lKeep item on death", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.CAKE,						1 , 12, 	"&e&lGive first join", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.CAKE,						1 , 13, 	"&e&lGive slot", 	false,	false, "&7&oIgnore if Give first join is &c&oFalse", "&aClick here to change", "&7actually: &e1" );
		createItem(Material.STONE_BUTTON,						1 , 14, 	"&e&lNeed player confirmation", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.ENCHANTED_BOOK,								1 , 15, 	"&e&lEnchantments", 	false,	false);
		//Decoration
		createItem(Material.LADDER, 	1 , 21, "&7", 				false, 	false);
		createItem(Material.LADDER, 	1 , 22, "&7Preview", 		false,	false);
		createItem(Material.LADDER, 	1 , 23, "&7", 				false,	false);
		createItem(Material.LADDER, 	1 , 30, "&7Preview", 		false, 	false);
		createItem(Material.LADDER, 	1 , 32, "&7Preview", 		false,	false);
		//Preview
		createItem(Material.DIAMOND, 					1 , 31, "&e&lDefault Name", true,	false, 	"", "&bDefault Lore" );
		//Reset menu
		createItem(Material.BARRIER, 					1 , 28, "&4&lReset", 		false,	false, 	"", "&cClick here to reset", "&call options of this item" );
		// exit
		createItem(Material.REDSTONE_BLOCK, 			1 , 27, "&4&l▶ Back to show menu", 		false,	false);
		//Save menu
		createItem(Material.EMERALD, 					1 , 35, "&2&lCreate this item", 		false,	false, 	"", "&aClick here to create this" , "&aitem in the config.yml" );
	}

	public ConfigGUIv2(ItemStack item) {
		super("&8&lEI Creator", 4*9);
		
		newItem=true;
		//Preview
		createItem(item.getType(), 					1 , 31, item.getItemMeta().getDisplayName(), true,	false );
		//Main Options
		createItem(item.getType(), 					1 , 0, 	"&e&lMaterial", 	false,	false, "", "&aClick here to change", "&7actually: &e"+item.getType() );
		createItem(Material.NAME_TAG, 					1 , 1, 	"&e&lDisplayName", 	false,	false, "", "&aClick here to change", "&7actually: "+item.getItemMeta().getDisplayName() );
		createItem(Material.PAPER, 						1 , 2, 	"&e&lLore", 		false,	false, "", "&aClick here to change", "&7actually: " );
		if(item.getItemMeta().hasLore()) updateLore(item.getItemMeta().getLore());
		else {
			List<String> lore= new ArrayList<>();
			lore.add("");
			lore.add("&bDefault Lore");
			updateLore(lore);
		}
		if(item.getItemMeta().hasEnchants()) {
			if(item.getEnchantments().size()!=0) createItem(Material.BEACON, 					1 , 3, 	"&e&lGlow", 		true,	false,  "", "&aClick here to change", "&7actually: &aTrue" );
			else createItem(Material.BEACON, 					1 , 3, 	"&e&lGlow", 		false,	false,  "", "&aClick here to change", "&7actually: &cFalse" );
		}
		else createItem(Material.BEACON, 					1 , 3, 	"&e&lGlow", 		false,	false,  "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.REDSTONE, 					1 , 4, 	"&e&lItem cooldown", 	false,	false, "", "&aClick here to change", "&7(in seconds)", "&7actually: &e30" );
		createItem(Material.REDSTONE, 					1 , 5, 	"&e&lOther items cooldown", 	false,	false, "", "&aClick here to change", "&7actually: " );
		createItem(Material.STICK, 						1 , 6, 	"&e&lClick", 		false,	false, "", "&aClick here to change", "&7actually: &eALL" );
		createItem(Material.BUCKET, 					1 , 7, 	"&e&lUsage", 		false,	false, "", "&aClick here to change", "&7actually: &e1" );
		createItem(Material.ARMOR_STAND,				1 , 8, 	"&e&lSneaking", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.BOOK,						1 , 9, 	"&e&lCommands", 	false,	false, "", "&aClick here to change", "&7actually: ", "&bNo command" );
		createItem(Material.HOPPER,						1 , 10, 	"&e&lCancel drop", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		createItem(Material.BONE,						1 , 11, 	"&e&lKeep item on death", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.CAKE,						1 , 12, 	"&e&lGive first join", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.CAKE,						1 , 13, 	"&e&lGive slot", 	false,	false, "&7&oIgnore if Give first join is &c&oFalse", "&aClick here to change", "&7actually: &e1" );
		createItem(Material.STONE_BUTTON,						1 , 14, 	"&e&lNeed player confirmation", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.ENCHANTED_BOOK,								1 , 15, 	"&e&lEnchantments", 	false,	false);
		if(item.getItemMeta().hasEnchants()) updateEnchantments(item.getEnchantments());
		//Decoration
		createItem(Material.LADDER, 	1 , 21, "&7", 				false, 	false);
		createItem(Material.LADDER, 	1 , 22, "&7Preview", 		false,	false);
		createItem(Material.LADDER, 	1 , 23, "&7", 				false,	false);
		createItem(Material.LADDER, 	1 , 30, "&7Preview", 		false, 	false);
		createItem(Material.LADDER, 	1 , 32, "&7Preview", 		false,	false);

		//Reset menu
		createItem(Material.BARRIER, 					1 , 28, "&4&lReset", 		false,	false, 	"", "&cClick here to reset", "&call options of this item" );
		// exit
		createItem(Material.REDSTONE_BLOCK, 			1 , 27, "&4&l▶ Back to show menu", 		false,	false);
		//Save menu
		createItem(Material.EMERALD, 					1 , 35, "&2&lCreate this item", 		false,	false, 	"", "&aClick here to create this" , "&aitem in the config.yml" );
	}

	public ConfigGUIv2(Item item) {
		super("&8&lEI Editor - "+item.getIdentification(), 4*9);
		newItem=false;
		//Preview
		createItem(item.getMaterial(), 							1 , 31, sc.coloredString(item.getName()), item.isGlow(), item.haveEnchant());
		//Main Options
		createItem(item.getMaterial(), 							1 , 0, 	"&e&lMaterial", 	false,	false, "", "&aClick here to change", "&7actually: &e"+item.getMaterial() );
		createItem(Material.NAME_TAG, 							1 , 1, 	"&e&lDisplayName", 	false,	false, "", "&aClick here to change", "&7actually: "+ sc.coloredString(item.getName()) );
		createItem(Material.PAPER, 								1 , 2, 	"&e&lLore", 		false,	false, "", "&aClick here to change", "&7actually: ");
		updateLore(item.getLore());
		if(item.isGlow()) createItem(Material.BEACON, 			1 , 3, 	"&e&lGlow", 		true,	item.haveEnchant(),  "", "&aClick here to change", "&7actually: &aTrue" );
		else if (item.haveEnchant()) createItem(Material.BEACON, 			1 , 3, 	"&e&lGlow", 		false,	item.haveEnchant(),  "", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.BEACON, 						1 , 3, 	"&e&lGlow", 		false,	item.haveEnchant(),  "", "&aClick here to change", "&7actually: &cFalse" );		
		createItem(Material.REDSTONE, 							1 , 4, 	"&e&lItem cooldown", 	false, 	false, "", "&aClick here to change", "&7(in seconds)", "&7actually: &e"+item.getCooldown() );
		createItem(Material.REDSTONE, 					1 , 5, 	"&e&lOther items cooldown", 	false,	false, "", "&aClick here to change", "&7actually: " );
		this.updateOtherCooldown(item.getOtherCooldown());
		createItem(Material.STICK, 								1 , 6, 	"&e&lClick", 		false,	false, "", "&aClick here to change", "&7actually: &e"+item.getClick().toUpperCase() );
		createItem(Material.BUCKET, 							1 , 7, 	"&e&lUsage", 		false,	false, "", "&aClick here to change", "&7actually: &e"+item.getUse() );
		if(item.isSneaking()) createItem(Material.ARMOR_STAND,	1 , 8, 	"&e&lSneaking", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.ARMOR_STAND,					1 , 8, 	"&e&lSneaking", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.BOOK,								1 , 9, 	"&e&lCommands", 	false,	false, "", "&aClick here to change", "&7actually: ");
		if(item.isCancelDrop()) createItem(Material.HOPPER,								1 , 10, 	"&e&lCancel drop", 	false, 	false,"", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.HOPPER,								1 , 10, 	"&e&lCancel drop", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		if(item.isKeepItemOnDeath()) createItem(Material.BONE,						1 , 11, 	"&e&lKeep item on death", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.BONE,						1 , 11, 	"&e&lKeep item on death", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		if(item.isGiveFirstJoin()) createItem(Material.CAKE,						1 , 12, 	"&e&lGive first join", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.CAKE,						1 , 12, 	"&e&lGive first join", 	false,	false, "", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.CAKE,						1 , 13, 	"&e&lGive slot", 	false,	false, "&7&oIgnore if Give first join is &c&oFalse", "&aClick here to change", "&7actually: &e"+item.getGiveSlot());
		if(item.isNeedConfirmBeforeUse())createItem(Material.STONE_BUTTON,						1 , 14, 	"&e&lNeed player confirmation", 	false,	false, "", "&aClick here to change", "&7actually: &aTrue" );
		else createItem(Material.STONE_BUTTON,						1 , 14, 	"&e&lNeed player confirmation", 	false, 	false,"", "&aClick here to change", "&7actually: &cFalse" );
		createItem(Material.ENCHANTED_BOOK,								1 , 15, 	"&e&lEnchantments", 	false, 	false);
		createItem(Material.ANVIL,								1 , 16, 	"&e&lID", 	false,	false, "", "&7actually: "+item.getIdentification());
		updateCommands(item.getCommands());
		updateEnchantments(item.getEnchants());
		//Decoration
		createItem(Material.LADDER, 	1 , 21, "&7", 				false,	false);
		createItem(Material.LADDER, 	1 , 22, "&7Preview", 		false,	false);
		createItem(Material.LADDER, 	1 , 23, "&7", 				false,	false);
		createItem(Material.LADDER, 	1 , 30, "&7Preview", 		false,	false);
		createItem(Material.LADDER, 	1 , 32, "&7Preview", 		false,	false);
		//Reset menu
		createItem(Material.BARRIER, 							1 , 28, "&4&lReset", 		false,	false, 	"", "&cClick here to reset", "&call options of this item" );
		// exit
		createItem(Material.REDSTONE_BLOCK, 					1 , 27, "&4&l▶ Back to show menu", 		false,	false);
		//Save menu
		createItem(Material.EMERALD, 							1 , 35, "&2&lSave", 		false,	false, 	"", "&aClick here to save" , "&ayour modification in config.yml" );
	}

	public ItemStack getPreviewItem() {
		return this.getInv().getItem(31);
	}

	public void updateMaterial(Material material) {
		ItemStack item = this.getByName("Material");
		ItemStack preview = getPreviewItem();
		item.setType(material);
		preview.setType(material);
		updateActually(item, "&e"+material.toString());
	}

	public void updateName(String name) {
		ItemStack item = this.getByName("DisplayName");
		ItemStack preview = getPreviewItem();
		ItemMeta toChange = preview.getItemMeta();
		toChange.setDisplayName(name);
		preview.setItemMeta(toChange);
		updateActually(item, name);
	}

	public void updateLore(List<String> lore) {
		int i=0;
		while(i<lore.size()) {
			lore.set(i, sc.coloredString(lore.get(i)));
			i++;
		}
		ItemStack item = this.getByName("Lore");
		ItemStack preview = getPreviewItem();
		ItemMeta toChange = item.getItemMeta();
		List<String> loreUpdate= toChange.getLore().subList(0, 3);
		loreUpdate.addAll(lore);
		toChange.setLore(loreUpdate);
		item.setItemMeta(toChange);

		toChange = preview.getItemMeta();	
		toChange.setLore(lore);
		preview.setItemMeta(toChange);
	}

	public void changeGlow() {
		ItemStack item = this.getByName("Glow");
		ItemStack preview = getPreviewItem();

		if(preview.getEnchantments().size()>1) {
			updateActually(item, "&aTrue");
		}
		else if(preview.getEnchantments().size()==1) {
			if(preview.getEnchantments().containsKey(Enchantment.PROTECTION_FALL)) if(preview.getEnchantments().get(Enchantment.PROTECTION_FALL)==6) {
				item.removeEnchantment(Enchantment.PROTECTION_FALL);
				preview.removeEnchantment(Enchantment.PROTECTION_FALL);
				updateActually(item, "&cFalse");
			}
		}
		else {
			item.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 6);
			preview.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 6);
			updateActually(item, "&aTrue");
		}
	}

	public void updateCooldown(int cooldown) {
		ItemStack item = this.getByName("Item cooldown");
		updateActually(item, "&e"+String.valueOf(cooldown));
	}
	
	public void updateOtherCooldown(List<String> cd) {
		ItemStack item = this.getByName("Other items cooldown");
		ItemMeta toChange = item.getItemMeta();
		List<String> cdUpdate= toChange.getLore().subList(0, 3);
		cdUpdate.addAll(cd);
		toChange.setLore(cdUpdate);
		item.setItemMeta(toChange);
	}
	
	public void updateOtherCooldown(Map<String, Integer> cd) {
		ItemStack item = this.getByName("Other items cooldown");
		ItemMeta toChange = item.getItemMeta();
		List<String> cdUpdate= toChange.getLore().subList(0, 3);
		for(String s: cd.keySet()) {
			cdUpdate.add(s+":"+cd.get(s));
		}
		toChange.setLore(cdUpdate);
		item.setItemMeta(toChange);
	}

	public void changeClick() {
		ItemStack item = this.getByName("Click");
		if(getActually(item).contains("ALL")) {
			updateActually(item, "&eCONSUME");
		}
		else if(getActually(item).contains("CONSUME")) {
			updateActually(item, "&eRIGHT");
		}
		else if(getActually(item).contains("RIGHT")) {
			updateActually(item, "&eLEFT");
		}
		else if(getActually(item).contains("LEFT")) {
			updateActually(item, "&ePROJECTILE");
		}
		else if(getActually(item).contains("PROJECTILE")) {
			updateActually(item, "&eTARGET");
		}
		else if(getActually(item).contains("TARGET")) {
			updateActually(item, "&eGRABPLAYERNAME");
		}
		else if(getActually(item).contains("GRABPLAYERNAME")) {
			updateActually(item, "&eALL");
		}
	}

	public void updateUsage(int usage) {
		ItemStack item = this.getByName("Usage");
		updateActually(item, "&e"+String.valueOf(usage));
	}

	public void changeSneaking() {
		ItemStack item = this.getByName("Sneaking");
		if(getActually(item).contains("True")) {
			updateActually(item, "&cFalse");
		}
		else {
			updateActually(item, "&aTrue");
		}
	}

	public void changeNeedConfirm() {
		ItemStack item = this.getByName("Need player confirmation");
		if(getActually(item).contains("True")) {
			updateActually(item, "&cFalse");
		}
		else {
			updateActually(item, "&aTrue");
		}
	}

	public void changeCancelDrop() {
		ItemStack item = this.getByName("Cancel drop");
		if(this.getActually(item).contains("True")) {
			updateActually(item, "&cFalse");
		}
		else {
			updateActually(item, "&aTrue");
		}
	}
	public void changeKeepItemOnDeath() {
		ItemStack item = this.getByName("Keep item on death");
		if(this.getActually(item).contains("True")) {
			updateActually(item, "&cFalse");
		}
		else {
			updateActually(item, "&aTrue");
		}
	}

	public void changeGiveFirstJoin() {
		ItemStack item = this.getByName("Give first join");
		if(this.getActually(item).contains("True")) {
			updateActually(item, "&cFalse");
		}
		else {
			updateActually(item, "&aTrue");
		}
	}

	public void changeGiveSlot() {
		ItemStack item = this.getByName("Give slot");
		if(this.getActually(item).contains("1")) {
			updateActually(item, "&e2");
		}
		else if(this.getActually(item).contains("2")) {
			updateActually(item, "&e3");
		}
		else if(this.getActually(item).contains("3")) {
			updateActually(item, "&e4");
		}
		else if(this.getActually(item).contains("4")) {
			updateActually(item, "&e5");
		}
		else if(this.getActually(item).contains("5")) {
			updateActually(item, "&e6");
		}
		else if(this.getActually(item).contains("6")) {
			updateActually(item, "&e7");
		}
		else if(this.getActually(item).contains("7")) {
			updateActually(item, "&e8");
		}
		else if(this.getActually(item).contains("8")) {
			updateActually(item, "&e9");
		}
		else if(this.getActually(item).contains("9")) {
			updateActually(item, "&eFirst empty");
		}
		else if(this.getActually(item).contains("First empty")) {
			updateActually(item, "&e1");
		}
	}

	public void updateEnchantments(Map<Enchantment, Integer> enchants) {


		ItemStack item = this.getByName("Enchantments");
		ItemMeta toChange = item.getItemMeta();

		if(enchants.size()>0) toChange.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		else toChange.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		for(Enchantment e : toChange.getEnchants().keySet()) {
			toChange.removeEnchant(e);
		}
		for(Enchantment enchant : enchants.keySet()) {
			toChange.addEnchant(enchant, enchants.get(enchant), true);
		}
		item.setItemMeta(toChange);


		item = this.getPreviewItem();
		toChange = item.getItemMeta();

		if(enchants.size()>0) toChange.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		else toChange.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		for(Enchantment e : toChange.getEnchants().keySet()) {
			toChange.removeEnchant(e);
		}
		for(Enchantment enchant : enchants.keySet()) {
			toChange.addEnchant(enchant, enchants.get(enchant), true);
		}
		item.setItemMeta(toChange);
	}

	public void updateCommands(List<String> commands) {
		int i=0;
		while(i<commands.size()) {
			commands.set(i, sc.coloredString(commands.get(i)));
			i++;
		}
		ItemStack item = this.getByName("Commands");
		ItemMeta toChange = item.getItemMeta();
		List<String> loreUpdate= toChange.getLore().subList(0, 3);
		loreUpdate.addAll(commands);
		toChange.setLore(loreUpdate);
		item.setItemMeta(toChange);
	}



	public String getActually(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		for(String s: lore) {
			if(sc.decoloredString(s).contains("actually: ")) {
				return sc.decoloredString(s).split("actually: ")[1];
			}
		}
		return null;
	}

	public String getActuallyWithColor(ItemStack item) {
		List<String> lore = item.getItemMeta().getLore();
		for(String s: lore) {
			if(sc.decoloredString(s).contains("actually: ")) {
				return s.replaceAll("§", "&").split("actually: ")[1];
			}
		}
		return null;
	}

	public void updateActually(ItemStack item, String update) {
		ItemMeta meta= item.getItemMeta();
		List<String> lore = meta.getLore();
		int cpt=0;
		for(String s: lore) {
			if(sc.decoloredString(s).contains("actually:")) {
				break;
			}
			cpt++;
		}
		lore.set(cpt, sc.coloredString("&7actually: "+update));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public String getIdentification() {
		return getActually(this.getByName("ID"));
	}

	public boolean isNewItem() {
		return newItem;
	}

	public void setNewItem(boolean newItem) {
		this.newItem = newItem;
	}


}

