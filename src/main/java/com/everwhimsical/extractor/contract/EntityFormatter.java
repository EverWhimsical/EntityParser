package com.everwhimsical.extractor.contract;

/**
 * This interface is used to provide custom implementations to Serialize and Deserialize input.
 */
public interface EntityFormatter {

    /**
     * Convert the POJO to String.
     *
     * @param clazz   POJO object
     * @param <Input> POJO class
     * @return String entity
     */
    <Input> String serialize(Input clazz);

    /**
     * Convert the String to POJO.
     *
     * @param text     String entity
     * @param clazz    POJO object
     * @param <Output> POJO class
     * @return POJO object
     */
    <Output> Output deserialize(String text, Class<Output> clazz);
}
