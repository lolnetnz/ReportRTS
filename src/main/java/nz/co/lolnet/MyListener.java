package nz.co.lolnet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.nyancraft.reportrts.data.Comment;
import com.nyancraft.reportrts.data.Ticket;
import com.nyancraft.reportrts.event.TicketCloseEvent;
import com.nyancraft.reportrts.event.TicketOpenEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Command", "sendToDiscord");
        jsonObject.addProperty("Event", eventName);
        jsonObject.addProperty("StaffName", ticket.getStaffName());
        jsonObject.addProperty("Message", ticket.getMessage());
        
        JsonArray jsonArray = new JsonArray();
        for (Comment comment : ticket.getComments()) {
            jsonArray.add(comment.getComment());
        }
        
        jsonObject.add("Comment", jsonArray);
        jsonObject.addProperty("TicketID", ticket.getId());
        jsonObject.addProperty("PlayerUUID", ticket.getUUID().toString());
        jsonObject.addProperty("Name", ticket.getName());
        jsonObject.addProperty("ServerName", ticket.getServer());
        RedisBungee.getApi().sendChannelMessage("ReportRTSBC", new Gson().toJson(jsonObject));
    }
}