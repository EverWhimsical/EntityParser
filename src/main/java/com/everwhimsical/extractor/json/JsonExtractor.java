package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityOutput;
import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.internal.utils.Args;
import com.everwhimsical.internal.utils.Commons;
import com.everwhimsical.parser.exceptions.ParserException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.everwhimsical.internal.utils.Constants.LINE_SEPARATOR;

/**
 * JsonExtractor hosts JSON and XML methods to parse data.
 */
public class JsonExtractor {

    private static final JSONParser JSON_PARSER = new JSONParser();
    private static final Pattern PATTERN_INDEX = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])");
    private static final Pattern PATTERN_KEY = Pattern.compile("(.*?)\\[(.*?)\\]");

    /**
     * Extract data from JSON String input.
     *
     * @param jsonText JSON contents
     * @param path     JSON path
     * @return {@link com.everwhimsical.extractor.contract.EntityOutput} holds extracted result.
     */
    public static EntityOutput get(String jsonText, String path) {
        return get(JsonFormatterPool.getDefaultJsonEntityFormatter(), jsonText, path);
    }

    /**
     * Extract data from JSON String input.
     *
     * @param entityFormatter Custom EntityFormatter
     * @param jsonText        JSON contents
     * @param path            JSON path
     * @return {@link com.everwhimsical.extractor.contract.EntityOutput} holds extracted result.
     */
    public static EntityOutput get(EntityFormatter entityFormatter, String jsonText, String path) {
        return getOutput(entityFormatter, jsonText, path);
    }

    /**
     * Extract data from JSON POJO input and default EntityFormatter.
     *
     * @param jsonPojo JSON Pojo object
     * @param path     JSON path
     * @param <Input>  Generic POJO class
     * @return {@link com.everwhimsical.extractor.contract.EntityOutput} holds extracted result.
     */
    public static <Input> EntityOutput get(Input jsonPojo, String path) {
        return get(JsonFormatterPool.getDefaultJsonEntityFormatter(), jsonPojo, path);
    }

    /**
     * Extract data from JSON POJO input and default EntityFormatter.
     *
     * @param entityFormatter Custom EntityFormatter
     * @param jsonPojo        JSON Pojo object
     * @param path            JSON path
     * @param <Input>         Generic POJO class
     * @return {@link com.everwhimsical.extractor.contract.EntityOutput} holds extracted result.
     */
    public static <Input> EntityOutput get(EntityFormatter entityFormatter, Input jsonPojo, String path) {
        return getOutput(entityFormatter, entityFormatter.serialize(jsonPojo), path);
    }

    private static EntityOutput getOutput(EntityFormatter entityFormatter, String jsonText, String path) {
        Args.notNull(entityFormatter, "Json EntityFormatter");
        Args.notEmpty(path, "Json path");
        Args.notEmpty(jsonText, "Json text");
//        Args.verifyJson(jsonText, "Json text");
        Object output;
        try {
            Object json = JSON_PARSER.parse(jsonText);
            Stack<String> xpathStack = parsePath(path);
            while (!xpathStack.isEmpty()) {
                String currentXpath = xpathStack.pop();
                json = parseIdentifier(json, currentXpath);
            }
            output = json;
        } catch (ParseException e) {
            throw new ParserException("JSON cannot contain invalid data\n" + jsonText);
        }
        return new JsonOutput(output, entityFormatter);
    }

    private static Stack<String> parsePath(String path) {
        String[] xpathArray = path.split(Pattern.quote("."));
        Stack<String> stack = new Stack<>();
        for (int i = xpathArray.length - 1; i >= 0; i--) {
            stack.add(xpathArray[i]);
        }
        return stack;
    }

    private static Object parseIdentifier(Object json, String currentXpath) {
        if (json instanceof JSONArray) {
            if (!isKeyArray(currentXpath)) {
                throw new ParserException("JSON does not contain the key as an Array. " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
            }
            json = parseArray((JSONArray) json, currentXpath);
        } else if (json instanceof JSONObject) {
            if (isKeyArray(currentXpath)) {
                String key = fetchArrayKey(currentXpath);
                if (!((JSONObject) json).containsKey(key)) {
                    throw new ParserException("JSON does not contain the key " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
                }
                Object obj = ((JSONObject) json).get(key);
                if (!(obj instanceof JSONArray)) {
                    throw new ParserException("JSON does not contain the key's value as an Array. " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
                }
                json = parseArray((JSONArray) obj, currentXpath);
            } else {
                json = parseMap((JSONObject) json, currentXpath);
            }
        } else {
            throw new ParserException("JSON does not contain the key " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
        }
        return json;
    }

    private static Object parseMap(JSONObject json, String currentXpath) {
        if (!json.containsKey(currentXpath)) {
            throw new ParserException("JSON does not contain the key " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
        }
        return json.get(currentXpath);
    }

    private static Object parseArray(JSONArray json, String currentXpath) {
        int index = fetchArrayIndex(currentXpath);
        if (index >= json.size()) {
            throw new ParserException("JSON does not contain the key " + currentXpath + LINE_SEPARATOR + "Current value is " + LINE_SEPARATOR + json);
        }
        return json.get(index);
    }

    private static int fetchArrayIndex(String currentXpath) {
        Matcher matcher = PATTERN_INDEX.matcher(currentXpath);
        String output = "";
        while (matcher.find()) {
            output = matcher.group(0);
        }
        return Integer.valueOf(output);
    }

    private static String fetchArrayKey(String currentXpath) {
        Matcher matcher = PATTERN_KEY.matcher(currentXpath);
        String output = "";
        while (matcher.find()) {
            output = matcher.group(1);
        }
        return output;
    }

    private static boolean isKeyArray(String currentXpath) {
        Matcher matcher = PATTERN_INDEX.matcher(currentXpath);
        return matcher.find();
    }
}
