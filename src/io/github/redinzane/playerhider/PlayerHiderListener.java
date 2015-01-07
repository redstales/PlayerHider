package io.github.redinzane.playerhider;


import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.WeakHashMap;

import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;



import io.github.redinzane.playerhider.packets.WrapperPlayServerEntityMetadata;

public class PlayerHiderListener extends PacketAdapter implements Listener {

    PlayerHider plugin;
    boolean combatTag;
    private static final int ENTITY_CROUCHED = 0x02;
    // Distance from which sneaking is visible
    double sneakdistance;
    boolean feature_LoS = false;

    // Whether or not a player can see autosneaking
    private static final String PERMISSION_HIDE_AUTO = "playerhider.hide.autosneak";

    // Last seen flag byte
    private Map<Player, Byte> flagByte = new WeakHashMap<Player, Byte>();
    
    public PlayerHiderListener(PlayerHider plugin, boolean combatTag, boolean los) {
        super(PacketAdapter.params(plugin, WrapperPlayServerEntityMetadata.TYPE));
        this.plugin = plugin;
        this.combatTag = combatTag;
        this.feature_LoS = los;
    }

    /**
     * Update the given player.
     * 
     * @param manager - reference to ProtocolLib
     * @param player - player to refresh.
     * @throws InvocationTargetException If we are unable to send a packet.
     */
    public void updatePlayer(ProtocolManager manager, Player player) throws InvocationTargetException {
        if (player.isDead()) {
            return;
        }
            
        Byte flag = flagByte.get(player);

        // It doesn't matter much
        if (flag == null) {
            flag = 0;
        }

        // Create the packet we will transmit
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata();
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, flag);

        packet.setEntityId(player.getEntityId());
        packet.setEntityMetadata(watcher.getWatchableObjects());

        // Broadcast the packet
        for (Player observer : manager.getEntityTrackers(player)) {
            manager.sendServerPacket(observer, packet.getHandle());
        }
    }

    @Override
    public void onPacketSending(PacketEvent event) {

        // If a user has this permission, he will see players normally
        if (event.getPlayer().hasPermission(PERMISSION_HIDE_AUTO)) {
            return;
        }

        // Initializing packets
        WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());
        Entity entity = packet.getEntity(event);

        if (entity instanceof Player) {
            Player target = (Player) entity;
            Player observer = event.getPlayer();
            double distance = 0;
            try {
                distance = target.getLocation().distance(observer.getLocation());
            } catch (IllegalArgumentException e) {
                return;
            }
            
            if(combatTag && plugin.getCombatAPI() != null) {
                if(plugin.getCombatAPI().isInCombat(target)) {
                    event.setPacket(packetManipulator(packet, target).getHandle());
                }
            } else if (distance >= sneakdistance) {
                event.setPacket(packetManipulatorCrouched(packet, target).getHandle());
            } else if (feature_LoS == true) {
                    if (observer.hasLineOfSight(target) == false) {
                        event.setPacket(packetManipulatorCrouched(packet, target).getHandle());
                    } else {
                        event.setPacket(packetManipulator(packet, target).getHandle());
                } 
            } else {
                    event.setPacket(packetManipulator(packet, target).getHandle());                
            }
        }
    }

    private WrapperPlayServerEntityMetadata packetManipulator(WrapperPlayServerEntityMetadata packet, Player player) {
        WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getEntityMetadata());
        Byte flag = watcher.getByte(0);

        if (flag != null) {
            // Store the last seen flag byte
            flagByte.put(player, flag);

            // Clone and update it
            packet = new WrapperPlayServerEntityMetadata(packet.getHandle().deepClone());
            watcher = new WrappedDataWatcher(packet.getEntityMetadata());
            watcher.setObject(0, (byte) (flag));
            
            return packet;
        } else {
            return packet;
        }
        
    }
    
    private WrapperPlayServerEntityMetadata packetManipulatorCrouched(WrapperPlayServerEntityMetadata packet, Player player) {
        WrappedDataWatcher watcher = new WrappedDataWatcher(packet.getEntityMetadata());
        Byte flag = watcher.getByte(0);

        if (flag != null) {
            // Store the last seen flag byte
            flagByte.put(player, flag);

            // Clone and update it
            packet = new WrapperPlayServerEntityMetadata(packet.getHandle().deepClone());
            watcher = new WrappedDataWatcher(packet.getEntityMetadata());
            watcher.setObject(0, (byte) (flag | ENTITY_CROUCHED));
            
            return packet;
        } else {
            return packet;
        }
    }
}
