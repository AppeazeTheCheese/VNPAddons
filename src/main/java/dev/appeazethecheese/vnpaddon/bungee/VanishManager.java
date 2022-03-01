package dev.appeazethecheese.vnpaddon.bungee;

import com.google.common.collect.ImmutableSet;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

class VanishManager {
    private static VanishManager instance = null;

    private final List<String> vanishedPlayers = Collections.synchronizedList(new ArrayList<>());

    static VanishManager getInstance(){
        if(instance == null)
            instance = new VanishManager();
        return instance;
    }

    void setPlayerVanished(ProxiedPlayer player){
        Main.PluginLogger.log(Level.INFO, player.getName() + " vanish");
        if(!vanishedPlayers.contains(player.getName()))
            vanishedPlayers.add(player.getName());
    }

    void setPlayerUnvanished(ProxiedPlayer player){
        Main.PluginLogger.log(Level.INFO, player.getName() + " unvanish");
        vanishedPlayers.remove(player.getName());
    }

    Set<String> getVanishedPlayers(){
        return ImmutableSet.copyOf(vanishedPlayers);
    }
}
