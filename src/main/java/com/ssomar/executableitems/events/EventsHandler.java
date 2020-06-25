package com.ssomar.executableitems.events;

import com.ssomar.executableitems.ExecutableItems;

public class EventsHandler {
	
	private static EventsHandler instance;
	
	private ExecutableItems main;
	
	
	public void setup(ExecutableItems main) {
		this.main=main;
		setupEvents();
		update();
	}
	
	public void update() {}
	
	public void setupEvents() {
		main.getServer().getPluginManager().registerEvents(new ItemInteract(), main);
		main.getServer().getPluginManager().registerEvents(new PlayerReconnexion(), main);
		main.getServer().getPluginManager().registerEvents(new PreventPlace(), main);
		main.getServer().getPluginManager().registerEvents(new InteractionGUI(), main);
		main.getServer().getPluginManager().registerEvents(new SecurityOPCommands(), main);
		main.getServer().getPluginManager().registerEvents(new ItemThrow(), main);
		main.getServer().getPluginManager().registerEvents(new PlayerFirstJoin(), main);
		main.getServer().getPluginManager().registerEvents(new AnvilInteraction(), main);
		main.getServer().getPluginManager().registerEvents(new PlayerDeath(), main);
	}

	public static EventsHandler getInstance() {
	    if (instance == null)
	      instance = new EventsHandler();
	    return instance;
	 }


	public static void setInstance(EventsHandler instance) {
		EventsHandler.instance = instance;
	}
	
	
	
	
	
	
	

}
