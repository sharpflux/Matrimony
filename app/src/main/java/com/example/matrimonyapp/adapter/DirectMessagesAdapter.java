package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ChatActivity;
import com.example.matrimonyapp.activity.ChatTest;
import com.example.matrimonyapp.activity.SignalRMessagesActivity;
import com.example.matrimonyapp.activity.SignalRUserChatsActivity;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.volley.URLs;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirectMessagesAdapter extends RecyclerView.Adapter<DirectMessagesAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<DirectMessagesModel> list;
    private boolean isChat;
    int unseenCount=0;

    private String theLastMessage, theLastMessageTime;

    public DirectMessagesAdapter(Context context, ArrayList<DirectMessagesModel> list, boolean isChat)
    {
        this.context = context;
        this.list = list;
        this.isChat = isChat;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final DirectMessagesModel directMessagesModel = list.get(position);

        if(list.get(position).getOnline())
        {
            holder.circleImage_activityStatus.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.circleImage_activityStatus.setVisibility(View.GONE);
        }


        holder.textView_userId.setText(list.get(position).getUserId());
        holder.textView_firebaseUserId.setText(list.get(position).getFirebaseUserId());
        holder.textView_userName.setText(list.get(position).getUserName());
        //holder.textView_lastMessage.setText(list.get(position).getLastMessage());
        //holder.textView_lastMessageTime.setText(list.get(position).getLastMessageTime());
        //holder.textView_send.setText(list.get(position).getUserAge());

        //holder.circleImage_profilePic.setImageURI(list.get(position).getProfilePic());
        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .placeholder(R.color.codeGray)
                .into(holder.circleImage_profilePic);



        holder.relativeLayout_directMesssages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatTest.class);
               // intent.putExtra("FromConnectionId", SignalRUserChatsActivity.FromConnectionId);
                intent.putExtra("connectionId", list.get(position).getFirebaseUserId());
                intent.putExtra("toUserId", list.get(position).getUserId());
                intent.putExtra("toUserName", list.get(position).getUserName());
                intent.putExtra("toUserProfilePic", list.get(position).getProfilePic());
                context.startActivity(intent);

            }
        });

/*        if(isChat)
        {
            lastMessage(directMessagesModel.getFirebaseUserId(), holder.textView_lastMessage, holder.textView_lastMessageTime);
            noOfUnseenMessage(directMessagesModel.getFirebaseUserId(), holder.textView_noOfUnseenMessage, holder.relativeLayout_noOfUnseenMessage);
        }*/
        /*else
        {
            holder.textView_lastMessage.setVisibility(View.GONE);
        }*/


           /* if(Boolean.parseBoolean(directMessagesModel.getActivityStatus()))
            {
                holder.circleImage_activityStatus.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.circleImage_activityStatus.setVisibility(View.GONE);
            }
*/



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_userId;
        public TextView textView_firebaseUserId;
        public TextView textView_userName;
        public TextView textView_lastMessage;
        public TextView textView_lastMessageTime;
        public TextView textView_send;
        public TextView textView_noOfUnseenMessage;
        public RelativeLayout relativeLayout_directMesssages;
        public RelativeLayout relativeLayout_noOfUnseenMessage;
        public CircleImageView circleImage_profilePic;
        public CircleImageView circleImage_activityStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_firebaseUserId = itemView.findViewById(R.id.textView_firebaseUserId);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.textView_lastMessage = itemView.findViewById(R.id.textView_lastMessage);
            this.textView_lastMessageTime = itemView.findViewById(R.id.textView_lastMessageTime);
            this.textView_noOfUnseenMessage = itemView.findViewById(R.id.textView_noOfUnseenMessage);
            this.relativeLayout_directMesssages = itemView.findViewById(R.id.relativeLayout_directMesssages);
            this.relativeLayout_noOfUnseenMessage = itemView.findViewById(R.id.relativeLayout_noOfUnseenMessage);


            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            this.circleImage_activityStatus = itemView.findViewById(R.id.circleImage_activityStatus);
            // this.textView_send = itemView.findViewById(R.id.textView_send);
        }
    }

    private void noOfUnseenMessage(final String firebaseUserId, final TextView textView_noOfUnseenMessage, final RelativeLayout relativeLayout_noOfUnseenMessage)
    {
        unseenCount=0;
       /* final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                unseenCount=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);

                    if (chatModel.getReceiverId().equals(firebaseUser.getUid()) && chatModel.getSenderId().equals(firebaseUserId)) //|| chatModel.getReceiverId().equals(firebaseUserId) && chatModel.getSenderId().equals(firebaseUser.getUid())
                    {
                        if(!chatModel.getMessageStatus().equals("seen"))
                        {
                            unseenCount++;
                        }

                    }

                }

                textView_noOfUnseenMessage.setText(String.valueOf(unseenCount));
                if(unseenCount==0)
                {
                    relativeLayout_noOfUnseenMessage.setVisibility(View.GONE);
                }
                else
                {
                    relativeLayout_noOfUnseenMessage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }


    private void lastMessage(final String firebaseUserId, final TextView textView_lastMessage, final TextView textView_lastMessageTime)
    {
       /* theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);

                    if(chatModel.getReceiverId().equals(firebaseUser.getUid()) && chatModel.getSenderId().equals(firebaseUserId)
                        || chatModel.getReceiverId().equals(firebaseUserId) && chatModel.getSenderId().equals(firebaseUser.getUid()))
                    {
                        theLastMessage = chatModel.getMessage();
                        theLastMessageTime = chatModel.getMessageTime();
                    }

                }

                switch (theLastMessage)
                {
                    case "default":
                        textView_lastMessage.setText("");
                        textView_lastMessageTime.setText("");
                        break;

                    default:
                        textView_lastMessage.setText(theLastMessage);
                        textView_lastMessageTime.setText(theLastMessageTime);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
*/
    }


}
