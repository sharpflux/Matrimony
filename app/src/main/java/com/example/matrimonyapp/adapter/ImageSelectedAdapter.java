package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ImageUploadSelectedActivity;
import com.example.matrimonyapp.activity.ViewProfileDetailsActivity;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.utils.PicHolder;
import com.example.matrimonyapp.utils.PictureFacer;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.view.ViewCompat.setTransitionName;


public class ImageSelectedAdapter  extends RecyclerView.Adapter<ImageSelectedAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<PictureFacer> list;

    boolean bool_like=false;

    public ImageSelectedAdapter(Context context, ArrayList<PictureFacer> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_selected_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageSelectedAdapter.ViewHolder holder, int position) {

        final PictureFacer item = list.get(position);

        Glide.with(context)
                .load(item.getPicturePath())
                .placeholder(R.color.codeGray)
                .into(holder.circleImage_profilePic);

        holder.textView_userName.setText(list.get(position).getPicturName());



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textView_userName;
        public CircleImageView circleImage_profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
        }
    }

}

