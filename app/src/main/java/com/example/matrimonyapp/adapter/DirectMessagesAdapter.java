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
import com.example.matrimonyapp.activity.ChatActivity;
import com.example.matrimonyapp.modal.DirectMessagesModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirectMessagesAdapter extends RecyclerView.Adapter<DirectMessagesAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<DirectMessagesModel> list;



    public DirectMessagesAdapter(Context context, ArrayList<DirectMessagesModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_direct_messages, parent, false);
        DirectMessagesAdapter.ViewHolder viewHolder = new DirectMessagesAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final DirectMessagesModel item = list.get(position);

        holder.textView_userId.setText(list.get(position).getUserId());
        holder.textView_lastMessage.setText(list.get(position).getLastMessage());
        holder.textView_lastMessageTime.setText(list.get(position).getLastMessageTime());
        //holder.textView_send.setText(list.get(position).getUserAge());

        holder.circleImage_profilePic.setImageURI(list.get(position).getUri_profilePic());

        holder.textView_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatActivity.class);
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
        public TextView textView_lastMessage;
        public TextView textView_lastMessageTime;
        public TextView textView_send;
        public CircleImageView circleImage_profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_lastMessage = itemView.findViewById(R.id.textView_lastMessage);
            this.textView_lastMessageTime = itemView.findViewById(R.id.textView_lastMessageTime);

            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            // this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }
}
