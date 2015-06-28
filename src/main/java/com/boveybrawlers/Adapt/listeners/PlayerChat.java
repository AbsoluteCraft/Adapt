package com.boveybrawlers.Adapt.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.boveybrawlers.Adapt.Adapt;

public class PlayerChat implements Listener {
	
	Adapt plugin;
	
	public PlayerChat(Adapt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		if(plugin.hasSpectator(player) > -1) {
			player.sendMessage(plugin.prefix + ChatColor.RED + "You may not chat while spectating Adapt");
			
			event.setCancelled(true);
		}
	}
	
}
