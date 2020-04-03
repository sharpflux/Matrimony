package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.ChatModel;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<ChatModel> list;



    public ChatAdapter(Context context, ArrayList<ChatModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_text_message, parent, false);
        ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ChatModel item = list.get(position);

        holder.textView_messageSent.setText(list.get(position).getMessageSent());
        holder.textView_messageTime.setText(list.get(position).getMessageTime());
        /*holder.textView_lastMessageTime.setText(list.get(position).getLastMessageTime());
        //holder.textView_send.setText(list.get(position).getUserAge());

        holder.circleImage_profilePic.setImageURI(list.get(position).getUri_profilePic());

        holder.textView_userId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatActivity.class);
                context.startActivity(intent);

            }
        });
*/



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

/*        public TextView textView_userId;
        public TextView textView_lastMessage;
        public TextView textView_lastMessageTime;
        public TextView textView_send;
        public CircleImageView circleImage_profilePic;*/

        public  TextView textView_messageSent, textView_messageTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_lastMessage = itemView.findViewById(R.id.textView_lastMessage);
            this.textView_lastMessageTime = itemView.findViewById(R.id.textView_lastMessageTime);*/

            this.textView_messageSent = itemView.findViewById(R.id.textView_messageSent);
            this.textView_messageTime = itemView.findViewById(R.id.textView_messageTime);

            //this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            // this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }
}
