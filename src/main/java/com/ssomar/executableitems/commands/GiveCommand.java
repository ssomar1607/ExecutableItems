package com.ssomar.executableitems.commands;

import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.util.StringConverter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class GiveCommand {

	private static StringConverter sc = new StringConverter();

	private CommandSender sender;

	private Player target;

	private Item i;

	private int quantity;



	public GiveCommand(CommandSender sender, String []args, boolean all) {

		this.sender=sender;
		if(all) allGive(args);
		else simpleGive(args);

	}
	public GiveCommand() {}

	public void simpleGive(Player p, Item i) {

		this.target=p;
		this.i=i;
		this.quantity=1;

		int [] whereGive = verifInventory();

		if(executeGive(whereGive[0], whereGive[1])) {

			if(whereGive[1]!=0) target.sendMessage(sc.replaceVariable(MessageMain.getInstance().getFullInventory(), target.getName(), i.getName(), String.valueOf(whereGive[1]), 0));
		}
	}


	private void simpleGive(String []args) {

		if(args.length>=2) {
			int quantity=1;
			if(verifArgsSimple(args)) {
				if(args.length>2) quantity= Integer.valueOf(args[2]);

				int [] whereGive = verifInventory();

				if(executeGive(whereGive[0], whereGive[1])) {

					sender.sendMessage(sc.replaceVariable(MessageMain.getInstance().getGiveMessage(),target.getName(),i.getName(),quantity+"",0));

					target.sendMessage(sc.replaceVariable(MessageMain.getInstance().getReceiveItem(),target.getName(),i.getName(),quantity+"", 0));

					if(whereGive[1]!=0) target.sendMessage(sc.replaceVariable(MessageMain.getInstance().getFullInventory(), target.getName(), i.getName(), String.valueOf(whereGive[1]), 0));
				}
			}
		}
		else sender.sendMessage(ChatColor.RED+"[ExecutableItems] Use /ei-give PLAYER ITEM QUANTITY");

	}

	private void allGive(String []args) {

		if(args.length>=1) {
			int quantity=1;
			if(verifArgsAll(args)) {
				if(args.length>1) quantity= Integer.valueOf(args[1]);

				for(Player player : sender.getServer().getOnlinePlayers()) {
					target=player;

					int [] whereGive = verifInventory();

					if(executeGive(whereGive[0], whereGive[1])) {


						target.sendMessage(sc.replaceVariable(MessageMain.getInstance().getReceiveItem(),target.getName(),i.getName(),quantity+"", 0));

						if(whereGive[1]!=0) target.sendMessage(sc.replaceVariable(MessageMain.getInstance().getFullInventory(), target.getName(), i.getName(), String.valueOf(whereGive[1]), 0));
					}
				}

				sender.sendMessage(sc.replaceVariable(MessageMain.getInstance().getGiveMessage(),"all",i.getName(), quantity+"",0));



			}
		}
		else sender.sendMessage(ChatColor.RED+"[ExecutableItems] Use /ei-giveall ITEM QUANTITY");
	}

	public boolean verifArgsSimple(String []args) {

		if((target=sender.getServer().getPlayerExact(args[0])) !=null);
		else {
			sender.sendMessage(ChatColor.RED+"[ExecutableItems] Player " + args[0] + " is not online.");
			return false;
		}

		if(ConfigMain.getInstance().getItems().containsIdentification(args[1])) {
			i=ConfigMain.getInstance().getItems().getByIdentification(args[1]);
		}
		else {
			sender.sendMessage(ChatColor.RED+"[ExecutableItems] Item "+args[1]+" not found");
			return false;
		}
		if(args.length>2) {
			if(args[2].matches("\\d+")) quantity=Integer.valueOf(args[2]);
			else {
				sender.sendMessage(ChatColor.RED+"[ExecutableItems] Quantity " + args[2] + " is invalid.");
				return false;
			}
		}
		else {
			this.quantity=1;
		}

		return true;




	}

	public boolean verifArgsAll(String []args) {


		if(ConfigMain.getInstance().getItems().containsIdentification(args[0])) {
			i=ConfigMain.getInstance().getItems().getByIdentification(args[0]);
		}
		else {
			sender.sendMessage(ChatColor.RED+"[ExecutableItems] Item "+args[0]+" not found");
			return false;
		}
		if(args.length>1) {
			if(args[1].matches("\\d+")) quantity=Integer.valueOf(args[1]);
			else {
				sender.sendMessage(ChatColor.RED+"[ExecutableItems] Quantity " + args[1] + " is invalid.");
				return false;
			}
		}
		else this.quantity=1;

		return true;




	}

	public int[] verifInventory(){

		int inInventory=0;
		int onTheGround=0;
		int [] result= new int[2];

		for(ItemStack is : target.getInventory().getStorageContents()) {
			if(quantity<=inInventory) {
				inInventory=quantity;
				break;
			}
			//1.12 correction
			if(is==null) {
				if(!Bukkit.getServer().getVersion().contains("1.12")) {
					if(i.getMaterial().equals(Material.PLAYER_HEAD)) {

						inInventory=inInventory+1;
					}

				}
				inInventory=inInventory+64;
			}
			else {
				if(!Bukkit.getServer().getVersion().contains("1.12")) {
					if(i.getMaterial().equals(Material.PLAYER_HEAD)) continue;
				}
				if(is.hasItemMeta()) {

					if(is.getItemMeta().hasLore() && is.getItemMeta().hasDisplayName()) {
						if(i.getUse()==0) {
							if(is.getItemMeta().getDisplayName().toString().equals(i.getName()) && is.getItemMeta().getLore().equals(i.getLore())) {
								inInventory= inInventory+ 64-is.getAmount();
							}
						}
						else if(is.getItemMeta().getDisplayName().toString().equals(i.getName()) && is.getItemMeta().getLore().subList(0, is.getItemMeta().getLore().size()-1).equals(i.getLore())) {
							List<String> loreIs = is.getItemMeta().getLore();
							if(loreIs.get(loreIs.size()-1).contains(MessageMain.getInstance().getUse())) {
								if(i.getUse()==Integer.valueOf(loreIs.get(loreIs.size()-1).split(MessageMain.getInstance().getUse())[1])) {
									inInventory= inInventory+ 64-is.getAmount();
								}
							}


						}
					}

				}
			}

		}
		if(quantity>inInventory) {
			onTheGround=quantity-inInventory;
		}

		result[0]= inInventory;
		result[1]= onTheGround;
		return result;
	}

	private boolean executeGive(int inInventory, int onTheground) {

		target.getInventory().addItem(i.formItem(inInventory));

		if(onTheground>0)target.getWorld().dropItem(target.getLocation().add(0, 1, 0),i.formItem(onTheground));

		return true;
	}

}
