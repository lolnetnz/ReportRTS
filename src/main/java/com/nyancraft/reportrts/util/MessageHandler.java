package com.nyancraft.reportrts.util;

import com.google.common.io.ByteStreams;
import com.nyancraft.reportrts.ReportRTS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import nz.co.lolnet.ConfigManager;

public class MessageHandler {

    private Configuration messageConfig = null;
    private File messageFile = null;

    public Map<String, String> messageMap = new HashMap<>();

    public void reloadMessageConfig() {
        messageConfig = ConfigManager.reloadConfig("messages.yml");
        messageFile = new File(ReportRTS.getPlugin().getDataFolder(), "messages.yml");
        // Ensure that the messages file is not from prior to the rewrite.
        if (messageConfig.getString("modreqFiledUser") != null) {

            // Rename the old messages file.
            if (!messageFile.renameTo(new File(ReportRTS.getPlugin().getDataFolder(), "messages.yml.old"))) {
                ReportRTS.getPlugin().getLogger().warning("Failed to move the old messages file. Does a backup already exist?");
                return;
            }

            // Get the new configuration.
            messageConfig = ConfigManager.reloadConfig("messages.yml");

            // Save to disk.
            saveMessageConfig();
        }
    }

    public Configuration getMessageConfig() {
        if (messageConfig == null) {
            this.reloadMessageConfig();
        }
        return messageConfig;
    }

    public void saveMessageConfig() {
        if (messageConfig == null || messageFile == null) {
            return;
        }
        ConfigManager.saveConfig(messageConfig, "messages.yml");
    }

    public void reloadMessageMap() {
        Collection<String> Messages = messageConfig.getKeys();
        for (String message : Messages) {
            messageMap.put(message, messageConfig.getString(message));
        }
    }
}
