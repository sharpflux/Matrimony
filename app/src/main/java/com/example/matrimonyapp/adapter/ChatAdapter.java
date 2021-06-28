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
import com.example.matrimonyapp.modal.DateItem;
import com.example.matrimonyapp.modal.ListItem;
import com.example.matrimonyapp.modal.MineItem;
import com.example.matrimonyapp.modal.OtherItem;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
/*import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;*/

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    List<ListItem> consolidatedList = new ArrayList<>();
    ArrayList<ChatModel> list;
    private List<ListItem> listObjects;
    ChatModel chatModel;
    public int CountAuxiliar = 0;
    private final ArrayList<Integer> selected = new ArrayList<>();
    androidx.appcompat.widget.Toolbar ChatToolbar;
    Boolean selectionMode=false;
    Integer lastSelectedPosition;
    public ChatAdapter(Context context, List<ListItem> consolidatedList,androidx.appcompat.widget.Toolbar chatToolbar,ArrayList<ChatModel> list) {
        this.consolidatedList = consolidatedList;
        this.mContext = context;
        this.list = list;
        this.ChatToolbar=chatToolbar;
    }

    public void setDataChange(List<ListItem> asList) {
        this.consolidatedList = asList;
        //now, tell the adapter about the update
        notifyDataSetChanged();
        //notifyItemInserted(asList.size()+1);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_OTHER:
                View currentUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_message_left, parent, false);
                viewHolder = new OtherMessageViewHolder(currentUserView); // view holder for normal items
                break;
            case ListItem.TYPE_MINE:
                View otherUserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_message_right, parent, false);
                viewHolder = new MineMessageViewHolder(otherUserView); // view holder for normal items
                break;
            case ListItem.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.recycler_view_date, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;



   /*     RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ListItem.TYPE_MINE:
                View v1 = inflater.inflate(R.layout.recycler_view_message_right, parent, false);
                viewHolder = new MineMessageViewHolder(v1);
                break;
            case ListItem.TYPE_OTHER:
                View v3 = inflater.inflate(R.layout.recycler_view_message_left, parent, false);
                viewHolder = new OtherMessageViewHolder(v3);
                break;
            case ListItem.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.recycler_view_date, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;*/
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {





        switch (viewHolder.getItemViewType()) {


            case ListItem.TYPE_MINE:
                final  MineItem generalItem   = (MineItem) consolidatedList.get(position);
                MineMessageViewHolder generalViewHolder= (MineMessageViewHolder) viewHolder;
                generalViewHolder.txtTitle.setText(generalItem.getPojoOfJsonArray().getMessage() +" "+position);
                generalViewHolder.tvTime.setText(generalItem.getPojoOfJsonArray().getTime());

                generalViewHolder.itemView.setBackgroundColor(generalItem.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);

                generalViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //chatModel.setSelected(true);
                        CountAuxiliar = 0;
                        selectionMode = true;
                        generalItem.getPojoOfJsonArray().setSelected(false);
                        lastSelectedPosition = viewHolder.getAdapterPosition();
                        v.setBackgroundColor(generalItem.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                        ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.GONE);
                        ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.VISIBLE);
                        return false;
                    }
                });


                generalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(selectionMode==true) {

                            generalItem.getPojoOfJsonArray().setSelected(!generalItem.getPojoOfJsonArray().IsSelected());
                            view.setBackgroundColor(generalItem.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                            lastSelectedPosition = position;
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



                break;
            case ListItem.TYPE_OTHER:

              final    OtherItem generalItem2   = (OtherItem) consolidatedList.get(position);
                OtherMessageViewHolder generalViewHolder2= (OtherMessageViewHolder) viewHolder;
                generalViewHolder2.txtTitle.setText(generalItem2.getPojoOfJsonArray().getMessage()  +" "+position);

                generalViewHolder2.tvTime.setText(generalItem2.getPojoOfJsonArray().getTime());
                generalViewHolder2.itemView.setBackgroundColor(generalItem2.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);

                generalViewHolder2.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //chatModel.setSelected(true);
                        CountAuxiliar = 0;
                        selectionMode = true;
                        generalItem2.getPojoOfJsonArray().setSelected(false);
                        lastSelectedPosition = viewHolder.getAdapterPosition();
                        v.setBackgroundColor(generalItem2.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                        ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.GONE);
                        ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.VISIBLE);
                        return false;
                    }
                });


                generalViewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(selectionMode==true) {

                            generalItem2.getPojoOfJsonArray().setSelected(!generalItem2.getPojoOfJsonArray().IsSelected());
                            view.setBackgroundColor(generalItem2.getPojoOfJsonArray().IsSelected() ? Color.parseColor("#A3E7F4") : Color.TRANSPARENT);
                            lastSelectedPosition = position;
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





                break;

            case ListItem.TYPE_DATE:



                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) viewHolder;
                dateViewHolder.txtTitle.setText(dateItem.getDate());
               /* String DATE_FORMAT_2 = "MM/dd/yyyy";
                String dtStart = dateItem.getDate();
                SimpleDateFormat fDate = new SimpleDateFormat (DATE_FORMAT_2, Locale.US);
                try {
                    Date date = fDate.parse(dtStart);

                    dateViewHolder.txtTitle.setText(DateFormat.getDateInstance(DateFormat.LONG).format(date).toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                break;
        }

        ChatToolbar.findViewById(R.id.imgDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedItems();
            }
        });

    }


//
    private Integer countSelectedItems() {
        Integer selectedCount= 0;

        for (int i=0; i< consolidatedList.size(); i++){
            switch (consolidatedList.get(i).getType()) {
                case ListItem.TYPE_OTHER:
                    OtherItem otherItem   = (OtherItem) consolidatedList.get(i);
                    if(otherItem.getPojoOfJsonArray().IsSelected())
                    {
                        selectedCount=  selectedCount+1;
                    }

                    break;
                case ListItem.TYPE_MINE:
                    MineItem generalItem   = (MineItem) consolidatedList.get(i);
                   if(generalItem.getPojoOfJsonArray().IsSelected())
                   {
                       selectedCount=  selectedCount+1;
                   }

                    break;

            }
        }


        return  selectedCount;
    }

    private void deleteSelectedItems() {

        for (int i=0; i< consolidatedList.size(); i++){
            switch (consolidatedList.get(i).getType()) {
                case ListItem.TYPE_OTHER:
                    OtherItem otherItem   = (OtherItem) consolidatedList.get(i);
                    if(otherItem.getPojoOfJsonArray().IsSelected())
                    {
                        consolidatedList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, consolidatedList.size());
                        selectionMode=false;
                        CountAuxiliar = 0;
                        ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                        ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
                        i--;
                    }

                    break;
                case ListItem.TYPE_MINE:
                    MineItem generalItem   = (MineItem) consolidatedList.get(i);
                    if(generalItem.getPojoOfJsonArray().IsSelected())
                    {
                        consolidatedList.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, consolidatedList.size());
                        selectionMode=false;
                        CountAuxiliar = 0;
                        ChatToolbar.findViewById(R.id.linearLayout_toolbar).setVisibility(View.VISIBLE);
                        ChatToolbar.findViewById(R.id.linearSelectionMode).setVisibility(View.GONE);
                        i--;
                    }

                    break;

            }
        }


    }

    // View holder for general row item
    class MineMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle,tvTime;

        public MineMessageViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.textView_message);
            this.tvTime=(TextView)v.findViewById(R.id.tvTime);
        }
    }

    class OtherMessageViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle,tvTime;

        public OtherMessageViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.textView_message);
            this.tvTime=(TextView)v.findViewById(R.id.tvTime);
        }
    }

    // ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle;

        public DateViewHolder(View v) {
            super(v);
            this.txtTitle = (TextView) v.findViewById(R.id.timeText);

        }
    }

    @Override
    public int getItemViewType(int position) {
       return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
       return consolidatedList != null ? consolidatedList.size() : 0;
    }

}



/*
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
    List<ListItem> consolidatedList = new ArrayList<>();
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

                  */
/*  if (lastSelectedPosition > 0) {
                        chatModel.setSelected(false);
                    }
                    *//*

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
        TextView timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.tvTime);
            this.timeText = itemView.findViewById(R.id.timeText);
            this.textView_message = itemView.findViewById(R.id.textView_message);

        }
    }
}
*/
