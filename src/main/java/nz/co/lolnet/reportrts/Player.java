package nz.co.lolnet.reportrts;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.nyancraft.reportrts.ReportRTS;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.SkinConfiguration;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author James
 */
public class Player implements ProxiedPlayer {
    
    static boolean redisNotFound = false;
    private static HashMap<UUID, Player> players = new HashMap<>();
    
    private static void resyncPlayerList() {
        HashMap<UUID, Player> players = new HashMap<>();
        
        if (MuiltServerSupport.enabled) {
            for (UUID uniqueId : RedisBungee.getApi().getPlayersOnline()) {
                players.put(uniqueId, getPlayer(uniqueId));
            }
        } else {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                players.put(player.getUniqueId(), getPlayer(player.getUniqueId()));
            }
        }
        
        Player.players = players;
    }
    
    ProxiedPlayer player;
    RedisPlayer playerR;
    
    public static Player getPlayer(ProxiedPlayer player) {
        String playerName = player.getName();
        for (Player player1 : players.values()) {
            if (player1.getName().equals(playerName) && (player1.player != null)) {
                return player1;
            }
        }
        
        return new Player(player);
    }
// sender instanceof ProxiedPlayer has to be true
    
    public static Collection<Player> getOnlinePlayers() {
        resyncPlayerList();
        return players.values();
    }
    
    public Player(ProxiedPlayer player) {
        this.player = player;
    }
    
    public Player(String playerName) {
        this.player = ProxyServer.getInstance().getPlayer(playerName);
        
        if (MuiltServerSupport.enabled) {
            this.playerR = new RedisPlayer(playerName);
        }
    }
    
    public Player(UUID playerUUID) {
        this.player = ProxyServer.getInstance().getPlayer(playerUUID);
        
        if (MuiltServerSupport.enabled) {
            this.playerR = new RedisPlayer(playerUUID);
        }
    }
    
    public static Player getPlayer(String playerName) {
        for (Player player1 : players.values()) {
            if (player1.getName().equals(playerName)) {
                return player1;
            }
        }
        
        if (ReportRTS.getPlugin().getProxy().getPlayer(playerName) != null) {
            return new Player(ReportRTS.getPlugin().getProxy().getPlayer(playerName));
        }
        
        return null;
    }
    
    public static Player getPlayer(UUID uniqueId) {
        Player player = players.getOrDefault(uniqueId, new Player(uniqueId));
        player.getPermissions();
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
    
    @Deprecated
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
            playerR.sendMessage(string);
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
        if (Permission.debug) {
            return true;
        }
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
    public boolean isConnected() {
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
    
    /*
    private boolean isOnline() {
        return RedisBungee.getApi().getPlayersOnline().contains(getUniqueId());
    }
     */
    
    @Override
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
    
    }
    
    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
    
    }
    
    @Override
    public byte getViewDistance() {
        return 0;
    }
    
    @Override
    public ChatMode getChatMode() {
        return null;
    }
    
    @Override
    public boolean hasChatColors() {
        return false;
    }
    
    @Override
    public SkinConfiguration getSkinParts() {
        return null;
    }
    
    @Override
    public MainHand getMainHand() {
        return null;
    }
    
    @Override
    public Scoreboard getScoreboard() {
        return null;
    }
}
