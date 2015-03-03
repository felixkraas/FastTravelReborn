package de.germanspacebuild.plugins.fasttravel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oneill011990 on 03.03.15.
 * Copyright
 *
 * @author oneill011990
 */
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
