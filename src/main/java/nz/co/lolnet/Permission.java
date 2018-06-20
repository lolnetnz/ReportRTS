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

    static boolean debug = false;

    public boolean playerHas(Player player, String permission) {
        if (player.getPermissions() == null) {
            return false;
        }
        if (player.getPermissions().contains("reportrts.*") || player.hasPermission("reportrts.*")) {
            return true;
        }
        
        return player.getPermissions().contains(permission) || player.hasPermission(permission);
    }

    public boolean has(CommandSender sender, String permission) {
        if (sender.getPermissions().contains("reportrts.*") || sender.hasPermission("reportrts.*")) {
            return true;
        }
        return sender.getPermissions().contains(permission) || sender.hasPermission(permission);
    }

}
