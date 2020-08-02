package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.listener.OnSwipeTouchListener;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<TimelineModel> list;

    ImageView imageView_like;
    boolean bool_like=false;
    boolean bool_favorite=false;
    Display display;

    public TimelineAdapter(Context context, ArrayList<TimelineModel> list, Display display)
    {
        this.context = context;
        this.list = list;
        this.display = display;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.profiles_recycler_view, parent, false);
        TimelineAdapter.ViewHolder viewHolder = new TimelineAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final TimelineModel item = list.get(position);


        holder.relativeLayout.setOnTouchListener(new OnSwipeTouchListener(context){
            @Override
            public void onDoubleTaps() {
                super.onDoubleTaps();

                holder.imageView_doubleTapFav.setVisibility(View.VISIBLE);

                holder.imageView_doubleTapFav.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.imageView_doubleTapFav.setVisibility(View.INVISIBLE);
                    }
                },400);

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*list.remove(position);
                        notifyDataSetChanged();*/
                        holder.relativeLayout.animate().alpha(1f).setDuration(400).translationX(0);
                    }
                },400);


                if(bool_favorite==false)
                {

                    holder.imageView_favorite.setImageResource(R.drawable.favoritefilled);

                    //holder.imageView_like.setBackgroundResource(R.drawable.red_heart);
                    bool_favorite= true;
                }
                else
                {
                    holder.imageView_favorite.setImageResource(R.drawable.start1);
                    bool_favorite = false;
                }


                /*
                    contains Code to Add profile to Favorites
                */

            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();


                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(-size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                },400);

               /* list.remove(position);
                notifyDataSetChanged();
*/
            }
        });


        holder.imageView_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(-size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                },400);


            }
        });


        holder.textView_userId.setText(list.get(position).getUserName());
        //holder.textView_userName.setText(list.get(position).getUserName());
        //holder.textView_userAge.setText(list.get(position).getUserAge());
        //holder.textView_userQualification.setText(list.get(position).getUserQualification());
        holder.textView_userBio.setText(Html.fromHtml("<b>"+list.get(position).getUserName()+"</b>  "
                +" "+list.get(position).getUserEmail()+" "));

        //list.get(position).getUserBio()+" "+list.get(position).getUserBio()+" "+list.get(position).getUserBio()
        //holder.imageView_profilePic.setImageURI(list.get(position).getProfilePic());
        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .circleCrop()
                .placeholder(R.drawable.noimage2)
                .into(holder.circleImage_profilePic);

        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .placeholder(R.drawable.noimage2)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.imageView_profilePic);

        //holder.circleImage_profilePic.setImageDrawable(holder.imageView_profilePic.getDrawable());

        holder.textView_userQualification.setText(list.get(position).getUserQualification());

        holder.textView_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewProfileActivity.class);
                context.startActivity(intent);

            }
        });



        holder.imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_like==false)
                {

                    holder.imageView_like.setImageResource(R.drawable.red_heart);

                    //holder.imageView_like.setBackgroundResource(R.drawable.red_heart);
                    bool_like = true;
                }
                else
                {
                    holder.imageView_like.setImageResource(R.drawable.black_heart);
                    bool_like = false;
                }
            }
        });

        holder.imageView_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_favorite==false)
                {

                    holder.imageView_favorite.setImageResource(R.drawable.favoritefilled);

                    //holder.imageView_like.setBackgroundResource(R.drawable.red_heart);
                    bool_favorite = true;
                }
                else
                {
                    holder.imageView_favorite.setImageResource(R.drawable.start1);

                    bool_favorite = false;
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_userId;
        public TextView textView_userName;
        public TextView textView_userQualification;
        public TextView textView_userBio;
        public TextView textView_userAge;
        public ImageView imageView_profilePic;
        public ImageView imageView_like;
        public ImageView imageView_favorite;
        public ImageView imageView_doubleTapFav;
        public ImageView imageView_reject;
        public de.hdodenhof.circleimageview.CircleImageView circleImage_profilePic;
        public RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            //this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            this.textView_userQualification = itemView.findViewById(R.id.textView_userQualification);
            this.textView_userBio = itemView.findViewById(R.id.textView_userBio);
            this.imageView_profilePic = itemView.findViewById(R.id.imageView_profilePic);
            this.imageView_reject = itemView.findViewById(R.id.imageView_reject);
            this.imageView_like = itemView.findViewById(R.id.imageView_like);
            this.imageView_favorite = itemView.findViewById(R.id.imageView_favorite);
            this.imageView_doubleTapFav = itemView.findViewById(R.id.imageView_doubleTapFav);

            setIsRecyclable(false);

        }
    }
}
