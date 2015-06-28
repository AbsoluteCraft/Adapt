package com.boveybrawlers.Adapt.utils;

import org.bukkit.scheduler.BukkitRunnable;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Arena;

public class QueueHandler extends BukkitRunnable {
	
	Adapt plugin;
	
	private Arena arena;
	private int time;
	
	public QueueHandler(Adapt plugin, Arena arena, int time) {
		this.plugin = plugin;
		
		this.arena = arena;
		this.time = time;
	}

	public void run() {
		if(this.time == 0) {
			this.cancel();
			
			arena.setQueue(false);
			arena.startCountdown();
		}
		
		arena.setQueueTime(time);
		
		this.time--;
	}
}
