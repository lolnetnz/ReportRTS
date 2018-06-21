package nz.co.lolnet.reportrts;

import io.github.lxgaming.bungeeplayer.BungeePlayer;
import io.github.lxgaming.bungeeplayer.api.IPlayer;
import net.md_5.bungee.api.ProxyServer;

/**
 * @author James
 */
public class PlayerTicketLocation {
    
    private final Player player;
    private int x;
    private int y;
    private int z;
    private int pitch;
    private int yaw;
    private int dimension;
    private String server;
    
    public PlayerTicketLocation(Player player) {
        this.player = player;
        populate();
    }
    
    private void populate() {
        if (ProxyServer.getInstance().getPluginManager().getPlugin("BungeePlayer") == null) {
            this.server = player.getCurrentServerName();
            return;
        }
        
        IPlayer bungeePlayer = BungeePlayer.getAPI().getPlayer(player.getUniqueId());
        if (bungeePlayer != null) {
            this.x = (int) bungeePlayer.getX();
            this.y = (int) bungeePlayer.getY();
            this.z = (int) bungeePlayer.getZ();
            this.pitch = (int) bungeePlayer.getPitch();
            this.yaw = (int) bungeePlayer.getYaw();
            this.dimension = bungeePlayer.getDimension();
            this.server = bungeePlayer.getServer();
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public int getPitch() {
        return pitch;
    }
    
    public int getYaw() {
        return yaw;
    }
    
    public int getDimension() {
        return dimension;
    }
    
    public String getServer() {
        return server;
    }
    
    public String getWorld() {
        return getServer() + " (" + getDimension() + ")";
    }
}