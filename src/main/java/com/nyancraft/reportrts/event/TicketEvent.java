package com.nyancraft.reportrts.event;

import com.nyancraft.reportrts.data.Ticket;
import net.md_5.bungee.api.plugin.Event;

/**
 * Base class for all event regarding Tickets.
 */
public abstract class TicketEvent extends Event {
    
    /**
     * The ticket that the event regards.
     */
    private Ticket ticket;
    
    public TicketEvent(Ticket ticket) {
        this.ticket = ticket;
    }
    
    /**
     * Get the ticket that this event regards.
     * This will have the data of the ticket after
     * the event has happened.
     * <p>
     * i.e. a claim event will have the .getModName() equal
     * to the user claiming the ticket.
     *
     * @return a Ticket object with all the request data in it.
     */
    public Ticket getTicket() {
        return this.ticket;
    }
}
