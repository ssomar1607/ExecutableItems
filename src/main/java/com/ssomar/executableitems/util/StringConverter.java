package com.ssomar.executableitems.util;

import java.util.ArrayList;
import java.util.List;

public class StringConverter {

	public String coloredString(String s) {
		return s.replaceAll("&", "§");
	}

	public List<String> replaceVariable(List<String> list, String player, String item, String quantity, int time) {
		List<String> newList= new ArrayList<>();
		for(int i=0; i<list.size();i++) {
			newList.add(this.replaceVariable(list.get(i), player, item, quantity, time));
		}
		return newList;
	}

	public String replaceVariable(String s, String player, String item, String quantity, int time) {
		s=s.replace("$", "REGEX-DOLARS");
		if(s.charAt(0)=='/') {
			s=s.replaceFirst("/", "");
		}
		item=item.replace("$", "REGEX-DOLARS");
		player=player.replace("$", "REGEX-DOLARS");
		s=s.replaceAll("%player%", player);
		s=s.replaceAll("%item%", item);
		s=s.replaceAll("%quantity%", quantity);
		s=s.replaceAll("REGEX-DOLARS", "\\$");
		int M = time / 60;
		int S = time % 60;
		int H = M / 60;
		M = M % 60;
		s=s.replaceAll("%time%", H+"H "+M+"M "+S+"S");
		return s;
	}


	public List<String> decoloredString(List<String> list) {
		List<String> result= new ArrayList<>();
		for(String s: list) {
			result.add(decoloredString(s));
		}
		return result;
	}

	public String decoloredString(String s) {

		StringBuilder sb = new StringBuilder();
		char[] sChar= s.toCharArray();
		for(int i= 0;i<sChar.length;i++) {
			if(sChar[i]=='&' || sChar[i]=='§') {
				i++;
			}else {
				sb.append(sChar[i]);
			}
		}
		return sb.toString();
	}

	public List<String> deconvertColor(List<String> list) {
		List<String> result= new ArrayList<>();
		for(String s: list) {
			result.add(deconvertColor(s));
		}
		return result;
	}

	public String deconvertColor(String s) {

		StringBuilder sb = new StringBuilder();
		char[] sChar= s.toCharArray();
		for(int i= 0;i<sChar.length;i++) {
			if(sChar[i]=='§') {
				sb.append('&');
			}else {
				sb.append(sChar[i]);
			}
		}
		return sb.toString();
	}
}
