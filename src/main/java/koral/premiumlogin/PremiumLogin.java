package koral.premiumlogin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.xephi.authme.api.v3.AuthMeApi;
import koral.premiumlogin.database.DatabaseConnection;
import koral.premiumlogin.database.Table;
import koral.premiumlogin.listeners.PlayerJoin;
import org.bukkit.plugin.java.JavaPlugin;

public final class PremiumLogin extends JavaPlugin {


    AuthMeApi authMeApi = AuthMeApi.getInstance();
    @Override
    public void onEnable() {
        saveDefaultConfig();
        DatabaseConnection.configureDbConnection();
        Table.createTables();

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        // Plugin startup logic

    }
    private ProtocolManager protocolManager;
    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL,
                        PacketType.Play.Server.CHAT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.CHAT) {
                            if(PlayerJoin.disabledChat.contains(event.getPlayer().getName()))
                              event.setCancelled(true);
                        }
                    }
                });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
