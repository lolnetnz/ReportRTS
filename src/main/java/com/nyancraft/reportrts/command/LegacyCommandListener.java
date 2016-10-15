package com.nyancraft.reportrts.command;

import com.nyancraft.reportrts.ReportRTS;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LegacyCommandListener implements Listener {

    private String readTicket;
    private String openTicket;
    private String closeTicket;
    private String reopenTicket;
    private String claimTicket;
    private String unclaimTicket;
    private String holdTicket;
    private String teleportToTicket;
    private String broadcastToStaff;
    private String listStaff;
    private String commentTicket;

    public LegacyCommandListener(String readTicket, String openTicket, String closeTicket, String reopenTicket, String claimTicket, String unclaimTicket, String holdTicket, String teleportToTicket, String broadcastToStaff, String listStaff, String commentTicket) {
        this.readTicket = readTicket;
        this.openTicket = openTicket;
        this.closeTicket = closeTicket;
        this.reopenTicket = reopenTicket;
        this.claimTicket = claimTicket;
        this.unclaimTicket = unclaimTicket;
        this.holdTicket = holdTicket;
        this.teleportToTicket = teleportToTicket;
        this.broadcastToStaff = broadcastToStaff;
        this.listStaff = listStaff;
        this.commentTicket = commentTicket;
    }
    
    @EventHandler
    public void onPlayerCommandPreprocessEvent(ChatEvent event) {
        if(event.isCancelled() || !event.isCommand() || event.getMessage().length() < 1) return;
        // Please don't use this unless you have to. :(
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + readTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + openTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + closeTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + reopenTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + claimTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + unclaimTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + holdTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + teleportToTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + broadcastToStaff)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + listStaff)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
        if(event.getMessage().split(" ")[0].equalsIgnoreCase("/" + commentTicket)) {
            ReportRTS.getPlugin().getProxy().getPluginManager().dispatchCommand((CommandSender) event.getSender(), "ticket " + event.getMessage().replaceFirst("/", ""));
            event.setCancelled(true);
        }
    }
}
