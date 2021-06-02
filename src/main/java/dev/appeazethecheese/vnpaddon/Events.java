package dev.appeazethecheese.vnpaddon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.appeazethecheese.vnpaddon.StaticManager.getVnpManager;

public class Events implements Listener {

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        if(getVnpManager() == null) return;
        if(!(event instanceof Player)) return;
        if(StaticManager.hasPermission((Player)event)) return;

        Set<String> vanishedPlayers = getVnpManager().getVanishedPlayers();
        List<String> completions = event.getCompletions();
        completions = completions.stream().filter(x -> vanishedPlayers.stream()
                .noneMatch(y -> y.toLowerCase(Locale.ROOT)
                        .contains(x.toLowerCase(Locale.ROOT)))).collect(Collectors.toList());

        event.setCompletions(completions);
    }
}
