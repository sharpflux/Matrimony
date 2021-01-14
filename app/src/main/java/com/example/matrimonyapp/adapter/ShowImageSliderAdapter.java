package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.volley.URLs;

import java.util.Objects;

public class ShowImageSliderAdapter extends PagerAdapter {

    Context context;
    String [] sliderImages;

    LayoutInflater layoutInflater;

    public ShowImageSliderAdapter(Context context, String[] sliderImages) {
        this.context = context;
        this.sliderImages = sliderImages;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sliderImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {


        return view == ((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View itemView =layoutInflater.inflate(R.layout.view_pager_show_image_slider, container, false);

        ImageView imageView_sliderImage = itemView.findViewById(R.id.imageView_sliderImage);


        Glide.with(context)
                .load(URLs.MainURL+sliderImages[position])
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.color.quantum_grey100)
                .into(imageView_sliderImage);

        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }
}
