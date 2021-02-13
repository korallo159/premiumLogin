package koral.premiumlogin.database;

import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static koral.premiumlogin.database.DatabaseConnection.hikari;

public class Statements {

    public static void createPlayerQuery(Player player) {
        Connection connection = null;
        String update = "INSERT INTO Players (NICK, UUID) VALUES (?,?) ON DUPLICATE KEY UPDATE NICK=?";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Players WHERE NICK=?");
            statement.setString(1, player.getUniqueId().toString());
            statement = connection.prepareStatement(update);
            statement.setString(1, player.getName());
            statement.setString(2, player.getUniqueId().toString());
            statement.setString(3, player.getName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, statement);
        }
    }

    public static void updatePlayer(Player player) {
        Connection connection = null;
        String update = "UPDATE Players SET REGISTERED=? WHERE NICK=?";
        PreparedStatement statement = null;
        try {
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM Players WHERE NICK=?");
            statement.setString(1, player.getName());
            statement = connection.prepareStatement(update);
            statement.setBoolean(1, AuthMeApi.getInstance().isRegistered(player.getName()));
            statement.setString(2, player.getName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, statement);
        }
    }

    public static boolean isPremium(Player player){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Boolean premium = false;
        try{
            connection = hikari.getConnection();
            statement = connection.prepareStatement("SELECT PREMIUM FROM Players WHERE NICK=?");
            statement.setString(1, player.getName());
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                premium = resultSet.getBoolean("PREMIUM");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeAll(connection, statement, resultSet);
        }
        return premium;
    }

    public static void closeAll(AutoCloseable... toClose) {
        for (AutoCloseable closeable : toClose)
            if (closeable != null)
                try {
                    closeable.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
}
