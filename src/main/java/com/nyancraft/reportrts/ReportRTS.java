package com.nyancraft.reportrts;

import com.nyancraft.reportrts.api.ApiServer;
import com.nyancraft.reportrts.command.LegacyCommandListener;
import com.nyancraft.reportrts.command.ReportRTSCommand;
import com.nyancraft.reportrts.command.TicketCommand;
import com.nyancraft.reportrts.data.Ticket;
import com.nyancraft.reportrts.persistence.DataProvider;
import com.nyancraft.reportrts.persistence.MySQLDataProvider;
import com.nyancraft.reportrts.util.Message;
import com.nyancraft.reportrts.util.MessageHandler;
import com.nyancraft.reportrts.util.TabCompleteHelper;
import com.nyancraft.reportrts.util.VersionChecker;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import nz.co.lolnet.ConfigManager;
import nz.co.lolnet.MuiltServerSupport;
import nz.co.lolnet.Permission;

import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ReportRTS extends Plugin {
    
    private static ReportRTS plugin;
    private final Logger log = Logger.getLogger("Minecraft");
    private static MessageHandler messageHandler = new MessageHandler();
    private VersionChecker versionChecker = new VersionChecker();
    private DataProvider provider;
    
    public Map<Integer, Ticket> tickets = new TreeMap<>();
    public Map<Integer, UUID> notifications = new HashMap<>();
    public Map<UUID, Integer> teleportMap = new HashMap<>();
    public Map<String, String> commandMap = new HashMap<>();
    
    public boolean notifyStaffOnNewRequest;
    public boolean hideNotification;
    public boolean hideWhenOffline;
    public boolean debugMode;
    public boolean outdated;
    public boolean vanishSupport;
    public boolean setupDone = true;
    public boolean ticketNagHeld;
    public boolean ticketPreventDuplicate;
    public boolean apiEnabled;
    public boolean legacyCommands;
    
    public int maxTickets;
    public int ticketDelay;
    public int ticketMinimumWords;
    public int ticketsPerPage;
    public int storagePort;
    public long ticketNagging;
    public long storageRefreshTime;
    public long bungeeCordSync;
    public String storageType;
    public String storageHostname;
    public String storageDatabase;
    public String storageUsername;
    public String storagePassword;
    public String storagePrefix;
    public String versionString;
    public String bungeeCordServerPrefix;
    public String lineSeparator = System.lineSeparator();
    
    public static Permission permission = null;
    
    private ApiServer apiServer;
    private int apiPort;
    private String apiPassword;
    private List<String> apiAllowedIPs = new ArrayList<>();
    
    private String serverIP;
    
    public void onDisable() {
        if (provider != null) {
            provider.close();
        }
        if (apiEnabled) {
            try {
                apiServer.getListener().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        messageHandler.saveMessageConfig();
    }
    
    public void onEnable() {
        permission = new Permission();
        plugin = this;
        reloadSettings();
        
        final PluginManager pm = plugin.getProxy().getPluginManager();
        
        // Register events that ReportRTS listens to.
        pm.registerListener(plugin, new RTSListener(plugin));
        
        // Ensure that storage information is not default as that may not work.
        if (assertConfigIsDefault("STORAGE")) {
            setupDone = false;
        } else {
            if (!storageType.equalsIgnoreCase("MYSQL")) {
                log.severe("Unsupported STORAGE type specified. Allowed types are: MySQL");
                return;
            }
            setDataProvider(new MySQLDataProvider(plugin));
            if (!provider.load()) {
                log.severe("Encountered an error while attempting to connect to the data-provider.  Disabling...");
                return;
            }
            reloadSettings();
            RTSFunctions.populateStaffMap();
        }
        
        // Check if plugin is up to date. TODO: This has to be updated for Spigot's website.
        outdated = !versionChecker.upToDate();
        
        // Enable fancier tickets if enabled and if ProtocolLib is enabled on the server.
        // Register commands.
        if (debugMode) {
            getLogger().info("legacyCommands =" + legacyCommands);
        }
        if (legacyCommands) {
            
            pm.registerListener(plugin, new LegacyCommandListener(commandMap.get("readTicket"), commandMap.get("openTicket"), commandMap.get("closeTicket"), commandMap.get("reopenTicket"),
                    commandMap.get("claimTicket"), commandMap.get("unclaimTicket"), commandMap.get("holdTicket"), commandMap.get("teleportToTicket"), commandMap.get("broadcastToStaff"),
                    commandMap.get("listStaff"), commandMap.get("commentTicket")));
        }
        
        plugin.getProxy().getPluginManager().registerCommand(plugin, new ReportRTSCommand(plugin));
        plugin.getProxy().getPluginManager().registerCommand(plugin, new TicketCommand(plugin));
        plugin.getProxy().getPluginManager().registerListener(plugin, new TabCompleteHelper(plugin));
        
        // Enable API. (Not recommended since it is very incomplete!)
        apiEnabled = false; // TODO: Remove hard-coded false when this works!
        if (apiEnabled) {
            try {
                Properties props = new Properties();
                props.load(new FileReader("server.properties"));
                serverIP = props.getProperty("server-ip", "ANY");
                if (serverIP.isEmpty()) {
                    serverIP = "ANY";
                }
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-256");
                    apiPassword = apiPassword + "ReportRTS";
                    md.update(apiPassword.getBytes("UTF-8"));
                    byte[] hash = md.digest();
                    StringBuffer sb = new StringBuffer();
                    for (byte b : hash) {
                        sb.append(String.format("%02x", b));
                    }
                    apiPassword = sb.toString();
                } catch (NoSuchAlgorithmException e) {
                    log.warning("[ReportRTS] Unable to hash password, consider disabling the API!");
                    e.printStackTrace();
                }
                apiServer = new ApiServer(plugin, serverIP, apiPort, apiAllowedIPs, apiPassword);
            } catch (IOException e) {
                log.warning("[ReportRTS] Unable to start API server!");
                e.printStackTrace();
            }
            apiServer.start();
        }
        
        // Enable nagging, staff will be reminded of unresolved tickets.
        if (ticketNagging > 0) {
            plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                public void run() {
                    int openTickets = tickets.size();
                    if (ticketNagHeld) {
                        int heldTickets = getDataProvider().countTickets(2);
                        if (heldTickets > 0) {
                            if (openTickets > 0) {
                                RTSFunctions.messageStaff(Message.ticketUnresolvedHeld(openTickets, heldTickets, (plugin.legacyCommands ? plugin.commandMap.get("readTicket") : "ticket " + plugin.commandMap.get("readTicket"))), false, true);
                            }
                        } else if (openTickets > 0) {
                            RTSFunctions.messageStaff(Message.ticketUnresolved(openTickets, (plugin.legacyCommands ? plugin.commandMap.get("readTicket") : "ticket " + plugin.commandMap.get("readTicket"))), false, true);
                        }
                    } else if (openTickets > 0) {
                        RTSFunctions.messageStaff(Message.ticketUnresolved(openTickets, (plugin.legacyCommands ? plugin.commandMap.get("readTicket") : "ticket " + plugin.commandMap.get("readTicket"))), false, true);
                    }
                }
            }, 120L, (ticketNagging), TimeUnit.MINUTES);
        }
        
        new MuiltServerSupport().setup();
    }
    
    public void reloadPlugin() {
        reloadSettings();
        RTSFunctions.sync();
    }
    
    public void reloadSettings() {
        reloadConfig();
        assertConfigUpToDate();
        messageHandler.reloadMessageConfig();
        messageHandler.saveMessageConfig();
        messageHandler.reloadMessageMap();
        notifyStaffOnNewRequest = getConfig().getBoolean("notifyStaff");
        hideNotification = getConfig().getBoolean("hideMessageIfEmpty");
        hideWhenOffline = getConfig().getBoolean("ticket.hideOffline");
        maxTickets = getConfig().getInt("ticket.max");
        ticketDelay = getConfig().getInt("ticket.delay");
        ticketMinimumWords = getConfig().getInt("ticket.minimumWords");
        ticketsPerPage = getConfig().getInt("ticket.perPage");
        ticketPreventDuplicate = getConfig().getBoolean("ticket.preventDuplicates", true);
        ticketNagging = getConfig().getLong("ticket.nag");
        ticketNagHeld = getConfig().getBoolean("ticket.nagHeld", false);
        storageRefreshTime = getConfig().getLong("storage.refreshTime");
        storageType = getConfig().getString("storage.type", "mysql");
        storagePort = getConfig().getInt("storage.port");
        storageHostname = getConfig().getString("storage.hostname");
        storageDatabase = getConfig().getString("storage.database");
        storageUsername = getConfig().getString("storage.username");
        storagePassword = getConfig().getString("storage.password");
        storagePrefix = getConfig().getString("storage.prefix");
        debugMode = getConfig().getBoolean("debug");
        vanishSupport = getConfig().getBoolean("VanishSupport", false);
        bungeeCordSync = getConfig().getLong("bungeecord.sync", 300L);
        bungeeCordServerPrefix = getConfig().getString("bungeecord.serverPrefix");
        apiEnabled = false; // TODO: Change to this when it's ready: getConfig().getBoolean("api.enable", false);
        apiPort = getConfig().getInt("api.port", 25567);
        apiPassword = getConfig().getString("api.password");
        apiAllowedIPs = getConfig().getStringList("api.whitelist");
        legacyCommands = getConfig().getBoolean("command.legacy", false);
        commandMap.clear();
        // Register all commands/subcommands.
        commandMap.put("readTicket", getConfig().getString("command.readTicket"));
        commandMap.put("openTicket", getConfig().getString("command.openTicket"));
        commandMap.put("closeTicket", getConfig().getString("command.closeTicket"));
        commandMap.put("reopenTicket", getConfig().getString("command.reopenTicket"));
        commandMap.put("claimTicket", getConfig().getString("command.claimTicket"));
        commandMap.put("unclaimTicket", getConfig().getString("command.unclaimTicket"));
        commandMap.put("holdTicket", getConfig().getString("command.holdTicket"));
        commandMap.put("teleportToTicket", getConfig().getString("command.teleportToTicket"));
        commandMap.put("broadcastToStaff", getConfig().getString("command.broadcastToStaff"));
        commandMap.put("listStaff", getConfig().getString("command.listStaff"));
        commandMap.put("assignTicket", getConfig().getString("command.assignTicket"));
        commandMap.put("commentTicket", getConfig().getString("command.commentTicket"));
        // Commands registered!
    }
    
    public static ReportRTS getPlugin() {
        return plugin;
    }
    
    public static MessageHandler getMessageHandler() {
        return messageHandler;
    }
    
    public DataProvider getDataProvider() {
        return provider;
    }
    
    public void setDataProvider(DataProvider provider) {
        if (this.provider != null) {
            this.provider.close();
        }
        
        this.provider = provider;
    }
    
    private void assertConfigUpToDate() {
        /**
         * What it does: - - - - - Checks if the mapping "requests" is located
         * in the config and replaces it with "ticket". - - - - - Since version:
         * 1.2.3
         */
        
        //disabled for now...
        /*
        if(getConfig().getConfigurationSection("request") != null) {
            getConfig().createSection("ticket", getConfig().getConfigurationSection("request").getValues(false));
            getConfig().set("request", null);
            log.info("Updated configuration. 'request' => 'ticket'.");
        }

        // Save changes.
        saveConfig(getConfig());
         */
    }
    
    private boolean assertConfigIsDefault(String path) {
        /**
         * What it does: - - - - - Checks if the specified configuration section
         * is default, returns a boolean depending on the result.
         */
        
        switch (path.toUpperCase()) {
            
            case "STORAGE":
                
                return (storageHostname.equalsIgnoreCase("localhost") && storagePort == 3306 && storageDatabase.equalsIgnoreCase("minecraft")
                        && storageUsername.equalsIgnoreCase("username") && storagePassword.equalsIgnoreCase("password")
                        && storagePrefix.equalsIgnoreCase("") && storageRefreshTime == 600);
        }
        return false;
    }
    
    public String getConsoleName() {
        return "console";
    }
    
    private Configuration reloadConfig() {
        return ConfigManager.reloadConfig("config.yml");
    }
    
    private Configuration getConfig() {
        return ConfigManager.reloadConfig("config.yml");
    }
    
    private void saveConfig(Configuration config) {
        ConfigManager.saveConfig(config, "config.yml");
    }
}
