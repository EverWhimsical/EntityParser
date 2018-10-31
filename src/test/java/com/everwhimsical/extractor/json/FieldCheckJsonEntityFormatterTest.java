package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.extractor.pojo.DummyEntity;
import com.everwhimsical.extractor.pojo.Phone;
import com.everwhimsical.internal.utils.Commons;
import com.everwhimsical.parser.exceptions.ParserException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class FieldCheckJsonEntityFormatterTest {
    private Phone phone3;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private EntityFormatter entityFormatter;
    private String phone3Str;

    @BeforeClass
    public void setup() throws ParseException {
        String jsonMapStr = Commons.readFile("json/map.json");
        final JSONParser parser = new JSONParser();
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = (Map<String, Object>) parser.parse(jsonMapStr);
        this.phone3Str = jsonMap.get("phone3").toString();
        this.phone3 = gson.fromJson(phone3Str, Phone.class);
        this.entityFormatter = JsonFormatterPool.getFieldCheckJsonEntityFormatter();
    }

    @Test
    public void testSerialize() throws Exception {
        String phoneActual = entityFormatter.serialize(phone3);
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneType").toString(), phone3.getPhoneType(), "Phone Type value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneOS").toString(), phone3.getPhoneOS(), "Phone OS value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneMake").toString(), phone3.getPhoneMake(), "Phone Make value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneSize").toString(), phone3.getPhoneSize(), "Phone Size value mismatch");
    }

    @Test
    public void testDeserializeNoAnnotation() throws Exception {
        Phone phoneActual = entityFormatter.deserialize(phone3Str, Phone.class);
        Assert.assertEquals(phoneActual, phone3, "Phone POJO values are different");
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON is missing required fields:.*")
    public void testDeserializeAnnotationNegative() throws Exception {
        String json = "{\"str1\" : \"val1\", \"str3\" : \"val3\"}";
        entityFormatter.deserialize(json, DummyEntity.class);
    }

    @Test
    public void testDeserializeAnnotationPositive() throws Exception {
        String json = "{\"str2\" : \"val1\", \"str3\" : \"val3\"}";
        DummyEntity dummyEntity = entityFormatter.deserialize(json, DummyEntity.class);
        Assert.assertEquals(dummyEntity.getStr2(), "val1", "Str2 value mismatch");
    }
}