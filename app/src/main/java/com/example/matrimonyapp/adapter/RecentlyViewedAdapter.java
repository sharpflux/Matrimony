package com.example.matrimonyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.listener.OnSwipeTouchListener;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.volley.URLs;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ViewHolder> {


    Context context;
    private static int pos;
    private LayoutInflater layoutInflater;
    ArrayList<TimelineModel> list;

    ImageView imageView_like;
    boolean bool_like=false;
    boolean bool_favorite=false;
    Display display;
    //private Bitmap bitmap;
    //private LinearLayout.LayoutParams params;//= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);;

    public RecentlyViewedAdapter(Context context, ArrayList<TimelineModel> list, Display display)
    {
        this.context = context;
        this.list = list;
        this.display = display;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_recently_viewed, parent, false);
        RecentlyViewedAdapter.ViewHolder viewHolder = new RecentlyViewedAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final TimelineModel timelineModel = list.get(position);

        holder.textView_userName.setText(timelineModel.getUserName());


        holder.textView_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("userId", timelineModel.getUserId());
                intent.putExtra("userName", timelineModel.getUserName());
                intent.putExtra("userProfilePic", timelineModel.getProfilePic());
                intent.putExtra("userQualification", timelineModel.getUserQualification());
                intent.putExtra("userOccupation", timelineModel.getUserOccupation());
                intent.putExtra("userCompany", timelineModel.getUserCompany());
                intent.putExtra("userAge", timelineModel.getUserAge());
                context.startActivity(intent);

            }
        });

        holder.textView_userId.setText("@UserId:"+list.get(position).getUserId());


        //list.get(position).getUserBio()+" "+list.get(position).getUserBio()+" "+list.get(position).getUserBio()
        //holder.imageView_profilePic.setImageURI(list.get(position).getProfilePic());
        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .placeholder(R.color.quantum_grey100)
                .into(holder.circleImage_profilePic);



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_userId;
        public TextView textView_userName;
        public TextView textView_send;

        public de.hdodenhof.circleimageview.CircleImageView circleImage_profilePic;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.textView_send = itemView.findViewById(R.id.textView_send);

            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);


            setIsRecyclable(false);

        }
    }


}
