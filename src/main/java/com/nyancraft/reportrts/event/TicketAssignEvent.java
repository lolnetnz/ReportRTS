package com.nyancraft.reportrts.event;

import com.nyancraft.reportrts.data.Ticket;
import net.md_5.bungee.api.CommandSender;

/**
 * Event that is called when a moderator
 * assigns another staff member a ticket, /ticket assign.
 *
 */
public class TicketAssignEvent extends TicketEvent {

	private CommandSender sender;
	
	public TicketAssignEvent(Ticket ticket, CommandSender sender) {
		super(ticket);
		this.sender = sender;
	}
	
	/**
	 * The user that assigned the request to the other user.
	 * Get the assignee with Ticket's .getStaffName()
	 * @return CommandSender object of the user that assigned the ticket.
	 */
	public CommandSender getSender(){
		return sender;
	}

}
