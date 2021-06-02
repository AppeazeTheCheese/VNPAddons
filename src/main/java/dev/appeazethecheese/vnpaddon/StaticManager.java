package dev.appeazethecheese.vnpaddon;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.vanish.VanishManager;

public class StaticManager {
    public static JavaPlugin Plugin = null;

    public static VanishManager getVnpManager() {
        try {
            return org.kitteh.vanish.staticaccess.VanishNoPacket.getManager();
        } catch (Exception e) {
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
