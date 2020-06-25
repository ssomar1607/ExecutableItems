package com.ssomar.executableitems.configs;

import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public abstract class Config {
  protected File pdfile;
  
  protected FileConfiguration config;
  
  protected String fileName;
  
  protected Config(String fileName) {
	  
    this.fileName = fileName;
  }
  
  public void setup(Plugin plugin) {
    if (!plugin.getDataFolder().exists())
      plugin.getDataFolder().mkdir(); 
    this.pdfile = new File(plugin.getDataFolder(), this.fileName);
    boolean firstCreate = false;
    if (!this.pdfile.exists()) {
      firstCreate = true;
      try {
        this.pdfile.createNewFile();
        try(InputStream is = plugin.getResource(this.fileName); 
            OutputStream os = new FileOutputStream(this.pdfile)) {
          ByteStreams.copy(is, os);
        } 
      } catch (IOException e) {
        throw new RuntimeException("Unable to create the file: " + this.fileName, e);
      } 
    } 
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.pdfile);
    load(firstCreate);
    update();
  }
  
  
  
  
  
  public abstract void load(boolean paramBoolean);
  
  public abstract void update();
  
  public void save() {
    try {
      this.config.save(this.pdfile);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe("Could not save " + this.fileName + "!");
    } 
  }
  
  public void reload() {
    this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.pdfile);
  }
  
  public FileConfiguration getConfig() {
    return this.config;
  }
  
  public String getFileName() {
    return this.fileName;
  }
}

