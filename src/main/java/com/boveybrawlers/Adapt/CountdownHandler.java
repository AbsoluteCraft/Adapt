package com.boveybrawlers.Adapt;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownHandler extends BukkitRunnable {
	private int time;
	
	public CountdownHandler(int time) {
		this.time = time;
	}
	
	public void run() {
		if(time == 0) {
			this.cancel();
			Game.countdown = false;
		}
		else {
			for(Adapter adapter : Game.adapters) {
				adapter.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "" + time);
			}
		}
		
		if(time == 1) {
			Game.start();
		}
		
		this.time--;
	}
}