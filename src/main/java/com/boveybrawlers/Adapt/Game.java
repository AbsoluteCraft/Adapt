package com.boveybrawlers.Adapt;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;

import com.boveybrawlers.AbsoluteCraft.AbsoluteCraft;

public class Game implements Listener {
	public static boolean playing = false;
	public static boolean inArena = false;
	public static ArrayList<Adapter> adapters = new ArrayList<Adapter>();
	
	public static ArrayList<Integer> takenSpawns = new ArrayList<Integer>();
	public static ArrayList<Integer> takenItems = new ArrayList<Integer>();
	
	public static QueueHandler queueTask = null;
	public static boolean queueCountdown = false;
	
	public static CountdownHandler countdownTask = null;
	public static boolean countdown = false;
	
	public static int getAdapterByName(String username) {
        for(Adapter adapter : adapters) {
            if(adapter.getName().equals(username)) {
                return adapters.indexOf(adapter);
            }
        }
        return -1;
    }
	
	public static void addPlayer(String username) {
		adapters.add(new Adapter(username));
		
		if(adapters.size() == 8) {
			countdown();
			queueTask.cancel();
		} else if(adapters.size() == 2) {
			if(queueCountdown == false) {
				queueCountdown();
			}
		} else if(adapters.size() == 1) {
			if(Bukkit.getWorld("Arena") == null) {
				Adapt.plugin.getLogger().info("World 'Arena' doesn't exist");
			} else {
				Adapt.plugin.world = Bukkit.getWorld("Arena");
			}
			
			Adapt.plugin.spawnControl = new Location(Adapt.plugin.world, 1, 4, 0);
			Adapt.plugin.lobby = new Location(Adapt.plugin.world, 1, 39.5, 0);
			
			Adapt.plugin.cyan = new Location(Adapt.plugin.world, 1, 18, -48, (float) 0, (float) -25);
			Adapt.plugin.yellow = new Location(Adapt.plugin.world, 35, 18, -34, (float) 45, (float) -10);
			Adapt.plugin.purple = new Location(Adapt.plugin.world, 49, 18, 0, (float) 90, (float) -25);
			Adapt.plugin.lightgreen = new Location(Adapt.plugin.world, 35, 18, 34, (float) 135, (float) -10);
			Adapt.plugin.pink = new Location(Adapt.plugin.world, 1, 18, 48, (float) -180, (float) -25);
			Adapt.plugin.red = new Location(Adapt.plugin.world, -33, 18, 34, (float) -135, (float) -10);
			Adapt.plugin.orange = new Location(Adapt.plugin.world, -47, 18, 0, (float) -90, (float) -25);
			Adapt.plugin.blue = new Location(Adapt.plugin.world, -33, 18, -34, (float) -45, (float) -10);
			
			Adapt.plugin.objective = Adapt.plugin.board.registerNewObjective("adapt", "dummy");
			Adapt.plugin.objective.setDisplayName(ChatColor.BOLD + "" + ChatColor.GRAY + "Adapt");
			Adapt.plugin.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

			Adapt.plugin.cyanTeam = Adapt.plugin.board.registerNewTeam("Cyan");
			Adapt.plugin.yellowTeam = Adapt.plugin.board.registerNewTeam("Yellow");
			Adapt.plugin.purpleTeam = Adapt.plugin.board.registerNewTeam("Purple");
			Adapt.plugin.lightgreenTeam = Adapt.plugin.board.registerNewTeam("Lime");
			Adapt.plugin.pinkTeam = Adapt.plugin.board.registerNewTeam("Pink");
			Adapt.plugin.redTeam = Adapt.plugin.board.registerNewTeam("Red");
			Adapt.plugin.orangeTeam = Adapt.plugin.board.registerNewTeam("Orange");
			Adapt.plugin.blueTeam = Adapt.plugin.board.registerNewTeam("Blue");

			Adapt.plugin.cyanTeam.setPrefix(ChatColor.AQUA + "");
			Adapt.plugin.yellowTeam.setPrefix(ChatColor.YELLOW + "");
			Adapt.plugin.purpleTeam.setPrefix(ChatColor.DARK_PURPLE + "");
			Adapt.plugin.lightgreenTeam.setPrefix(ChatColor.GREEN + "");
			Adapt.plugin.pinkTeam.setPrefix(ChatColor.LIGHT_PURPLE + "");
			Adapt.plugin.redTeam.setPrefix(ChatColor.RED + "");
			Adapt.plugin.orangeTeam.setPrefix(ChatColor.GOLD + "");
			Adapt.plugin.blueTeam.setPrefix(ChatColor.BLUE + "");
			Adapt.plugin.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		
		Adapter adapter = adapters.get(getAdapterByName(username));
		if(Bukkit.getWorld("Arena") == null) {
			adapter.sendMessage("Can't teleport you because world is null");
		} else {
			adapter.teleport(Adapt.plugin.lobby);
		}
		adapter.setGameMode(0);
		
		for(Adapter a : adapters) {
			a.sendMessage(Adapt.plugin.prefix + ChatColor.GREEN + username + " has joined");
		}
	}
	
	public static void removePlayer(int index, boolean killed) {
		String username = adapters.get(index).getName();
		
		Player player = adapters.get(index).getPlayer();
		
		if(playing == true) {
			Team dead = player.getScoreboard().getPlayerTeam(player);
			if(dead != null) {
				dead.setPrefix(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "");
			}
		}
		
		player.setScoreboard(Adapt.plugin.manager.getNewScoreboard());
		
		if(killed == false) {
			for(Adapter adapter : adapters) {
				adapter.sendMessage(Adapt.plugin.prefix + ChatColor.RED +  username + " has left");
			}
		}
		
		adapters.get(index).removeInventory();
		adapters.get(index).heal();
		adapters.get(index).teleport(Adapt.plugin.lobby);
		
		adapters.remove(index);
		
		if(playing == false && adapters.size() == 1) {
			if(queueCountdown == true) {
				queueTask.cancel();
			}
			if(countdown == true) {
				countdownTask.cancel();
			}
			for(Adapter adapter : adapters) {
				adapter.teleport(Adapt.plugin.lobby);
				adapter.sendMessage(Adapt.plugin.prefix + ChatColor.RED + "Not enough players left to start a game");
			}
			
			// Reset game
			Adapt.plugin.board.clearSlot(DisplaySlot.SIDEBAR);
			Adapt.plugin.board.clearSlot(DisplaySlot.PLAYER_LIST);
			Adapt.plugin.manager = Bukkit.getScoreboardManager();
			Adapt.plugin.board = Adapt.plugin.manager.getNewScoreboard();
			
			adapters.clear();
			takenItems.clear();
			takenSpawns.clear();
			Adapt.plugin.spawnControl.getBlock().setType(Material.AIR);
			playing = false;
			inArena = false;
		}
		
		if(playing == true && adapters.size() == 1) {
			for(Adapter adapter : adapters) {
				Bukkit.getServer().broadcastMessage(Adapt.plugin.prefix + ChatColor.DARK_PURPLE + adapter.getName() + " has won the game! " + ChatColor.GREEN + "[+3]");
				
				adapter.removeInventory();
				adapter.heal();
				adapter.teleport(Adapt.plugin.lobby);
				
				adapter.getPlayer().setScoreboard(Adapt.plugin.manager.getNewScoreboard());
				
				AbsoluteCraft.tokens.add(adapter.getPlayer().getUniqueId(), adapter.getName(), 3);
			}
			
			// Reset game
			Adapt.plugin.board.clearSlot(DisplaySlot.SIDEBAR);
			Adapt.plugin.board.clearSlot(DisplaySlot.PLAYER_LIST);
			Adapt.plugin.manager = Bukkit.getScoreboardManager();
			Adapt.plugin.board = Adapt.plugin.manager.getNewScoreboard();
			
			adapters.clear();
			takenItems.clear();
			takenSpawns.clear();
			Adapt.plugin.spawnControl.getBlock().setType(Material.AIR);
			playing = false;
			inArena = false;
		}
	}
	
	private static void queueCountdown() {
		queueCountdown = true;
		queueTask = new QueueHandler(50);
		queueTask.runTaskTimer(Adapt.plugin, 0, 20);
	}

	public static void countdown() {
		inArena = true;
		Adapt.plugin.spawnControl.getBlock().setType(Material.AIR);
		
		for(Adapter adapter : adapters) {
			int randomNum;
			do {
				Random rand = new Random();
				randomNum = rand.nextInt(8);
			}  while(takenSpawns.contains(randomNum));
			takenSpawns.add(randomNum);
			
			Player player = adapter.getPlayer();
			player.setScoreboard(Adapt.plugin.board);
			
			switch(randomNum) {
				case 0:
					Adapt.plugin.cyanTeam.addPlayer(player);
					Adapt.plugin.cyanTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.cyan);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 9));
				break;
				case 1:
					Adapt.plugin.yellowTeam.addPlayer(player);
					Adapt.plugin.yellowTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.yellow);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 4));
				break;
				case 2:
					Adapt.plugin.purpleTeam.addPlayer(player);
					Adapt.plugin.purpleTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.purple);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 10));
				break;
				case 3:
					Adapt.plugin.lightgreenTeam.addPlayer(player);
					Adapt.plugin.lightgreenTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.lightgreen);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 5));
				break;
				case 4:
					Adapt.plugin.pinkTeam.addPlayer(player);
					Adapt.plugin.pinkTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.pink);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 6));
				break;
				case 5:
					Adapt.plugin.redTeam.addPlayer(player);
					Adapt.plugin.redTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.red);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 14));
				break;
				case 6:
					Adapt.plugin.orangeTeam.addPlayer(player);
					Adapt.plugin.orangeTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.orange);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 1));
				break;
				case 7:
					Adapt.plugin.blueTeam.addPlayer(player);
					Adapt.plugin.blueTeam.setDisplayName(player.getName());
					
					adapter.teleport(Adapt.plugin.blue);
					adapter.setHat(new ItemStack(Material.WOOL, 1, (short) 11));
				break;
			}
			
			Adapt.plugin.objective.getScore(player.getName()).setScore(0);
			
			adapter.setInventory("start");
		}
		
		
		countdownTask = new CountdownHandler(10);
		countdownTask.runTaskTimer(Adapt.plugin, 0, 20);
	}
	
	public static void start() {
		playing = true;
		for(Adapter adapter : adapters) {
			adapter.sendMessage(Adapt.plugin.prefix + ChatColor.GREEN + "GO!");
			adapter.heal();
		}
		Adapt.plugin.spawnControl.getBlock().setType(Material.REDSTONE_BLOCK);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInterract(PlayerInteractEvent event) {
		if(playing == true) {
			Player player = event.getPlayer();
			
			int index = getAdapterByName(player.getName());
		
			if(index != -1) {
				Adapter adapter = adapters.get(index);
				
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
					if(event.getClickedBlock().getState() instanceof Chest) {
						if(adapter.hasDiamond() == false) {
							adapter.setDiamond(true);
							
							int randomNum;
							do {
								Random rand = new Random();
								randomNum = rand.nextInt(8);
							} while(takenItems.contains(randomNum));
							takenItems.add(randomNum);
							
							switch(randomNum) {
								case 0:
									player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 1:
									player.getInventory().addItem(new ItemStack(Material.RAW_FISH, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.GRAY + "Unlucky!");
								break;
								case 2:
									player.getInventory().addItem(new ItemStack(Material.DIAMOND_CHESTPLATE, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 3:
									player.getInventory().addItem(new ItemStack(Material.DIAMOND_LEGGINGS, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 4:
									player.getInventory().addItem(new ItemStack(Material.DIAMOND_BOOTS, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 5:
									player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8201));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 6:
									player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8194));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
								case 7:
									player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
									player.sendMessage(Adapt.plugin.prefix + ChatColor.AQUA + "Here ya go!");
								break;
							}
						}
						event.setCancelled(true);
					}
				}
			}
		}
	}
		
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
	    if(event.getEntity() instanceof Player) {
	    	Player player = (Player) event.getEntity();
	    	
	    	player.getItemInHand().setDurability((short) 1);
	    	
	    	int playerIndex = getAdapterByName(player.getName());
	    	if(playerIndex != -1) {
	    		if(event.getDamage() >= ((Player) event.getEntity()).getHealth()) {
		            event.setDamage(0);
		            player.teleport(Adapt.plugin.lobby);
		            
		            Player kill = null;
		            
		            if(event.getDamager() instanceof Arrow) {
			    		Arrow arrow = (Arrow) event.getDamager();
			    		
			    		if(arrow.getShooter() instanceof Player) {
			    			kill = (Player) arrow.getShooter();
			    		}
			    	} else if(player.getKiller() instanceof Player) {
		            	kill = (Player) event.getDamager();
		            }
		            
		            if(kill != null) {
			            int killerIndex = getAdapterByName(kill.getName());
		            	if(killerIndex != -1) {
		            		Adapter killer = adapters.get(killerIndex);
		            		
		            		for(Adapter adapter : adapters) {
		            			adapter.sendMessage(Adapt.plugin.prefix + ChatColor.DARK_AQUA + killer.getName() + " killed " + player.getName() + ChatColor.GREEN + " [+1]");
		            		}
		            		
		            		killer.addKill();
		            		AbsoluteCraft.tokens.add(killer.getPlayer().getUniqueId(), killer.getName(), 1);
		            		AbsoluteCraft.leaderboard.add(killer.getName(), "adapt", 1);
		            	}
		            }
		            
		            removePlayer(playerIndex, true);
		        }
	    	}
	    }
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
	    	Player player = (Player) event.getEntity();
	    	
	    	int index = getAdapterByName(player.getName());
	    	if(index != -1) {
	    		if(event.getDamage() >= ((Player) event.getEntity()).getHealth()) {
		            event.setDamage(0);
		            player.teleport(Adapt.plugin.lobby);
		            
		            for(Adapter adapter : adapters) {
		            	adapter.sendMessage(Adapt.plugin.prefix + ChatColor.RED + player.getName() + " died");
		            }
		            
		            removePlayer(index, true);
	    		}
	    	}
		}
	}
	
	@EventHandler
    public void onCommandType(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        String msg = event.getMessage();
        
        if(playing == true && (!player.isOp() || !player.hasPermission("adapt.admin"))) {
	        int index = getAdapterByName(player.getName());
	        if (index != -1) {
	        	if(msg.contains("/adapt leave")) {
	        		return;
	        	} else if (msg.startsWith("/")){
	                player.sendMessage(Adapt.plugin.prefix + ChatColor.RED + " You cannot use commands whilst playing!");
	                event.setCancelled(true);
	            }
	        }
        }
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		int index = getAdapterByName(player.getName());
		if(index != -1) {
			removePlayer(index, false);
		}
	}
}