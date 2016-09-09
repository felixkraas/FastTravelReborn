/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2016 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 *  NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.data;

import de.germanspacebuild.plugins.fasttravel.FastTravel;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by oneill011990 on 02.04.2016
 * for FastTravelReborn
 *
 * @author oneill011990
 */
public class MySQL extends Database {

    private FastTravel plugin;

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
