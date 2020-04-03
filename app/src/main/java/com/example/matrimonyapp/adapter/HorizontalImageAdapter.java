package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewImageActivity;
import com.example.matrimonyapp.modal.SingleImage;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalImageAdapter extends RecyclerView.Adapter<HorizontalImageAdapter.ViewHolder> {

    Intent intent;
    Context context;
    ArrayList<SingleImage> arrayList_horizontalImage;

    public HorizontalImageAdapter(Context context, ArrayList<SingleImage> arrayList_horizontalImage)
    {
        this.context = context;
        this.arrayList_horizontalImage = arrayList_horizontalImage;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_image_gallery,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.circleImageView.setImageURI(Uri.parse(arrayList_horizontalImage.get(position).getUri()));

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(context, ViewImageActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("arrayList",(Serializable)arrayList_horizontalImage);
                bundle.putInt("position",position);

                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList_horizontalImage.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleImage_horizontalImage);

        }
    }
}
