package com.example.matrimonyapp.adapter;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.GalleryImageModel;

import java.util.ArrayList;

public class GalleryImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<GalleryImageModel> galleryImageModelArrayList;
    private static OnItemClickListener onItemClickListener;
    private final static int IMAGE_LIST = 0;
    private final static int IMAGE_PICKER = 1;

    public GalleryImagesAdapter (Context context, ArrayList<GalleryImageModel> galleryImageModelArrayList) {
        this.context = context;
        this.galleryImageModelArrayList = galleryImageModelArrayList;
    }

    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //if (viewType == IMAGE_LIST)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_gallery_image, parent, false);
            return new ImageListViewHolder(view);
        }

        /* else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_picker_list, parent, false);
            return new ImagePickerViewHolder(view);
        }*/
    }

    @Override
    public int getItemViewType(int position) {
        return position < 2 ? IMAGE_PICKER : IMAGE_LIST;
    }

    @Override
    public  void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //if (holder.getItemViewType() == IMAGE_LIST)
        {;
            final ImageListViewHolder viewHolder = (ImageListViewHolder) holder;
            Glide.with(context)
                    .load(galleryImageModelArrayList.get(position).getImagePath())
                    .placeholder(R.color.codeGray)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(viewHolder.image);

            if (galleryImageModelArrayList.get(position).isSelected()) {;
                viewHolder.checkBox.setChecked(true);
            } else {;
                viewHolder.checkBox.setChecked(false);
            }
        }
        /*else {;
            ImagePickerViewHolder viewHolder = (ImagePickerViewHolder) holder;
            //viewHolder.image.setImageResource(imageList.get(position).getResImg());


            Glide.with(context).load(galleryImageModelArrayList.get(position).getImagePath())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(viewHolder.image);


            viewHolder.title.setText(galleryImageModelArrayList.get(position).getTitle());

        }*/
    }

    @Override
    public int getItemCount() {
        return galleryImageModelArrayList.size();
    }

    public class ImageListViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CheckBox checkBox;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            checkBox = itemView.findViewById(R.id.circle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public class ImagePickerViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ImagePickerViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

}