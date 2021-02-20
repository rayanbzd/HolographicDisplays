/*
 * Copyright (C) filoghost and contributors
 *
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package me.filoghost.holographicdisplays.listener;

import me.filoghost.fcommons.logging.Log;
import me.filoghost.holographicdisplays.HolographicDisplays;
import me.filoghost.holographicdisplays.nms.interfaces.NMSManager;
import me.filoghost.holographicdisplays.nms.interfaces.entity.NMSEntityBase;
import me.filoghost.holographicdisplays.object.APIHologram;
import me.filoghost.holographicdisplays.object.line.TouchSlimeLineImpl;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class MainListener implements Listener {
    
    private final NMSManager nmsManager;
    
    private final Map<Player, Long> anticlickSpam = new HashMap<>();
    
    public MainListener(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSlimeInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.SLIME) {
            return;
        }
            
        Player clicker = event.getPlayer();
        if (clicker.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        
        NMSEntityBase entityBase = nmsManager.getNMSEntityBase(event.getRightClicked());
        if (entityBase == null || !(entityBase.getHologramLine() instanceof TouchSlimeLineImpl)) {
            return;
        }
        
        TouchSlimeLineImpl touchSlime = (TouchSlimeLineImpl) entityBase.getHologramLine();
        if (touchSlime.getTouchablePiece().getTouchHandler() == null || !touchSlime.getParent().getVisibilityManager().isVisibleTo(clicker)) {
            return;
        }
        
        Long lastClick = anticlickSpam.get(clicker);
        if (lastClick != null && System.currentTimeMillis() - lastClick < 100) {
            return;
        }
        
        anticlickSpam.put(event.getPlayer(), System.currentTimeMillis());
        
        try {
            touchSlime.getTouchablePiece().getTouchHandler().onTouch(event.getPlayer());
        } catch (Throwable t) {
            Plugin plugin = touchSlime.getParent() instanceof APIHologram ? ((APIHologram) touchSlime.getParent()).getOwner() : HolographicDisplays.getInstance();
            Log.warning("The plugin " + plugin.getName() + " generated an exception when the player " + event.getPlayer().getName() + " touched a hologram.", t);
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        anticlickSpam.remove(event.getPlayer());
    }
    
}
