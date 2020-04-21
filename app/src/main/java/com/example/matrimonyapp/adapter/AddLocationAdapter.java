package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.AddLOcationModal;
import com.example.matrimonyapp.sqlite.SQLiteSetpreference;

import java.util.ArrayList;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.FruitViewHolder> implements Filterable {

    private ArrayList<AddLOcationModal> mDataset;
    private List<AddLOcationModal> exampleListFull;
    RecyclerViewItemClickListener recyclerViewItemClickListener;
    String selectedItem;
    SQLiteSetpreference sqLiteSetpreference;
    Context context;
//, RecyclerViewItemClickListener listener
    public AddLocationAdapter(Context context,ArrayList<AddLOcationModal>  myDataset) {
        this.context = context;
        mDataset = myDataset;
       // this.recyclerViewItemClickListener = listener;
        exampleListFull = new ArrayList<>(mDataset);
        sqLiteSetpreference = new SQLiteSetpreference(context);


    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int i) {
        holder.mTextView.setText(mDataset.get(i).getName());
        holder.FilterBy=mDataset.get(i).getFilterBy();
        holder.ItemId=mDataset.get(i).getId();
        holder.sqLiteSetpreference=sqLiteSetpreference;

        holder.sqLiteSetpreference = sqLiteSetpreference;
        if (holder.FilterBy != null) {
            switch (holder.FilterBy) {
                case "STATE":
                    /*Cursor StateCursor = sqLiteSetpreference.FilterGetByFilterName("STATE");
                    while (StateCursor.moveToNext()) {

                        if (StateCursor.getString(0).equals(holder.ItemId)) {
                            holder.mTextView.setCheckMarkDrawable(R.drawable.ic_check_box_black_24dp);
                            holder.mTextView.setChecked(true);
                        }else
                            {
                                holder.mTextView.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
                                holder.mTextView.setChecked(false);
                            }
                    }*/

                    if(holder.mTextView.isChecked()){
                        holder.mTextView.setChecked(true);
                    }
                    else {
                        holder.mTextView.setChecked(false);
                    }
                    break;

                case "DISTRICT":
                    Cursor DISTRICTCursor = sqLiteSetpreference.FilterGetByFilterName("DISTRICT");
                    while (DISTRICTCursor.moveToNext()) {
                        if (DISTRICTCursor.getString(0).equals(holder.ItemId)) {
                            holder.mTextView.setCheckMarkDrawable(R.drawable.ic_check_box_black_24dp);
                            holder.mTextView.setChecked(true);
                        }
                    }
                    break;
            }
            }


    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<AddLOcationModal> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(exampleListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (AddLOcationModal item : exampleListFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;

            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataset.clear();
                mDataset.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }



    public  class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatCheckedTextView mTextView;
        String  FilterBy = "",ItemId="",childName = "";
        SQLiteSetpreference sqLiteSetpreference;
        int position = 0;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (AppCompatCheckedTextView) v.findViewById(R.id.tv_name);

          //  mDataset.size();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           Boolean value=mTextView.isSelected();
           String pid="",n="";
            if (value) {

                sqLiteSetpreference.DeleteRecord(FilterBy, ItemId);
                mTextView.setCheckMarkDrawable(R.drawable.ic_check_box_outline_blank_black_24dp);
                mTextView.setChecked(false);
                AddLOcationModal filter = mDataset.get(position);
                filter.setChecked(false);
                //Toast.makeText(c, pid, Toast.LENGTH_LONG).show();
            } else {

                pid = ItemId;
                n = childName;
                Log.e(pid, n);
                AddLOcationModal filter = mDataset.get(position);
                filter.setChecked(true);
                sqLiteSetpreference.FilterInsert(FilterBy, ItemId);
                // set check mark drawable and set checked property to true
                mTextView.setCheckMarkDrawable(R.drawable.ic_check_box_black_24dp);
                mTextView.setChecked(true);
            }

           // recyclerViewItemClickListener.clickOnItem(mDataset.get(this.getAdapterPosition()));

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(AddLOcationModal data);
    }

}
