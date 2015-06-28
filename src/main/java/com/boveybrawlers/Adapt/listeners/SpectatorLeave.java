package com.boveybrawlers.Adapt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Arena;

public class SpectatorLeave implements Listener {
	
	Adapt plugin;
	
	public SpectatorLeave(Adapt plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		int id = plugin.hasSpectator(player);
		if(id > -1) {
			Arena arena = plugin.arenaManager.getArenas().get(id);
			
			if(player.getLocation().distance(arena.getLocation("spectator")) > 70) {
				player.teleport(arena.getLocation("spectator"));
			}
		}
	}
}
