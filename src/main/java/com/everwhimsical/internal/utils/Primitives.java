package com.everwhimsical.internal.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * This class holds the primitive types provided by JVM.
 */
public final class Primitives {
    private static final List<Class<?>> PRIMITIVE_LIST = new ArrayList<Class<?>>() {{
        add(Boolean.TYPE);
        add(Byte.TYPE);
        add(Character.TYPE);
        add(Double.TYPE);
        add(Float.TYPE);
        add(Integer.TYPE);
        add(Long.TYPE);
        add(Short.TYPE);
        add(Void.TYPE);
    }};

    private Primitives() {
    }

    public static List<Class<?>> getPrimitiveList() {
        return PRIMITIVE_LIST;
    }
}

