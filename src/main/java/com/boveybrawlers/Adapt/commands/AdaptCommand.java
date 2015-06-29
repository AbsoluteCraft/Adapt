package com.boveybrawlers.Adapt.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Adapter;
import com.boveybrawlers.Adapt.Arena;

public class AdaptCommand implements CommandExecutor {
	
	Adapt plugin;
	
	public AdaptCommand(Adapt plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(plugin.prefix + "v2.0 by boveybrawlers");
			sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
			sender.sendMessage(ChatColor.WHITE + "/adapt join [id]");
			sender.sendMessage(ChatColor.WHITE + "/adapt leave");
			if(sender.hasPermission("adapt.start") || sender.isOp()) {
				sender.sendMessage(ChatColor.WHITE + "/adapt start [id]");
			}
		} else {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				if(args[0].equalsIgnoreCase("join")) {
					int arena_id = plugin.hasPlayer(player);
					if(arena_id == -1) {
						if(args.length == 1) {
							// Find the most populated arena, that isn't playing, else join first empty arena
							Arena arena = null;
							for(Arena a : plugin.arenaManager.getArenas()) {
								if(arena == null) {
									// If arena is not playing and there is space
									if(!a.isPlaying() && a.getSize() < a.getMaxSize()) {
										arena = a;
									}
								} else {
									// If arena is not playing, there is space and it is more populated than current chosen arena
									if(!a.isPlaying() && a.getSize() < a.getMaxSize() && a.getSize() > arena.getSize()) {
										arena = a;
									}
								}
							}
							
							if(arena != null) {
								Adapter adapter = new Adapter(player);
								arena.addAdapter(adapter);
							} else {
								player.sendMessage(plugin.prefix + ChatColor.RED + "Sorry, there aren't any available arenas right now. Try again in a minute.");
							}
						} else {
							// Try to join a specific arena ID
							Integer id = null;
							try {
								id = Integer.parseInt(args[1]);
							} catch(NumberFormatException e) {
								player.sendMessage(plugin.prefix + ChatColor.RED + "Could not find that arena");
							}
							
							if(id != null) {
								if(plugin.arenaManager.hasArena(id)) {
									Arena arena = plugin.arenaManager.getArenas().get(id);
									
									if(!arena.isPlaying()) {
										if(arena.getSize() < arena.getMaxSize()) {
											Adapter adapter = new Adapter(player);
											arena.addAdapter(adapter);
										} else {
											player.sendMessage(plugin.prefix + ChatColor.RED + "That arena is currently full. Try again in a minute.");
										}
									} else {
										TextComponent message = new TextComponent(plugin.prefix + ChatColor.RED + "That arena is currently playing. Click here to ");
										
										TextComponent spectate = new TextComponent("[Spectate]");
										spectate.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
										spectate.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adapt spectate " + id));
										message.addExtra(spectate);
										
										player.spigot().sendMessage(message);
									}
								}
							}
						}
					} else {
						player.sendMessage(plugin.prefix + ChatColor.RED + "You are already in arena " + arena_id);
					}
				} else if(args[0].equalsIgnoreCase("leave")) {
					int adapter_id = plugin.hasPlayer(player);
					int spectator_id = plugin.hasSpectator(player);
					if(adapter_id > -1) {
						Arena arena = plugin.arenaManager.getArenas().get(adapter_id);
						Adapter adapter = plugin.getAdapter(adapter_id, player);
						
						arena.removeAdapter(adapter, false);
					} else if(spectator_id > -1) {
							Arena arena = plugin.arenaManager.getArenas().get(adapter_id);
							
							arena.removeSpectator(player);
					} else {
						player.sendMessage(plugin.prefix + ChatColor.RED + "You are not in a game");
					}
				} else if(args[0].equalsIgnoreCase("spectate")) {
					if(args.length == 1) {
						// Find the player's last arena and if it's playing, spectate it
						if(player.hasMetadata("adapt_last_arena_id")) {
							int id = player.getMetadata("adapt_last_arena_id").get(0).asInt();
							
							if(plugin.arenaManager.hasArena(id)) {
								Arena arena = plugin.arenaManager.getArenas().get(id);
								
								if(arena.isPlaying()) {
									arena.addSpectator(player);
								} else {
									player.sendMessage(plugin.prefix + ChatColor.RED + "That arena is currently not playing");
								}
							}
						} else {
							player.sendMessage(plugin.prefix + ChatColor.RED + "Could not find the last arena you visited, please type /adapt spectate <id>");
						}
					} else {
						// Try to spectate a specific arena ID
						Integer id = null;
						try {
							id = Integer.parseInt(args[1]);
						} catch(NumberFormatException e) {
							player.sendMessage(plugin.prefix + ChatColor.RED + "Could not find that arena");
						}
						
						if(id != null) {
							if(plugin.arenaManager.hasArena(id)) {
								Arena arena = plugin.arenaManager.getArenas().get(id);
								
								if(arena.isPlaying()) {
									arena.addSpectator(player);
								} else {
									player.sendMessage(plugin.prefix + ChatColor.RED + "That arena is currently not playing");
								}
							}
						}
					}
				} else if(args[0].equalsIgnoreCase("start") && player.hasPermission("adapt.start")) {
					if(args.length == 2) {
						Integer id = null;
						try {
							id = Integer.parseInt(args[1]);
						} catch(NumberFormatException e) {
							player.sendMessage(plugin.prefix + ChatColor.RED + "Could not find that arena");
						}
						
						if(id != null) {
							if(plugin.arenaManager.hasArena(id)) {
								Arena arena = plugin.arenaManager.getArenas().get(id);
								
								arena.startCountdown();
							}
						}
					} else {
						int id = plugin.hasPlayer(player);
						if(id > -1) {
							Arena arena = plugin.arenaManager.getArenas().get(id);
							arena.start();
						}
					}
				}
			}
		}
		
		return true;
	}

}