/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2015 CraftyCreeper, minebot.net, oneill011990
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.germanspacebuild.plugins.fasttravel.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Created by oneill011990 on 29.04.2015.
 */
public abstract class Database {

    protected Connection dbConn;
    protected Statement dbStatement;
    private static final HashMap<DBType, Database> dbSystems;

    static {
        dbSystems = new HashMap<>();
    }

    protected abstract void connect() throws ClassNotFoundException, SQLException;

    private void setupTables() {
        createTable("FastTravelSigns", "name TEXT, creator TEXT, signloc_World TEXT, signloc_X INTEGER," +
                " signloc_Y INTEGER, signloc_Z INTEGER, signloc_Yaw REAL, " +
                "tploc_World TEXT, tploc_X INTEGER, tploc_Y INTEGER, tploc_Z INTEGER, tploc_Yaw REAL," +
                "automatic BOOLEAN, price REAL, range INTEGER, players LONGBLOB");
    }

    public void init() {
        try {
            connect();
            dbStatement = dbConn.createStatement();
            setupTables();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            dbStatement.close();
            dbConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean createTable(String tableName, String columns) {
        if (dbStatement != null) {
            try {
                dbStatement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS  FastTravelSigns (" +
                                "name TEXT," +
                                "creator TEXT," +
                                "signloc_World TEXT," +
                                "signloc_X INTEGER," +
                                "signloc_Y INTEGER, " +
                                "signloc_Z INTEGER, " +
                                "signloc_Yaw REAL, " +
                                "tploc_World TEXT," +
                                "tploc_X INTEGER, " +
                                "tploc_Y INTEGER, " +
                                "tploc_Z INTEGER, " +
                                "tploc_Yaw REAL," +
                                "automatic BOOLEAN," +
                                "price REAL, " +
                                "range INTEGER," +
                                "players TEXT" +
                                ");"
                );
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;
    }

    public boolean tableContains(String column, String value) {
        try {
            ResultSet rs = dbStatement.executeQuery("SELECT COUNT(" + column + ") AS " + column +
                    "Count FROM FastTravelSigns WHERE " + column + "='" + value + "'");
            if (rs == null) return false;
            if (rs.isAfterLast()) return false;
            if (rs.isBeforeFirst()) rs.next();
            if (rs.getInt(1) == 0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Static Stuff
    public static void registerDatabaseSystem(DBType systemName, Database dbSystem) {
        dbSystems.put(systemName, dbSystem);
    }

    public static Database getDatabaseBySystem(DBType systemName) {
        return dbSystems.get(systemName);
    }

    public boolean parseBoolean(int bool) {
        if (bool == 1)
            return true;
        else
            return false;
    }
}
