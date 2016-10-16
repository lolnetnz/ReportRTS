/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.util.BungeeCord;
import java.net.InetSocketAddress;
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

/**
 *
 * @author James
 */
public class Player implements ProxiedPlayer {

    public static Player getPlayer(ProxiedPlayer player) {
        return new Player(player);
    }
// sender instanceof ProxiedPlayer has to be true

    public static Iterable<Player> getOnlinePlayers() {
        return players.values();
    }

    static HashMap<UUID, Player> players = new HashMap<>();

    ProxiedPlayer player;
    RedisPlayer playerR;

    public Player(ProxiedPlayer player) {
        this.player = player;
    }

    public Player(String playerName) {
        this.player = null;
        this.playerR = new RedisPlayer(playerName);
    }

    public Player(UUID playerUUID) {
        this.player = null;
        this.playerR = new RedisPlayer(playerUUID);
    }

    public static Player getPlayer(String playerName) {
        for (Player player1 : players.values()) {
            if (player1.getName().equals(playerName)) {
                return player1;

            }
        }
        if (ReportRTS.getPlugin().getProxy().getPlayer(playerName) != null)
        {
            return new Player(ReportRTS.getPlugin().getProxy().getPlayer(playerName));
        }
        return null;
    }

    public static Player getPlayer(UUID playerUUID) {
        Player player = players.get(playerUUID);
        if (player == null && ReportRTS.getPlugin().getProxy().getPlayer(playerUUID) != null)
        {
            player = new Player(ReportRTS.getPlugin().getProxy().getPlayer(playerUUID));
        }
        return player;

    }

    @Override
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UUID getUniqueId() {
        if (player != null) {
            return player.getUniqueId();
        } else if (playerR != null) {
            return playerR.getUniqueId();
        }
        return null;
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
        if (player != null) {
            return player.getName();
        } else if (playerR != null) {
            return playerR.getName();
        }
        return null;
    }

    @Override
    public void sendMessage(String string) {
        if (player != null) {
            player.sendMessage(string);
        } else if (playerR != null) {
            player.sendMessage(string);
        }

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
        if (player != null) {
            return player.hasPermission(string);
        } else if (playerR != null) {
            return playerR.hasPermission(string);
        }
        return false;
    }

    @Override
    public void setPermission(String string, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> getPermissions() {
        if (player != null) {
            return player.getPermissions();
        } else if (playerR != null) {
            return playerR.getPermissions();
        }
        return null;
    }

    String getCurrentServerName() {
        if (player != null) {
            return player.getServer().getInfo().getName();
        } else if (playerR != null) {
            return playerR.getCurrentServerName();
        }
        return null;
    }

    public boolean canSee(Player player) {
        return true;
    }

}
