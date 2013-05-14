package io.github.redinzane.playerhider;

import java.util.List;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
 
public final class PlayerHider extends JavaPlugin {
	
	protected static List<Player> onlinePlayers = new Vector<Player>();
	
	@Override
    public void onEnable(){
		getLogger().info("onEnable has been invoked!");
		
		Player[] temporaryPlayerArray;
		temporaryPlayerArray = Bukkit.getServer().getOnlinePlayers();
		for(Player player: temporaryPlayerArray)
		{
			onlinePlayers.add(player);
		}
    }
 
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    	
    	getLogger().info("onDisable has been invoked!");
    	
    }
	
}