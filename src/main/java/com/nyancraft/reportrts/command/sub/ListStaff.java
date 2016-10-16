package com.nyancraft.reportrts.command.sub;

import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.util.Message;

import java.util.UUID;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nz.co.lolnet.Player;

public class ListStaff {

    private static ReportRTS plugin = ReportRTS.getPlugin();

    /**
     * Initial handling of the Staff sub-command.
     * @param sender player that sent the command
     * @return true if command handled correctly
     */
    public static boolean handleCommand(CommandSender sender) {

        // TODO: Possible to-do. No cross server functionality!
        if(!RTSPermissions.canListStaff(sender)) return true;
        StringBuilder staff = new StringBuilder();
        String separator = Message.staffListSeparator();

        for(UUID uuid : plugin.staff) {
            Player player = Player.getPlayer(uuid);
            if(player == null) return false;
            if(plugin.vanishSupport && sender instanceof ProxiedPlayer) {
                if(!(Player.getPlayer(sender.getName())).canSee(player)) continue;
            }
            staff.append(player.getDisplayName());
            staff.append(separator);
        }
        if(staff.length() == 0) {
            sender.sendMessage(Message.staffListEmpty());
            return true;
        }
        String staffString = staff.substring(0, staff.length() - separator.length());

        sender.sendMessage(Message.staffListOnline(staffString));
        return true;
    }
}
