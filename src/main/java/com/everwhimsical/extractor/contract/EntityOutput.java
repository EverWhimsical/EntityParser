package com.everwhimsical.extractor.contract;

/**
 * EntityOutput holds contract for converting the extracted output to needed form.
 */
public interface EntityOutput {
    /**
     * Convert to object.
     *
     * @return Plain object of result.
     */
    Object toObject();

    /**
     * Convert to a needed POJO using default serializer.
     *
     * @param clazz    POJO Class
     * @param <Output> Generic POJO class
     * @return POJO object containing result
     */
    <Output> Output toClass(Class<Output> clazz);

    /**
     * Convert to String.
     *
     * @return String representation of result.
     */
    String toString();
}
