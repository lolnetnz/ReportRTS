package com.nyancraft.reportrts;

import com.nyancraft.reportrts.data.Ticket;
import com.nyancraft.reportrts.persistence.DataProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import nz.co.lolnet.reportrts.Player;
import nz.co.lolnet.reportrts.Staff;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.UUID;

public class RTSFunctions {
    
    private static ReportRTS plugin = ReportRTS.getPlugin();
    private static DataProvider data = plugin.getDataProvider();
    
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    
    /**
     * Join a String[] into a single string with a joiner
     */
    public static String implode(String[] array, String glue) {
        
        String out = "";
        
        if (array.length == 0) {
            return out;
        }
        
        for (String part : array) {
            if (part == null) continue;
            out = out + part + glue;
        }
        out = out.substring(0, out.length() - glue.length());
        
        return out;
    }
    
    public static String cleanUpSign(String[] lines) {
        
        String out = "";
        for (String part : lines) {
            if (!part.isEmpty()) out = out + part.trim() + " ";
        }
        return out;
    }
    
    /**
     * *
     * Message all online staff on the server
     *
     * @param message   - message to be displayed
     * @param playSound - boolean play sound or not.
     */
    
    public static void messageStaff(String message, boolean playSound) {
        messageStaff(message, playSound, false);
    }
    
    public static void messageStaff(String message, boolean playSound, boolean localBungeeOnly) {
        
        for (UUID uuid : Staff.getAll()) {
            
            if (localBungeeOnly) {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if (player != null) {
                    player.sendMessage(message);
                }
            } else {
                Player player = Player.getPlayer(uuid);
                if (player != null) {
                    player.sendMessage(message);
                }
            }
            
        }
        
        // Make sure Console sees this too!
        plugin.getProxy().getConsole().sendMessage(message);
        
    }
    
    /**
     * Synchronizes ticket data from the given ticket ID.
     *
     * @param ticketId - ticket ID to be synchronized.
     */
    public static boolean syncTicket(int ticketId) {
        
        Ticket ticket = ReportRTS.getPlugin().getDataProvider().getTicket(ticketId);
        
        plugin.tickets.put(ticketId, ticket);
        
        return plugin.tickets.get(ticketId).equals(ticket);
        
    }
    
    /**
     * Synchronizes everything.
     */
    public static void sync() {
        ReportRTS.getPlugin().tickets.clear();
        ReportRTS.getPlugin().notifications.clear();
        // Staff.clear();
        
        data.load();
        
        RTSFunctions.populateStaffMap();
    }
    
    public static void sync(boolean updateStaff) {
        ReportRTS.getPlugin().tickets.clear();
        ReportRTS.getPlugin().notifications.clear();
        if (updateStaff) {
            // Staff.clear();
        }
        
        data.load();
        
        if (updateStaff) {
            RTSFunctions.populateStaffMap();
        }
    }
    
    /**
     * Returns true if the person is online.
     *
     * @param uuid - UUID of player
     * @return boolean
     */
    public static boolean isUserOnline(UUID uuid) {
        return Player.getPlayer(uuid) != null;
    }
    
    /**
     * Get number of open request by the specified user.
     *
     * @param uuid - UUID of user that sent the command.
     * @return amount of open requests by a specific user
     */
    public static int getOpenTicketsByUser(UUID uuid) {
        int i = 0;
        for (Map.Entry<Integer, Ticket> entry : ReportRTS.getPlugin().tickets.entrySet()) {
            if (entry.getValue().getUUID().equals(uuid)) {
                i++;
            }
        }
        return i;
    }
    
    public static long checkTimeBetweenTickets(UUID uuid) {
        for (Map.Entry<Integer, Ticket> entry : ReportRTS.getPlugin().tickets.entrySet()) {
            if (entry.getValue().getUUID().equals(uuid)) {
                if (entry.getValue().getTimestamp() > ((System.currentTimeMillis() / 1000) - ReportRTS.getPlugin().ticketDelay)) {
                    return entry.getValue().getTimestamp() - (System.currentTimeMillis() / 1000 - ReportRTS.getPlugin().ticketDelay);
                }
            }
        }
        return 0;
    }
    
    public static String getTimeSpent(double start) {
        DecimalFormat decimal = new DecimalFormat("##.###");
        return decimal.format((System.nanoTime() - start) / 1000000);
    }
    
    public static String shortenMessage(String message) {
        if (message.length() >= 20) {
            message = message.substring(0, 20) + "...";
        }
        return message;
    }
    
    public static void populateStaffMap() {
        for (Player player : Player.getOnlinePlayers()) {
            if (RTSPermissions.isStaff(player)) {
                Staff.add(player.getUniqueId(), true);
            }
        }
    }
    
    /**
     * Check if the provided String is a number or not.
     *
     * @param number as a String
     * @return true if String is a number
     */
    public static boolean isNumber(String number) {
        return (number.matches("-?\\d+") && !(Long.parseLong((number)) <= 0L) && (Long.parseLong((number)) < Integer.MAX_VALUE));
    }
    
    /**
     * Separate text whenever a certain amount of words are reached. PS: If you
     * know how to stop Windows servers from printing the CR (Carriage Return)
     * character, please let me know!
     *
     * @param text that you want to separate.
     * @param when X amount of words have been displayed.
     * @return String with line separators.
     */
    public static String separateText(String text, int when) {
        int i = 0;
        StringBuilder message = new StringBuilder();
        for (String t : text.split(" ")) {
            if (i >= when) {
                i = 0;
                message.append(ReportRTS.getPlugin().lineSeparator);
            }
            message.append(t).append(" ");
            i++;
        }
        return message.toString().trim();
    }
    
    /**
     * Retrieves relative time for use in /ticket read.
     *
     * @param time Since specified time
     * @return String with relative time
     */
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        
        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }
        
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return ChatColor.GREEN + "just now" + ChatColor.GOLD;
        } else if (diff < 2 * MINUTE_MILLIS) {
            return ChatColor.GREEN + "1 minute ago" + ChatColor.GOLD; // a minute ago
        } else if (diff < 50 * MINUTE_MILLIS) {
            return "" + ChatColor.GREEN + diff / MINUTE_MILLIS + " min ago" + ChatColor.GOLD;
        } else if (diff < 90 * MINUTE_MILLIS) {
            return ChatColor.GREEN + "1 hour ago" + ChatColor.GOLD;
        } else if (diff < 24 * HOUR_MILLIS) {
            return "" + ChatColor.YELLOW + diff / HOUR_MILLIS + " hours ago" + ChatColor.GOLD;
        } else if (diff < 48 * HOUR_MILLIS) {
            return ChatColor.RED + "yesterday" + ChatColor.GOLD;
        } else {
            return "" + ChatColor.RED + diff / DAY_MILLIS + " days ago" + ChatColor.GOLD;
        }
    }
}
