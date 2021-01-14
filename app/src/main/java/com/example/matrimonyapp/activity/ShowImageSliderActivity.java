package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ShowImageSliderAdapter;

import java.util.ArrayList;

public class ShowImageSliderActivity extends AppCompatActivity {

    private ViewPager viewPager_imageSlider;
    private ShowImageSliderAdapter imageSliderAdapter;
    private String []sliderImages;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_slider);
       /* String []sliderImages= new String[5];*/

        bundle = getIntent().getExtras();

        sliderImages = new String[5];
        if(bundle!=null)
        {
            sliderImages = bundle.getStringArray("sliderImages");
        }

        /*
        sliderImages[0] = images;
        sliderImages[1] = images;
        sliderImages[2] = images;
        sliderImages[3] = images;
        sliderImages[4] = images;*/


        viewPager_imageSlider = findViewById(R.id.viewPager_imageSlider);
        imageSliderAdapter = new ShowImageSliderAdapter(this, sliderImages);

        viewPager_imageSlider.setAdapter(imageSliderAdapter);

    }
}