package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<ChatModel> list;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private UserModel userModel;

    private FirebaseUser firebaseUser;


    public ChatAdapter(Context context, ArrayList<ChatModel> list)
    {
        this.context = context;
        this.list = list;
        userModel = CustomSharedPreference.getInstance(context).getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());


        if(viewType == MSG_TYPE_LEFT)
        {
            View listItem = layoutInflater.inflate(R.layout.recycler_view_message_left, parent, false);
            ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(listItem);
            return viewHolder;

        }
        else
        {
            View listItem = layoutInflater.inflate(R.layout.recycler_view_message_right, parent, false);
            ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(listItem);
            return viewHolder;

        }


    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = list.get(position);

        //firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatModel.getSenderId().equals(userModel.getUserId()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }



    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ChatModel chatModel = list.get(position);

        holder.textView_message.setText(list.get(position).getMessage());
//        holder.textView_messageTime.setText(list.get(position).getMessageTime());

      /*  if(chatModel.getMessageStatus().equals("seen"))
        {
            holder.imageView_messageStatus.setImageDrawable(context.getDrawable(R.drawable.double_check));
        }
        else if(chatModel.getMessageStatus().equals("delivered"))
        {
            holder.imageView_messageStatus.setImageDrawable(context.getDrawable(R.drawable.check));
        }
        else if(chatModel.getMessageStatus().equals("notDelivered"))
        {
            holder.imageView_messageStatus.setImageDrawable(context.getDrawable(R.drawable.clock));
        }
*/


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

        public  TextView textView_message, textView_messageTime;
        public ImageView imageView_messageStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            /*this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_lastMessage = itemView.findViewById(R.id.textView_lastMessage);
            this.textView_lastMessageTime = itemView.findViewById(R.id.textView_lastMessageTime);*/

            this.textView_message = itemView.findViewById(R.id.textView_message);
            /*this.textView_messageTime = itemView.findViewById(R.id.textView_messageTime);
            this.imageView_messageStatus = itemView.findViewById(R.id.imageView_messageStatus);*/
            //this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            // this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }
}
