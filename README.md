# Entity Parser

## Synopsis

EntityParser is used to **extract, insert, update, delete** data from a valid JSON entity

## Motivation

The goal behind building this project is to quickly handle simple and complex entity parsing operations with ease.

## Installation

To use EntityParser, kindly add the dependency to your JAVA project.
```
<dependency>
    <groupId>com.everwhimsical</groupId>
    <artifactId>entity-parser</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
**P.S: This dependency is not deployed into maven repository yet.**

## Code Example

### Extract 
We would be able to extract contents for a specific JSON path. 
The output can be redirected to String or Class (By using default/custom Serializer) 
For instance, 

Consider that the below JSON content is stored in variable `jsonMapStr`,
```
{
  "phone3" : {
    "phoneType": "Bar",
    "phoneOS" : "Android",
    "phoneMake": "HTC",
    "phoneSize" : 6,
    "storage": {
      "storageType": "internal",
      "storageMemory" : "16 GB"
    }
  }
}
```
Example 1: Let us fetch the value of JSON path `phone3.phoneType`. It's value is `Bar`. 
`String phoneType = JsonExtractor.get(jsonMapStr, "phone3.phoneType").toString();`
The above code will query the JSON contents, iterate using the key and fetch the value.

Example 2: Let us fetch the value from a JSON Array. 

Consider that the below JSON content is stored in variable `jsonArrayStr2`,
```
{
  "phoneList": [
    {
      "phoneType": "Bar",
      "phoneOS": "iOS",
      "phoneMake": "Apple",
      "phoneSize": 4
    },
    {
      "phoneType": "Bar",
      "phoneOS": "Android",
      "phoneMake": "HTC",
      "phoneSize": 6,
      "storage": {
        "storageType": "internal",
        "storageMemory": "16 GB"
      }
    }
  ]
}
```
The `phoneList` is an array and we need to fetch the second index in it.
`String storageType = JsonExtractor.get(jsonMapStr, "phoneList[1].storage.storageType").toString();`
The JSON path is `phoneList[1].storage.storageType` and it's value is `internal`.

If we need to extract the contents and load the result into a POJO, we have to call `JsonExtractor.get(jsonStr, jsonPath).toClass(<Class>)`
Let us extract the storage value from `jsonArrayStr2` and load it into below POJO.
There should be a defined class like below matching the JSON syntax,
```
//Storage.class
public class Storage {
    private String storageMemory;
    private String storageType;

    public String getStorageMemory() {
        return storageMemory;
    }

    public void setStorageMemory(String storageMemory) {
        this.storageMemory = storageMemory;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    @Override
    public String toString() {
        return "ClassPojo [storageMemory = " + storageMemory + ", storageType = " + storageType + "]";
    }
}
```
`Storage actualStorage = JsonExtractor.get(jsonArrayStr2, "phoneList[1].storage").toClass(Storage.class);`
The `actualStorage` object now contains the values of `storageType` ("internal") and `storageMemory` ("16 GB").

In some cases, the JSON input will be as a POJO. 
`Storage actualStorage = JsonExtractor.get(this.phone3, "phoneList[1].storage").toClass(Storage.class);`

For any serialize or deserialize operation, we can either use implementations provided in `com.everwhimsical.extractor.json.JsonFormatterPool`

1. `JsonFormatterPool#getDefaultJsonEntityFormatter()`
    Get entity formatter with default serialize and deserialize operations. This is included by default if no formatter is passed.
2. `JsonFormatterPool#getFieldCheckJsonEntityFormatter()`
    Get entity formatter with custom serialize and deserialize operations. 
    The deserialize operation will check the POJO class for fields with `com.everwhimsical.extractor.contract.FieldRequired` annotations.
    If a field contains it, the output of deserialize must have that field in it.
    Else `com.everwhimsical.parser.exceptions.ParserException` will be thrown for missing fields.

(or) 

Provide your custom operation by implementing the interface `com.everwhimsical.extractor.contract.EntityFormatter`

## API Reference
<!--TODO Include Javadoc reference link
Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live.
-->
## Tests

To test the project, clone it and run the command `mvn clean test` from the project base directory. 

## Contributors

To contribute to the project, kindly fork the repo and create a PR.


## License
<!--TODO Include license into the project
A short snippet describing the license (MIT, Apache, etc.)
-->