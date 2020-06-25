package com.ssomar.executableitems.configs.ingame;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.util.StringConverter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigGUIManager {

	private static ConfigGUIManager instance;	
	private ExecutableItems main;	
	private HashMap<Player, ConfigGUIv2> cache;
	private HashMap<Player, String> requestWriting;
	private HashMap<Player, List<String>> currentWriting;
	private static StringConverter sc= new StringConverter();

	public ConfigGUIManager() {
		cache = new HashMap<>();
		requestWriting = new HashMap<>();
		currentWriting = new HashMap<>();
		instance=this;
	}

	public void setup(ExecutableItems main) {
		this.main=main;
	}

	public void startEditing(Player p) {
		if(cache.containsKey(p)) {
			if(cache.get(p).isNewItem()) cache.get(p).openGUISync(p);
			else {
				cache.put(p, new ConfigGUIv2());
				cache.get(p).openGUISync(p);
			}
		}
		else {
			cache.put(p, new ConfigGUIv2());
			cache.get(p).openGUISync(p);
		}
	}
	
	public void startEditing(Player p, ItemStack item) {
			cache.put(p, new ConfigGUIv2(item));
			cache.get(p).openGUISync(p);
	}

	public void startEditing(Player p, Item item) {

		cache.put(p, new ConfigGUIv2(item));
		cache.get(p).openGUISync(p);
	}

	public void clicked(Player p, ItemStack item) {
		if(item!=null) {
			String name= sc.decoloredString(item.getItemMeta().getDisplayName());
			if(name.contains("Material")) {
				requestWriting.put(p, "MATERIAL");
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aEnter a material:"));
				space(p);
			}
			else if(name.contains("DisplayName")) {
				requestWriting.put(p, "DISPLAYNAME");
				p.closeInventory();
				space(p);

				TextComponent message = new TextComponent( sc.coloredString("&a&l[ExecutableItems] &aEnter a new name or &aedit &athe &aname: "));

				TextComponent edit = new TextComponent( sc.coloredString("&e&l[EDIT]"));
				edit.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, cache.get(p).getActuallyWithColor(item) ));
				edit.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&eClick here to edit the current name") ).create() ) );

				TextComponent newName = new TextComponent( sc.coloredString("&a&l[NEW]"));
				newName.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "Type the new name here.."));
				newName.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&aClick here to set new name") ).create() ) );

				message.addExtra(new TextComponent(" "));
				message.addExtra(edit);
				message.addExtra(new TextComponent(" "));
				message.addExtra(newName);

				p.spigot().sendMessage(message);
				space(p);
			}
			else if(name.contains("Lore")) {
				requestWriting.put(p, "LORE");
				if(!currentWriting.containsKey(p)) {
					if(cache.get(p).getPreviewItem().getItemMeta().hasLore()) currentWriting.put(p, cache.get(p).getPreviewItem().getItemMeta().getLore());
					else currentWriting.put(p, new ArrayList<>());
				}
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION LORE:"));
				showLoreEditor(p);
				space(p);
			}
			else if(name.contains("Other items cooldown")) {
				requestWriting.put(p, "OTHERCD");
				if(!currentWriting.containsKey(p)) {
					currentWriting.put(p, cache.get(p).getByName("Other items cooldown").getItemMeta().getLore().subList(3, cache.get(p).getByName("Other items cooldown").getItemMeta().getLore().size()));
				}
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION OTHER ITEMS COOLDOWN:"));
				this.showOtherCooldown(p);
				space(p);
			}
			else if(name.contains("Glow")) {
				cache.get(p).changeGlow();
			}
			else if(name.contains("Item cooldown")) {
				requestWriting.put(p, "COOLDOWN");
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aEnter a cooldown (min 0, in seconds):"));
				space(p);
			}
			else if(name.contains("Click")) {
				cache.get(p).changeClick();
			}
			else if(name.contains("Usage")) {
				requestWriting.put(p, "USAGE");
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aEnter how many usage you want (infinite: -1 / else min: 1):"));
				space(p);
			}
			else if(name.contains("Sneaking")) {
				cache.get(p).changeSneaking();
			}
			else if(name.contains("Need player confirmation")) {
				cache.get(p).changeNeedConfirm();
			}
			else if(name.contains("Cancel drop")) {
				cache.get(p).changeCancelDrop();
			}
			else if(name.contains("Keep item on death")) {
				cache.get(p).changeKeepItemOnDeath();
			}
			else if(name.contains("Give first join")) {
				cache.get(p).changeGiveFirstJoin();
			}
			else if(name.contains("Give slot")) {
				cache.get(p).changeGiveSlot();
			}
			else if(name.contains("Enchantments")) {
				requestWriting.put(p, "ENCHANT");
				if(!currentWriting.containsKey(p)) {
					Map<Enchantment, Integer> enchants= cache.get(p).getByName("Enchantments").getItemMeta().getEnchants();
					List<String> result= new ArrayList<>();
					for(Enchantment enchant: enchants.keySet()) {
						result.add(enchant.getKey().toString().replace("minecraft:", "")+":"+enchants.get(enchant));
					}
					currentWriting.put(p, result);
				}
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION ENCHANTMENTS:"));
				showEnchantEditor(p);
				space(p);
			}
			else if(name.contains("Commands")) {
				requestWriting.put(p, "COMMANDS");
				if(!currentWriting.containsKey(p)) {
					List<String> lore = cache.get(p).getByName("Commands").getItemMeta().getLore();
					currentWriting.put(p, lore.subList(3, lore.size()));
				}
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION LORE:"));
				showCommandsEditor(p);
				space(p);
			}
			else if(name.contains("Create this item")) {
				requestWriting.put(p, "IDENTIFICATION");
				p.closeInventory();
				space(p);
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aEnter the id of the executable item :"));
				space(p);
			}

			else if(name.contains("Reset")) {
				p.closeInventory();
				cache.replace(p, new ConfigGUIv2());
				cache.get(p).openGUISync(p);
			}

			else if(name.contains("Save")) {
				saveItem(p, cache.get(p).getIdentification(), false);
				p.closeInventory();
				new ShowGUI(p).openGUISync(p);
			}

			else if(name.contains("Exit")) {
				p.closeInventory();
			}

			else if(name.contains("Back")) {
				p.closeInventory();
				new ShowGUI(1,p).openGUISync(p);
			}
		}

	}

	public void receivedMessage(Player p, String message) {

		if(message.equals("exit")) {
			if(requestWriting.get(p).equals("LORE")) {
				cache.get(p).updateLore(currentWriting.get(p));
			}
			else if(requestWriting.get(p).equals("COMMANDS")) {
				cache.get(p).updateCommands(currentWriting.get(p));
			}
			else if(requestWriting.get(p).equals("OTHERCD")) {
				cache.get(p).updateOtherCooldown(currentWriting.get(p));
			}
			else if(requestWriting.get(p).equals("ENCHANT")) {
				Map<Enchantment, Integer> enchants= new HashMap<>();
				for(String s: currentWriting.get(p)) {
					String enchant=s.split(":")[0];
					String lvl=s.split(":")[1];
					
					Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
					int level=Integer.valueOf(lvl);
					enchants.put(enchantment, level);	
				}
				cache.get(p).updateEnchantments(enchants);
			}
			currentWriting.remove(p);
			requestWriting.remove(p);
			cache.get(p).openGUISync(p);
		}

		else if(message.contains("delete line <")) {	
			space(p);
			space(p);
			int line = Integer.valueOf(message.split("delete line <")[1].split(">")[0]);
			deleteLine(p, line);
			p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION &aYou have delete the line: "+line+" !"));
			if(requestWriting.get(p).equals("LORE")) showLoreEditor(p);
			else if (requestWriting.get(p).equals("COMMANDS")) showCommandsEditor(p);
			else if (requestWriting.get(p).equals("ENCHANT")) showEnchantEditor(p);
			space(p);
			space(p);			
		}

		else if(message.contains("up line <")) {

			space(p);
			space(p);
			int line= Integer.valueOf(message.split("up line <")[1].split(">")[0]);
			if (line!=0) {
				String current = currentWriting.get(p).get(line);
				currentWriting.get(p).set(line, currentWriting.get(p).get(line-1));
				currentWriting.get(p).set(line-1, current);
			}

			if(requestWriting.get(p).equals("LORE")) showLoreEditor(p);
			else if (requestWriting.get(p).equals("COMMANDS")) showCommandsEditor(p);
			else if (requestWriting.get(p).equals("ENCHANT")) showEnchantEditor(p);

			space(p);
			space(p);

		}
		
		else if(message.contains("down line <")) {

			space(p);
			space(p);
			int line= Integer.valueOf(message.split("down line <")[1].split(">")[0]);
			if (line!=currentWriting.get(p).size()-1) {
				String current = currentWriting.get(p).get(line);
				currentWriting.get(p).set(line, currentWriting.get(p).get(line+1));
				currentWriting.get(p).set(line+1, current);
			}

			if(requestWriting.get(p).equals("LORE")) showLoreEditor(p);
			else if (requestWriting.get(p).equals("COMMANDS")) showCommandsEditor(p);
			else if (requestWriting.get(p).equals("ENCHANT")) showEnchantEditor(p);

			space(p);
			space(p);

		}

		else if(message.contains("edit line <")) {

			space(p);
			space(p);
			int line=0;
			boolean error=false;
			try {
				line = Integer.valueOf(message.split("edit line <")[1].split(">")[0]);
				String newLine;
				if(message.split("edit line <")[1].split("->").length==1) newLine="&7";
				else newLine=  message.split("edit line <")[1].split("->")[1];
				editLine(p, line, newLine);	
			}
			catch(NumberFormatException e) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &cError incorrect number line, dont edit the message before the '->' !"));
				error=true;
			}
			if(!error) {
				p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION &aYou have edit the line: "+line+" !"));
				if(requestWriting.get(p).equals("LORE")) showLoreEditor(p);
				else if (requestWriting.get(p).equals("COMMANDS")) showCommandsEditor(p);
				else if (requestWriting.get(p).equals("ENCHANT")) showEnchantEditor(p);
			}
			space(p);
			space(p);

		}

		else if(requestWriting.get(p).equals("MATERIAL")) {
			try {
				cache.get(p).updateMaterial(Material.valueOf(sc.decoloredString(message.toUpperCase())));
				cache.get(p).openGUISync(p);
				requestWriting.remove(p);
			}
			catch(IllegalArgumentException e) {
				p.sendMessage(sc.coloredString("\n &c&l[ExecutableItems] &cError invalid material: " +message+ "\n&cPlease enter valid material: (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)"));
			}
		}

		else if(requestWriting.get(p).equals("DISPLAYNAME")) {
			cache.get(p).updateName(sc.coloredString(message));
			cache.get(p).openGUISync(p);
			requestWriting.remove(p);
		}
		
		else if(requestWriting.get(p).equals("ENCHANT")) {
			if(message.isEmpty() || message.equals(" ")) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cYou have send empty message !"));
				return;
			}
			if(message.split(":").length!=2) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cIncorect form for the enchant ex: KNOCKBACK:2"));
				return;
			}
			String enchant=message.split(":")[0];
			String lvl=message.split(":")[1];
			
			Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchant.toLowerCase()));
			if(enchantment==null) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cThe enchant: " +enchant +" is not valid!"));
				return;
			}
			try {
				Integer.valueOf(lvl);
			}catch(NumberFormatException e) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cThe level: " +lvl +" is not valid!"));
				return;
			}	
			
			if(currentWriting.containsKey(p)) {
				currentWriting.get(p).add(sc.coloredString(message));
			}
			else {
				ArrayList<String> list = new ArrayList<>();
				list.add(sc.coloredString(message));
				currentWriting.put(p, list);
			}
			p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION &aYou have added new line !"));
			showEnchantEditor(p);
		}
		
		else if(requestWriting.get(p).equals("OTHERCD")) {
			if(message.isEmpty() || message.equals(" ")) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cYou have send empty message !"));
				return;
			}
			if(message.split(":").length!=2) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cIncorect form for the enchant ex: ID:COOLDOWN"));
				return;
			}
			String id=message.split(":")[0];
			String cdMessage=message.split(":")[1];
			
			if(!ConfigMain.getInstance().getItems().containsIdentification(id)){
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cIncorect id, this id doesnt exist "+ id));
				return;
			}
			try {
				Integer.valueOf(cdMessage);
			}catch(NumberFormatException e) {
				p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &4&lEDITION &cThe cooldown: " +cdMessage +" is not valid!"));
				return;
			}	
			
			if(currentWriting.containsKey(p)) {
				currentWriting.get(p).add(message);
			}
			else {
				ArrayList<String> list = new ArrayList<>();
				list.add(message);
				currentWriting.put(p, list);
			}
			p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION &aYou have added new line !"));
			this.showOtherCooldown(p);
		}

		else if(requestWriting.get(p).equals("LORE") || requestWriting.get(p).equals("COMMANDS")) {
			if(message.isEmpty() || message.equals(" ")) {
				message="&7";
			}
			if(currentWriting.containsKey(p)) {
				currentWriting.get(p).add(sc.coloredString(message));
			}
			else {
				ArrayList<String> list = new ArrayList<>();
				list.add(sc.coloredString(message));
				currentWriting.put(p, list);
			}
			p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &2&lEDITION &aYou have added new line !"));
			if(requestWriting.get(p).equals("LORE")) showLoreEditor(p);
			else if (requestWriting.get(p).equals("COMMANDS")) showCommandsEditor(p);
		}

		else if(requestWriting.get(p).equals("COOLDOWN")) {
			boolean error=false;

			try { Integer.valueOf(message); }
			catch(NumberFormatException e) { error=true; }
			if(error && Integer.valueOf(message)<0) {
				p.sendMessage(sc.coloredString("\n &c&l[ExecutableItems] &cError invalid cooldown: " +message+ "\n&cPlease enter valid cooldown: (min: 1, in seconds)"));
			}
			else {
				cache.get(p).updateCooldown(Integer.valueOf(message));
				cache.get(p).openGUISync(p);
				requestWriting.remove(p);
			}
		}

		else if(requestWriting.get(p).equals("USAGE")) {
			boolean error=false;

			try { Integer.valueOf(message); }
			catch(NumberFormatException e) { error=true; }
			if(error || Integer.valueOf(message)<-1) {
				p.sendMessage(sc.coloredString("\n &c&l[ExecutableItems] &cError invalid usage: " +message+ "\n&cPlease enter valid usage: (unlimited: -1)"));
			}
			else {
				cache.get(p).updateUsage(Integer.valueOf(message));
				cache.get(p).openGUISync(p);
				requestWriting.remove(p);
			}
		}

		else if(requestWriting.get(p).equals("IDENTIFICATION")) {
			saveItem(p, message, false);
		}
	}

	public void deleteLine(Player p, int nb) {
		if(currentWriting.containsKey(p)) {
			currentWriting.get(p).remove(nb);
		}
	}

	public void editLine(Player p, int nb, String edition) {
		if(currentWriting.containsKey(p)) {
			currentWriting.get(p).set(nb, edition);
		}
	}

	public void space(Player p) {
		p.sendMessage("");
	}

	public void showEnchantEditor(Player p) {
		p.sendMessage(sc.coloredString("&7➤ EXEMPLE: KNOCKBACK:2 (ENCHANT:LEVEL)"));
		p.sendMessage(sc.coloredString("&7➤ Your custom enchantment(s):"));
		space(p);
		space(p);
		showEditor(p, "enchant");
	}

	public void showLoreEditor(Player p) {
		p.sendMessage(sc.coloredString("&7➤ Your custom lore:"));
		space(p);
		space(p);
		showEditor(p, "lore");
	}
	
	public void showOtherCooldown(Player p) {
		p.sendMessage(sc.coloredString("&7➤ EXEMPLE: heal:5 (ID:COOLDOWN)"));
		p.sendMessage(sc.coloredString("&7➤ Your other items cooldown:"));
		space(p);
		space(p);
		showEditor(p, "othercd");
	}

	public void showCommandsEditor(Player p) {
		p.sendMessage(sc.coloredString("&7➤Variable: player= %player%"));
		p.sendMessage(sc.coloredString("&7➤Wiki: https://github.com/ssomar1607/ExecutableItems/wiki/Commands"));
		p.sendMessage(sc.coloredString("&7➤ Your commands: (the '/' is useless)"));
		space(p);
		space(p);
		showEditor(p , "commands");
	}

	public void showEditor(Player p, String type) {

		int cpt=0;
		if(currentWriting.containsKey(p) && currentWriting.get(p)!=null ) {
			if(!currentWriting.get(p).isEmpty()) {
				for(String s: currentWriting.get(p)) {

					TextComponent message;
					if(type.equals("lore")) {
						message = new TextComponent( sc.coloredString("&7"+cpt+"."+sc.coloredString("&5"+s)));
					}
					else message = new TextComponent( sc.coloredString("&7"+cpt+"."+sc.coloredString(s)));

					TextComponent edit = new TextComponent( sc.coloredString("&e&l[EDIT]"));
					edit.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "edit line <"+cpt+"> ->"+s.replaceAll("§", "&") ));
					edit.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&eClick Here to edit line: "+ cpt) ).create() ) );

					TextComponent delete = new TextComponent( sc.coloredString("&c&l[X]"));
					delete.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "delete line <"+cpt+">"));
					delete.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&cClick here to delete the line: "+ cpt) ).create() ) );

					TextComponent downLine = new TextComponent( sc.coloredString("&a&l[↓]"));
					downLine.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "down line <"+cpt+">"));
					downLine.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&cClick here to go down the line: "+ cpt) ).create() ) );

					TextComponent upLine = new TextComponent( sc.coloredString("&a&l[↑]"));
					upLine.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "up line <"+cpt+">"));
					upLine.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&cClick here to go up the line: "+ cpt) ).create() ) );

					message.addExtra(new TextComponent(" "));
					message.addExtra(edit);
					message.addExtra(new TextComponent(" "));
					message.addExtra(delete);
					message.addExtra(new TextComponent(" "));
					message.addExtra(downLine);
					message.addExtra(upLine);

					p.spigot().sendMessage(message);		
					cpt++;		
				}
			}
			else {
				p.sendMessage(sc.coloredString("&7EMPTY"));
			}
		}else {
			p.sendMessage(sc.coloredString("&7EMPTY"));
		}
		space(p);
		space(p);

		TextComponent message = new TextComponent(sc.coloredString("&7➤Options: "));

		TextComponent finish = new TextComponent( sc.coloredString("&4&l[FINISH]"));
		finish.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "exit"));
		finish.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&4Click Here when you have finish to edit the "+ type) ).create() ) );

		TextComponent addLine = new TextComponent( sc.coloredString("&2&l[ADD LINE]"));
		addLine.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "Type new line here.."));
		addLine.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( sc.coloredString("&2Click Here if you want add new line " +type) ).create() ) );

		message.addExtra(finish);
		message.addExtra(new TextComponent(" "));
		message.addExtra(addLine);

		p.spigot().sendMessage(message);
	}

	public void saveItem(Player p, String id, boolean newItem) {
		ItemStack extraction= cache.get(p).getPreviewItem();
		ItemStack extractionUsage= cache.get(p).getByName("Usage");
		ItemStack extractionCooldown= cache.get(p).getByName("Item cooldown");
		ItemStack extractionOtherCooldown= cache.get(p).getByName("Other items cooldown");
		ItemStack extractionClick= cache.get(p).getByName("Click");
		ItemStack extractionSneaking= cache.get(p).getByName("Sneaking");
		ItemStack extractionNeedConfirm= cache.get(p).getByName("Need player confirmation");
		ItemStack extractionCancelDrop= cache.get(p).getByName("Cancel drop");
		ItemStack extractionKeepItemOnDeath= cache.get(p).getByName("Keep item on death");
		ItemStack extractionGiveFirstJoin= cache.get(p).getByName("Give first join");
		ItemStack extractionGiveSlot= cache.get(p).getByName("Give slot");
		ItemStack extractionCommands= cache.get(p).getByName("Commands");
		ItemStack extractionEnchant= cache.get(p).getByName("Enchantments");

		String material= extraction.getType().toString();
		String displayName= extraction.getItemMeta().getDisplayName();
		List<String> lore=null;
		
		if(!extraction.getItemMeta().hasLore()){
			p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &cError Lore is empty, you need to set a least 1 line"));
			return;
		}
		else lore= extraction.getItemMeta().getLore();
		if(extraction.getItemMeta().getLore().size()==1 && extraction.getItemMeta().getLore().get(0).equals("")){
			p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &cError Lore is empty, you need to set a least 1 line"));
			return;
		}
		boolean glow= extraction.containsEnchantment(Enchantment.PROTECTION_FALL);
		int use = Integer.valueOf(sc.decoloredString(extractionUsage.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		int cooldown = Integer.valueOf(sc.decoloredString(extractionCooldown.getItemMeta().getLore().get(3)).split("actually: ")[1]);
		
		List<String> otherCooldown= extractionOtherCooldown.getItemMeta().getLore().subList(3, extractionOtherCooldown.getItemMeta().getLore().size());
		
		String click= sc.decoloredString(extractionClick.getItemMeta().getLore().get(2)).split("actually: ")[1].toLowerCase();
		if(click.toLowerCase().equals("projectile") && (material!="EGG" && material!="ENDER_PEARL" && material!="LINGERING_POTION" && material!="SNOWBALL" && material!="SPLASH_POTION" && material!="EXPERIENCE_BOTTLE")) {
			p.sendMessage(sc.coloredString("[ExecutableItems] Invalid click, projectile is ONLY compatible with material EGG, ENDER_PEARL, LINGERING_POTION, SNOWBALL, SPLASH_POTION, EXPERIENCE_BOTTLE"));
			return;
		}
		boolean sneaking = Boolean.valueOf(sc.decoloredString(extractionSneaking.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		boolean needConfirm = Boolean.valueOf(sc.decoloredString(extractionNeedConfirm.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		boolean cancelDrop = Boolean.valueOf(sc.decoloredString(extractionCancelDrop.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		boolean keepItemOnDeath = Boolean.valueOf(sc.decoloredString(extractionKeepItemOnDeath.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		boolean giveFirstJoin = Boolean.valueOf(sc.decoloredString(extractionGiveFirstJoin.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		int giveSlot=-1;
		if(sc.decoloredString(extractionGiveSlot.getItemMeta().getLore().get(2)).split("actually: ")[1].contains("First empty")) {
			giveSlot=0;
		}
		else {
			giveSlot = Integer.valueOf(sc.decoloredString(extractionGiveSlot.getItemMeta().getLore().get(2)).split("actually: ")[1]);
		}

		Map<Enchantment, Integer> enchants= extractionEnchant.getEnchantments();
		
		List<String> commands= new ArrayList<>();
		int i = 3;
		int size= extractionCommands.getItemMeta().getLore().size();
		while(i<size) {
			commands.add(extractionCommands.getItemMeta().getLore().get(i));
			i++;
		}

		if(commands.isEmpty()) {
			p.sendMessage(sc.coloredString("&c&l[ExecutableItems] &cError list of commands is empty, you need to set a least 1 command"));
		}
		else {
			ConfigWriter cw = new ConfigWriter(main);
			if(cw.createItem(p, id, material, displayName, lore, glow, use, cooldown, otherCooldown, click, sneaking, needConfirm, cancelDrop, keepItemOnDeath, giveFirstJoin, giveSlot, enchants, commands, newItem)) {
				if(newItem)
					p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aYou have succesfully added the new item: ")+ id);
				else
					p.sendMessage(sc.coloredString("&a&l[ExecutableItems] &aYou have succesfully update the item: ")+ id);	
				cache.remove(p);
				main.onReload(true);
				requestWriting.remove(p);
			}

		}
	}



	public HashMap<Player, String> getRequestWriting() {
		return requestWriting;
	}

	public void setRequestWriting(HashMap<Player, String> requestWriting) {
		this.requestWriting = requestWriting;
	}

	public static ConfigGUIManager getInstance() {
		if(instance==null) return new ConfigGUIManager();
		return instance;
	}

	public HashMap<Player, ConfigGUIv2> getCache() {
		return cache;
	}

	public void setCache(HashMap<Player, ConfigGUIv2> cache) {
		this.cache = cache;
	}



}
