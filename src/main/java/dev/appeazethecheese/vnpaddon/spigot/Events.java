package dev.appeazethecheese.vnpaddon.spigot;

import com.google.common.io.ByteStreams;
import dev.appeazethecheese.vnpaddon.shared.Channels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.appeazethecheese.vnpaddon.spigot.StaticManager.getVnpManager;

public class Events implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if(getVnpManager() == null) return;
        if(!(event.getSender() instanceof Player player)) return;
        if(StaticManager.hasSeeVanishedPermission(player)) return;

        Set<String> vanishedPlayers = getVnpManager().getVanishedPlayers();
        List<String> completions = event.getCompletions();
        completions = completions.stream().filter(completion -> vanishedPlayers.stream()
                .noneMatch(name -> completion.toLowerCase(Locale.ROOT)
                        .contains(name.toLowerCase(Locale.ROOT)))).collect(Collectors.toList());

        event.setCompletions(completions);
    }

    @EventHandler
    public void onVanishStatusChange(VanishStatusChangeEvent event){
        var out = ByteStreams.newDataOutput();
        out.writeBoolean(event.isVanishing());
        event.getPlayer().sendPluginMessage(StaticManager.Plugin, Channels.VANISH_EVENT_CHANNEL, out.toByteArray());
    }
}
