package nz.co.lolnet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.imaginarycode.minecraft.redisbungee.RedisBungee;

import java.util.HashSet;
import java.util.UUID;

/**
 * @author James
 */
public class Staff {
    
    private static HashSet<UUID> staff = new HashSet<>();
    
    private static void updateStaffListToOtherServers() {
        JsonArray jsonArray = new JsonArray();
        for (UUID uniqueId : getAll()) {
            jsonArray.add(uniqueId.toString());
        }
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Command", "syncStaffList");
        jsonObject.add("StaffList", jsonArray);
        RedisBungee.getApi().sendChannelMessage("ReportRTSBC", new Gson().toJson(jsonObject));
        
    }
    
    protected static void update(HashSet<UUID> newSet) {
        staff = newSet;
    }
    
    public static boolean contains(UUID playerUUID) {
        return staff.contains(playerUUID);
    }
    
    public static boolean add(UUID playerUUID, boolean update) {
        boolean result = staff.add(playerUUID);
        if (update && MuiltServerSupport.enabled) {
            updateStaffListToOtherServers();
        }
        
        return result;
    }
    
    public static boolean remove(UUID playerUUID, boolean update) {
        boolean result = staff.remove(playerUUID);
        if (result && update && MuiltServerSupport.enabled) {
            updateStaffListToOtherServers();
        }
        
        return result;
    }
    
    public static HashSet<UUID> getAll() {
        return staff;
    }
    
    public static void clear() {
        return;
    }
}
