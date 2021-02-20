package me.filoghost.holographicdisplays.listener;

import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class SpawnListener implements Listener {

    private final NMSManager nmsManager;

    public SpawnListener(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (nmsManager.isNMSEntityBase(event.getEntity())) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (nmsManager.isNMSEntityBase(event.getEntity())) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onItemSpawn(ItemSpawnEvent event) {
        if (nmsManager.isNMSEntityBase(event.getEntity())) {
            event.setCancelled(false);
        }
    }

}
