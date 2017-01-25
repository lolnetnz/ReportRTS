package com.nyancraft.reportrts;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.nyancraft.reportrts.util.BungeeCord;
import com.nyancraft.reportrts.util.Message;
import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import nz.co.lolnet.MuiltServerSupport;
import nz.co.lolnet.Player;
import nz.co.lolnet.RedisPlayer;

public class RTSListener implements Listener {
    
    private final ReportRTS plugin;
    
    public RTSListener(ReportRTS plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PostLoginEvent event) {
        if (MuiltServerSupport.enabled) {
            MuiltServerSupport.requestPermissionsUpdate(event.getPlayer().getUniqueId());
        }
        int createUser = plugin.getDataProvider().createUser(event.getPlayer().getUniqueId());
        BungeeCord.triggerAutoSync();
        BungeeCord.processPendingRequests();
        
        if (!plugin.notifications.isEmpty()) {
            Map<Integer, UUID> keys = new HashMap<>();
            for (Map.Entry<Integer, UUID> entry : plugin.notifications.entrySet()) {
                if (entry.getValue().equals(event.getPlayer().getUniqueId())) {
                    plugin.getProxy().getScheduler().schedule(plugin, new LoginTask(plugin, event.getPlayer().getUniqueId(), entry), 5, TimeUnit.SECONDS);
                    keys.put(entry.getKey(), entry.getValue());
                }
            }
            if (keys.size() >= 2) {
                event.getPlayer().sendMessage(Message.ticketCloseOfflineMulti(keys.size(), (plugin.legacyCommands ? plugin.commandMap.get("readTicket") + " self" : "ticket " + plugin.commandMap.get("readTicket") + " self")));
                for (Map.Entry<Integer, UUID> entry : keys.entrySet()) {
                    plugin.getProxy().getScheduler().schedule(plugin, new LoginTask(plugin, event.getPlayer().getUniqueId(), entry), 5, TimeUnit.SECONDS);
                }
            } else {
                for (Map.Entry<Integer, UUID> entry : keys.entrySet()) {
                    plugin.getProxy().getScheduler().schedule(plugin, new LoginTask(plugin, event.getPlayer().getUniqueId(), entry), 5, TimeUnit.SECONDS);
                }
            }
            
            for (int key : keys.keySet()) {
                plugin.notifications.remove(key);
            }
            
            if (!plugin.teleportMap.isEmpty()) {
                Integer g = plugin.teleportMap.get(event.getPlayer().getUniqueId());
                if (g != null) {
                    event.getPlayer().sendMessage(Message.teleportXServer("/" + (plugin.legacyCommands ? plugin.commandMap.get("teleportToTicket") : "ticket " + plugin.commandMap.get("teleportToTicket")) + " " + Integer.toString(g)));
                    plugin.getProxy().getPluginManager().dispatchCommand(event.getPlayer(), "ticket " + plugin.commandMap.get("teleportToTicket") + " " + Integer.toString(g));
                    plugin.teleportMap.remove(event.getPlayer().getUniqueId());
                }
            }
        }
        
        if (!RTSPermissions.isStaff(Player.getPlayer(event.getPlayer()))) {
            return;
        }
        
        if (!plugin.staff.contains(event.getPlayer().getUniqueId())) {
            plugin.staff.add(event.getPlayer().getUniqueId());
        }
        
        int openTickets = plugin.tickets.size();
        
        if (openTickets < 1 && !plugin.hideNotification) {
            event.getPlayer().sendMessage(Message.ticketReadNone());
        }
        
        if (openTickets > 0) {
            event.getPlayer().sendMessage(Message.ticketUnresolved(openTickets, (plugin.legacyCommands ? plugin.commandMap.get("readTicket") : "ticket " + plugin.commandMap.get("readTicket"))));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        BungeeCord.triggerAutoSync();
        if (plugin.staff.contains(event.getPlayer().getUniqueId())) {
            plugin.staff.remove(event.getPlayer().getUniqueId());
        }
        RedisPlayer.removePlayer(event.getPlayer().getUniqueId());
    }
}
