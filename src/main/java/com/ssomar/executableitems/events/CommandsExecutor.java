package com.ssomar.executableitems.events;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.actionbar.ActionbarHandler;
import com.ssomar.executableitems.compatibility.PlayerPointsC;
import com.ssomar.executableitems.configs.MessageMain;
//import com.ssomar.executableitems.compatibility.PlayerPointsC;
import com.ssomar.executableitems.items.Item;
import com.ssomar.executableitems.util.StringConverter;



public class CommandsExecutor {

	private HashMap<String,ArrayList<String>> disconnectedCommands= new HashMap<>();

	private HashMap<String,ArrayList<String>> serverOffCommands= new HashMap<>();

	private HashMap<Player,List<String>> securityOP = new HashMap<>();

	private static StringConverter sc = new StringConverter();

	private static CommandsExecutor instance;

	ExecutableItems main;

	public void setup(ExecutableItems main) {
		this.main=main;
	}


	public boolean runCommands(List<String> commands, Player p, Item i) {

		int delay=0;
		List<String> commandsRandom= new ArrayList<String>();
		boolean inRandom=false;
		int nbRandom=0;

		for(String command: commands) {
			
			command= sc.replaceVariable(command, "%player%", "%item%", "",0);
			
			if(command.contains("RANDOM RUN:")) {
				nbRandom= Integer.valueOf(command.split("RANDOM RUN:")[1].replaceAll(" ",""));
				inRandom=true;
				continue;
			}
			else if(command.contains("RANDOM END")) {
				inRandom=false;
				while(nbRandom>0 && commandsRandom.size()>0) {
					int rdn=(int)(Math.random()*commandsRandom.size());
					if(delay>0) runDelayedCommand(commandsRandom.get(rdn), p, delay);
					else runCommand(commandsRandom.get(rdn), p);
					commandsRandom.remove(rdn);
					nbRandom--;
				}
				continue;
			}
			else if(inRandom) {
				commandsRandom.add(command);
				continue;
			}
			
			
			else if(command.contains("ACTIONBAR ON")) {
				ActionbarHandler.getInstance().startActionbar(p , i, getDelayActionbar(commands));
			}
			else if(command.contains("ACTIONBAR OFF")) {
				continue;
			}
			else if(command.contains("DELAY ")) {
				delay=delay+(Integer.valueOf(command.replaceAll("DELAY ", ""))*20);
			}else {
				if(delay>0) {
					runDelayedCommand(command, p, delay);
				}
				else {
					runCommand(command, p);
				}
			}
		}
		return true;
	}

	public int getDelayActionbar(List<String> commands) {

		int delayActionbar=0;

		boolean activeActionbar=false;

		for(String command: commands) {
			if(command.contains("ACTIONBAR ON")) {
				activeActionbar=true;
			}
			else if(command.contains("DELAY ") && activeActionbar) {
				delayActionbar=delayActionbar+(Integer.valueOf(command.replaceAll("DELAY ", "")));
			}
			else if(command.contains("ACTIONBAR OFF")) {
				activeActionbar=false;
				break;
			}
		}
		return delayActionbar;
	}


	public boolean runServerOffCommands(Player p) {
		if(serverOffCommands.containsKey(p.getName())) {
			List<String> commands= serverOffCommands.get(p.getName());
			for(String command: commands) {
				runCommand(command, p);
			}
			serverOffCommands.remove(p.getName());
		}
		return true;
	}

	public boolean runDisconnectedCommands(Player p) {
		if(disconnectedCommands.containsKey(p.getName())) {
			List<String> commands= disconnectedCommands.get(p.getName());
			for(String command: commands) {
				runCommand(command, p);

				if(serverOffCommands.containsKey(p.getName())) {
					ArrayList<String> changement= serverOffCommands.get(p.getName());
					changement.remove(command);
					if(changement.isEmpty()) {
						serverOffCommands.remove(p.getName());
					}
					else {
						serverOffCommands.replace(p.getName(), changement);
					}
				}
			}

			disconnectedCommands.remove(p.getName());
		}
		return true;
	}


