package dev.appeazethecheese.vnpaddon.spigot;

import com.comphenix.protocol.PacketType;
import dev.appeazethecheese.vnpaddon.shared.Channels;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.appeazethecheese.vnpaddon.spigot.StaticManager.getProtocolManager;

public class Main extends JavaPlugin {

    public Main() {
        StaticManager.Plugin = this;
        StaticManager.Logger = this.getLogger();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(), this);
        getProtocolManager().addPacketListener(new PacketHandler(this, PacketType.Play.Server.CHAT));
        getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.VANISH_EVENT_CHANNEL);

    }
}
