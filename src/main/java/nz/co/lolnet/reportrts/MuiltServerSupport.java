package nz.co.lolnet.reportrts;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.ReportRTS;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author James
 */
public class MuiltServerSupport implements Listener {
    
    public static boolean enabled;
    
    public static void requestPermissionsUpdate(UUID playerUUID) {
        if (!enabled) {
            return;
        }
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Command", "requestPermissionsUpdate");
        jsonObject.addProperty("PlayerUUID", playerUUID.toString());
        RedisBungee.getApi().sendChannelMessage("ReportRTSBC", new Gson().toJson(jsonObject));
    }
    
    public static void syncDatabase() {
        if (!enabled) {
            return;
        }
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Command", "syncDatabase");
        RedisBungee.getApi().sendChannelMessage("ReportRTSBC", new Gson().toJson(jsonObject));
    }
    
    public void setup() {
        if (ProxyServer.getInstance().getPluginManager().getPlugin("RedisBungee") == null) {
            enabled = false;
            return;
        }
        
        enabled = true;
        RedisBungee.getApi().registerPubSubChannels("ReportRTSBC");
        ReportRTS.getPlugin().getProxy().getPluginManager().registerListener(ReportRTS.getPlugin(), this);
        ReportRTS.getPlugin().getProxy().getPluginManager().registerListener(ReportRTS.getPlugin(), new MyListener());
    }
    
    public static Collection<String> getListOfPlayersOnline() {
        return RedisBungee.getApi().getHumanPlayersOnline();
    }
    
    @EventHandler
    public void onPubSubMessageEvent(PubSubMessageEvent event) {
        if (!event.getChannel().equals("ReportRTSBC")) {
            return;
        }
        
        JsonObject object;
        try {
            object = new Gson().fromJson(event.getMessage(), JsonObject.class);
        } catch (JsonSyntaxException ex) {
            return;
        }
        
        
        String command = object.get("Command").getAsString();
        if (command == null) {
            return;
        }
        
        if (command.equals("sendMessageToPlayer")) {
            
            UUID playerUUID = UUID.fromString(object.get("PlayerUUID").getAsString());
            String message = object.get("Message").getAsString();
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
            if (player != null) {
                ProxyServer.getInstance().getPlayer(playerUUID).sendMessage(message);
            }
        } else if (command.equals("requestPermissionsUpdate")) {
            UUID playerUUID = UUID.fromString(object.get("PlayerUUID").getAsString());
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
            if (player != null) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Command", "requestPermissionsUpdateReply");
                jsonObject.addProperty("PlayerUUID", playerUUID.toString());
                JsonArray jsonArray = new JsonArray();
                for (String permission : player.getPermissions()) {
                    jsonArray.add(permission);
                }
                
                jsonObject.add("Permissions", jsonArray);
                RedisBungee.getApi().sendChannelMessage("ReportRTSBC", new Gson().toJson(jsonObject));
            }
        } else if (command.equals("requestPermissionsUpdateReply")) {
            UUID playerUUID = UUID.fromString(object.get("PlayerUUID").getAsString());
            if (RedisPlayer.redisPlayers.containsKey(playerUUID)) {
                RedisPlayer.redisPlayers.get(playerUUID).playerPermissions = new Gson().fromJson(object.get("Permissions"), new TypeToken<List<String>>() {
                }.getType());
            }
        } else if (command.equals("syncDatabase")) {
            RTSFunctions.sync(false);
        } else if (command.equals("syncStaffList")) {
            List<String> staffList = new Gson().fromJson(object.get("StaffList"), new TypeToken<List<String>>() {
            }.getType());
            for (String uuid : staffList) {
                Staff.add(UUID.fromString(uuid), false);
            }
        }
    }
}
