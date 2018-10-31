package com.everwhimsical.extractor.pojo;

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

    @Override
    public boolean equals(Object obj) {
        boolean out;
        if (obj == this) {
            out = true;
        } else if (!(obj instanceof Storage)) {
            out = false;
        } else {
            Storage storageObj = (Storage) obj;
            out = storageObj.getStorageMemory().equals(getStorageMemory())
                    && storageObj.getStorageType().equals(getStorageType());
        }
        return out;
    }
}