package koral.premiumlogin.database;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import koral.premiumlogin.PremiumLogin;

public class DatabaseConnection {

    public static HikariDataSource hikari;

    public static void configureDbConnection() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(PremiumLogin.getPlugin(PremiumLogin.class).getConfig().getString("jdbcurl"));
        hikariConfig.setMaxLifetime(900000); // zeby uniknac wiekszy lifetime hikari niz mysql
        hikariConfig.addDataSourceProperty("user", PremiumLogin.getPlugin(PremiumLogin.class).getConfig().getString("username"));
        hikariConfig.addDataSourceProperty("password", PremiumLogin.getPlugin(PremiumLogin.class).getConfig().getString("password"));
        hikariConfig.addDataSourceProperty( "cachePrepStmts" , "true" ); //pozwala lepiej wspolpracowac z prepared statements
        hikariConfig.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        hikariConfig.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        hikari = new HikariDataSource(hikariConfig);
    }

}
