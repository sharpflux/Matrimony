package com.example.matrimonyapp.modal;

public class RegisterTransporterModel {

    String vehicleType, capacity, rate;

    public RegisterTransporterModel() {
        vehicleType="";
        capacity="";
        rate="";
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public RegisterTransporterModel(String vehicleType, String capacity, String rate) {
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.rate = rate;
    }
}
