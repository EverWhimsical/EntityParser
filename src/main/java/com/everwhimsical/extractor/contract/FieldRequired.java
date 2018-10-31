package com.everwhimsical.extractor.contract;

import com.everwhimsical.parser.exceptions.ParserException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The deserialize operation will check the POJO class for fields with {@link com.everwhimsical.extractor.contract.FieldRequired} annotations.
 * <br>If a field contains it, the output of deserialize must have that field in it.
 * <br>Else {@link ParserException} will be thrown for missing fields.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldRequired {

}
