package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.extractor.contract.FieldRequired;
import com.everwhimsical.internal.utils.Primitives;
import com.everwhimsical.internal.utils.ReflectionUtil;
import com.everwhimsical.parser.exceptions.ParserException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.everwhimsical.internal.utils.Constants.LINE_SEPARATOR;

class FieldCheckJsonEntityFormatter implements EntityFormatter {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public <Input> String serialize(Input jsonPojo) {
        return GSON.toJson(jsonPojo);
    }

    @Override
    public <Output> Output deserialize(String text, Class<Output> clazz) {
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(clazz, new AnnotatedDeserializer<Output>()).create();
        return gson.fromJson(text, clazz);
    }

    private class AnnotatedDeserializer<T> implements JsonDeserializer<T> {

        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) {

            T target = GSON.fromJson(je, type);
            checkRequired(target);
            return target;
        }

        private List<Field> findMissingFields(Object target, List<Field> invalidFields) {

            for (Field field : target.getClass().getDeclaredFields()) {
                if (field.getAnnotation(FieldRequired.class) != null) {
                    Object fieldValue = ReflectionUtil.getFieldValue(target, field);

                    if (fieldValue == null) {
                        invalidFields.add(field);
                        continue;
                    }

                    if (!isPrimitive(fieldValue)) {
                        findMissingFields(fieldValue, invalidFields);
                    }
                }
            }
            return invalidFields;
        }

        private void checkRequired(Object target) {

            List<Field> invalidFields = new ArrayList<>();
            findMissingFields(target, invalidFields);

            if (!invalidFields.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Field invalidField : invalidFields) {
                    sb.append(invalidField.getDeclaringClass().getName())
                            .append(" - ")
                            .append(invalidField.getName())
                            .append(" (")
                            .append(invalidField.getType().getName())
                            .append(")")
                            .append(LINE_SEPARATOR);
                }
                throw new ParserException("JSON is missing required fields:" + LINE_SEPARATOR + sb.toString());
            }
        }

        private boolean isPrimitive(Object target) {

            for (Class<?> primitiveClass : Primitives.getPrimitiveList()) {
                if (primitiveClass.equals(target.getClass())) {
                    return true;
                }
            }
            return false;
        }

    }
}