	public boolean runDelayedCommand(String command, Player p, int delay) {

		if(serverOffCommands.containsKey(p.getName())) {
			ArrayList<String> changement= serverOffCommands.get(p.getName());
			changement.add(command);
			serverOffCommands.replace(p.getName(), changement);
		}else {
			ArrayList<String> newList = new ArrayList<>();
			newList.add(command);
			serverOffCommands.put(p.getName(), newList);
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask( main , new Runnable(){
			public void run(){			

				if(Bukkit.getServer().getOnlinePlayers().contains(p)) {
					runCommand(command, p);

					if(serverOffCommands.containsKey(p.getName())) {
						ArrayList<String> changement= serverOffCommands.get(p.getName());
						changement.remove(command);
						if(changement.isEmpty()) {
							serverOffCommands.remove(p.getName());
						}
						else {
							serverOffCommands.replace(p.getName(), changement);
						}
					}
				}
				else {
					if(disconnectedCommands.containsKey(p.getName())) {
						ArrayList<String> changement = disconnectedCommands.get(p.getName());
						changement.add(command);
						disconnectedCommands.replace(p.getName(), changement);
					}
					else {
						ArrayList<String> newList = new ArrayList<>();
						newList.add(command);
						disconnectedCommands.put(p.getName(), newList);
					}
				}				

			}
		}, delay);
		return true;
	}

	public boolean runCommand(String command, Player p) {

		command=sc.replaceVariable(command, p.getName(), "", "",0);

		if(command.contains("AROUND")) {
			String [] verifAround= command.split("'");
			double distance= Double.valueOf(verifAround[1]);
			int cpt=0;
			for (Entity e: p.getNearbyEntities(distance, distance, distance)) {
				if(e instanceof Player) {

					Player target= (Player) e;
					if(!target.hasMetadata("NPC")) {
						if(target!=p) {
							runCommand(verifAround[5].replaceAll("%target%", target.getName()), target);
							cpt++;
						}

						if(Boolean.valueOf(verifAround[3].toUpperCase())) p.sendMessage(sc.replaceVariable(MessageMain.getInstance().getValidHit(), target.getName(), "", "", 0));
					}
				}
			}
			if(cpt==0) if(Boolean.valueOf(verifAround[3].toUpperCase())) p.sendMessage(MessageMain.getInstance().getNoHit());
			return true;
		}


		else if(command.contains("SENDMESSAGE")) {
			p.sendMessage(command.replace("SENDMESSAGE ", ""));
			return true;
		}

		else if(command.contains("ADD POINTS")) {
			if(Bukkit.getPluginManager().getPlugin("PlayerPoints")==null) {
				p.sendMessage("&4&l[&c&lExecutableItems&4&l] &cError PLAYERPOINTS not detected, please contact staff");
				return false;
			}
			else {
				PlayerPointsC.getInstance().addPoints(p, Integer.valueOf(command.replace("ADD POINTS ", "")));
				return true;
			}
		}



		else if(command.contains("PARTICLE")) {
			String [] particle=command.split("'");
			p.getWorld().spawnParticle(Particle.valueOf(particle[1]),
					p.getLocation(),
					Integer.valueOf(particle[3]),
					Double.valueOf(particle[5]),
					Double.valueOf(particle[5]) ,
					Double.valueOf(particle[5]) ,
					Double.valueOf(particle[7]), null);

			return true;
		}

		else if(command.contains("FLY ON")) {
			p.setAllowFlight(true);
			return true;
		}

		else if(command.contains("FLY OFF")) {
			if(!p.isOnGround()) {
				Location playerLocation= p.getLocation();
				while(playerLocation.getBlock().isEmpty()) {
					playerLocation.subtract(0, 1, 0);
				}
				playerLocation.add(0, 1, 0);
				p.teleport(playerLocation);
			}
			p.setAllowFlight(false);
			p.setFlying(false);
			return true;
		}

		else if(command.contains("SUDO ")) {
			String pCommand= command.split("SUDO ")[1].replace("/", "");
			p.performCommand(pCommand);
			return true;
		}

		else if(command.contains("SUDOOP ")) {
			String pCommand= command.split("SUDOOP ")[1].replace("/", "");
			if(p.isOp()) {
				p.performCommand(pCommand);
			}
			else {
				try {
					if(securityOP.containsKey(p)) {
						securityOP.get(p).add(pCommand); 
					}
					else {
						ArrayList<String> cList= new ArrayList<>();
						cList.add(pCommand);
						securityOP.put(p, cList);
					}
					p.setOp(true);
					p.performCommand(pCommand);
				} finally {
					p.setOp(false);
					if(securityOP.get(p).size()==1) {
						securityOP.remove(p);
					}
					else {
						securityOP.get(p).remove(pCommand);
					}
				}
			}
			return true;
		}

		else return runConsoleCommand(command);
	}

	public boolean runConsoleCommand(String command) {
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		Bukkit.dispatchCommand(console, command);
		return true;
	}

	public static CommandsExecutor getInstance() {
		if (instance == null)
			instance = new CommandsExecutor(); 
		return instance;
	}

	public HashMap<String, ArrayList<String>> getDisconnectedCommands() {
		return disconnectedCommands;
	}

	public void setDisconnectedCommands(HashMap<String, ArrayList<String>> disconnectedCommands) {
		this.disconnectedCommands = disconnectedCommands;
	}


	public HashMap<String, ArrayList<String>> getServerOffCommands() {
		return serverOffCommands;
	}


	public void setServerOffCommands(HashMap<String, ArrayList<String>> serverOffCommands) {
		this.serverOffCommands = serverOffCommands;
	}


	public HashMap<Player, List<String>> getSecurityOP() {
		return securityOP;
	}	

}
