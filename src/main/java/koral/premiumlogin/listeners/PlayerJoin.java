package koral.premiumlogin.listeners;


import fr.xephi.authme.api.v3.AuthMeApi;
import koral.premiumlogin.PremiumLogin;
import koral.premiumlogin.database.Statements;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class PlayerJoin implements Listener {
    final String prefix = "§7§l[§4§lJBWM§7§l]";
    public static Set<String> disabledChat = new HashSet<>();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> Statements.createPlayerQuery(e.getPlayer()));

        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
            if(Statements.isPremium(e.getPlayer())){


                loginPremiumPlayer(e.getPlayer(), 6);

                Bukkit.getScheduler().runTaskLater(PremiumLogin.getPlugin(PremiumLogin.class), ()-> {
                    if(AuthMeApi.getInstance().isAuthenticated(e.getPlayer())) {
                        enableChat(e.getPlayer());
                    }
                    else{
                        loginPremiumPlayer(e.getPlayer(), 12);
                        Bukkit.getScheduler().runTaskLater(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
                            if(AuthMeApi.getInstance().isAuthenticated(e.getPlayer()))
                                enableChat(e.getPlayer());
                            else e.getPlayer().kickPlayer("Błąd logowania premium, spróbuj ponownie!");

                        }, 30);
                    }
                }, 18);
            }
        });

    }
    //6, 12
    public void loginPremiumPlayer(Player player, int registerDelay){
        String password = new Random().ints(10, 33, 122).mapToObj(i -> String.valueOf((char)i)).collect(Collectors.joining());
        disabledChat.add(player.getName());
        AuthMeApi.getInstance().forceUnregister(player);
        Bukkit.getScheduler().runTaskLater(PremiumLogin.getPlugin(PremiumLogin.class), () -> AuthMeApi.getInstance().forceRegister(player, password, true), registerDelay);
    }

    public void enableChat(Player player){
        disabledChat.remove(player.getName());
        player.sendMessage(prefix + "§a Zalogowano z konta premium.");
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Bukkit.getScheduler().runTaskAsynchronously(PremiumLogin.getPlugin(PremiumLogin.class), () -> {
            Statements.updatePlayer(e.getPlayer());
        });

    }





}
