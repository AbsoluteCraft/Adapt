package com.boveybrawlers.Adapt.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.boveybrawlers.Adapt.Adapt;
import com.boveybrawlers.Adapt.Adapter;
import com.boveybrawlers.Adapt.Arena;

public class ClickChest implements Listener {
	
	Adapt plugin;
	
	public ClickChest(Adapt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
				int id = plugin.hasPlayer(player);
				if(id > -1) {
					event.setCancelled(true);
					
					Arena arena = plugin.arenaManager.getArenas().get(id);
					Adapter adapter = arena.getAdapter(player);
					
					if(!adapter.hasDiamond()) {
						adapter.setDiamond(true);
						
						Integer randomNum;
						do {
							Random rand = new Random();
							randomNum = rand.nextInt(8);
						} while(arena.getTakenItems().contains(randomNum));
						
						arena.getTakenItems().add(randomNum);
						
						switch(randomNum) {
							case 0:
								ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
								ItemMeta meta = sword.getItemMeta();
								meta.spigot().setUnbreakable(true);
								sword.setItemMeta(meta);
								player.getInventory().addItem(sword);
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 1:
								if(player.hasPermission("adapt.superfish")) {
									ItemStack cookedFish = new ItemStack(Material.COOKED_FISH, 1);
									meta = cookedFish.getItemMeta();
									meta.setDisplayName(ChatColor.GOLD + "Super Fish");
									List<String> lore = new ArrayList<String>();
									lore.add("The fish is now lucky! Slap people with the fish for extra damage and humiliation.");
									meta.setLore(lore);
									cookedFish.setItemMeta(meta);
									player.getInventory().addItem(cookedFish);
									player.sendMessage(plugin.prefix + ChatColor.GOLD + ChatColor.BOLD + "Super Fish activated!");
								} else {
									player.getInventory().addItem(new ItemStack(Material.RAW_FISH, 1));
									player.sendMessage(plugin.prefix + ChatColor.GRAY + "Unlucky!");
								}
							break;
							case 2:
								ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
								meta = chestplate.getItemMeta(); 
								meta.spigot().setUnbreakable(true);
								List<String> lore = new ArrayList<String>();
								lore.add(ChatColor.GRAY + "Unbreaking X");
								meta.setLore(lore);
								chestplate.setItemMeta(meta);
								player.getInventory().addItem(chestplate);
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 3:
								ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
								meta = leggings.getItemMeta(); 
								meta.spigot().setUnbreakable(true);
								lore = new ArrayList<String>();
								lore.add(ChatColor.GRAY + "Unbreaking X");
								meta.setLore(lore);
								leggings.setItemMeta(meta);
								player.getInventory().addItem(leggings);
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 4:
								ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
								meta = boots.getItemMeta(); 
								meta.spigot().setUnbreakable(true);
								lore = new ArrayList<String>();
								lore.add(ChatColor.GRAY + "Unbreaking X");
								meta.setLore(lore);
								boots.setItemMeta(meta);
								player.getInventory().addItem(boots);
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 5:
								player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8201));
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 6:
								player.getInventory().addItem(new ItemStack(Material.POTION, 1, (short) 8194));
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
							case 7:
								player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 1));
								player.sendMessage(plugin.prefix + ChatColor.AQUA + "Here ya go!");
							break;
						}
					}
				}
			}
		}
	}
}