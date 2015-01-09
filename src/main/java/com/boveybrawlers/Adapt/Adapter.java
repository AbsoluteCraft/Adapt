package com.boveybrawlers.Adapt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Adapter {
	String username = null;
	Integer kills = 0;
	boolean hasDiamond = false;
	
	public Adapter(String username) {
		this.username = username;
	}
	
	public String getName() {
		return this.username;
	}
	
	public Integer getKills() {
		return this.kills;
	}
	
	@SuppressWarnings("deprecation")
	public void addKill() {
		this.kills++;
		Adapt.plugin.objective.getScore(Bukkit.getServer().getPlayer(this.username)).setScore(this.kills);
	}
	
	public boolean hasDiamond() {
		return this.hasDiamond;
	}
	
	public void setDiamond(boolean hasDiamond) {
		this.hasDiamond = hasDiamond;
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(this.username);
	}
	
	public void heal() {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.setHealth((double) 20);
		player.setFoodLevel(20);
		if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) == true) {
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		}
		if (player.hasPotionEffect(PotionEffectType.SPEED) == true) {
	        player.removePotionEffect(PotionEffectType.SPEED);
		}
		if(player.hasPotionEffect(PotionEffectType.ABSORPTION) == true) {
			player.removePotionEffect(PotionEffectType.ABSORPTION);
		}
	}
	
	public void setGameMode(int mode) {
		if(mode == 0) {
			Bukkit.getServer().getPlayer(this.username).setGameMode(GameMode.SURVIVAL);
		} else if(mode == 1) {
			Bukkit.getServer().getPlayer(this.username).setGameMode(GameMode.CREATIVE);
		}
	}
	
	public void sendMessage(String message) {
		Bukkit.getServer().getPlayer(this.username).sendMessage(message);
	}
	
	public void teleport(Location location) {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.teleport(location);
	}
	
	public void setHat(ItemStack wool) {
		Bukkit.getServer().getPlayer(this.username).getInventory().setHelmet(wool);
	}
	
	public void removeInventory() {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.getInventory().setHelmet(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory().clear();
	}
	
	public void setInventory(String type) {
		Player player = Bukkit.getServer().getPlayer(this.username);
		player.getInventory().clear();
		
		if(type == "start") {
			ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
			sword.addEnchantment(Enchantment.DURABILITY, 3);
			
			ItemStack bow = new ItemStack(Material.BOW, 1);
			bow.addEnchantment(Enchantment.DURABILITY, 3);
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			
			
			player.getInventory().addItem(sword);
			player.getInventory().addItem(bow);
			player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			player.getInventory().addItem(new ItemStack(Material.GRILLED_PORK, 2));
			player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
			player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
			player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		}
	}
}
