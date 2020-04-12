package com.example.matrimonyapp.adapter;

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

import java.util.ArrayList;
import java.util.List;

import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.FruitViewHolder> implements Filterable {

    private ArrayList<AddLOcationModal> mDataset;
    private List<AddLOcationModal> exampleListFull;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public AddLocationAdapter(ArrayList<AddLOcationModal>  myDataset, RecyclerViewItemClickListener listener) {
        mDataset = myDataset;
        this.recyclerViewItemClickListener = listener;
        exampleListFull = new ArrayList<>(mDataset);
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText(mDataset.get(i).getName());
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

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (AppCompatCheckedTextView) v.findViewById(R.id.tv_name);

            mDataset.size();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mTextView.isChecked()){
                mTextView.setChecked(false);
                mTextView.setText("Not Checked");
            }
            else mTextView.setChecked(true);
            {
                mTextView.setText("Checked");
            }
            recyclerViewItemClickListener.clickOnItem(mDataset.get(this.getAdapterPosition()));

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(AddLOcationModal data);
    }

}
