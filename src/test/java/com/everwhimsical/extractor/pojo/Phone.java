package com.everwhimsical.extractor.pojo;

public class Phone {
    private String phoneType;
    private String phoneOS;
    private String phoneMake;
    private String phoneSize;
    private Storage storage;

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneOS() {
        return phoneOS;
    }

    public void setPhoneOS(String phoneOS) {
        this.phoneOS = phoneOS;
    }

    public String getPhoneMake() {
        return phoneMake;
    }

    public void setPhoneMake(String phoneMake) {
        this.phoneMake = phoneMake;
    }

    public String getPhoneSize() {
        return phoneSize;
    }

    public void setPhoneSize(String phoneSize) {
        this.phoneSize = phoneSize;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String toString() {
        return "ClassPojo [phoneType = " + phoneType + ", phoneOS = " + phoneOS + ", phoneMake = " + phoneMake + ", phoneSize = " + phoneSize + "]";
    }

    @Override
    public boolean equals(Object obj) {
        boolean out;
        if (obj == this) {
            out = true;
        } else if (!(obj instanceof Phone)) {
            out = false;
        } else {
            Phone phoneObj = (Phone) obj;
            out = phoneObj.getPhoneMake().equals(getPhoneMake())
                    && phoneObj.getPhoneOS().equals(getPhoneOS())
                    && phoneObj.getPhoneSize().equals(getPhoneSize())
                    && phoneObj.getPhoneType().equals(getPhoneType());
            Storage storageObj = phoneObj.getStorage();
            boolean out2 = (storageObj == null && getStorage() == null) || (storageObj != null && getStorage() != null
                    && storageObj.equals(getStorage()));
            out = (out && out2);
        }
        return out;
    }
}