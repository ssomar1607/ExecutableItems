package com.ssomar.executableitems.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.items.ItemsHandler;
import com.ssomar.executableitems.util.StringConverter;

public final class ConfigMain extends Config {
	private static ConfigMain instance;

	private static StringConverter sc = new StringConverter();

	private ItemsHandler items;

	private String locale;


	private ConfigMain() {
		super("config.yml");
	}

	public void load(boolean firstCreate) {



		//ITEMS CONFIG
		items = new ItemsHandler();
		ConfigurationSection itemsSection = config.getConfigurationSection("items");
		try {
			for(String item : itemsSection.getKeys(false)) {

				ConfigurationSection itemSection= itemsSection.getConfigurationSection(item);
				String identification= item;
				if(items.containsIdentification(item)) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Duplicate item: " + item);
					continue;
				}

				String name;
				try {
					name= sc.coloredString(itemSection.getString("name"));
				}catch(NullPointerException error) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid name for item: " + item);
					continue;
				}

				List<String> lore;
				try {
					lore= itemSection.getStringList("lore");
					for(int i = 0; i<lore.size();i++) {
						lore.set(i, sc.coloredString(lore.get(i)));
					}
				}catch(NullPointerException error) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid lore for item: " + item);
					continue;
				}

				Material material;
				String headValue="";

