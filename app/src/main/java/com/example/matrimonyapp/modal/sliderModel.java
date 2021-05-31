package com.example.matrimonyapp.modal;

public class sliderModel {
    String SliderImgUrl;
    int SliderImgId;


    public sliderModel(String sliderImgUrl, int sliderImgId) {
        SliderImgUrl = sliderImgUrl;
        SliderImgId = sliderImgId;
    }

    public String getSliderImgUrl() {
        return SliderImgUrl;
    }

    public void setSliderImgUrl(String sliderImgUrl) {
        SliderImgUrl = sliderImgUrl;
    }

    public int getSliderImgId() {
        return SliderImgId;
    }

    public void setSliderImgId(int sliderImgId) {
        SliderImgId = sliderImgId;
    }
}

