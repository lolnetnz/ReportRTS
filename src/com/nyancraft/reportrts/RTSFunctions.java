package com.nyancraft.reportrts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nyancraft.reportrts.data.HelpRequest;

public class RTSFunctions {

	private static int openRequestsByUser;
	
	/**
	 * Combines array and returns an imploded string.
	 * @param args String[] array.
	 * @return String, imploded array.
	 */
	public static String combineString(String[] args){
        List<String> temp_list = new LinkedList<String>();
        temp_list.addAll(Arrays.asList(args));
        while (temp_list.contains("")) {
            temp_list.remove("");
        }
        args = temp_list.toArray(new String[0]);
        
        return implode(args, " ");
	}
    /**
     * Join a String[] into a single string with a joiner
     */
    public static String implode( String[] array, String glue ) {

	String out = "";

	if( array.length == 0 ) {
	    return out;
	}
	
	for( String part : array ) {
	    out = out + part + glue;
	}
	out = out.substring(0, out.length() - glue.length() );

	return out;
    }
    
    public static String cleanUpSign(String[] lines){
    	
    	String out = "";
    	for(String part : lines){
    		out = out + part.trim(); 
    	}
    	return out;
    }
    /***
     * Messages all online moderators on the server
     * @param message - message to be displayed
     * @param players - Player[] array
     */
    public static void messageMods(String message, Player[] players){
    	for(Player player : players){
    		if(RTSPermissions.isModerator(player)) player.sendMessage(message);
    	}
    }
    
	/**
	 * Returns true if the person is online.
	 * @param username - String name of player
	 * @param server - server object
	 * @return boolean
	 */
    public static boolean isUserOnline(String username, Server server) {
    	return server.getOfflinePlayer(username).isOnline();
    }
    
    /***
     * Attempts to parse string as integer
     * @param i - String to be parsed
     * @return true if possible to parse
     */
    public static boolean isParsableToInt(String i){
    	try{
    		Integer.parseInt(i);
    		return true;
    	} catch(NumberFormatException nfe){
    		return false;
    	}
    }
    
    /**
     * Populates the requestMap with data regarding held requests.
     */
    public static void populateHeldRequestsWithData(){
    	for(Map.Entry<Integer, HelpRequest> entry : ReportRTS.getPlugin().requestMap.entrySet()){
    		if(entry.getValue().getStatus() == 1){
    			int ticketId = entry.getValue().getId();
    			ResultSet rs = RTSDatabaseManager.db.query("SELECT * FROM reportrts_request as request INNER JOIN reportrts_user as user ON request.mod_id = user.id WHERE request.id = '" + ticketId + "'");
    			try {
    				entry.getValue().setModName(rs.getString("name"));
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    }
    
    /**
     * Get number of open request by the specified user.
     * @param player
     * @return
     */
    public static int getOpenRequestsByUser(CommandSender sender){
    	openRequestsByUser = 0;
		for(Map.Entry<Integer, HelpRequest> entry : ReportRTS.getPlugin().requestMap.entrySet()){
			if(entry.getValue().getName().equals(sender.getName())) openRequestsByUser++;
		}
    	return openRequestsByUser;
    }
}
