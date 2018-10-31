package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.extractor.pojo.Phone;
import com.everwhimsical.internal.utils.Commons;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class DefaultJsonEntityFormatterTest {
    private Phone phone1;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private EntityFormatter entityFormatter;
    private String phone1Str;

    @BeforeClass
    public void setup() throws ParseException {
        String jsonMapStr = Commons.readFile("json/map.json");
        final JSONParser parser = new JSONParser();
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = (Map<String, Object>) parser.parse(jsonMapStr);
        this.phone1Str = jsonMap.get("phone1").toString();
        this.phone1 = gson.fromJson(phone1Str, Phone.class);
        this.entityFormatter = JsonFormatterPool.getDefaultJsonEntityFormatter();
    }

    @Test
    public void testSerialize() {
        String phoneActual = entityFormatter.serialize(phone1);
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneType").toString(), phone1.getPhoneType(), "Phone Type value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneOS").toString(), phone1.getPhoneOS(), "Phone OS value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneMake").toString(), phone1.getPhoneMake(), "Phone Make value mismatch");
        Assert.assertEquals(JsonExtractor.get(phoneActual, "phoneSize").toString(), phone1.getPhoneSize(), "Phone Size value mismatch");
    }

    @Test
    public void testDeserialize() throws Exception {
        Phone phoneActual = entityFormatter.deserialize(phone1Str, Phone.class);
        Assert.assertEquals(phoneActual, phone1, "Phone POJO values are different");
    }
}