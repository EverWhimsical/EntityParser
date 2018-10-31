package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Standard implementation to serialize and deserialize JSON objects.
 */
class DefaultJsonEntityFormatter implements EntityFormatter {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public <Input> String serialize(Input jsonPojo) {
        return GSON.toJson(jsonPojo);
    }

    @Override
    public <Output> Output deserialize(String text, Class<Output> clazz) {
        return GSON.fromJson(text, clazz);
    }
}
