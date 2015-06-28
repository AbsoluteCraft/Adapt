package com.boveybrawlers.Adapt.utils;

import org.bukkit.ChatColor;

public class TeamInfo {
	
	public static String getNameFromInt(int id) {
		switch(id) {
			case 0:
				return "cyan";
			case 1:
				return "yellow";
			case 2:
				return "purple";
			case 3:
				return "lightgreen";
			case 4:
				return "pink";
			case 5:
				return "red";
			case 6:
				return "orange";
			case 7:
				return "blue";
			default:
				return null;
		}
	}

	public static short getWoolDurability(int id) {
		switch(id) {
			case 0:
				return 9;
			case 1:
				return 4;
			case 2:
				return 10;
			case 3:
				return 5;
			case 4:
				return 6;
			case 5:
				return 14;
			case 6:
				return 1;
			case 7:
				return 11;
			default:
				return 0;
		}
	}

	public static String getColorFromInt(int id) {
		switch(id) {
			case 0:
				return ChatColor.BLUE + "";
			case 1:
				return ChatColor.YELLOW + "";
			case 2:
				return ChatColor.DARK_PURPLE + "";
			case 3:
				return ChatColor.GREEN + "";
			case 4:
				return ChatColor.LIGHT_PURPLE + "";
			case 5:
				return ChatColor.RED + "";
			case 6:
				return ChatColor.GOLD + "";
			case 7:
				return ChatColor.DARK_BLUE + "";
			default:
				return null;
		}
	}
}
