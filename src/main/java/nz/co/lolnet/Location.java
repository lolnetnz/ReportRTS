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
public class Location {

    Player player;
    public Location(Player player) {
        this.player = player;
    }
    
    

    public String getWorld() {
        return player.getCurrentServerName();
    }

    public double getY() {
        return 0;
    }

    public double getX() {
        return 0;
    }

    public double getZ() {
        return 0;
    }

    public double getYaw() {
        return 0;
    }

    public double getPitch() {
        return 0;
    }
    
}
