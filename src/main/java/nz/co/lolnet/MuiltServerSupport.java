/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.util.BungeeCord;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author James
 */
public class MuiltServerSupport implements Listener {

    public static boolean enabled;

    public static void requestPermissionsUpdate(UUID playerUUID) {
        com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI api = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi();
        JSONObject dataToSend = new JSONObject();
        dataToSend.put("Command", "requestPermissionsUpdate");
        dataToSend.put("playerUUID", playerUUID);
        api.sendChannelMessage("ReportRTSBC", dataToSend.toJSONString());
    }

    public void setup() {
        RedisBungee.getApi().registerPubSubChannels("ReportRTSBC");
        ReportRTS.getPlugin().getProxy().getPluginManager().registerListener(ReportRTS.getPlugin(), this);
        enabled = true;
    }

    public static Collection<String> getListOfPlayersOnline() {
        return com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().getHumanPlayersOnline();
    }

    @EventHandler
    public void onPubSubMessageEvent(com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent event) {
        if (!event.getChannel().equals("ReportRTSBC")) {
            return;
        }
        JSONParser parser = new JSONParser();
        JSONObject object;
        try {
            object = (JSONObject) parser.parse(event.getMessage());
        } catch (ParseException ex) {
            Logger.getLogger(MuiltServerSupport.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        String command = (String) object.get("Command");
        if (command.equals("sendMessageToPlayer")) {
            UUID playerUUID = (UUID) object.get("PlayerUUID");
            String message = (String) object.get("Message");
            ProxiedPlayer player = net.md_5.bungee.BungeeCord.getInstance().getPlayer(playerUUID);
            if (player != null) {
                net.md_5.bungee.BungeeCord.getInstance().getPlayer(playerUUID).sendMessage(message);
            }
        } else if (command.equals("requestPermissionsUpdate")) {
            UUID playerUUID = (UUID) object.get("PlayerUUID");
            ProxiedPlayer player = net.md_5.bungee.BungeeCord.getInstance().getPlayer(playerUUID);
            if (player != null) {
                com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI api = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi();
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("Command", "requestPermissionsUpdateReply");
                dataToSend.put("playerUUID", playerUUID);
                dataToSend.put("Permissions", player.getPermissions());
                api.sendChannelMessage("ReportRTSBC", dataToSend.toJSONString());
            }
        } else if (command.equals("requestPermissionsUpdateReply")) {
            UUID playerUUID = (UUID) object.get("PlayerUUID");
            if (RedisPlayer.redisPlayers.containsKey(playerUUID))
            {
                RedisPlayer.redisPlayers.get(playerUUID).playerPermissions = (Collection<String>) object.get("Permissions");
            }
        }
    }
}
