package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.parser.exceptions.ParserException;

/**
 * This class is used to expose {@link EntityFormatter} implementations provided.
 */
public final class JsonFormatterPool {

    /**
     * Get entity formatter with default serialize and deserialize operations.
     * <br>This is included by default if no formatter is passed.
     *
     * @return {@link EntityFormatter} object
     */
    public static EntityFormatter getDefaultJsonEntityFormatter() {
        return new DefaultJsonEntityFormatter();
    }

    /**
     * Get entity formatter with custom serialize and deserialize operations.
     * <br>The deserialize operation will check the POJO class for fields with {@link com.everwhimsical.extractor.contract.FieldRequired} annotations.
     * <br>If a field contains it, the output of deserialize must have that field in it.
     * <br>Else {@link ParserException} will be thrown for missing fields.
     *
     * @return {@link EntityFormatter} object
     * @throws ParserException will be thrown for missing fields.
     */
    public static EntityFormatter getFieldCheckJsonEntityFormatter() {
        return new FieldCheckJsonEntityFormatter();
    }
}
