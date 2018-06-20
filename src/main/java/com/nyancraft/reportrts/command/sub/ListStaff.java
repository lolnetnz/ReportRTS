package com.nyancraft.reportrts.command.sub;

import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.util.Message;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nz.co.lolnet.Player;
import nz.co.lolnet.Staff;

import java.util.UUID;

public class ListStaff {
    
    private static ReportRTS plugin = ReportRTS.getPlugin();
    
    /**
     * Initial handling of the Staff sub-command.
     *
     * @param sender player that sent the command
     * @return true if command handled correctly
     */
    public static boolean handleCommand(CommandSender sender) {
        
        // TODO: Possible to-do. No cross server functionality!
        if (!RTSPermissions.canListStaff(sender)) return true;
        String staff = "";
        String separator = Message.staffListSeparator();
        
        for (UUID uuid : Staff.getAll()) {
            Player player = Player.getPlayer(uuid);
            if (player == null) return false;
            if (plugin.vanishSupport && sender instanceof ProxiedPlayer) {
                if (!(Player.getPlayer(sender.getName())).canSee(player)) continue;
            }
            staff = staff + player.getDisplayName() + separator;
        }
        if (staff.length() == 0) {
            sender.sendMessage(Message.staffListEmpty());
            return true;
        }
        staff = staff.substring(0, staff.length() - separator.length());
        
        sender.sendMessage(Message.staffListOnline(staff));
        return true;
    }
}
