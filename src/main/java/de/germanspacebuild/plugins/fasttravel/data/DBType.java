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

import java.io.File;
import java.sql.SQLException;

public enum DBType {

    /*
     * 1 = SQLite
     * 2 = MySQL
     */
    SQLite, MySQL;

    /**
     * Created by oneill011990 on 03.03.2016
     * for FastTravelReborn
     *
     * @author oneill011990
     */
    private static DBType type;

    public static void save(File saveFile) {
        switch (type) {
            case SQLite:
                try {
                    SQLDBHandler.save();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case MySQL:
                try {
                    SQLDBHandler.save();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void load() {

    }

    public static void load(File saveFile) {
        switch (type) {
            case SQLite:
                SQLDBHandler.load();
                break;
            case MySQL:
                SQLDBHandler.load();
        }
    }

    public static DBType getDBType() {
        return type;
    }

    public static void setDBType(DBType dbtype) {
        type = dbtype;
    }
}
