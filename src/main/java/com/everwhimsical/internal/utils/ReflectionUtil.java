package com.everwhimsical.internal.utils;

import java.lang.reflect.Field;

/**
 * Basic Reflection utils to fetch and check values.
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * Get the field value
     *
     * @param target Target
     * @param field  Field to be checked
     * @return value of the field
     */
    public static Object getFieldValue(Object target, Field field) {
        try {
            boolean originalFlag = changeAccessibleFlag(field);
            Object fieldValue = field.get(target);
            restoreAccessibleFlag(field, originalFlag);
            return fieldValue;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field " + field.getDeclaringClass().getName() + "/"
                    + field.getName(), e);
        }
    }

    private static void restoreAccessibleFlag(Field field, boolean flag) {
        field.setAccessible(flag);
    }

    private static boolean changeAccessibleFlag(Field field) {
        boolean flag = field.isAccessible();
        field.setAccessible(true);
        return flag;
    }
}