package com.boveybrawlers.Adapt.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerWinEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	private int kills;
	
	public PlayerWinEvent(Player player, int kills) {
		this.player = player;
		
		this.kills = kills;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getKills() {
		return this.kills;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
