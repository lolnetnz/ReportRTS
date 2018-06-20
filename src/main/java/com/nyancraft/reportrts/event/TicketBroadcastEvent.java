package com.nyancraft.reportrts.event;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Event;

/**
 * Event that is called when staff broadcasts a message.
 */
public class TicketBroadcastEvent extends Event {
    
    String message;
    CommandSender sender;
    
    public TicketBroadcastEvent(CommandSender sender, String message) {
        this.sender = sender;
        this.message = message;
    }
    
    /**
     * Get the broadcast message.
     *
     * @return a String with the broadcast message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Get the sender of the broadcast message.
     *
     * @return a CommandSender object of the user that sent the broadcast.
     */
    public CommandSender getSender() {
        return sender;
    }
    
}
