package com.everwhimsical.internal.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Commons class holds methods that are widely used.
 */
public class Commons {

    /**
     * Read the file contents that is present in the project.
     *
     * @param resourcePath Relative file path of the resource.
     * @return Contents of file.
     */
    public static String readFile(String resourcePath) {
        String contents;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            URL resource = classLoader.getResource(resourcePath);
            Args.notNull(resource, "File path not found " + resourcePath);
            File file = new File(resource.getFile());
            contents = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return contents;
    }

    /**
     * Check if the String content is either null or empty.
     *
     * @param contents Contents of String.
     * @return True if param is null or empty.
     */
    public static boolean isNullOrEmpty(String contents) {
        return contents == null || contents.isEmpty();
    }

    /**
     * Check if the String content is both null and empty.
     *
     * @param contents Contents of String.
     * @return True if param is null and empty.
     */
    public static boolean isNotNullAndNotEmpty(String contents) {
        return contents != null && !contents.isEmpty();
    }
}
