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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.activity.ViewProfileDetailsActivity;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestListAdapter extends RecyclerView.Adapter<InterestListAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<TimelineModel> list;

    boolean bool_like=false;

    public InterestListAdapter(Context context, ArrayList<TimelineModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_interest_list, parent, false);
        InterestListAdapter.ViewHolder viewHolder = new InterestListAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final TimelineModel item = list.get(position);
        Glide.with(context)
                .load(URLs.MainURL+item.getProfilePic())
                .placeholder(R.color.codeGray)
                //.centerCrop()
               // .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(holder.circleImage_profilePic);
       // holder.textView_userBirthday.setText(list.get(position).getUserId());
        holder.textView_userName.setText(list.get(position).getUserName());
        //holder.textView_send.setText(list.get(position).getUserAge());




        holder.linearLayout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewProfileDetailsActivity.class);
                intent.putExtra("userId", item.getUserId());
                intent.putExtra("userName", item.getUserName());
                intent.putExtra("userProfilePic", item.getProfilePic());
                context.startActivity(intent);

            }
        });




    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_userId;
        public TextView textView_userBirthday;
        public TextView textView_userName;
        public TextView textView_send;
        public LinearLayout linearLayout_user, linearLayout_requested;

        public CircleImageView circleImage_profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_userBirthday = itemView.findViewById(R.id.textView_userBirthday);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.linearLayout_user = itemView.findViewById(R.id.linearLayout_user);
            this.linearLayout_requested = itemView.findViewById(R.id.linearLayout_requested);

            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }
}
