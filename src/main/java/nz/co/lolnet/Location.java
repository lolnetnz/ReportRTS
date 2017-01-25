/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import io.github.lxgaming.bungeeplayer.BungeePlayer;



/**
 *
 * @author James
 */
public class Location {

    Player player;
    public Location(Player player) {
        this.player = player;
    }
    
    
    
    public String getWorld() {
        return player.getCurrentServerName() + "(" +BungeePlayer.getApi().getData().getPlayer(player).getLocation().getDimension() +")";
    }

    public double getY() {
        return BungeePlayer.getApi().getData().getPlayer(player).getLocation().getY();
    }

    public double getX() {
        return BungeePlayer.getApi().getData().getPlayer(player).getLocation().getX();
    }

    public double getZ() {
        return BungeePlayer.getApi().getData().getPlayer(player).getLocation().getZ();
    }

    public double getYaw() {
        return BungeePlayer.getApi().getData().getPlayer(player).getLocation().getYaw();
    }

    public double getPitch() {
        return BungeePlayer.getApi().getData().getPlayer(player).getLocation().getPitch();
    }
    
}
