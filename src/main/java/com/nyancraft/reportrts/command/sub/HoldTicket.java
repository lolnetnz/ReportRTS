package com.nyancraft.reportrts.command.sub;


import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.data.NotificationType;
import com.nyancraft.reportrts.data.User;
import com.nyancraft.reportrts.event.TicketHoldEvent;
import com.nyancraft.reportrts.persistence.DataProvider;
import com.nyancraft.reportrts.util.BungeeCord;
import com.nyancraft.reportrts.util.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nz.co.lolnet.Player;

import java.io.IOException;

public class HoldTicket {
    
    private static ReportRTS plugin = ReportRTS.getPlugin();
    private static DataProvider data = plugin.getDataProvider();
    
    /**
     * Initial handling of the Hold sub-command.
     *
     * @param sender player that sent the command
     * @param args   arguments
     * @return true if command handled correctly
     */
    public static boolean handleCommand(CommandSender sender, String[] args) {
        
        if (!RTSPermissions.canPutTicketOnHold(sender) || args.length < 2 || !RTSFunctions.isNumber(args[1]))
            return false;
        
        args[0] = "";
        int ticketId = Integer.parseInt(args[1]);
        
        String reason = RTSFunctions.implode(args, " ").trim();
        
        if (reason.length() <= args[1].length()) {
            reason = "No reason provided.";
        } else {
            reason = reason.substring(args[1].length()).trim();
        }
        
        User user = sender instanceof ProxiedPlayer ? data.getUser((Player.getPlayer(sender.getName())).getUniqueId(), 0, true) : data.getConsole();
        if (user.getUsername() == null) {
            sender.sendMessage(Message.error("user.getUsername() returned NULL! Are you using plugins to modify names?"));
            return true;
        }
        
        if (data.setTicketStatus(ticketId, user.getUuid(), sender.getName(), 2, false, System.currentTimeMillis() / 1000) < 1) {
            sender.sendMessage(Message.error("Unable to put ticket #" + args[0] + " on hold."));
            return true;
        }
        
        if (plugin.tickets.containsKey(ticketId)) {
            
            Player player = Player.getPlayer(plugin.tickets.get(ticketId).getUUID());
            if (player != null) {
                player.sendMessage(Message.ticketHoldUser(sender.getName(), ticketId));
                player.sendMessage(Message.ticketHoldText(plugin.tickets.get(ticketId).getMessage(), reason.trim()));
            }
            
            plugin.getProxy().getPluginManager().callEvent(new TicketHoldEvent(plugin.tickets.get(ticketId), reason, sender));
            
            plugin.tickets.remove(ticketId);
        }
        
        try {
            BungeeCord.globalNotify(Message.ticketHold(args[1], sender.getName()), ticketId, NotificationType.HOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RTSFunctions.messageStaff(Message.ticketHold(args[1], sender.getName()), false);
        return true;
    }
}