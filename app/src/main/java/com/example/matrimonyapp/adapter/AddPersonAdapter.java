package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.ChatModel;

import java.util.ArrayList;

public class AddPersonAdapter extends RecyclerView.Adapter<AddPersonAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<AddPersonModel> list;



    public AddPersonAdapter(Context context, ArrayList<AddPersonModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_add_person, parent, false);
        AddPersonAdapter.ViewHolder viewHolder = new AddPersonAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final AddPersonModel model = list.get(position);

        holder.textView_id.setText(model.getId());
        holder.textView_name.setText(model.getName());
        holder.textView_mobileNo.setText(model.getMobileNo());
        holder.textView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogAddSibling customDialogAddSibling = new CustomDialogAddSibling(context, model.getId());
                customDialogAddSibling.show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{


        public  TextView textView_id, textView_name, textView_mobileNo, textView_edit;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_id = itemView.findViewById(R.id.textView_id);
            this.textView_name = itemView.findViewById(R.id.textView_name);
            this.textView_mobileNo = itemView.findViewById(R.id.textView_mobileNo);
            this.textView_edit = itemView.findViewById(R.id.textView_edit);


        }
    }
}
