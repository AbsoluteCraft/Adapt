package com.boveybrawlers.Adapt;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(Adapt.plugin.prefix + "v0.1 by boveybrawlers");
			sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
			sender.sendMessage(ChatColor.WHITE + "/adapt join");
			sender.sendMessage(ChatColor.WHITE + "/adapt leave");
			if(sender.hasPermission("adapt.start") || sender.isOp()) {
				sender.sendMessage(ChatColor.WHITE + "/adapt start");
			}
			return true;
		} else {
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
					
					if(args[0].equalsIgnoreCase("join")) {
						if(Game.getAdapterByName(player.getName()) !=  -1) {
							player.sendMessage(Adapt.plugin.prefix + "You are already in game");
						} else if(Game.adapters.size() < 8 && Game.playing == false) { 
							Game.addPlayer(player.getName());
						} else if(Game.inArena == true || Game.playing == true) {
							player.sendMessage(Adapt.plugin.prefix + ChatColor.RED + "There's already a game playing, try again in a minute");
						} else {
							player.sendMessage(Adapt.plugin.prefix + ChatColor.RED + "The current game is full");
						}
						return true;
					} else if(args[0].equalsIgnoreCase("leave")) {
						int index = Game.getAdapterByName(player.getName());
						if(index != -1) {
							Game.removePlayer(index, false);
						}
						return true;
					} else if(args[0].equalsIgnoreCase("start") && (player.isOp() && player.hasPermission("adapt.admin"))) {
						if(Game.adapters.size() > 1 && Game.queueCountdown == true) {
							Game.queueTask.cancel();
							Game.countdown();
						} else {
							sender.sendMessage(Adapt.plugin.prefix + ChatColor.RED + "The game does not have enough players to start");
						}
						return true;
					}
				}
			}
		}
		return false;
	}

}
