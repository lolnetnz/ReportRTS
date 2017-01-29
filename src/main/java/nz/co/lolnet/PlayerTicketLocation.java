/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import io.github.lxgaming.bungeeplayer.BungeePlayer;
import io.github.lxgaming.bungeeplayer.Location;

/**
 *
 * @author James
 */
public class PlayerTicketLocation {

    Player player;
    Location location = null;

    public PlayerTicketLocation(Player player) {
        this.player = player;
        if (BungeePlayer.getApi().getData().getPlayer(player) != null) {
            this.location = BungeePlayer.getApi().getData().getPlayer(player).getLocation();
        }

        if (this.location == null) {
            this.location = new Location();
        }
    }

    public String getWorld() {
        return player.getCurrentServerName() + "(" + location.getDimension() + ")";
    }

    public double getY() {
        return location.getY();
    }

    public double getX() {
        return location.getX();
    }

    public double getZ() {
        return location.getZ();
    }

    public double getYaw() {
        return location.getYaw();
    }

    public double getPitch() {
        return location.getPitch();
    }

    public String getServer() {
        return player.getCurrentServerName();
    }

}
