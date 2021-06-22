package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/

import java.util.ArrayList;
import java.util.Calendar;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<ChatModel> list;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private UserModel userModel;
    ChatModel chatModel;
    //    private FirebaseUser firebaseUser;
    public Boolean selectionMode = false;
    public int CountAuxiliar = 0;
    private final ArrayList<Integer> selected = new ArrayList<>();
    androidx.appcompat.widget.Toolbar ChatToolbar;
    private int lastSelectedPosition = -1;  // declare this variable
    public ChatAdapter(Context context, ArrayList<ChatModel> list,  androidx.appcompat.widget.Toolbar chatToolbar) {
        this.context = context;
        this.list = list;
        userModel = CustomSharedPreference.getInstance(context).getUser();
        this.ChatToolbar=chatToolbar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());


        if (viewType == MSG_TYPE_LEFT) {
            View listItem = layoutInflater.inflate(R.layout.recycler_view_message_left, parent, false);
            ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(listItem);
            return viewHolder;

        } else {
            View listItem = layoutInflater.inflate(R.layout.recycler_view_message_right, parent, false);
            ChatAdapter.ViewHolder viewHolder = new ChatAdapter.ViewHolder(listItem);
            return viewHolder;

        }


    }

    @Override
    public int getItemViewType(int position) {
        ChatModel chatModel = list.get(position);

        if (chatModel.getSenderId().equals(userModel.getUserId())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        chatModel = list.get(position);

        holder.textView_message.setText(list.get(position).getMessage());
        holder.tvTime.setText(list.get(position).getMessageTime());
        holder.itemView.setBackgroundColor(chatModel.IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //chatModel.setSelected(true);
                CountAuxiliar = 0;
                selectionMode = true;
                chatModel.setSelected(false);
                lastSelectedPosition = holder.getAdapterPosition();
                v.setBackgroundColor(chatModel.IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.GONE);
                ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.VISIBLE);
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(selectionMode==true) {
                    chatModel = list.get(holder.getAdapterPosition());

                  /*  if (lastSelectedPosition > 0) {
                        chatModel.setSelected(false);
                    }
                    */
                    chatModel.setSelected(!chatModel.IsSelected());
                    holder.itemView.setBackgroundColor(chatModel.IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                    lastSelectedPosition = holder.getAdapterPosition();
                    if (chatModel.IsSelected()) {
                        CountAuxiliar++;
                    } else {
                        CountAuxiliar--;
                    }
                    TextView textView = (TextView) ChatToolbar.findViewById(R.id.tvSelectionCount);
                    textView.setText(String.valueOf(countSelectedItems()));
                    if(CountAuxiliar==0){
                        selectionMode=false;
                        ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                        ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
                    }

                }
            }
        });


        ChatToolbar.findViewById(R.id.imgDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedItems();
            }
        });

        ChatToolbar.findViewById(R.id.imgSelectionModeBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionMode=false;
                unSelectItems();
                ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
            }
        });


    }

    private void deleteSelectedItems() {
        if (chatModel != null){
            for (int i=0; i<list.size(); i++){
                if (list.get(i).IsSelected()){
                    list.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, list.size());
                    selectionMode=false;
                    CountAuxiliar = 0;
                    ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                    ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
                    i--;
                }
            }
        }
    }

    private void unSelectItems() {
        if (chatModel != null){
            for (int i=0; i<list.size(); i++){
                if (list.get(i).IsSelected()){
                    list.get(i).setSelected(false);
                    notifyItemRangeChanged(i, list.size());
                    selectionMode=false;
                    CountAuxiliar = 0;
                    ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                    ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
                    i--;
                }
            }
        }
    }

    private Integer countSelectedItems() {
        Integer selectedCount= 0;
        if (chatModel != null){
            for (int i=0; i<list.size(); i++){
                if (list.get(i).IsSelected()){
                    selectedCount=  selectedCount+1;
                }
            }
        }
        return  selectedCount;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView_message, tvTime;
        public ImageView imgeView_messageStatus;
        LinearLayout timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.tvTime);
            this.timeText = itemView.findViewById(R.id.textView);
            this.textView_message = itemView.findViewById(R.id.textView_message);

        }
    }
}
