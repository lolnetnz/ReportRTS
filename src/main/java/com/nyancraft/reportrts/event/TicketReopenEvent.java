package com.nyancraft.reportrts.event;

import net.md_5.bungee.api.CommandSender;

import com.nyancraft.reportrts.data.Ticket;

/**
 * Event that is called when staff
 * reopens a previously closed or held ticket.
 */
public class TicketReopenEvent extends TicketEvent {

	private final CommandSender sender;
	
	public TicketReopenEvent(Ticket ticket, CommandSender sender) {
		super(ticket);
		this.sender = sender;
	}
	
	/**
	 * The user that reopened the ticket.
	 * @return CommandSender object of the user that reopened the ticket
	 */
	public CommandSender getSender(){
		return sender;
	}

}
