package com.boveybrawlers.Adapt;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.boveybrawlers.Adapt.utils.ConfigAccessor;

public class ArenaManager {
	
	Adapt plugin;
	
	private ConfigAccessor accessor;
	
	private List<Arena> arenas = new ArrayList<Arena>();
	
	public ArenaManager(Adapt plugin) {
		this.plugin = plugin;
		
		this.accessor = new ConfigAccessor(plugin, "arenas.yml");
		
		ConfigurationSection arenaList = this.accessor.getConfig().getConfigurationSection("arenas");
		if(arenaList != null) {

			for(String key : arenaList.getKeys(false)) {
				ConfigurationSection arenaData = arenaList.getConfigurationSection(key);
				
				Arena arena = new Arena(plugin, Integer.parseInt(key), arenaData.getInt("teams"));
				
				plugin.getLogger().log(Level.INFO, "Teams: " + arenaData.getInt("teams"));
				
				World world = Bukkit.getServer().getWorld(arenaData.getString("world"));
				
				plugin.getLogger().log(Level.INFO, "World: " + arenaData.getString("world"));
				
				ConfigurationSection locations = arenaData.getConfigurationSection("locations");
				if(locations != null) {
					for(String name : locations.getKeys(false)) {
						ConfigurationSection locationData = locations.getConfigurationSection(name);
						
						double x = locationData.getDouble("x");
						double y = locationData.getDouble("y");
						double z = locationData.getDouble("z");
						float yaw = (float) locationData.getDouble("yaw");
						float pitch = (float) locationData.getDouble("pitch");
						
						arena.addLocation(name, new Location(world, x, y, z, yaw, pitch));
							
						plugin.getLogger().log(Level.SEVERE, "Location: w:" + world.getName() + " x:" + x + " y:" + y + " z:" + z + " yaw:" + yaw + " pitch:" + pitch);
					}
				} else {
					plugin.getLogger().log(Level.SEVERE, "Locations list is null");
				}
				
				this.arenas.add(arena);
				
			}
		} else {
			plugin.getLogger().log(Level.SEVERE, "Arena List is null");
		}
	}
	
	public List<Arena> getArenas() {
		return arenas;
	}
	
	public boolean hasArena(int id) {
		if(this.arenas.size() >= id) {
			if(this.arenas.get(id) != null) {
				return true;
			}
		}
		
		return false;
	}
	
	public void reloadArenas() {
		accessor.reloadConfig();
	}
}
