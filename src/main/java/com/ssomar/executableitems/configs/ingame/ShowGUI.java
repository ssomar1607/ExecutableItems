package com.ssomar.executableitems.configs.ingame;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.Item;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowGUI extends GUI {

	static int index;

	//Page 1
	public ShowGUI(Player p) {
		super("&8&lEI Show - Page 1", 5*9);
		setIndex(1);
		loadItems(p);
	}

	// other pages
	public ShowGUI(int index, Player p) {
		super("&8&lEI Show - Page "+index, 5*9);
		setIndex(index);
		loadItems(p);
	}

	public void loadItems(Player p) {
		List<String> items = ConfigMain.getInstance().getAllItems();
		int i=0;
		int total=0;
		for(String str: items) {
			if((index-1)*36<=total && total<index*36) {
				if(ConfigMain.getInstance().getItems().containsIdentification(str)) {
					Item item= ConfigMain.getInstance().getItems().getByIdentification(str);
					List<String> desc = new ArrayList<>();
					desc.add("");
					desc.add("&4(shift + left click to delete)");
					desc.add("&7(click to edit)");
					desc.add("&a&l➤ WORK FINE");
					desc.add("&7• Name: " +item.getName());
					desc.add("&7• Lore: ");
					for(int j=0; j<item.getLore().size();j++) {
						desc.add(" "+item.getLore().get(j));
					}
					desc.add("&7• Cooldown: &e" + item.getCooldown());
					desc.add("&7• Click: &e" + item.getClick());
					desc.add("&7• Sneaking: &e" + item.isSneaking());
					desc.add("&7• Cancel drop: &e" + item.isCancelDrop());
					desc.add("&7• Usage: &e" + item.getUse());
					desc.add("&7• Enchantments: ");
					for(Enchantment e : item.getEnchants().keySet()) {
						desc.add(" "+e.getKey().toString().replace("minecraft:", "")+":"+item.getEnchants().get(e));
					}
					desc.add("&7• Commands: ");
					for(int j=0; j<item.getCommands().size();j++) {
						desc.add(" "+item.getCommands().get(j));
					}
					String[]descArray= new String[desc.size()];
					for(int j=0; j<desc.size();j++) {
						if(desc.get(j).length()>40) {
							descArray[j]=desc.get(j).substring(0, 39)+"...";
						}
						else {
							descArray[j]=desc.get(j);
						}

					}

					createItem(item.getMaterial(), 	1 , i, 	"&2&l✦ ID: &a"+str, 	item.isGlow() ||item.haveEnchant(), false, descArray);
				}
				else {
					createItem(Material.BARRIER, 	1 , i, 	"&4&l✦ ID: &c"+str, 	false, false, "", "&7(click to edit)", "&4(shift + left click to delete)", "&c&l➤ ERROR WITH THIS ITEM" );
				}
				i++;
			}
			total++;
		}
		if(items.size()>36 && index*36<items.size()) {
			createItem(Material.ARROW, 	1 , 44, 	"&e&l▶ Next page ", 	false, false);
		}
		if(index>1) {
			createItem(Material.ARROW, 	1 , 37, 	"&e&lPrevious page ◀", 	false, false);
		}
		createItem(Material.REDSTONE_BLOCK, 	1 , 36, 	"&4&l▶ Exit", 	false, false);
		createItem(Material.EMERALD, 	1 , 40, 	"&2&l✚ New Item", 	true, false);
		createItem(Material.HOPPER, 	1 , 42, 	"&2&l✚ Test your item", 	true, false);
		//Last Edit
		if(ConfigGUIManager.getInstance().getCache().containsKey(p)) {
			createItem(Material.ANVIL, 							1 , 39, "&e&lReturn to your last edit", 		false, false, 	"", "&6Click here to continue" , "&6your last edit" );
		}
	}

	public void clicked(Player p, String itemName, String currentPage) {
		currentPage=sc.decoloredString(currentPage);
		itemName=sc.decoloredString(itemName);

		if(itemName.contains("Next page")) {
			p.closeInventory();
			new ShowGUI(Integer.valueOf(currentPage.split("Page ")[1])+1, p).openGUISync(p);
		}
		else if(itemName.contains("Previous page")) {
			p.closeInventory();
			new ShowGUI(Integer.valueOf(currentPage.split("Page ")[1])-1, p).openGUISync(p);
		}
		else if(itemName.contains("Exit")) {
			p.closeInventory();
		}
		else if(itemName.contains("New Item")) {
			p.closeInventory();
			ConfigGUIManager.getInstance().startEditing(p);
		}
		else if(itemName.contains("Return")) {
			p.closeInventory();
			ConfigGUIManager.getInstance().getCache().get(p).openGUISync(p);
		}
		else if(itemName.contains("Test")) {
			p.closeInventory();
			new TestGUI(p).openGUISync(p);
		}
		else {
			ConfigGUIManager.getInstance().startEditing(p, ConfigMain.getInstance().getItem(itemName.split("ID: ")[1]));
		}
	}

	public void shiftLeftClicked(Player p, String itemName) {
		itemName= sc.decoloredString(itemName);
		if(itemName.contains("ID")) {
			p.closeInventory();
			new ConfigWriter(ExecutableItems.getPlugin()).deleteItem(itemName.split("ID: ")[1]);
			ExecutableItems.getPlugin().onReload(true);
			new ShowGUI(1,p).openGUISync(p);
		}
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		ShowGUI.index = index;
	}




}
