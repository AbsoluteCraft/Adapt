package com.boveybrawlers.Adapt.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Adapter;
import com.boveybrawlers.Adapt.Arena;

public class PlayerDamage implements Listener {
	
	Adapt plugin;
	
	public PlayerDamage(Adapt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			if(event.getDamage() >= player.getHealth()) {
				if(event.getCause() != DamageCause.ENTITY_ATTACK && event.getCause() != DamageCause.PROJECTILE) {
					int id = plugin.hasPlayer(player);
					if(id > -1) {
						Arena arena = plugin.arenaManager.getArenas().get(id);
						Adapter adapter = plugin.getAdapter(id, player);
						
						event.setDamage(0);
						adapter.getPlayer().teleport(arena.getLocation("lobby"));
						
						arena.sendMessage(ChatColor.RED + player.getName() + " died");
						
						arena.removeAdapter(adapter, true);
					}
				}
			}
		}
	}
	
}
