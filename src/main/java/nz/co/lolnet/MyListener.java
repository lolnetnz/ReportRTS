/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI;
import com.nyancraft.reportrts.data.Comment;
import com.nyancraft.reportrts.data.Ticket;
import com.nyancraft.reportrts.event.TicketCloseEvent;
import com.nyancraft.reportrts.event.TicketOpenEvent;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONObject;

/**
 *
 * @author James
 */
public class MyListener implements Listener {

    @EventHandler
    public void onTicketClose(TicketCloseEvent event) {
        System.out.println("TicketCloseEvent");
        sendMessage(event.getTicket(), "TicketClosed");
    }

    @EventHandler
    public void onTicketOpen(TicketOpenEvent event) {
        System.out.println("TicketOpenEvent");
        sendMessage(event.getTicket(), "TicketOpen");
    }

    public void sendMessage(Ticket ticket, String eventName) {
        RedisBungeeAPI api = RedisBungee.getApi();
        JSONObject dataToSend = new JSONObject();

        dataToSend.put("Command", "sendToDiscord");
        dataToSend.put("Event", eventName);
        dataToSend.put("StaffName", ticket.getStaffName());
        dataToSend.put("Message", ticket.getMessage());
        List<String> commentList = new ArrayList<>();
        if (!ticket.getComments().isEmpty()) {
            for (Comment comment : ticket.getComments()) {
                commentList.add(comment.getComment());
            }
        }
        dataToSend.put("Comment", commentList);
        dataToSend.put("TicketID", ticket.getId());
        dataToSend.put("PlayerUUID", ticket.getUUID());
        dataToSend.put("Name", ticket.getName());
        dataToSend.put("ServerName", ticket.getServer());
        api.sendChannelMessage("ReportRTSBC", dataToSend.toJSONString());
    }

}
