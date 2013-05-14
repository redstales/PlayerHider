package io.github.redinzane.playerhider;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerHiderListener implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        PlayerHider.onlinePlayers.add(event.getPlayer());
    }
}