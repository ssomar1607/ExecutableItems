package com.ssomar.executableitems.configs;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;



public class Message {

	protected FileConfiguration config;

	private static Message instance;
	
	protected String fileName;
	
	protected Plugin plugin;

	public Message(Plugin plugin) {
		this.plugin=plugin;
		this.fileName = "Locale_"+ConfigMain.getInstance().getLocale();
	}



	public void setup() throws IOException
	{
		File pdfile = new File(plugin.getDataFolder() + "/locale/", fileName + ".yml");
		InputStream in = this.getClass().getResourceAsStream("/com/ssomar/executableitems/configs/locale/" + fileName + ".yml");

		if(!pdfile.exists())
		{
			plugin.getDataFolder().mkdirs();
			pdfile.getParentFile().mkdirs();
			pdfile.createNewFile();
		}else return;

		OutputStream out = new FileOutputStream(pdfile);
		byte[] buffer = new byte[1024];
		int current = 0;

		while((current = in.read(buffer)) > -1)
		{
			out.write(buffer, 0, current);
		}

		out.close();
		in.close();

	}



	public static Message getInstance() {
		return instance;
	}

	public static void setInstance(Message instance) {
		Message.instance = instance;
	}



	public Plugin getPlugin() {
		return plugin;
	}



	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	

}