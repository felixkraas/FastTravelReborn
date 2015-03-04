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

package de.germanspacebuild.plugins.fasttravel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDUtil {
    /**
     * Converts a list of strings to UUIDs.
     * @param strings String to convert.
     * @return List of UUIDs.
     */
    public static List<UUID> stringToUUID(List<String> strings){
        List<UUID> ids = new ArrayList<UUID>();
        for (String string : strings) {
            ids.add(UUID.fromString(string));
        }
        return ids;
    }

    /**
     * Convert a list of UUIDs into strings.
     * @param ids UUIDs to convert.
     * @return Converted UUIDs.
     */
    public static List<String> uuidToString(List<UUID> ids){
        List<String> strings = new ArrayList<>();
        for (UUID id : ids) {
            strings.add(id.toString());
        }
        return strings;
    }
}
