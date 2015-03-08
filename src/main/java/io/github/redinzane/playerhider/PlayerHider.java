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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.trc202.CombatTag.CombatTag;
import com.trc202.CombatTagApi.CombatTagApi;

public final class PlayerHider extends JavaPlugin {

    
    private static PlayerHider instance;
    // Configuration
    private PlayerHiderConfiguration config;
    // Minecraft packet handling
    private PlayerHiderListener listener;
    // Reference to PL
    private ProtocolManager manager;
    // ID of the runnable task
    private int taskID = -1;
    // Dependencies
    private CombatTagApi combatAPI = null;

    int updateCooldown = 0;
    private long nextCall = 0;

    @Override
    public void onEnable() {
        instance = this;
        // Creates a Config
        this.saveDefaultConfig();
        config = new PlayerHiderConfiguration(getConfig());

        // Packet handling
        manager = ProtocolLibrary.getProtocolManager();
        listener = new PlayerHiderListener(this, config.getCombatTag(), config.getLoS());

        // Register listeners
        manager.addPacketListener(listener);
        getServer().getPluginManager().registerEvents(listener, this);
        listener.sneakdistance = config.getDistance();
        this.updateCooldown = config.getCooldown();
        
        // Handle Dependencies
        handleDependencies();
        
        try {
            taskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                public void run() {
                    updateAllPlayers();
                }
            }, 0, 1);
            if (taskID == -1) {
                throw new Exception("PlayerHider Update Task could not be scheduled");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private void handleDependencies() {
        combatAPI = handleCombatTag();
    }
    private CombatTagApi handleCombatTag() {
        CombatTagApi combatAPI = null;
        if(getServer().getPluginManager().getPlugin("CombatTag") != null) {
            combatAPI = new CombatTagApi((CombatTag)getServer().getPluginManager().getPlugin("CombatTag"));
        }
        return combatAPI;  
    }
    
    
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTask(taskID);
    }

    public void updateAllPlayers() {
        if (nextCall <= 0) {
           nextCall = updateCooldown;
            Collection<? extends Player> tempPlayers = Bukkit.getServer().getOnlinePlayers();
            for (Player player : tempPlayers) {
                try {
                    listener.updatePlayer(ProtocolLibrary.getProtocolManager(), player);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }
        nextCall--;
    }
    
    // Getters
    public static PlayerHider getInstance() {
        return instance;
    }
    
    public CombatTagApi getCombatAPI() {
        return combatAPI;
    }

    public PlayerHiderConfiguration getPlayerHiderConfig() {
        return config;
    }

}
