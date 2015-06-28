package com.boveybrawlers.Adapt.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKillEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private Player killer;
	
	private int kills;
	
	public PlayerKillEvent(Player player, Player killer, int kills) {
		this.player = player;
		
		this.killer = killer;
		
		this.kills = kills;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Player getKiller() {
		return this.killer;
	}
	
	public int getKillerKills() {
		return this.kills;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
