package com.boveybrawlers.Adapt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Adapter;
import com.boveybrawlers.Adapt.Arena;
import com.boveybrawlers.Adapt.events.PlayerKillEvent;

public class PlayerDamageByPlayer implements Listener {
	
	Adapt plugin;
	
	public PlayerDamageByPlayer(Adapt plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			
			int id = plugin.hasPlayer(player);
			if(id > -1) {
				Arena arena = plugin.arenaManager.getArenas().get(id);
				Adapter adapter = arena.getAdapter(player);
				
				Player killer = null;
				if(event.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) event.getDamager();
					
					if(arrow.getShooter() instanceof Player) {
						killer = (Player) arrow.getShooter();
					}
				} else if(event.getDamager() instanceof Player) {
					killer = (Player) event.getDamager();
				}
				
				if(killer != null) {
					if(killer.getItemInHand().getType() == Material.COOKED_FISH) {
						if(player.getHealth() <= 8) {
							event.setDamage(0);
							
							arena.getLocation("lobby").getWorld().strikeLightning(player.getLocation());
							
							Adapter adaptKiller = plugin.getAdapter(id, killer);
							adaptKiller.addKill();
							
							arena.getPlayerObjective(killer).setScore(adaptKiller.getKills());
							
							arena.getPlayerObjective(player).getScoreboard().getPlayerTeam(player).setPrefix(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH);
							
							PlayerKillEvent e = new PlayerKillEvent(player, killer, adaptKiller.getKills());
							Bukkit.getServer().getPluginManager().callEvent(e);
							
							arena.sendMessage(ChatColor.DARK_AQUA + killer.getDisplayName() + " slapped " + player.getDisplayName() + " with the " + ChatColor.GOLD + "Super Fish" + ChatColor.DARK_AQUA + "!" + ChatColor.GREEN + " [+1]");
							
							arena.removeAdapter(adapter, true);
						} else {
							event.setDamage(8);
						}
					} else {
						if(event.getDamage() >= player.getHealth()) {
							arena.getLocation("lobby").getWorld().strikeLightning(player.getLocation());
							
							event.setDamage(0);
							
							Adapter adaptKiller = plugin.getAdapter(id, killer);
							adaptKiller.addKill();
							
							arena.getPlayerObjective(killer).setScore(adaptKiller.getKills());
							
							arena.getPlayerObjective(player).getScoreboard().getPlayerTeam(player).setPrefix(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH);
							
							PlayerKillEvent e = new PlayerKillEvent(player, killer, adaptKiller.getKills());
							Bukkit.getServer().getPluginManager().callEvent(e);
							
							arena.sendMessage(ChatColor.DARK_AQUA + killer.getDisplayName() + " killed " + player.getDisplayName() + ChatColor.GREEN + " [+1]");
							
							arena.removeAdapter(adapter, true);
						}
					}
				}
			}
		}
	}
	
}
