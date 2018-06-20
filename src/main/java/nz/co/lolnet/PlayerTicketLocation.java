/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

/**
 *
 * @author James
 */
public class PlayerTicketLocation {

    Player player;
    int dimension = 0;
    double x = 0;
    double y = 0;
    double z = 0;
    double yaw = 0;
    double pitch = 0;

    public PlayerTicketLocation(Player player) {
        this.player = player;
        try {
            if (io.github.lxgaming.bungeeplayer.BungeePlayer.getApi().getData().getPlayer(player) != null) {
                io.github.lxgaming.bungeeplayer.Location location = io.github.lxgaming.bungeeplayer.BungeePlayer.getApi().getData().getPlayer(player).getLocation();
                if (location != null) {
                    this.dimension = location.getDimension();
                    this.x = location.getX();
                    this.y = location.getY();
                    this.z = location.getZ();
                    this.yaw = location.getYaw();
                    this.pitch = location.getPitch();
                }
            }
        } catch (Exception e) { //This is here incaase the plugin is not installed
        }

    }

    public String getWorld() {
        return player.getCurrentServerName() + "(" + this.dimension + ")";
    }

    public double getY() {
        return this.y;
    }

    public double getX() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

    public String getServer() {
        return player.getCurrentServerName();
    }

}
