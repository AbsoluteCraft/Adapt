package com.boveybrawlers.Adapt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Adapt extends JavaPlugin {
	public static Adapt plugin = null;
	public String prefix = ChatColor.GRAY + "" + ChatColor.BOLD + "Adapt" + ChatColor.RESET + ChatColor.DARK_GRAY + " | ";
	
	public World world;
	
	public Location spawnControl = null;
	public Location lobby = null;
	
	public Location cyan = null;
	public Location yellow = null;
	public Location purple = null;
	public Location lightgreen = null;
	public Location pink = null;
	public Location red = null;
	public Location orange = null;
	public Location blue = null;
	
	public ScoreboardManager manager = null;
	public Scoreboard board = null;
	public Objective objective = null;
	
	public Team cyanTeam = null;
	public Team yellowTeam = null;
	public Team purpleTeam = null;
	public Team lightgreenTeam = null;
	public Team pinkTeam = null;
	public Team redTeam = null;
	public Team orangeTeam = null;
	public Team blueTeam = null;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		manager = Bukkit.getScoreboardManager();
		board = manager.getNewScoreboard();
		
		this.getCommand("adapt").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new Game(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
