package com.boveybrawlers.Adapt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.boveybrawlers.Adapt.events.PlayerWinEvent;
import com.boveybrawlers.Adapt.utils.CountdownHandler;
import com.boveybrawlers.Adapt.utils.QueueHandler;
import com.boveybrawlers.Adapt.utils.TeamInfo;

public class Arena {
	
	Adapt plugin;
	
	private int id;
	
	private Map<String, Location> locations = new HashMap<String, Location>();
	private List<Adapter> adapters = new ArrayList<Adapter>();
	private List<Player> spectators = new ArrayList<Player>();
	
	ScoreboardManager manager;
	Scoreboard board;
	Objective objective;
	private Map<String, Team> teams = new HashMap<String, Team>();
	
	private List<Integer> takenSpawns = new ArrayList<Integer>();
	private List<Integer> takenItems = new ArrayList<Integer>();
	
	private int maxSize;
	
	private boolean playing = false;

	private boolean queuing = false;
	private QueueHandler queueTask;

	private boolean countdown;
	private CountdownHandler countdownTask;
	
	public Arena(Adapt plugin, int id, int maxSize) {
		this.plugin = plugin;
		
		this.id = id;
		
		this.maxSize = maxSize;
		
		this.manager = Bukkit.getScoreboardManager();
		this.board = manager.getNewScoreboard();
		
		for(int i = 0; i < this.maxSize; i++) {
			String name = TeamInfo.getNameFromInt(i);
			this.teams.put(name, board.registerNewTeam(name));
			
			this.teams.get(name).setPrefix(TeamInfo.getColorFromInt(i));
		}
	}

	public Location getLocation(String name) {
		return this.locations.get(name);
	}
	
	public void addLocation(String name, Location location) {
		this.locations.put(name, location);
	}
	
	public int getSize() {
		return this.adapters.size();
	}
	
	public int getMaxSize() {
		return this.maxSize;
	}
	
	public boolean isPlaying() {
		return this.playing;
	}
	
	public boolean hasPlayer(Player player) {
		for(Adapter adapter : this.adapters) {
			if(adapter.getPlayer() == player) {
				return true;
			}
		}
		
		return false;
	}
	
	public Adapter getAdapter(Player player) {
		for(Adapter adapter : this.adapters) {
			if(adapter.getPlayer() == player) {
				return adapter;
			}
		}
		
		return null;
	}
	
	public void addAdapter(Adapter adapter) {
		this.adapters.add(adapter);
		
		adapter.getPlayer().teleport(this.locations.get("lobby"));
		adapter.getPlayer().setGameMode(GameMode.SURVIVAL);
		adapter.heal();
		
		this.sendMessage(ChatColor.GREEN + adapter.getPlayer().getDisplayName() + " has joined");
		
		if(this.getSize() == this.getMaxSize()) {
			this.startCountdown();
		} else if(this.getSize() == 2) {
			this.startQueue();
		}
	}
	
	public void removeAdapter(Adapter adapter, boolean killed) {
		Player player = adapter.getPlayer();
		
		if(killed == false) {
			this.sendMessage(ChatColor.RED + player.getDisplayName() + " has left");
		}
		
		player.getInventory().clear();
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		
		adapter.heal();
		player.teleport(this.getLocation("lobby"));
		
		adapters.remove(adapter);
		
		player.setScoreboard(this.manager.getNewScoreboard());
		
		if(!this.isPlaying() && this.getSize() == 1) {
			if(this.isQueuing()) {
				this.queueTask.cancel();
				this.setQueueTime(0);
			}
			if(this.isCountingDown()) {
				this.countdownTask.cancel();
			}
			
			this.sendMessage(ChatColor.RED + "Not enough players left to start a game");
			
			this.reset();
		} else if(this.isPlaying() && this.getSize() == 1) {
			for(Adapter a : this.adapters) {
				a.getPlayer().getInventory().clear();
				player.getInventory().setHelmet(new ItemStack(Material.AIR));
				player.getInventory().setChestplate(new ItemStack(Material.AIR));
				player.getInventory().setLeggings(new ItemStack(Material.AIR));
				player.getInventory().setBoots(new ItemStack(Material.AIR));
				
				a.heal();
				a.getPlayer().teleport(this.getLocation("lobby"));
				
				a.getPlayer().setScoreboard(this.manager.getNewScoreboard());
				
				PlayerWinEvent event = new PlayerWinEvent(a.getPlayer(), a.getKills());
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				Bukkit.getServer().broadcastMessage(plugin.prefix + ChatColor.DARK_PURPLE + a.getPlayer().getDisplayName() + " has won Adapt! " + ChatColor.GREEN + "[+3]");
			}
			
			this.reset();
		} else {
			this.addSpectator(player);
		}
	}
	
	public boolean isQueuing() {
		return this.queuing;
	}
	
	private void startQueue() {
		this.queuing = true;
		this.queueTask = new QueueHandler(plugin, this, 60);
		this.queueTask.runTaskTimer(plugin, 0, 20);
		
		this.locations.get("spawnControl").getBlock().setType(Material.AIR);
	}
	
