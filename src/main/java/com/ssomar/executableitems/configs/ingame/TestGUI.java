package com.ssomar.executableitems.configs.ingame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.ssomar.executableitems.commands.GiveCommand;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.items.Item;

public class TestGUI extends GUI{

	static int index;

	//Page 1
	public TestGUI(Player p) {
		super("&8&lEI Test - Page 1", 5*9);
		setIndex(1);
		loadItems(p);
	}

	// other pages
	public TestGUI(int index, Player p) {
		super("&8&lEI Test - Page "+index, 5*9);
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
					desc.add("&a(click to test (give to you))");
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
		createItem(Material.REDSTONE_BLOCK, 	1 , 36, 	"&4&l▶ Back to show menu", 	false, false);
		createItem(Material.DISPENSER, 	1 , 40, 	"&b&l▶ Give to you all items", 	true, false);
	}

	public void clicked(Player p, String itemName, String currentPage) {
		currentPage=sc.decoloredString(currentPage);
		itemName=sc.decoloredString(itemName);

		if(itemName.contains("Next page")) {
			p.closeInventory();
			new TestGUI(Integer.valueOf(currentPage.split("Page ")[1])+1, p).openGUISync(p);
		}
		else if(itemName.contains("Previous page")) {
			p.closeInventory();
			new TestGUI(Integer.valueOf(currentPage.split("Page ")[1])-1, p).openGUISync(p);
		}
		else if(itemName.contains("Exit")) {
			p.closeInventory();
		}
		else if(itemName.contains("Back")) {
			p.closeInventory();
			new ShowGUI(1,p).openGUISync(p);
		}
		else if(itemName.contains("Give to you")) {
			p.closeInventory();
			for(Item i : ConfigMain.getInstance().getItems()) {
				 GiveCommand gc = new GiveCommand();
				 gc.simpleGive(p, i);
				 p.sendMessage(sc.coloredString("&2&l[ExecutableItems] &aYou received &e"+i.getIdentification()));
			}
		}
		else {
		    GiveCommand gc = new GiveCommand();
		    gc.simpleGive(p, ConfigMain.getInstance().getItem(itemName.split("ID: ")[1]));
		    p.sendMessage(sc.coloredString("&2&l[ExecutableItems] &aYou received &e"+itemName.split("ID: ")[1]));
			
		}
	}

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		TestGUI.index = index;
	}

}
