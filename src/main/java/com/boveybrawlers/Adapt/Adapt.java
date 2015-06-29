package com.boveybrawlers.Adapt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.boveybrawlers.Adapt.commands.AdaptCommand;
import com.boveybrawlers.Adapt.listeners.ClickChest;
import com.boveybrawlers.Adapt.listeners.PlayerChat;
import com.boveybrawlers.Adapt.listeners.PlayerDamage;
import com.boveybrawlers.Adapt.listeners.PlayerDamageByPlayer;
import com.boveybrawlers.Adapt.listeners.PlayerQuit;

public class Adapt extends JavaPlugin {
	
	public Adapt plugin;
	
	public ArenaManager arenaManager;

	public String prefix = ChatColor.GRAY + "" + ChatColor.BOLD + "Adapt" + ChatColor.RESET + ChatColor.DARK_GRAY + " | " + ChatColor.RESET;
	
	public void onEnable() {
		plugin = this;
		
		arenaManager = new ArenaManager(this);
		
		this.getCommand("adapt").setExecutor(new AdaptCommand(this));
		
		this.getServer().getPluginManager().registerEvents(new ClickChest(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerChat(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamage(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamageByPlayer(this), this);
		this.getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
//		this.getServer().getPluginManager().registerEvents(new SpectatorLeave(this), this);
	}
	
	public void onDisable() {
		for(Arena arena : arenaManager.getArenas()) {
			if(arena.getSize() > 0) {
				arena.reset();
			}
		}
	}
	
	public int hasPlayer(Player player) {
		int i = 0;
		for(Arena arena : this.arenaManager.getArenas()) {
			if(arena.hasPlayer(player)) {
				return i;
			}
			
			i++;
		}
		
		return -1;
	}
	
	public Adapter getAdapter(int arena, Player player) {
		return this.arenaManager.getArenas().get(arena).getAdapter(player);
	}
	
	public int hasSpectator(Player player) {
		int i = 0;
		for(Arena arena : this.arenaManager.getArenas()) {
			if(arena.hasSpectator(player)) {
				return i;
			}
			
			i++;
		}
		
		return -1;
	}
	
}