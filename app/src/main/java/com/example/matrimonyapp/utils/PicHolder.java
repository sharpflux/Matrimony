package com.example.matrimonyapp.utils;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;


/**
 *Author CodeBoy722
 *
 * picture_Adapter's ViewHolder
 */

public class PicHolder extends RecyclerView.ViewHolder{

    public ImageView picture;
    public ImageView videos_icon;
    public CheckBox checkbox;

    public PicHolder(@NonNull View itemView) {
        super(itemView);

        checkbox = itemView.findViewById(R.id.checkbox);
        picture = itemView.findViewById(R.id.imageview);
        videos_icon = itemView.findViewById(R.id.videos_icon);
    }
}
