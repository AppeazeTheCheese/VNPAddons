package dev.appeazethecheese.vnpaddon.bungee;

import com.google.common.io.ByteStreams;
import dev.appeazethecheese.vnpaddon.shared.Channels;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.event.TabCompleteResponseEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import java.util.Locale;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EventListener implements Listener {
    @EventHandler
    public void pluginMessageReceived(PluginMessageEvent event) {
        if (!(event.getReceiver() instanceof ProxiedPlayer player)) {
            return;
        }

        if(event.getTag().equalsIgnoreCase(Channels.VANISH_EVENT_CHANNEL)){
            var data = ByteStreams.newDataInput(event.getData());
            var vanished = data.readBoolean();

            var managerInstance = VanishManager.getInstance();
            if (vanished) {
                managerInstance.setPlayerVanished(player);
            } else {
                managerInstance.setPlayerUnvanished(player);
            }
        }
    }

    @EventHandler
    public void onAutoComplete(TabCompleteEvent event){
        var vanishedPlayers = VanishManager.getInstance().getVanishedPlayers();

        var suggestions = event.getSuggestions();
        var completionsToRemove = suggestions.stream().filter(completion -> vanishedPlayers.stream()
                .anyMatch(player -> completion.toLowerCase(Locale.ROOT)
                        .contains(player.toLowerCase(Locale.ROOT)))).toList();

        event.getSuggestions().removeAll(completionsToRemove);
    }
}
