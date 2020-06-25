package com.ssomar.executableitems.configs;


import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.executableitems.util.StringConverter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageMain extends Config{

	private static MessageMain instance;

	private static StringConverter sc = new StringConverter();

	private String receiveItem;
	private String fullInventory;
	private String timeLeft;
	private String requirePermission;
	private String giveMessage;
	private String setActionbarOn;
	private String setActionbarOff;
	private String haveActionbarOn;
	private String haveActionbarOff;
	private String actionbarMessage;
	private String actionbarEnd;
	private String use;
	private String noHit;
	private String validHit;
	private String receivedHit;
	private String confirmMessage;


	private MessageMain() {

		super("/locale/Locale_"+ConfigMain.getInstance().getLocale()+".yml");
	}

	public void load(boolean firstCreate) {
		System.out.println("[ExecutableItems] Load Locale_"+ConfigMain.getInstance().getLocale()+".yml");
		receiveItem= loadMessage("receiveItem");
		fullInventory= loadMessage("fullInventory");
		timeLeft= loadMessage("timeLeft");
		requirePermission= loadMessage("requirePermission");
		giveMessage= loadMessage("giveMessage");
		setActionbarOn= loadMessage("setActionbarOn");
		setActionbarOff= loadMessage("setActionbarOff");
		haveActionbarOn= loadMessage("haveActionbarOn");
		haveActionbarOff= loadMessage("haveActionbarOff");
		actionbarMessage= loadMessage("actionbarMessage");
		actionbarEnd= loadMessage("actionbarEnd");
		setUse(loadMessage("use"));
		noHit= loadMessage("noHit");
		validHit= loadMessage("validHit");
		receivedHit= loadMessage("receivedHit");
		confirmMessage= loadMessage("confirmMessage");
		

	}
	
	public String loadMessage(String message) {
		if(config.getString(message)!=null) return sc.coloredString(config.getString(message));
		else return sc.coloredString(write(message));
	}

	public String write(String what) {

		String insert="Can't load this string, contact the developper";
		try{

			InputStream flux= ExecutableItems.class.getResourceAsStream("/com/ssomar/executableitems/configs/locale/Locale_"+ConfigMain.getInstance().getLocale()+".yml");
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);
			String ligne;
			Boolean isNotUpdate=true;
			while ((ligne=buff.readLine())!=null && isNotUpdate){
				if(ligne.contains(what)) {
					System.out.println("[ExecutableItems] Update of "+what+" in your "+ConfigMain.getInstance().getLocale()+".yml");
					insert= ligne.split("\"")[1];
					config.set(what, insert);
					this.config.save(this.pdfile);
					isNotUpdate=false;
				}
			}
			buff.close(); 
		}		
		catch (Exception e){
			System.out.println("[ExecutableItems] ERROR LOAD MESSAGE "+e.toString());
		}

		return insert;
	}


	public void reload() {
		this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.pdfile);
		load(false);
	}	

	public static FileConfiguration get() {
		return (getInstance()).config;
	}

	public static MessageMain getInstance() {
		if (instance == null)
			instance = new MessageMain(); 
		return instance;
	}

	public void update() {}


	public static void setInstance(MessageMain instance) {
		MessageMain.instance = instance;
	}

	public String getReceiveItem() {
		return receiveItem;
	}

	public void setReceiveItem(String receiveItem) {
		this.receiveItem = receiveItem;
	}

	public String getFullInventory() {
		return fullInventory;
	}

	public void setFullInventory(String fullInventory) {
		this.fullInventory = fullInventory;
	}

	public String getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}

	public String getRequirePermission() {
		return requirePermission;
	}

	public void setRequirePermission(String requirePermission) {
		this.requirePermission = requirePermission;
	}

	public String getGiveMessage() {
		return giveMessage;
	}

	public void setGiveMessage(String giveMessage) {
		this.giveMessage = giveMessage;
	}

	public String getSetActionbarOn() {
		return setActionbarOn;
	}

	public void setSetActionbarOn(String setActionbarOn) {
		this.setActionbarOn = setActionbarOn;
	}

	public String getSetActionbarOff() {
		return setActionbarOff;
	}

	public void setSetActionbarOff(String setActionbarOff) {
		this.setActionbarOff = setActionbarOff;
	}

	public String getHaveActionbarOn() {
		return haveActionbarOn;
	}

	public void setHaveActionbarOn(String haveActionbarOn) {
		this.haveActionbarOn = haveActionbarOn;
	}

	public String getHaveActionbarOff() {
		return haveActionbarOff;
	}

	public void setHaveActionbarOff(String haveActionbarOff) {
		this.haveActionbarOff = haveActionbarOff;
	}

	public String getActionbarMessage() {
		return actionbarMessage;
	}

	public void setActionbarMessage(String actionbarMessage) {
		this.actionbarMessage = actionbarMessage;
	}

	public String getActionbarEnd() {
		return actionbarEnd;
	}

	public void setActionbarEnd(String actionbarEnd) {
		this.actionbarEnd = actionbarEnd;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public String getNoHit() {
		return noHit;
	}

	public void setNoHit(String noHit) {
		this.noHit = noHit;
	}

	public String getValidHit() {
		return validHit;
	}

	public void setValidHit(String validHit) {
		this.validHit = validHit;
	}

	public String getReceivedHit() {
		return receivedHit;
	}

	public void setReceivedHit(String receivedHit) {
		this.receivedHit = receivedHit;
	}

	public String getConfirmMessage() {
		return confirmMessage;
	}

	public void setConfirmMessage(String confirmMessage) {
		this.confirmMessage = confirmMessage;
	}
	
	

	














}
