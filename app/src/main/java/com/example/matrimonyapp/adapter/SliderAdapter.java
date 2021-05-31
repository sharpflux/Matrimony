package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.sliderModel;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    ArrayList<Uri> arrayList_imageSlider;
    ArrayList<sliderModel> sliderModelArrayList;

    public SliderAdapter(Context context, ArrayList<sliderModel> arrayList_imageSlider) {
        this.context = context;
        this.sliderModelArrayList = arrayList_imageSlider;

    }

    @Override
    public int getCount() {
        return sliderModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.home_image_slider, null);

        ImageView imageView_slider = (ImageView) view.findViewById(R.id.imageView_slider);
        final    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(sliderModelArrayList.get(position).getSliderImgUrl())
                .placeholder(R.drawable.default_profile)
                .error(R.drawable.default_profile)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView_slider);

/*        Glide.with(context)
                .load(sliderModelArrayList.get(position).getSliderImgUrl())
                .placeholder(R.color.quantum_bluegrey900)
                .into(imageView_slider);*/





        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}