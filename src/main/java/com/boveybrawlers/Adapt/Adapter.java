package com.boveybrawlers.Adapt;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

public class Adapter {
	
	private Player player;
	
	private int kills = 0;
	private boolean hasDiamond = false;
	
	public Adapter(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getKills() {
		return this.kills;
	}
	
	public void addKill() {
		this.kills++;
	}
	
	public boolean hasDiamond() {
		return this.hasDiamond;
	}
	
	public void setDiamond(boolean hasDiamond) {
		this.hasDiamond = hasDiamond;
	}
	
	public void heal() {
		this.player.setHealth((double) 20);
		this.player.setFoodLevel(20);
		this.player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		this.player.removePotionEffect(PotionEffectType.SPEED);
		this.player.removePotionEffect(PotionEffectType.ABSORPTION);
	}
	
	public void setInventory(String type) {
		this.player.getInventory().clear();
		
		if(type == "start") {
			ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
			ItemMeta meta = sword.getItemMeta();
			meta.spigot().setUnbreakable(true);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.GRAY + "Unbreaking X");
			meta.setLore(lore);
			sword.setItemMeta(meta);
			
			ItemStack bow = new ItemStack(Material.BOW, 1);
			meta = bow.getItemMeta();
			meta.spigot().setUnbreakable(true);
			lore.clear();
			lore.add(ChatColor.GRAY + "Unbreaking X");
			meta.setLore(lore);
			bow.setItemMeta(meta);
			bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
			
			
			this.player.getInventory().addItem(sword);
			this.player.getInventory().addItem(bow);
			this.player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
			this.player.getInventory().addItem(new ItemStack(Material.GRILLED_PORK, 2));
			this.player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE, 1));
			this.player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS, 1));
			this.player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS, 1));
		}
	}
}
