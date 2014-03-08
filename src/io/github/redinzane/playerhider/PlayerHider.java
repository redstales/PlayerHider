/*
 *  PlayerHider - A simple plugin that tries to hide underground bases by letting players appear sneaked to players that are further away.
 *  Copyright (C) 2013 Moritz A. Schwab
 *  Uses code from:
 *  Sneaky (C) 2013 Kristian S. Stangeland, Licensed under the GNU GPL2, available at http://www.comphenix.net/Sneaky
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 2 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *  02111-1307 USA
 */

package io.github.redinzane.playerhider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
 
public final class PlayerHider extends JavaPlugin {
	
	// Configuration
	private PlayerHiderConfiguration config;
	// Minecraft packet handling
	private PlayerHiderListener listener;
	// Reference to PL
	private ProtocolManager manager;
	//ID of the runnable task
	private int taskID = -1;
	//Metrics
	private Metrics metrics;
	
	int updateCooldown = 500;
	private long lastCall = 0;
	
	
	@Override
    public void onEnable()
	{
		//Creates a Config
		config = new PlayerHiderConfiguration(getConfig());
		
		if (!hasConfig()) 
		{
			getConfig().options().copyDefaults(true);
			saveConfig();
			
			// Load it again
			config = new PlayerHiderConfiguration(getConfig());
			getLogger().info("Creating default configuration.");
		}
		
		// Packet handling
		manager = ProtocolLibrary.getProtocolManager();
		listener = new PlayerHiderListener(this);

		// Register listeners
		manager.addPacketListener(listener);
		getServer().getPluginManager().registerEvents(listener, this);
		listener.sneakdistance = config.getDistance();
		this.updateCooldown = config.getCooldown();
		listener.feature_LoS = config.getLoS();
		try
		{
			taskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){public void run(){updateAllPlayers();} }, 0, 1);
			if(taskID == -1)
			{
				throw new Exception("PlayerHider Update Task could not be scheduled");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try 
		{
			metrics = new Metrics(this);
			metrics.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
    }
 
    @Override
    public void onDisable()
    {
    	getServer().getScheduler().cancelTask(taskID);
    }
    
    //Checks if a config file exists
    private boolean hasConfig() {
		File file = new File(getDataFolder(), "config.yml");
		return file.exists();
	}
    
    public void updateAllPlayers()
	{
		long time = System.currentTimeMillis();
		if((time-lastCall)>updateCooldown)
		{
			lastCall = System.currentTimeMillis();
			Player[] tempPlayers = Bukkit.getServer().getOnlinePlayers();
			for(Player player: tempPlayers)
			{			
				try 
				{
					listener.updatePlayer(ProtocolLibrary.getProtocolManager(), player);
				}
				catch (InvocationTargetException e) 
				{
					e.printStackTrace();
				}
			}							
		}
		else
		{
			
		}
	}
}