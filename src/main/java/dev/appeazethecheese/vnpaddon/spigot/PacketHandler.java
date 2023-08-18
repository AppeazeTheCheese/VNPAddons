package dev.appeazethecheese.vnpaddon.spigot;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Level;

public class PacketHandler extends PacketAdapter {

    public PacketHandler (Plugin plugin, com.comphenix.protocol.PacketType... types){ super(plugin, types); }

    public PacketHandler(AdapterParameteters params){
        super(params);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        var packetType = event.getPacketType();


        // ACHIEVEMENT ANNOUNCEMENT PACKETS
        if (packetType == PacketType.Play.Server.CHAT) {
            PacketContainer e = event.getPacket();
            if (e.getChatComponents() == null) {
                return;
            }

            List<WrappedChatComponent> components = e.getChatComponents().getValues();

            for (WrappedChatComponent wrappedChatComponent : components) {
                if (wrappedChatComponent == null)
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

        // TAB LIST PACKETS
        if (packetType == PacketType.Play.Server.PLAYER_INFO) {


            var e = event.getPacket();
            var action = (EnumWrappers.PlayerInfoAction) e.getPlayerInfoAction().read(0);
            var playerInfos = new ArrayList<>(e.getPlayerInfoDataLists().read(0));

            StaticManager.Logger.log(Level.INFO, action + " sent to " + event.getPlayer().getName() + " for players " + String.join(", ", playerInfos.stream().map(x -> x.getProfile().getUUID().toString()).toList()));

            /*if(StaticManager.hasSeeVanishedPermission(event.getPlayer())
                    || action == EnumWrappers.PlayerInfoAction.REMOVE_PLAYER) {
                return;
            }

            var playerInfos = new ArrayList<>(e.getPlayerInfoDataLists().read(0));
            StaticManager.Logger.log(Level.INFO, event.getPlayer().getName() + " " + action + " " + playerInfos.get(0).getProfile().getUUID() + " " + playerInfos.get(0).getProfile().getName());

            var vanishedPlayerIds = new ArrayList<>(playerInfos.stream().filter(x ->
                    StaticManager.getVnpManager().isVanished(x.getProfile().getName())).map(x ->
                    x.getProfile().getUUID()).toList());

            //if(action == EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE){
                for(var player : playerInfos){
                    if(vanishedPlayerIds.contains(player.getProfile().getUUID())) continue;

                    var offlinePlayer = plugin.getServer().getOfflinePlayer(player.getProfile().getUUID());
                    if(StaticManager.Permission.playerHas(plugin.getServer().getWorlds().get(0).getName(), offlinePlayer, "vanish.joinvanished")){
                        vanishedPlayerIds.add(player.getProfile().getUUID());
                    }
                }
            //}

            var vanishedUuids = vanishedPlayerIds.stream().map(x -> x.toString()).toList();
            StaticManager.Logger.log(Level.INFO, "Vanished: " + String.join(", ", vanishedUuids));

            if(playerInfos.size() == 1){
               if(vanishedPlayerIds.size() > 0){
                   event.setCancelled(true);
                   Bukkit.getScheduler().runTaskLater(plugin, () -> {
                       try {
                           sendRemovePlayerPacket(event.getPlayer(), playerInfos.get(0));
                           StaticManager.Logger.log(Level.INFO, "Successfully sent remove_player packet to " + event.getPlayer().getName());
                       }catch (Exception ex){
                           StaticManager.Logger.log(Level.SEVERE, "Failed to send remove_player packet to " + event.getPlayer().getName() + ": " + ex);
                       }
                   }, 100L);

               }
               return;
            }

            var newInfo = playerInfos.stream().takeWhile(x -> !vanishedPlayerIds.contains(x.getProfile().getUUID())).toList();
            e.getPlayerInfoDataLists().write(0, newInfo);


            StaticManager.Logger.log(Level.INFO, String.valueOf(e.getPlayerInfoDataLists().read(0).size()));
            */
        }
    }

    private void sendRemovePlayerPacket(Player recipient, PlayerInfoData playerInfo) throws InvocationTargetException {
        /*var player = Bukkit.getServer().getPlayer(playerInfo.getProfile().getUUID());
        recipient.hidePlayer(plugin, player);*/

        var playerId = UUID.nameUUIDFromBytes(ByteBuffer.allocate(16).putInt(102).array());
        var profile = new WrappedGameProfile(playerId, "\u0000" + 102);
        var newInfo = new PlayerInfoData(profile, playerInfo.getLatency(), playerInfo.getGameMode(), playerInfo.getDisplayName());

        StaticManager.Logger.log(Level.INFO, "Remove " + playerInfo.getProfile().getUUID());
        var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet.getPlayerInfoDataLists().write(0, List.of(newInfo));
        ProtocolLibrary.getProtocolManager().sendServerPacket(recipient, packet, false);
    }
}
