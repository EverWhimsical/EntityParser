package com.everwhimsical.extractor.json;

import com.everwhimsical.extractor.contract.EntityOutput;
import com.everwhimsical.extractor.contract.EntityFormatter;

class JsonOutput implements EntityOutput {

    private final EntityFormatter entityFormatter;
    private Object jsonObj;

    JsonOutput(Object jsonObj, EntityFormatter entityFormatter) {
        this.jsonObj = jsonObj;
        this.entityFormatter = entityFormatter;
    }

    @Override
    public Object toObject() {
        return jsonObj;
    }

    @Override
    public <Output> Output toClass(Class<Output> clazz) {
        return entityFormatter.deserialize(jsonObj.toString(), clazz);
    }

    @Override
    public String toString() {
        return jsonObj.toString();
    }
}
