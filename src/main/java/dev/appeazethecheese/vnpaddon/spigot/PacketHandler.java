package dev.appeazethecheese.vnpaddon.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PacketHandler extends PacketAdapter {
    public PacketHandler (Plugin plugin, com.comphenix.protocol.PacketType... types){
        super(plugin, types);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.CHAT && event.getPacketType() != PacketType.Play.Client.CHAT) return;
        PacketContainer e = event.getPacket();
        if (e.getChatComponents() == null) {
            return;
        }

        List<WrappedChatComponent> components = e.getChatComponents().getValues();

        for (WrappedChatComponent wrappedChatComponent : components) {
            if(wrappedChatComponent == null)
                continue;
            String json = wrappedChatComponent.getJson();
            if (json == null || json.trim().isEmpty()) return;
            TextComponent component = new TextComponent(ComponentSerializer.parse(json));

            String plainText = component.toPlainText();
            if (plainText == null) return;
            if (plainText.toLowerCase(Locale.ROOT).contains("has completed the challenge") ||
                    plainText.toLowerCase(Locale.ROOT).contains("has made the advancement")) {
                // Is an achievement announcement
                String playerName = plainText.split(" ")[0];
                if (Objects.requireNonNull(StaticManager.getVnpManager()).isVanished(playerName)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
