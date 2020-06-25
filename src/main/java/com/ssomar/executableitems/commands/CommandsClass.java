package com.ssomar.executableitems.commands;


import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.actionbar.ActionbarHandler;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.configs.ingame.ConfigGUIManager;
import com.ssomar.executableitems.configs.ingame.ShowGUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CommandsClass implements CommandExecutor{

	private ExecutableItems main;

	public CommandsClass(ExecutableItems main) {
		this.main = main;
	}
	public CommandsClass() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(command.getName().equalsIgnoreCase("ei-create")) {
			if(!(sender instanceof Player)) return false;

				Player player = (Player) sender;
				
				//Bukkit.broadcastMessage(player.getInventory().getItemInMainHand()+"");
				if(player.getInventory().getItemInMainHand().getType()!=Material.AIR) {
					ConfigGUIManager.getInstance().startEditing(player, player.getInventory().getItemInMainHand());
				}
				else ConfigGUIManager.getInstance().startEditing(player);		
		}

		if(command.getName().equalsIgnoreCase("ei-reload")) {

			if(sender instanceof Player) {

				Player player = (Player) sender;
				main.onReload(true);
				player.sendMessage(ChatColor.GREEN+"ExecutableItems has been reload");
				System.out.println("[ExecutableItems] Successfully reload !");

			}
			else sender.sendMessage(ChatColor.RED+ "Only players can execute this command.");	
		}

		/*if(command.getName().equalsIgnoreCase("ei-test")) {

			for(String arg: args) {
				sender.sendMessage(arg);
			}

		}*/

		if(command.getName().equalsIgnoreCase("ei-show")) {
			if(!(sender instanceof Player)) {
				ConfigMain.getInstance().getItems().showItems();
				return false;
			}

			Player player = (Player) sender;
			new ShowGUI(player).openGUISync(player);

		}

		if(command.getName().equalsIgnoreCase("ei-give")) {

			new GiveCommand(sender, args, false);

		}

		if(command.getName().equalsIgnoreCase("ei-giveall")) {

			new GiveCommand(sender, args, true);

		}

		if(command.getName().equalsIgnoreCase("ei-actionbar")) {
			if(args.length==1) {
				ArrayList<String> listoff= ActionbarHandler.getInstance().getListPlayerActionbarOff();
				if(args[0].equals("on")) {
					if(listoff.contains(sender.getName())){
						listoff.remove(sender.getName());
						sender.sendMessage(MessageMain.getInstance().getSetActionbarOn());
					}
					else {
						sender.sendMessage(MessageMain.getInstance().getHaveActionbarOn());
					}
				}
				else if(args[0].equals("off")) {
					if(listoff.contains(sender.getName())){
						sender.sendMessage(MessageMain.getInstance().getHaveActionbarOff());
					}
					else {
						sender.sendMessage(MessageMain.getInstance().getSetActionbarOff());
						listoff.add(sender.getName());
					}
				}
				else {
					return true;
				}
				ActionbarHandler.getInstance().setListPlayerActionbarOff(listoff);
			}

		}

		return true;

	}

	public void actionbar(String[] args) {
		if(args.length==1) {
			if(args[0].equals("on")) {
				System.out.println("okkk");
			}

		}
	}


}

