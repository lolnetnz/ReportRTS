package com.nyancraft.reportrts.util;

import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.ReportRTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TabCompleteHelper implements Listener {

    private ReportRTS plugin;

    public TabCompleteHelper(ReportRTS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTab(TabCompleteEvent event) {
        if (!event.getSuggestions().isEmpty()) {
            return; //If suggestions for this command are handled by other plugin don't add any
        }

        String[] args = event.getCursor().split(" ");

        //Argument checker, DO NOT LEAVE THIS UNCOMMENTED IN PRODUCTION 
        /*int tempI = -1;
        for (String arg : args) {
        tempI++;
        System.out.println("Position: " + tempI + " | Actual Position: "
        + (tempI + 1) + " | Argument: " + arg);
        }*/
        /**
         * LOOK ABOVE *
         */
        if (args.length == 0) {
            return;
        }
        args[0] = args[0].replaceFirst("/", "");
        if (args.length == 1 && !event.getCursor().contains(" ")) {
            if (args[0].startsWith("com")) {
                event.getSuggestions().add("/" + "compass");
            }
            for (String command : plugin.commandMap.values()) {
                if (command.toLowerCase().startsWith(args[0].toLowerCase()) && !command.equalsIgnoreCase(args[0])) {
                    event.getSuggestions().add("/" + command);
                }
            }
        }

        if ((!args[0].equalsIgnoreCase(plugin.commandMap.get("readTicket")) && !args[0].equalsIgnoreCase(plugin.commandMap.get("closeTicket"))
                && !args[0].equalsIgnoreCase(plugin.commandMap.get("teleportToTicket")) && !args[0].equalsIgnoreCase(plugin.commandMap.get("holdTicket"))
                && !args[0].equalsIgnoreCase(plugin.commandMap.get("claimTicket")) && !args[0].equalsIgnoreCase(plugin.commandMap.get("unclaimTicket"))
                && !args[0].equalsIgnoreCase(plugin.commandMap.get("assignTicket")))) {
            // If you got here then the sub-command you tried to tab-complete does not support it.

            return;

        }
        if (args.length < 2 || args.length >= 2 && (!RTSFunctions.isNumber(args[1]) || (event.getSender() instanceof ProxiedPlayer && args[1].equalsIgnoreCase(((ProxiedPlayer) event.getSender()).getName()))) || plugin.tickets.size() < 1) {

            if (args.length < 2 || args[1].isEmpty()) {

                if (args.length >= 2) {
                    event.getSuggestions().add((args[1].equalsIgnoreCase(" ") ? " " : "") + plugin.tickets.keySet().toArray()[0].toString());
                } else if (plugin.tickets.keySet().toArray().length != 0) {
                    event.getSuggestions().add(plugin.tickets.keySet().toArray()[0].toString());
                }
                return;
            }
            return;
        }
        final Set<Integer> keys = plugin.tickets.keySet();
        int initialKey = Integer.parseInt(args[1]);
        if (initialKey <= 0) {
            return;
        }
        int prevKey = 0;
        for (int key : keys) {
            if (!Integer.toString(key).startsWith(args[1]) && !(key == prevKey) || initialKey > key) {
                continue;
            }
            if (initialKey == key) {
                for (int i : keys) {
                    if (i <= initialKey) {
                        continue;
                    }
                    prevKey = i;
                    break;
                }
                break;
            }
            prevKey = key;
            break;
        }
        if (prevKey == 0) {
            return;
        }
        List<String> response = new ArrayList<>();
        response.add(Integer.toString(prevKey));
        return;
    }

}
