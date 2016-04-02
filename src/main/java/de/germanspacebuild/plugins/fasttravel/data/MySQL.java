package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.FastTravel;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by oneill011990 on 02.04.2016.
 */
public class MySQL extends Database {

    FastTravel plugin;

    public MySQL(FastTravel plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void connect() throws ClassNotFoundException, SQLException {
        String host = plugin.getConfig().getString("MySQL.Host");
        int port = plugin.getConfig().getInt("MySQL.Port");
        String name = plugin.getConfig().getString("MySQL.Name");
        String username = plugin.getConfig().getString("MySQL.Username");
        String password = plugin.getConfig().getString("MySQL.Password");
        Class.forName("com.mysql.jdbc.Driver");
        dbConn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name, username, password);
    }
}
