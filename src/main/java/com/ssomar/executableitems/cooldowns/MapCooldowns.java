package com.ssomar.executableitems.cooldowns;

import java.util.HashMap;

import com.ssomar.executableitems.util.Couple;

public class MapCooldowns extends HashMap<Couple<String, String>,Long> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean containsKey(Couple<String, String> couple) {
		if(!this.isEmpty())
			for(Couple<String, String> c : keySet()) {
				if(c.getElem1().equals(couple.getElem1()) && c.getElem2().equals(couple.getElem2())) {
					return true;
				}
			}
		return false;
	}



	public Long getValue(Couple<String, String> couple) {
		if(!this.isEmpty()) {
			for(Couple<String, String> c : keySet()) {
				if(c.getElem1().equals(couple.getElem1()) && c.getElem2().equals(couple.getElem2())) {
					return this.get(c);
				}
			}
		}
		return null;
	}
	
	public void replaceKey(Couple<String, String> couple, Long value) {
		boolean notIn=true;
			for(Couple<String, String> c : keySet()) {
				if(c.getElem1().equals(couple.getElem1()) && c.getElem2().equals(couple.getElem2())) {
					this.replace(c, value);
					notIn=false;
				}
			}
		
		if(notIn) {
			this.put(couple, value);
		}
	}
}
