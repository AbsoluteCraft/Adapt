package com.boveybrawlers.Adapt.utils;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Arena;

public class CountdownHandler extends BukkitRunnable {
	
	Adapt plugin;
	
	private Arena arena;
	private int time;
	
	public CountdownHandler(Adapt plugin, Arena arena, int time) {
		this.plugin = plugin;
		
		this.arena = arena;
		this.time = time;
	}

	public void run() {
		if(this.time == 0) {
			this.cancel();
			
			arena.setCountdown(false);
			arena.sendMessage(ChatColor.GREEN + "GO!");
		} else {
			arena.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + time);
		}
		
		if(time == 1) {
			arena.start();
		}
		
		this.time--;
	}
}
