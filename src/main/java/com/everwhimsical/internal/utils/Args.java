package com.everwhimsical.internal.utils;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Args class is used to hold common verify methods
 */
public class Args {

    public static void notNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
    }

    public static void notEmpty(String obj, String name) {
        notNull(obj, name);
        if (obj.isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
    }

    public static void verifyJson(String jsonContents, String name) {
        notEmpty(jsonContents, name);
        JSONParser parser = new JSONParser();
        try {
            parser.parse(jsonContents);
        } catch (ParseException e) {
            throw new IllegalArgumentException(name + " cannot contain invalid data");
        }
    }
}
