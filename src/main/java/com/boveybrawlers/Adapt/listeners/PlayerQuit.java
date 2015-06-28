package com.boveybrawlers.Adapt.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Adapter;
import com.boveybrawlers.Adapt.Arena;

public class PlayerQuit implements Listener {
	
	Adapt plugin;
	
	public PlayerQuit(Adapt plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		int id = plugin.hasPlayer(player);
		if(id > -1) {
			Arena arena = plugin.arenaManager.getArenas().get(id);
			Adapter adapter = plugin.getAdapter(id, player);
			
			arena.removeAdapter(adapter, false);
		} else {
			id = plugin.hasSpectator(player);
			
			if(id > -1) {
				Arena arena = plugin.arenaManager.getArenas().get(id);
				arena.removeSpectator(player);
			}
		}
	}
}
