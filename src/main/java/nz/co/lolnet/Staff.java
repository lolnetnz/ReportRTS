/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.json.simple.JSONObject;

/**
 *
 * @author James
 */
public class Staff {

    private static HashSet<UUID> staff = new HashSet<>();

    private static void update() {
        List<String> staffUUID = new ArrayList<>();
        for (UUID playerUUID : getAll()) {
            staffUUID.add(playerUUID.toString());
        }
        com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI api = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi();
        JSONObject dataToSend = new JSONObject();
        dataToSend.put("Command", "syncStaffList");
        dataToSend.put("StaffList", staffUUID);
        api.sendChannelMessage("ReportRTSBC", dataToSend.toJSONString());

    }
    
    protected static void update(HashSet<UUID> newSet)
    {
        staff = newSet;
    }
    
    public static boolean contains(UUID playerUUID) {
        boolean result = staff.contains(playerUUID);
        return result;
    }

    public static boolean add(UUID playerUUID) {
        boolean result = staff.add(playerUUID);
        update();

        return result;
    }

    public static boolean remove(UUID playerUUID) {
        boolean result = staff.remove(playerUUID);
        if (result) {
            update();
        }

        return result;
    }

    public static HashSet<UUID> getAll() {
        return staff;
    }

    public void clear() {
       return;
    }
}
