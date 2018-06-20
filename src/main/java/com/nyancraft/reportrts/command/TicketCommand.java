package com.nyancraft.reportrts.command;

import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.command.sub.AssignTicket;
import com.nyancraft.reportrts.command.sub.BroadcastMessage;
import com.nyancraft.reportrts.command.sub.ClaimTicket;
import com.nyancraft.reportrts.command.sub.CloseTicket;
import com.nyancraft.reportrts.command.sub.CommentTicket;
import com.nyancraft.reportrts.command.sub.HoldTicket;
import com.nyancraft.reportrts.command.sub.ListStaff;
import com.nyancraft.reportrts.command.sub.OpenTicket;
import com.nyancraft.reportrts.command.sub.ReadTicket;
import com.nyancraft.reportrts.command.sub.ReopenTicket;
import com.nyancraft.reportrts.command.sub.UnclaimTicket;
import com.nyancraft.reportrts.util.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class TicketCommand extends Command {
    
    private ReportRTS plugin;
    
    public TicketCommand(ReportRTS plugin) {
        super("ticket");
        this.plugin = plugin;
    }
    
    @Override
    public void execute(CommandSender cs, String[] strings) {
        boolean onCommand = onCommand(cs, strings);
        if (!onCommand) {
            cs.sendMessage(ChatColor.RED + "Command requires more words");
        }
    }
    
    public boolean onCommand(CommandSender sender, String[] args) {
        
        /** Argument checker, DO NOT LEAVE THIS UNCOMMENTED IN PRODUCTION *
         int i = -1;
         for(String arg : args) {
         i++;
         System.out.println("Position: " + i + " | Actual Position: " + (i + 1) + " | Argument: " + arg);
         }
         /** LOOK ABOVE **/
        
        if (args.length < 1) return false;
        
        double start = 0;
        if (plugin.debugMode) start = System.nanoTime();
        boolean result;
        
        // For lack of a better way. Please enlighten me if you have a suggestion to improve anything below.
        
        /** Read a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("readTicket"))) {
            result = ReadTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Open a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("openTicket"))) {
            result = OpenTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Close a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("closeTicket"))) {
            result = CloseTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Reopen a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("reopenTicket"))) {
            result = ReopenTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Claim a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("claimTicket"))) {
            result = ClaimTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Unclaim a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("unclaimTicket"))) {
            result = UnclaimTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Hold a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("holdTicket"))) {
            result = HoldTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Broadcast to staff. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("broadcastToStaff"))) {
            result = BroadcastMessage.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** List staff. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("listStaff"))) {
            result = ListStaff.handleCommand(sender);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Assign a ticket **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("assignTicket"))) {
            result = AssignTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        /** Comment on a ticket. **/
        if (args[0].equalsIgnoreCase(plugin.commandMap.get("commentTicket"))) {
            result = CommentTicket.handleCommand(sender, args);
            if (plugin.debugMode)
                Message.debug(sender.getName(), this.getClass().getSimpleName(), start, "ticket", args);
            return result;
        }
        
        return true;
    }
}