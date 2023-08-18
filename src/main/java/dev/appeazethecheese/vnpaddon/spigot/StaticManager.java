package dev.appeazethecheese.vnpaddon.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.milkbowl.vault.permission.Permission;
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

    static VanishManager getVnpManager() {
        try {
            return ((VanishPlugin) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("VanishNoPacket"))).getManager();
        } catch (Exception e) {
            Logger.log(Level.SEVERE, e.toString());
            return null;
        }
    }

    static ProtocolManager getProtocolManager() {
        return ProtocolLibrary.getProtocolManager();
    }
    static boolean hasSeeVanishedPermission(Player player){
        return player.hasPermission("vanish.see");
    }
    static boolean hasJoinVanishedPermission(Player player) { return player.hasPermission("vanish.joinvanished"); }
}
