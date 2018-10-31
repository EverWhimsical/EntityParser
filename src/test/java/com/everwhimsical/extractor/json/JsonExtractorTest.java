package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityFormatter;
import com.everwhimsical.extractor.pojo.DummyEntity;
import com.everwhimsical.parser.exceptions.ParserException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.everwhimsical.extractor.pojo.Phone;
import com.everwhimsical.extractor.pojo.PhoneList;
import com.everwhimsical.extractor.pojo.Storage;
import com.everwhimsical.internal.utils.Commons;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonExtractorTest {
    private final String jsonMapStr;
    private final String jsonArrayStr1;
    private final Phone phone1;
    private final Phone phone2;
    private final Phone phone3;
    private final List<Phone> phones;
    private final PhoneList phoneList;
    private final Storage storage3;
    private final Type listType = new TypeToken<List<Phone>>(){}.getType();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final EntityFormatter entityFormatter;
    private final JSONParser parser = new JSONParser();

    public JsonExtractorTest() throws ParseException {
        jsonMapStr = Commons.readFile("json/map.json");
        jsonArrayStr1 = Commons.readFile("json/array1.json");
        String jsonArrayStr2 = Commons.readFile("json/array2.json");
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = (Map<String, Object>) parser.parse(jsonMapStr);
        this.phone1 = gson.fromJson(jsonMap.get("phone1").toString(), Phone.class);
        this.phone2 = gson.fromJson(jsonMap.get("phone2").toString(), Phone.class);
        this.phone3 = gson.fromJson(jsonMap.get("phone3").toString(), Phone.class);
        this.storage3 = phone3.getStorage();

        @SuppressWarnings("unchecked")
        List<Object> jsonArray = (List<Object>) parser.parse(jsonArrayStr1);
        this.phones = gson.fromJson(jsonArray.toString(), listType);

        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap2 = (Map<String, Object>) parser.parse(jsonArrayStr2);
        this.phoneList = gson.fromJson(jsonMap2.toString(), PhoneList.class);

        this.entityFormatter = new DefaultJsonEntityFormatter();
    }

    @Test
    public void testJsonStringArray() {
        String phoneType = JsonExtractor.get(jsonMapStr, "phone1.phoneType").toString();
        Assert.assertEquals(phoneType, "Bar", "Mismatch in value for phoneType");
        Assert.assertEquals(JsonExtractor.get(jsonArrayStr1, "[0].phoneType").toString(), "Bar", "Mismatch in value for phoneType in Array");
        Assert.assertEquals(JsonExtractor.get(jsonArrayStr1, "[1].phoneMake").toString(), "Samsung", "Mismatch in value for phoneType in Array");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(jsonArrayStr1, "[2].phoneSize").toString()), 6, "Mismatch in value for phoneType in Array");
    }

    @Test
    public void testJsonString() {
        @SuppressWarnings("unchecked")
        Map<String, String> storageMap = (Map<String, String>) JsonExtractor.get(jsonArrayStr1, "[2].storage").toObject();
        Assert.assertEquals(storageMap.get("storageType"), "internal", "Storage Type mismatch");
        Assert.assertEquals(storageMap.get("storageMemory"), "16 GB", "Storage Type mismatch");
    }

    @Test
    public void testMapJsonToClass() {
        Phone actualPhone1 = JsonExtractor.get(jsonMapStr, "phone1").toClass(Phone.class);
        Assert.assertEquals(actualPhone1, phone1, "Phone values are different");
        Phone actualPhone2 = JsonExtractor.get(jsonMapStr, "phone2").toClass(Phone.class);
        Assert.assertEquals(actualPhone2, phone2, "Phone values are different");
    }

    @Test
    public void testArrayJsonToClass() {
        Phone actualPhone3 = JsonExtractor.get(jsonArrayStr1, "[0]").toClass(Phone.class);
        Assert.assertEquals(actualPhone3, phone1, "Phone values are different");
        Phone actualPhone4 = JsonExtractor.get(jsonArrayStr1, "[1]").toClass(Phone.class);
        Assert.assertEquals(actualPhone4, phone2, "Phone values are different");
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key.*")
    public void testArrayJsonToClassNegative1() {
        JsonExtractor.get(jsonArrayStr1, "[0].phoneMakeInvalid").toClass(Phone.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key.*")
    public void testArrayJsonToClassNegative2() {
        JsonExtractor.get(jsonArrayStr1, "[3]").toClass(Phone.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key.*")
    public void testArrayJsonToClassNegative3() {
        JsonExtractor.get(jsonArrayStr1, "[2].storage.storageType.noKey");
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key.*")
    public void testJsonStringNegative() {
        JsonExtractor.get(jsonArrayStr1, "[2].storage.storagetype");
    }

    @Test
    public void testJsonFieldCheckSerializer() throws ParseException {
        String json = "{\"dummy1\": {\"str2\" : \"val2\", \"str3\" : \"val3\"}}";
        EntityFormatter fieldFormatter = JsonFormatterPool.getFieldCheckJsonEntityFormatter();
        DummyEntity dummyEntity = JsonExtractor.get(fieldFormatter, parser.parse(json), "dummy1").toClass(DummyEntity.class);
        Assert.assertEquals(dummyEntity.getStr2(), "val2", "str2 value mismatch");
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON is missing required fields.*")
    public void testJsonFieldCheckSerializerNegative() throws ParseException {
        String json = "{\"dummy1\": {\"str1\" : \"val1\", \"str3\" : \"val3\"}}";
        EntityFormatter fieldFormatter = JsonFormatterPool.getFieldCheckJsonEntityFormatter();
        JsonExtractor.get(fieldFormatter, parser.parse(json), "dummy1").toClass(DummyEntity.class);
    }

    @Test
    public void testJsonPojo1() {
        Assert.assertEquals(JsonExtractor.get(phone1, "phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(phone1, "phoneOS").toString(), "iOS", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(phone1, "phoneMake").toString(), "Apple", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(phone1, "phoneSize").toString()), 4, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonPojo2() {
        Assert.assertEquals(JsonExtractor.get(phone2, "phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(phone2, "phoneOS").toString(), "Android", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(phone2, "phoneMake").toString(), "Samsung", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(phone2, "phoneSize").toString()),5, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonPojo3() {
        //Array
        Assert.assertEquals(JsonExtractor.get(phones, "[0].phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(phones, "[0].phoneOS").toString(), "iOS", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(phones, "[0].phoneMake").toString(), "Apple", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(entityFormatter,phones, "[0].phoneSize").toString()), 4, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonPojoArray() {
        Object phoneList = JsonExtractor.get(this.phoneList, "phoneList");
        List<Phone> actual = gson.fromJson(phoneList.toString(), listType);
        Assert.assertEquals(actual, phones, "Mismatch in array contents");
    }

    @Test
    public void testJsonSerializer1() {
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone1, "phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone1, "phoneOS").toString(), "iOS", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone1, "phoneMake").toString(), "Apple", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(entityFormatter, phone1, "phoneSize").toString()), 4, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonSerializer2() {
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone2, "phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone2, "phoneOS").toString(), "Android", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phone2, "phoneMake").toString(), "Samsung", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(entityFormatter, phone2, "phoneSize").toString()),5, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonSerializer3() {
        //Array
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phones, "[0].phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phones, "[0].phoneOS").toString(), "iOS", "Mismatch in phoneOS");
        Assert.assertEquals(JsonExtractor.get(entityFormatter, phones, "[0].phoneMake").toString(), "Apple", "Mismatch in phoneMake");
        Assert.assertEquals(Integer.parseInt(JsonExtractor.get(entityFormatter,phones, "[0].phoneSize").toString()), 4, "Mismatch in phoneSize");
    }

    @Test
    public void testJsonSerializer4() {
        Object phoneList = JsonExtractor.get(entityFormatter, this.phoneList, "phoneList");
        List<Phone> actual = gson.fromJson(phoneList.toString(), listType);
        Assert.assertEquals(actual, phones, "Mismatch in array contents");
    }

    @Test
    public void testJsonSerializer5() {
        Storage actualStorage3 = JsonExtractor.get(entityFormatter, this.phone3, "storage").toClass(Storage.class);
        Assert.assertEquals(actualStorage3, storage3, "Mismatch in phone 3 values");
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON cannot contain invalid data.*")
    public void testJsonInvalid() {
        String json = "{\"dummy1\": val1}";
        JsonExtractor.get(json, "dummy1").toClass(Storage.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key as an Array.*")
    public void testJsonArrayKeyInvalid() {
        JsonExtractor.get(this.phones, "dummy1").toClass(Storage.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key's value as an Array.*")
    public void testJsonMapArrayKeyInvalid1() {
        JsonExtractor.get(this.phone3, "storage[0]").toClass(Storage.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key's value as an Array.*")
    public void testJsonMapArrayKeyInvalid2() {
        JsonExtractor.get(this.phone2, "phoneOS[0]").toClass(Storage.class);
    }

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = ".*JSON does not contain the key.*")
    public void testJsonMapKeyInvalid() {
        JsonExtractor.get(this.phoneList, "phoneList1[3]");
    }

    @Test
    public void testJsonMapArrayKey() {
        Assert.assertEquals(JsonExtractor.get(this.phoneList, "phoneList[2].phoneType").toString(), "Bar", "Mismatch in phoneType");
        Assert.assertEquals(JsonExtractor.get(this.phoneList, "phoneList[1].phoneMake").toString(), "Samsung", "Mismatch in phoneMake");
    }
}