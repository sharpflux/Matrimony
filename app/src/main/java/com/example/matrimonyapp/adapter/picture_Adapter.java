package com.example.matrimonyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.GalleryItemDisplayActivity;
import com.example.matrimonyapp.utils.PicHolder;
import com.example.matrimonyapp.utils.PictureFacer;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;


public class picture_Adapter extends RecyclerView.Adapter<PicHolder> {

    private ArrayList<PictureFacer> pictureList;
    private GalleryItemDisplayActivity pictureContx;



    public picture_Adapter(ArrayList<PictureFacer> pictureList, GalleryItemDisplayActivity pictureContx) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_gallery_holder, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {

        final PictureFacer imagedata = pictureList.get(position);

        Glide.with(pictureContx)
                .load(imagedata.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);

        setTransitionName(holder.picture, String.valueOf(position) + "_image");

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkbox.isChecked()) {
                    holder.checkbox.setChecked(false);
                    pictureContx.onClick(imagedata, false);
                } else {
                    holder.checkbox.setChecked(true);
                    pictureContx.onClick(imagedata, true);
                }
            }
        });
        if (imagedata.getType().equals("Video")) {
            holder.videos_icon.setVisibility(View.VISIBLE);
        } else {
            holder.videos_icon
                    .setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
