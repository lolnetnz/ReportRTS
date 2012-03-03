package com.nyancraft.reportrts.command;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nyancraft.reportrts.RTSDatabaseManager;
import com.nyancraft.reportrts.RTSFunctions;
import com.nyancraft.reportrts.RTSPermissions;
import com.nyancraft.reportrts.ReportRTS;
import com.nyancraft.reportrts.data.HelpRequest;

public class TeleportCommand implements CommandExecutor {
	
	private ReportRTS plugin;
	
	public TeleportCommand(ReportRTS plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("[ReportRTS] You need to be player to teleport.");
			return true;
		}
		if(!RTSPermissions.canTeleport(sender)) return true;
		try{
			if(!RTSFunctions.isParsableToInt(args[0])) return false;
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
		
		Player player = (Player) sender;
		
		if(!plugin.requestMap.containsKey(Integer.parseInt(args[0]))){
			
			ResultSet rs = RTSDatabaseManager.db.query("SELECT `x`, `y`, `z`, `world` FROM reportrts_request WHERE `id` = '" + Integer.parseInt(args[0]) + "' LIMIT 1");
			Location location = null;
			try {
				if(!rs.isBeforeFirst()){
					sender.sendMessage(ChatColor.RED + "[ReportRTS] That request ID does not exist.");
					return true;
				}
				location = new Location(player.getServer().getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z"));
				rs.close();
			} catch (SQLException e) {
				sender.sendMessage(ChatColor.RED + "[ReportRTS] An unexpected error occured when trying to fall back upon the database.");
				e.printStackTrace();
				return true;
			}
			if(!player.teleport(location)){
				sender.sendMessage(ChatColor.RED + "[ReportRTS] Teleportation failed due to an unexpected error.");
				return true;
			}
			return true;
		}
		
		HelpRequest currentRequest = plugin.requestMap.get(Integer.parseInt(args[0]));
		
		Location location = new Location(player.getServer().getWorld(currentRequest.getWorld()), currentRequest.getX(), currentRequest.getY(), currentRequest.getZ());
		
		if(!player.teleport(location)){
			sender.sendMessage(ChatColor.RED + "[ReportRTS] Teleportation failed due to an unexpected error.");
			return true;
		}
		player.sendMessage(ChatColor.BLUE + "[ReportRTS] Teleported to request #" + Integer.parseInt(args[0]));
		return true;
	}

}