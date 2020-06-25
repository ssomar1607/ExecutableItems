package com.ssomar.executableitems.configs.ingame;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.util.StringConverter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ConfigWriter {

	protected FileConfiguration config;

	private static StringConverter sc= new StringConverter();

	private ExecutableItems main;

	public ConfigWriter() {
		
	}
	public ConfigWriter(ExecutableItems main) {
		config= main.getConfig();
		this.main=main;
	}

	public boolean createItem(Player creator,String id, String material,  String displayName, List<String> lore, boolean glow, int use, int cooldown, List<String> otherCooldown, String click, boolean sneaking, boolean needConfirmBeforeUse, boolean cancelDrop, boolean keepItemOnDeath, boolean giveFirstJoin, int giveSlot, Map<Enchantment, Integer> enchants, List<String> commands, boolean newItem) {

		ConfigurationSection itemsConfig = config.getConfigurationSection("items");
		if(itemsConfig.isConfigurationSection(id) && newItem) {
			creator.sendMessage(sc.coloredString("&c&l[ExecutableItems] &cError this identifiant already exists in the config.yml"));
			return false;
		}
		
		itemsConfig.set(id+".name",sc.deconvertColor(displayName));
		itemsConfig.set(id+".lore",sc.deconvertColor(lore));
		itemsConfig.set(id+".material",sc.deconvertColor(material));
		itemsConfig.set(id+".glow",glow);
		itemsConfig.set(id+".cooldown",cooldown);
		itemsConfig.set(id+".otherCooldown",otherCooldown);
		itemsConfig.set(id+".click",click);
		itemsConfig.set(id+".sneaking",sneaking);
		itemsConfig.set(id+".cancel-item-drop",cancelDrop);
		itemsConfig.set(id+".keepItemOnDeath",keepItemOnDeath);
		itemsConfig.set(id+".give-first-join",giveFirstJoin);
		itemsConfig.set(id+".give-slot",giveSlot);
		itemsConfig.set(id+".usage",use);
		itemsConfig.set(id+".needConfirmBeforeUse", needConfirmBeforeUse);
		itemsConfig.set(id+".commands",sc.deconvertColor(commands));
		itemsConfig.set(id+".enchant",null);
		for(Enchantment e: enchants.keySet()) {
			itemsConfig.set(id+".enchant."+e.getKey().toString().replace("minecraft:", "")+".level",enchants.get(e));
		}
		main.saveConfig();

		return true;

	}
	
	public void deleteItem(String id) {
		ConfigurationSection itemsConfig = config.getConfigurationSection("items");
		itemsConfig.set(id, null);
		main.saveConfig();
		
	}


}
