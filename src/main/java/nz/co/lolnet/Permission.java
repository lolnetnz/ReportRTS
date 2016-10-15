/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.lolnet;

import net.md_5.bungee.api.CommandSender;

/**
 *
 * @author James
 */
public class Permission {

    public boolean playerHas(Player player, String permission) {
        return player.hasPermission(permission);
    }

    public boolean has(CommandSender sender, String permission) {
        return sender.hasPermission(permission);
    }
    
}
