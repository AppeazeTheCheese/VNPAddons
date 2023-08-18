package dev.appeazethecheese.vnpaddon.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import dev.appeazethecheese.vnpaddon.shared.Channels;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.appeazethecheese.vnpaddon.spigot.StaticManager.getProtocolManager;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        StaticManager.Plugin = this;
        StaticManager.Logger = this.getLogger();

        getServer().getPluginManager().registerEvents(new Events(), this);
        var params = new PacketAdapter.AdapterParameteters()
                .plugin(this)
                .types(PacketType.Play.Server.CHAT/*, PacketType.Play.Server.PLAYER_INFO*/)
                .listenerPriority(ListenerPriority.HIGHEST);

        getProtocolManager().addPacketListener(new PacketHandler(params));

        getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.VANISH_EVENT_CHANNEL);

        //RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        //if(permissionProvider == null){
        //    Logger.log(Level.INFO, "Vault is not on this server. Vanished players will not be removed from the tab list.");
        //    return;
        //}
        //StaticManager.Permission = permissionProvider.getProvider();
    }
}
