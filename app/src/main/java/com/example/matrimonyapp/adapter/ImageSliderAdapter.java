package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ShowImageSliderActivity;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.modal.SingleImage;
import com.example.matrimonyapp.volley.URLs;
import com.google.android.gms.common.api.internal.ConnectionCallbacks;

import java.util.ArrayList;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder >{

    ImageView imageView;
    ArrayList<String> arrayList_singleImage;
    ViewPager2 viewPager2_singleImage;
    Context context;


    public ImageSliderAdapter(Context context, ArrayList<String> arrayList_singleImage, ViewPager2 viewPager2_singleImage) {
        this.arrayList_singleImage = arrayList_singleImage;
        this.viewPager2_singleImage = viewPager2_singleImage;
        this.context = context;
    }


    void setImageView(SingleImage singleImage)
    {
        imageView.setImageURI(Uri.parse(singleImage.getUri()));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view_image_new,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //holder.imageView_galleryImage.setImageURI(Uri.parse(arrayList_singleImage.get(position).getUri()));

        Glide.with(context)
                .load(URLs.MainURL+arrayList_singleImage.get(position))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.color.quantum_grey100)
                .into(holder.imageView_galleryImage);

        holder.imageView_galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ShowImageSliderActivity.class);
                String []sliderImages = new String[arrayList_singleImage.size()];
                for (int i=0; i<arrayList_singleImage.size(); i++)
                {
                    sliderImages[i] = arrayList_singleImage.get(i);
                }
                intent.putExtra("sliderImages", sliderImages);
                context.startActivity(intent);
            }
        });

        if(position == arrayList_singleImage.size() - 1)
        {
            viewPager2_singleImage.post(runnable);
        }




    }

    @Override
    public int getItemCount() {
        return arrayList_singleImage.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView_galleryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_galleryImage = itemView.findViewById(R.id.imageView_galleryImage);



        }



    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2_singleImage.setCurrentItem(0);
            //arrayList_singleImage.addAll(arrayList_singleImage);
        }
    };
}
