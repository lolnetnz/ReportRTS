package com.nyancraft.reportrts.command.sub;

import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.data.Comment;
import com.nyancraft.reportrts.data.Ticket;
import com.nyancraft.reportrts.data.NotificationType;
import com.nyancraft.reportrts.data.User;
import com.nyancraft.reportrts.event.TicketOpenEvent;
import com.nyancraft.reportrts.persistence.DataProvider;
import com.nyancraft.reportrts.util.BungeeCord;
import com.nyancraft.reportrts.util.Message;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nz.co.lolnet.PlayerTicketLocation;
import nz.co.lolnet.Player;
import sun.security.pkcs11.P11TlsKeyMaterialGenerator;

public class OpenTicket {

    private static ReportRTS plugin = ReportRTS.getPlugin();
    private static DataProvider data = plugin.getDataProvider();

    /**
     * Initial handling of the Open sub-command.
     * @param sender player that sent the command
     * @param args arguments
     * @return true if command handled correctly
     */
    public static boolean handleCommand(CommandSender sender, String[] args) {

        if(!RTSPermissions.canOpenTicket(sender)) return true;
        //if(args.length < 2) return false;

        // Check if ticket message is too short.
        if(plugin.ticketMinimumWords > (args.length - 1)) {
            sender.sendMessage(Message.ticketTooShort(plugin.ticketMinimumWords));
            return true;
        }

        // Store these variables, we're gonna need them.
        User user = new User();
        user.setUsername(sender.getName());
        PlayerTicketLocation location = null;
        if(!(sender instanceof ProxiedPlayer)) {
            // Sender is more than likely Console.
            user = data.getConsole();
            location = new PlayerTicketLocation(Player.getPlayer(sender.getName()));
        }
        else {
            // Sender is a Player.
            ProxiedPlayer player = (ProxiedPlayer) sender;
            user = data.getUser(player.getUniqueId(), 0, true);
            location = new PlayerTicketLocation(Player.getPlayer(player));
        }

        // The user is banned and can not create a ticket.
        if(user.getBanned()) {
            sender.sendMessage(Message.errorBanned());
            return true;
        }

        if(RTSFunctions.getOpenTicketsByUser(user.getUuid()) >= plugin.maxTickets && !RTSPermissions.canBypassLimit(sender)) {
            sender.sendMessage(Message.ticketTooMany());
            return true;
        }

        // Check if the sender can open another ticket yet.
        if(plugin.ticketDelay > 0) {
            if(!RTSPermissions.canBypassLimit(sender)){
                long timeBetweenRequest = RTSFunctions.checkTimeBetweenTickets(user.getUuid());
                if(timeBetweenRequest > 0) {
                    sender.sendMessage(Message.ticketTooFast(timeBetweenRequest));
                    return true;
                }
            }
        }

        args[0] = null;
        String message = RTSFunctions.implode(args, " ");

        // Prevent duplicate requests by comparing UUID and message to other currently open requests.
        if(plugin.ticketPreventDuplicate) {
            for(Map.Entry<Integer, Ticket> entry : plugin.tickets.entrySet()){
                if(!entry.getValue().getUUID().equals(user.getUuid())) continue;
                if(!entry.getValue().getMessage().equalsIgnoreCase(message)) continue;
                sender.sendMessage(Message.ticketDuplicate());
                return true;
            }
        }

        // Create a ticket and store the ticket ID.
        int ticketId = data.createTicket(user, location, message);
        // If less than 1, then the creation of the ticket failed.
        if(ticketId < 1) {
            sender.sendMessage(Message.error("Ticket could not be opened."));
            return true;
        }

        sender.sendMessage(Message.ticketOpenUser(Integer.toString(ticketId)));
        plugin.getLogger().log(Level.INFO, "" + user.getUsername() + " filed a request.");

        // Notify staff members about the new request.
        if(plugin.notifyStaffOnNewRequest) {
            try {
                // Attempt to notify all servers connected to BungeeCord that run ReportRTS.
                BungeeCord.globalNotify(Message.ticketOpen(user.getUsername(), Integer.toString(ticketId)), ticketId, NotificationType.NEW);
            } catch(IOException e) {
                e.printStackTrace();
            }
            RTSFunctions.messageStaff(Message.ticketOpen(user.getUsername(), Integer.toString(ticketId)), true);
        }

        Ticket ticket;
        ticket = new Ticket(user.getUsername(), user.getUuid(), ticketId, System.currentTimeMillis()/1000, message, 0, location , BungeeCord.getServer());
        plugin.getProxy().getPluginManager().callEvent(new TicketOpenEvent(ticket));
        plugin.tickets.put(ticketId, ticket);

        return true;
    }
}