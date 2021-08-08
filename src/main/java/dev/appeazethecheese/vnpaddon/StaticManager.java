package dev.appeazethecheese.vnpaddon;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.VanishPlugin;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticManager {
    public static JavaPlugin Plugin = null;
    public static Logger Logger = null;

    public static VanishManager getVnpManager() {
        try {
            return ((VanishPlugin) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("VanishNoPacket"))).getManager();
        } catch (Exception e) {
            Logger.log(Level.SEVERE, e.toString());
            return null;
        }
    }

    public static ProtocolManager getProtocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }
    public static boolean hasPermission(Player player){
        return player.hasPermission("vanish.see");
    }

}
