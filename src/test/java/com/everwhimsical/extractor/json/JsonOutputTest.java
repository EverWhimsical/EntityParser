package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityOutput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.everwhimsical.extractor.pojo.Phone;
import com.everwhimsical.extractor.pojo.Storage;
import com.everwhimsical.internal.utils.Commons;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class JsonOutputTest {
    private final String jsonMapStr;
    private final Phone phone1;
    private final Phone phone2;
    private final Storage storage3;
    private final DefaultJsonEntityFormatter serializer;

    public JsonOutputTest() throws ParseException {
        jsonMapStr = Commons.readFile("json/map.json");
        JSONParser parser = new JSONParser();
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = (Map<String, Object>) parser.parse(jsonMapStr);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.phone1 = gson.fromJson(jsonMap.get("phone1").toString(), Phone.class);
        this.phone2 = gson.fromJson(jsonMap.get("phone2").toString(), Phone.class);
        Phone phone3 = gson.fromJson(jsonMap.get("phone3").toString(), Phone.class);
        this.storage3 = phone3.getStorage();

        this.serializer = new DefaultJsonEntityFormatter();
    }

    @Test
    public void testToObject() {
        EntityOutput jsonOutput = JsonExtractor.get(jsonMapStr, "phone1.phoneType");
        Assert.assertEquals(jsonOutput.toObject(), "Bar", "Phone values are different");
    }

    @Test
    public void testToClass() {
        EntityOutput jsonOutput = JsonExtractor.get(jsonMapStr, "phone1");
        Assert.assertEquals(jsonOutput.toClass(Phone.class), phone1, "Phone pojo values are different");
    }

    @Test
    public void testToClassSerializer() {
        EntityOutput jsonOutput1 = JsonExtractor.get(serializer, jsonMapStr, "phone2");
        Assert.assertEquals(jsonOutput1.toClass(Phone.class), phone2, "Phone pojo values are different");

        EntityOutput jsonOutput2 = JsonExtractor.get(serializer, jsonMapStr, "phone3.storage");
        Assert.assertEquals(jsonOutput2.toClass(Storage.class), storage3, "Phone pojo values are different");
    }

    @Test
    public void testToString() {
        EntityOutput jsonOutput = JsonExtractor.get(jsonMapStr, "phone1.phoneType");
        Assert.assertEquals(jsonOutput.toString(), "Bar", "Phone values are different");
    }
}
