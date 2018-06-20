/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import org.json.simple.JSONObject;

/**
 *
 * @author James
 */
public class RedisPlayer implements ProxiedPlayer {

    public static HashMap<UUID, RedisPlayer> redisPlayers = new HashMap<>();
    String playerName;
    UUID playerUUID;
    Collection<String> playerPermissions;

    public RedisPlayer(String playerName) {
        this.playerName = playerName;
        this.playerUUID = getUniqueId();
        redisPlayers.put(playerUUID, this);
    }

    public RedisPlayer(UUID playerUUID) {
        this.playerUUID = playerUUID;
        playerName = getName();
        redisPlayers.put(playerUUID, this);
    }

    public static void removePlayer(UUID playerUUID) {
        redisPlayers.remove(playerUUID);
    }

    public boolean isOnline() {
        return com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().isPlayerOnline(playerUUID);
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    @Override
    public void setDisplayName(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(ChatMessageType cmt, BaseComponent... bcs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(ChatMessageType cmt, BaseComponent bc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connect(ServerInfo si) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connect(ServerInfo si, Callback<Boolean> clbck) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Server getServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getPing() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendData(String string, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PendingConnection getPendingConnection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void chat(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ServerInfo getReconnectServer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setReconnectServer(ServerInfo si) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getUUID() {
        return getUniqueId().toString();
    }

    @Override
    public UUID getUniqueId() {
        if (playerUUID == null && playerName != null && isOnline()) {
            this.playerUUID = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().getUuidFromName(playerName);
        }
        return playerUUID;
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTabHeader(BaseComponent bc, BaseComponent bc1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setTabHeader(BaseComponent[] bcs, BaseComponent[] bcs1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resetTabHeader() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendTitle(Title title) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isForgeUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, String> getModList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InetSocketAddress getAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect(BaseComponent... bcs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect(BaseComponent bc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        if (playerName == null && playerUUID != null && isOnline()) {
            this.playerName = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().getNameFromUuid(playerUUID);
        }
        return playerName;
    }

    @Override
    public void sendMessage(String string) {
        com.imaginarycode.minecraft.redisbungee.RedisBungeeAPI api = com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi();
        JSONObject dataToSend = new JSONObject();
        dataToSend.put("Command", "sendMessageToPlayer");
        dataToSend.put("playerName", playerName);
        dataToSend.put("PlayerUUID", playerUUID.toString());
        dataToSend.put("Message", string);
        api.sendChannelMessage("ReportRTSBC", dataToSend.toJSONString());

    }

    @Override
    public void sendMessages(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(BaseComponent... bcs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(BaseComponent bc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> getGroups() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addGroups(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeGroups(String... strings) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasPermission(String string) {
        return playerPermissions.contains(string);
    }

    @Override
    public void setPermission(String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean isConnected() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> getPermissions() {
        if (playerPermissions == null) {
            updatePermissions();
        }
        return playerPermissions;
    }

    private void updatePermissions() {
        MuiltServerSupport.requestPermissionsUpdate(playerUUID);
    }

    String getCurrentServerName() {
        for (String serverName : com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().getServerToPlayers().keySet()) {
            if (com.imaginarycode.minecraft.redisbungee.RedisBungee.getApi().getServerToPlayers().get(serverName).contains(playerUUID)) {
                return serverName;
            }
        }
        return null;
    }

}
