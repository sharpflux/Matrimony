package com.example.matrimonyapp.adapter;

import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.SingleImage;

import java.util.ArrayList;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder >{

    ImageView imageView;
    ArrayList<SingleImage> arrayList_singleImage;
    ViewPager2 viewPager2_singleImage;

    public ImageSliderAdapter(ArrayList<SingleImage> arrayList_singleImage, ViewPager2 viewPager2_singleImage) {
        this.arrayList_singleImage = arrayList_singleImage;
        this.viewPager2_singleImage = viewPager2_singleImage;
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

        holder.imageView_galleryImage.setImageURI(Uri.parse(arrayList_singleImage.get(position).getUri()));


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
