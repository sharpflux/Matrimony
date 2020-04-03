package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.modal.TimelineModel;

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

        holder.textView_userId.setText(list.get(position).getUserId());
        holder.textView_userName.setText(list.get(position).getUserName());
        //holder.textView_send.setText(list.get(position).getUserAge());

        holder.circleImage_profilePic.setImageURI(list.get(position).getProfilePic());

        holder.textView_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewProfileActivity.class);
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
        public TextView textView_userName;
        public TextView textView_send;
        public CircleImageView circleImage_profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);

            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
           // this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }
}
