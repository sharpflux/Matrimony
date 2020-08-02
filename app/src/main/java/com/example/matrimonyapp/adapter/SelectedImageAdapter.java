package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;

import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.FullImageActivity;
import com.example.matrimonyapp.activity.GalleryActivity;

import java.util.ArrayList;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.ViewHolder>{

    Context context;

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    ArrayList<String> stringArrayList;
    Display display;
    public int folderPosition;

    public SelectedImageAdapter(Context context, ArrayList<String> stringArrayList, Display display) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        this.display = display;
        //this.folderPosition = folderPosition;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_selected_image, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public  void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context)
                .load(stringArrayList.get(position))
                .placeholder(R.color.codeGray)
                .centerCrop()
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullImageActivity.class).putExtra("image", stringArrayList.get(position)));
            }
        });


        holder.imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Point size = new Point();
                display.getSize(size);
                //stringArrayList.remove(position);

                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationY(size.y+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run()
                    {
                        ((GalleryActivity)context).getFolderPosition( stringArrayList.get(position));
                        notifyDataSetChanged();
                    }
                },400);

            }
        });



    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image, imageView_cancel;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            imageView_cancel = (ImageView) itemView.findViewById(R.id.imageView_cancel);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);

            setIsRecyclable(false);
        }
    }
}