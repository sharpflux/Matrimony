package com.example.matrimonyapp.modal;

public class AddPersonModel {

    String id, details_id,  name, mobileNo, address;

    public AddPersonModel() {
    }
/*

    public AddPersonModel(String id, String name, String mobileNo) {
        this.id = id;
        this.name = name;
        this.mobileNo = mobileNo;
    }
*/

    public AddPersonModel(String id, String details_id, String name, String address) {
        this.id = id;
        this.name = name;
        this.details_id = details_id;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

/*    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }*/

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
