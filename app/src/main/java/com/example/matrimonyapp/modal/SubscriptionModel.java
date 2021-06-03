package com.example.matrimonyapp.modal;

public class SubscriptionModel {

    private String packName, tag, amount, description, packType,PackageId;

    public SubscriptionModel() {
    }

    public SubscriptionModel(String packName, String tag, String amount, String description, String packType) {
        this.packName = packName;
        this.tag = tag;
        this.amount = amount;
        this.description = description;
        this.packType = packType;
    }

    public String getPackageId() {
        return PackageId;
    }

    public void setPackageId(String packageId) {
        PackageId = packageId;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackType() {
        return packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }



}
