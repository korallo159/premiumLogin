package koral.premiumlogin;

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

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
