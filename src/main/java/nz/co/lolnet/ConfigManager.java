package nz.co.lolnet;

import com.google.common.io.ByteStreams;
import com.nyancraft.reportrts.ReportRTS;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author James
 */
public class ConfigManager {
    
    public static Configuration reloadConfig(String fileName) {
        Configuration config = null;
        if (!fileName.contains(".yml")) {
            fileName += ".yml";
        }
        if (!ReportRTS.getPlugin().getDataFolder().exists()) {
            ReportRTS.getPlugin().getDataFolder().mkdir();
        }
        File configFile = new File(ReportRTS.getPlugin().getDataFolder(), fileName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                if (ReportRTS.getPlugin().getResourceAsStream(fileName) == null) {
                    System.out.println("Failed to obtain " + fileName + " from jar file");
                }
                try (InputStream is = ReportRTS.getPlugin().getResourceAsStream(fileName);
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file: " + fileName, e);
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        saveConfig(config, fileName);
        return config;
    }
    
    public static void saveConfig(Configuration config, String fileName) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(ReportRTS.getPlugin().getDataFolder(), fileName));
        } catch (IOException ex) {
            Logger.getLogger(ConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
