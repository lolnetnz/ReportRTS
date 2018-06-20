package com.nyancraft.reportrts.util;

import com.nyancraft.reportrts.data.NotificationType;
import nz.co.lolnet.Player;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class BungeeCord {
    
    private static List<byte[]> pendingRequests = new CopyOnWriteArrayList<>();
    
    private static boolean noPlayersOnline;
    
    private static String serverName;
    
    public static void processPendingRequests() {
        return;
    }
    
    public static void triggerAutoSync() {
        return;
    }
    
    public static boolean isServerEmpty() {
        return noPlayersOnline;
    }
    
    public static String getServerName() {
        return serverName;
    }
    
    public static String getServer() {
        return "";
    }
    
    public static void setServer(String server) {
        return;
    }
    
    public static void teleportUser(Player player, String targetServer, int ticketId) throws IOException {
        return;
    }
    
    public static void globalNotify(String message, int ticketId, NotificationType notifyType) throws IOException {
        return;
    }
    
    public static void notifyUser(UUID uuid, String message, int ticketId) throws IOException {
        return;
    }
    
    public static void handleNotify(byte[] bytes) {
        return;
    }
}