				if(!itemSection.getString("material", "INVALID").contains("PLAYER_HEAD-")) {
					try {
						Material.valueOf(itemSection.getString("material", "INVALID"));
					}catch(IllegalArgumentException error) {
						ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid material for item: " + item);
						continue;
					}
				}
				if(Bukkit.getServer().getVersion().contains("1.12") && itemSection.getString("material").contains("PLAYER_HEAD")) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid material for item: " + item +" PLAYER_HEAD is only support by 1.13+ version");
					continue;
				}
				else if(itemSection.getString("material").contains("PLAYER_HEAD-")) {
					material=Material.PLAYER_HEAD;
					headValue=itemSection.getString("material").split("-")[1];
				}
				else {	
					material= Material.valueOf(itemSection.getString("material"));		
				}

				Boolean glow= itemSection.getBoolean("glow", true);

				int cooldown= itemSection.getInt("cooldown", 30);
				
				Map<String, Integer> otherCooldown= new HashMap<>();
				try {
					for(String s: itemSection.getStringList("otherCooldown")){
						if(s.contains(":")){
							int ICooldown=0;
							try {
								ICooldown=Integer.valueOf(s.split(":")[1]);
							}catch(NumberFormatException e) {
								ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid cooldown for othercooldown "+ s.split(":")[1]+" for item: " + item);
								continue;
							}
							otherCooldown.put(s.split(":")[0], ICooldown);
						}
						else {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid form for otherCooldown: " + s +" (ID:COOLDOWN)");
							continue;
						}
					}
				}catch(NullPointerException error) {
					otherCooldown=null;
				}

				String click= itemSection.getString("click", "right");
				click=click.toLowerCase();
				if(!click.toLowerCase().equals("left") && !click.toLowerCase().equals("right") && !click.toLowerCase().equals("all") && !click.toLowerCase().equals("consume") && !click.toLowerCase().equals("projectile") && !click.toLowerCase().equals("target") && !click.toLowerCase().equals("grabplayername")) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid click "+ click+" for item: " + item);
					continue;
				}

				if(click.toLowerCase().equals("projectile") && (material!=Material.EGG && material!=Material.ENDER_PEARL && material!=Material.LINGERING_POTION && material!=Material.SNOWBALL && material!=Material.SPLASH_POTION && material!=Material.EXPERIENCE_BOTTLE)) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid click "+ click+" for item: " + item+ " projectile is ONLY compatible material with EGG, ENDER_PEARL, LINGERING_POTION, SNOWBALL, SPLASH_POTION, EXPERIENCE_BOTTLE");
					continue;
				}


				Boolean sneaking= itemSection.getBoolean("sneaking", false);

				Boolean cancelDrop= itemSection.getBoolean("cancel-item-drop", true);

				Boolean keepItemOnDeath= itemSection.getBoolean("keepItemOnDeath", false);
				
				Boolean giveFirstJoin= itemSection.getBoolean("give-first-join", false);

				int giveSlot=-1;
				if(giveFirstJoin) {
					giveSlot= itemSection.getInt("give-slot", 0);
				}

				Boolean needConfirmBeforeUse= itemSection.getBoolean("needConfirmBeforeUse", false);

				int use= itemSection.getInt("usage", 0);

				HashMap<Enchantment, Integer> enchants= new HashMap<>();
				try {
					ConfigurationSection enchantsSection= itemSection.getConfigurationSection("enchant");
					for(String enchant : enchantsSection.getKeys(false)) {
						
							Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
							if(enchantment==null) {
								ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid enchant: "+enchant+" for the item: "+item);
								continue;
							}
							int level = enchantsSection.getInt(enchant+".level",1);
							enchants.put(enchantment, level);
	
					}
				}catch(NullPointerException error) {}

				boolean invalidCommand=false;
				List<String> commands= itemSection.getStringList("commands");
				for(int i = 0; i<commands.size();i++) {
					commands.set(i, sc.coloredString(commands.get(i)));
					if(commands.get(i).contains("DELAY ")) {
						try {
							Integer.valueOf(commands.get(i).replaceAll("DELAY ", ""));
						}catch(NumberFormatException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid delay "+ commands.get(i)+" for item: " + item);
							invalidCommand=true;
						}
					}
					else if(commands.get(i).contains("AROUND ")) {
						String [] verifAround= commands.get(i).split("'");
						try {
							Integer.valueOf(verifAround[1]);
						}catch(NumberFormatException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid distance "+ verifAround[1]+" for item: " + item);
							invalidCommand=true;
						}
						if(!verifAround[3].toUpperCase().equals("TRUE") && !verifAround[3].toUpperCase().equals("FALSE")) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid msgPlayerAffected "+ verifAround[3]+" (true or false) for item: " + item);
							invalidCommand=true;
						}

					}
					else if(commands.get(i).contains("ADD POINTS") ) {
						if(Bukkit.getPluginManager().getPlugin("PlayerPoints")==null) ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] ERROR 'ADD POINTS' NEED THE PLUGIN PLAYERPOINTS for item " + item);

						else {
							try {
								Integer.valueOf(commands.get(i).replace("ADD POINTS ", ""));
							}
							catch(NumberFormatException e) {
								ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid Points in command ADD POINTS for item " + item);
								invalidCommand=true;
							}
						}
					}
					//"PARTICLE type: 'DRAGON_BREATH' quantity: '500' offset: '3' speed: '0.2'"
					else if(commands.get(i).contains("PARTICLE")) {
						String [] verifParticle= commands.get(i).split("'");
						try {
							if(verifParticle.length!=8) {
								ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE "+ commands.get(i)+" for item: " + item);
								ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] FOLLOW EXEMPLE PARTICLE :  \"PARTICLE type: 'DRAGON_BREATH' quantity: '500' offset: '3' speed: '0.2'\"");
								invalidCommand=true;
							}
						}catch(NullPointerException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE "+ commands.get(i)+" for item: " + item);
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] FOLLOW EXEMPLE PARTICLE :  \"PARTICLE type: 'DRAGON_BREATH' quantity: '500' offset: '3' speed: '0.2'\"");
							invalidCommand=true;
						}
						try {
							if(Particle.valueOf(verifParticle[1])==null)continue;
						}catch(IllegalArgumentException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE TYPE "+ verifParticle[1]+" for item: " + item);
							invalidCommand=true;
						}
						try {
							Integer.valueOf(verifParticle[3]);
						}catch(NumberFormatException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE QUANTITY "+ verifParticle[3]+" for item: " + item);
							invalidCommand=true;
						}

						try {
							Double.valueOf(verifParticle[5]);
						}catch(NumberFormatException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE OFFSET "+ verifParticle[5]+" for item: " + item);
							invalidCommand=true;
						}

						try {
							Double.valueOf(verifParticle[7]);
						}catch(NumberFormatException e) {
							ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid PARTICLE SPEED "+ verifParticle[7]+" for item: " + item);
							invalidCommand=true;
						}

					}
				}
				if(invalidCommand) {
					ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] "+item+" was not loaded !");
				}
				else {
					items.add(new Item(identification, name, lore, material, headValue, glow, cooldown, otherCooldown, click, sneaking, cancelDrop, keepItemOnDeath, giveFirstJoin, giveSlot, needConfirmBeforeUse, use, enchants, commands));
					ExecutableItems.plugin.getServer().getLogger().fine("[ExecutableItems] "+item+" was successfully loaded !");
				}

			}

			//LOCALE CONFIG
			locale= config.getString("locale");
			if(locale.equals("FR") || locale.equals("EN") || locale.equals("ES") || locale.equals("HU")) {
				ExecutableItems.plugin.getServer().getLogger().fine("[ExecutableItems] locale " + locale+ " loaded");
			}
			else {
				ExecutableItems.plugin.getServer().getLogger().severe("[ExecutableItems] Invalid locale name: " + locale);
				locale="EN";
			}
		}
		catch (NullPointerException e){

		}

	}




	public void reload() {
		this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.pdfile);
		load(false);
	}	

	public static FileConfiguration get() {
		return (getInstance()).config;
	}

	public static ConfigMain getInstance() {
		if (instance == null)
			instance = new ConfigMain(); 
		return instance;
	}

	public List<String> getAllItems(){

		ArrayList<String> result= new ArrayList<>();
		ConfigurationSection itemsSection = config.getConfigurationSection("items");
		for(String item : itemsSection.getKeys(false)) {
			result.add(item);
		}
		return result;
	}

	public void update() {}


	public static void setInstance(ConfigMain instance) {
		ConfigMain.instance = instance;
	}

	public ItemsHandler getItems() {
		return items;
	}

	public void setItems(ItemsHandler items) {
		this.items = items;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Item getItem(String id) {
		ConfigurationSection itemSection= config.getConfigurationSection("items."+id);
		String identification= id;

		String name;
		try {
			name= sc.coloredString(itemSection.getString("name"));
		}catch(NullPointerException error) {
			name=null;
		}

		List<String> lore;
		try {
			lore= itemSection.getStringList("lore");
			for(int i = 0; i<lore.size();i++) {
				lore.set(i, sc.coloredString(lore.get(i)));
			}
		}catch(NullPointerException error) {
			lore=null;
		}

		Material material;
		String headValue="";
		try {
			if(Bukkit.getServer().getVersion().contains("1.12") && itemSection.getString("material").contains("PLAYER_HEAD")) {
				material=Material.DIAMOND;
			}
			else if(itemSection.getString("material").contains("PLAYER_HEAD-")) {
				material=Material.PLAYER_HEAD;
				headValue=itemSection.getString("material").split("-")[1];
			}
			else {	
				try {
					material= Material.valueOf(itemSection.getString("material"));		
				}
				catch(IllegalArgumentException e) {
					material=Material.DIAMOND;
				}
			}
		}
		catch(NullPointerException error) {
			material=Material.DIAMOND;
		}

		Boolean glow= itemSection.getBoolean("glow", true);

		int cooldown= itemSection.getInt("cooldown", 30);
		
		Map<String, Integer> otherCooldown= new HashMap<>();
		try {
			for(String s: itemSection.getStringList("otherCooldown")){
				if(s.contains(":")){
					int ICooldown=0;
					try {
						ICooldown=Integer.valueOf(s.split(":")[1]);
					}catch(NumberFormatException e) {
						
						continue;
					}
					otherCooldown.put(s.split(":")[0], ICooldown);
				}
				else {
				
					continue;
				}
			}
		}catch(NullPointerException error) {
			otherCooldown=null;
		}

		String click= itemSection.getString("click", "right");

		Boolean sneaking= itemSection.getBoolean("sneaking", false);

		Boolean cancelDrop= itemSection.getBoolean("cancel-item-drop", true);
		
		Boolean keepItemOnDeath= itemSection.getBoolean("keepItemOnDeath", false);

		Boolean giveFirstJoin= itemSection.getBoolean("give-first-join", false);

		int giveSlot=-1;
		if(giveFirstJoin) {
			giveSlot= itemSection.getInt("give-slot", 0);
		}

		int use= itemSection.getInt("usage", 0);

		HashMap<Enchantment, Integer> enchants= new HashMap<>();
		try {
			ConfigurationSection enchantsSection= itemSection.getConfigurationSection("enchant");
			for(String enchant : enchantsSection.getKeys(false)) {
				
					Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
					if(enchantment==null) {
						continue;
					}
					int level = enchantsSection.getInt(enchant+".level",1);
					enchants.put(enchantment, level);

			}
		}catch(NullPointerException error) {}

		Boolean needConfirmBeforeUse= itemSection.getBoolean("needConfirmBeforeUse", false);

		List<String> commands= itemSection.getStringList("commands");

		return (new Item(identification, name, lore, material, headValue, glow, cooldown, otherCooldown, click, sneaking, cancelDrop, keepItemOnDeath, giveFirstJoin, giveSlot, needConfirmBeforeUse, use, enchants, commands));


	}



}

