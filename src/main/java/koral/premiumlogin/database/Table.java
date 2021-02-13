package koral.premiumlogin.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static koral.premiumlogin.database.DatabaseConnection.hikari;
import static koral.premiumlogin.database.Statements.closeAll;

public class Table {
    public static void createTables() {

        try (Connection connection = hikari.getConnection()) {
            for (String create : new String[]{
                    "CREATE TABLE IF NOT EXISTS Players(" +
                            "NICK VARCHAR(16), " +
                            "UUID VARCHAR(36), " +
                            "REGISTERED BIT, " +
                            "PREMIUM BIT, " +
                            "PRIMARY KEY (NICK))",
            })
                try (PreparedStatement statement = connection.prepareStatement(create)) {
                    statement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            finally {
                   closeAll(connection);
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
