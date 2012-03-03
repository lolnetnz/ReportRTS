package com.nyancraft.reportrts.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.nyancraft.reportrts.RTSDatabaseManager;
import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;

public class ReopenCommand implements CommandExecutor{

	private ReportRTS plugin;
	public ReopenCommand(ReportRTS plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!RTSPermissions.canCompleteRequests(sender)) return true;
		if(args.length == 0) return false;
		if(!RTSFunctions.isParsableToInt(args[0])) return false;

		if(!RTSDatabaseManager.setRequestStatus(Integer.parseInt(args[0]), sender.getName(), 0)){
			sender.sendMessage(ChatColor.RED + "[ReportRTS] Unable to reopen request #" + args[0] + ".");
			return true;
		}
		
		// For lack of a better way of doing it. SHOULD DO SOMETHING ABOUT THIS!
		plugin.requestMap.clear();
		RTSDatabaseManager.getOpenRequests();
		
		RTSFunctions.messageMods(ChatColor.GOLD + "[ReportRTS] " + sender.getName() + " has reopened request #" + args[0] + ".", sender.getServer().getOnlinePlayers());
		sender.sendMessage(ChatColor.GOLD + "[ReportRTS] Request #" + args[0] + " has been reopened.");
		
		return true;
	}
}