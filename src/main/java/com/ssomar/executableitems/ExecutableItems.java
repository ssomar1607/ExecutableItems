package com.ssomar.executableitems;


import com.ssomar.executableitems.actionbar.ActionbarHandler;
import com.ssomar.executableitems.commands.CommandsClass;
import com.ssomar.executableitems.compatibility.PlayerPointsC;
//import com.ssomar.executableitems.compatibility.PlayerPointsC;
import com.ssomar.executableitems.configs.ConfigMain;
import com.ssomar.executableitems.configs.Message;
import com.ssomar.executableitems.configs.MessageMain;
import com.ssomar.executableitems.configs.ingame.ConfigGUIManager;
import com.ssomar.executableitems.data.Database;
import com.ssomar.executableitems.events.CommandsExecutor;
import com.ssomar.executableitems.events.EventsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ExecutableItems extends JavaPlugin {

    public static ExecutableItems plugin;

    private CommandsClass commandClass;

    private Message message;

    @Override
    public void onEnable() {
        System.out.println("================ [ExecutableItems] ================");
        plugin = this;

        commandClass = new CommandsClass(this);
        ConfigMain.getInstance().setup(this);
        message = new Message(this);
        try {
            message.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageMain.getInstance().setup(this);
        CommandsExecutor.getInstance().setup(this);
        EventsHandler.getInstance().setup(this);
        ActionbarHandler.getInstance().setup(this);
        ConfigGUIManager.getInstance().setup(this);
        Database.getInstance().setup(this);
        /*if(Bukkit.getPluginManager().getPlugin("PlayerPoints")!=null) {
            System.out.println("[ExecutableItems] PlayerPoints hooked !");
            PlayerPointsC.getInstance().setup(this);
        }*/
		/*if(Bukkit.getPluginManager().getPlugin("Vault")!=null) {
			System.out.println("[ExecutableItems] Vault hooked !");
			VaultC.getInstance().setup(this);
		}*/
        //Database.getInstance().selectAll();


        this.getCommand("ei-reload").setExecutor(commandClass);
        this.getCommand("ei-give").setExecutor(commandClass);
        this.getCommand("ei-giveall").setExecutor(commandClass);
        this.getCommand("ei-show").setExecutor(commandClass);
        this.getCommand("ei-actionbar").setExecutor(commandClass);
        this.getCommand("ei-create").setExecutor(commandClass);

        int pluginId = 7233;
        @SuppressWarnings("unused")
        MetricsLite metrics = new MetricsLite(this, pluginId);

        System.out.println("================ [ExecutableItems] ================");


        for(Player p : Bukkit.getOnlinePlayers()) {
            List<String> commands=Database.getInstance().selectCommandsForPlayer(p.getName());
            if(!commands.isEmpty()) {
                CommandsExecutor.getInstance().runCommands(commands,p, null);
                Database.getInstance().deleteCommandsForPlayer(p.getName());
            }
        }

    }


    public void onReload(boolean PluginCommand) {
        System.out.println("================ [ExecutableItems] ================");
        plugin.saveDefaultConfig();
        ConfigMain.getInstance().reload();
        message.setPlugin(null);
        message = new Message(this);
        try {
            message.setup();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageMain.getInstance().reload();

        System.out.println("================ [ExecutableItems] ================");

    }

    @Override
    public void onDisable() {
        HashMap<String,ArrayList<String>> saveCommands= CommandsExecutor.getInstance().getServerOffCommands();
        for(String player: saveCommands.keySet()) {
            for(String command: saveCommands.get(player)) {
                Database.getInstance().insert(player, command);
            }
        }


    }

    public static ExecutableItems getPlugin() {
        return plugin;
    }
}
