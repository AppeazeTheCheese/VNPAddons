package dev.appeazethecheese.vnpaddon.bungee;

import dev.appeazethecheese.vnpaddon.shared.Channels;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class Main extends Plugin {
    static Logger PluginLogger;

    @Override
    public void onEnable(){
        PluginLogger = this.getLogger();
        getProxy().registerChannel(Channels.VANISH_EVENT_CHANNEL);
        getProxy().getPluginManager().registerListener(this, new EventListener());
    }
}
