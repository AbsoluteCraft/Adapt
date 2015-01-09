package com.boveybrawlers.Adapt;

import org.bukkit.scheduler.BukkitRunnable;

public class QueueHandler extends BukkitRunnable {
	private int time;
	
	public QueueHandler(int time) {
		this.time = time;
	}
	
	public void run() {
		if(time == 0) {
			this.cancel();
			Game.queueCountdown = false;
			Game.countdown();
		} else {
			if(time == 50) {
				for(Adapter adapter : Game.adapters) {
					adapter.sendMessage(Adapt.plugin.prefix + "60 seconds until spawning");
				}
			} else if(time == 20) {
				for(Adapter adapter : Game.adapters) {
					adapter.sendMessage(Adapt.plugin.prefix + "30 seconds remaining");
				}
			}
		}
		this.time--;
	}
	
}