	public void setQueue(boolean queueing) {
		this.queuing = queueing;
	}
	
	public boolean isCountingDown() {
		return this.countdown;
	}
	
	public void setCountdown(boolean countdown) {
		this.countdown = countdown;
	}

	@SuppressWarnings("deprecation")
	public void startCountdown() {
		this.playing = true;
		
		if(this.isQueuing()) {
			this.setQueue(false);
			this.queueTask.cancel();
			this.setQueueTime(0);
		}
		
		this.objective = board.registerNewObjective("adapt" + this.id, "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.GRAY + "Adapt");
		
		// Again, just in case of /adapt start
		this.locations.get("spawnControl").getBlock().setType(Material.AIR);
		
		for(Adapter adapter : adapters) {
			int randomNum;
			do {
				Random rand = new Random();
				randomNum = rand.nextInt(this.getMaxSize());
			} while(this.takenSpawns.contains(randomNum));
			
			takenSpawns.add(randomNum);
			
			Player player = adapter.getPlayer();
			String team = TeamInfo.getNameFromInt(randomNum);
			
			Team t = this.teams.get(team);
			t.addPlayer(player);
			t.setDisplayName(player.getDisplayName());
			
			player.setScoreboard(this.board);
			
			this.objective.getScore(player).setScore(0);
			
			player.setGameMode(GameMode.SURVIVAL);
			player.teleport(this.locations.get(team));
			player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, TeamInfo.getWoolDurability(randomNum)));
			
			player.getInventory().clear();
			player.getInventory().setHelmet(new ItemStack(Material.AIR));
			player.getInventory().setChestplate(new ItemStack(Material.AIR));
			player.getInventory().setLeggings(new ItemStack(Material.AIR));
			player.getInventory().setBoots(new ItemStack(Material.AIR));
			
			adapter.setInventory("start");
		}
		
		this.countdownTask = new CountdownHandler(plugin, this, 10);
		this.countdownTask.runTaskTimer(plugin, 0, 20);
		this.setCountdown(true);
	}

	public void sendMessage(String message) {
		for(Adapter adapter : this.adapters) {
			adapter.getPlayer().sendMessage(plugin.prefix + message);
		}
	}
	
	public void teleport(String name) {
		for(Adapter adapter : this.adapters) {
			adapter.getPlayer().teleport(this.getLocation(name));
		}
		
		for(Player player : this.spectators) {
			player.teleport(this.getLocation(name));
		}
	}

	public boolean hasSpectator(Player player) {
		for(Player p : this.spectators) {
			if(p == player) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addSpectator(Player player) {
		this.spectators.add(player);
		
		player.setScoreboard(this.board);
		
		player.setGameMode(GameMode.SPECTATOR);
		player.teleport(this.getLocation("spectate"));
	}
	
	public void removeSpectator(Player player) {
		this.spectators.remove(player);
		
		player.setScoreboard(this.manager.getNewScoreboard());
		
		player.setGameMode(GameMode.SURVIVAL);
		player.teleport(this.getLocation("lobby"));
	}

	public void setQueueTime(int time) {
		for(Adapter adapter : this.adapters) {
			adapter.getPlayer().setLevel(time);
		}
	}

	public void start() {
		this.countdown = false;
		this.countdownTask.cancel();
		
		for(Adapter adapter : this.adapters) {
			adapter.heal();
		}
		
		this.locations.get("spawnControl").getBlock().setType(Material.REDSTONE_BLOCK);
	}
	
	public List<Integer> getTakenItems() {
		return this.takenItems;
	}
	
	public void reset() {
		this.board = this.manager.getNewScoreboard();
		
		this.teleport("lobby");
		
		for(Adapter adapter : this.adapters) {
			adapter.getPlayer().setScoreboard(this.manager.getNewScoreboard());
			
			adapter.getPlayer().getInventory().clear();
			adapter.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
			adapter.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
			adapter.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
			adapter.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
			
			adapter.heal();
		}
		
		for(Player spectator : this.spectators) {
			spectator.setScoreboard(this.manager.getNewScoreboard());
			
			spectator.setGameMode(GameMode.SURVIVAL);
			
			spectator.getInventory().clear();
			spectator.getInventory().setHelmet(new ItemStack(Material.AIR));
			spectator.getInventory().setChestplate(new ItemStack(Material.AIR));
			spectator.getInventory().setLeggings(new ItemStack(Material.AIR));
			spectator.getInventory().setBoots(new ItemStack(Material.AIR));
		}
		
		adapters.clear();
		spectators.clear();
		
		takenItems.clear();
		takenSpawns.clear();
		
		this.getLocation("spawnControl").getBlock().setType(Material.AIR);
		
		this.playing = false;
		this.queuing = false;
	}

	@SuppressWarnings("deprecation")
	public Score getPlayerObjective(Player player) {
		return this.objective.getScore(player);
	}

}