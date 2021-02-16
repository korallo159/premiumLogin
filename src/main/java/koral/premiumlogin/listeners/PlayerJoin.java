package koral.premiumlogin.listeners;

import fr.xephi.authme.api.v3.AuthMeApi;
import koral.premiumlogin.PremiumLogin;
import koral.premiumlogin.database.Statements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;


public class PlayerJoin implements Listener {
    final String prefix = "§7§l[§4§lJBWM§7§l]";
    public static Set<String> disabledChat = new HashSet<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> Statements.createPlayerQuery(e.getPlayer()));

        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
           if(Statements.isPremium(e.getPlayer())){
               disabledChat.add(e.getPlayer().getName());
              AuthMeApi.getInstance().forceUnregister(e.getPlayer());
              Bukkit.getScheduler().runTaskLaterAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
                  AuthMeApi.getInstance().forceRegister(e.getPlayer(), "1234567wdfsd", false);
                  }, 4);
              Bukkit.getScheduler().runTaskLaterAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> AuthMeApi.getInstance().forceLogin(e.getPlayer()), 6);
               Bukkit.getScheduler().runTaskLater(PremiumLogin.getPlugin(PremiumLogin.class), ()-> {
                   disabledChat.remove(e.getPlayer().getName());
                   e.getPlayer().sendMessage(prefix + "§a Zalogowano z konta premium.");
                   e.getPlayer().sendMessage(prefix + "§c Logowanie jest w fazie BETA, jeżeli masz problem z logowaniem premium zrób relog, zgłoś problem administratorowi.");
               }, 18);
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
