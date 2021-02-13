package koral.premiumlogin.listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import koral.premiumlogin.PremiumLogin;
import koral.premiumlogin.database.Statements;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> Statements.createPlayerQuery(e.getPlayer()));

        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
           if(Statements.isPremium(e.getPlayer())){
              AuthMeApi.getInstance().forceUnregister(e.getPlayer());
              Bukkit.getScheduler().runTaskLaterAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> AuthMeApi.getInstance().forceRegister(e.getPlayer(), "1234567wdfsd", true), 10);
           }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
            Statements.updatePlayer(e.getPlayer());
        });

    }



}
