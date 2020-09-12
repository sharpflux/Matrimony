package com.example.matrimonyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.MyItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.matrimonyapp.R.id.textView_name;

public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.ViewHolder> implements Filterable {

    public ArrayList<MyItem> listData,newlist;
    public  RecyclerViewItemClickListener recyclerViewItemClickListener;
    EditText editTextName;
    TextView textViewId;
   // Dialog dialog;

    public PromptAdapter(ArrayList<MyItem> listData, EditText editTextName,
                         TextView textViewId, RecyclerViewItemClickListener listener)// , Dialog dialog
    {

        this.listData = listData;
        recyclerViewItemClickListener = listener;
        this.editTextName = editTextName;
        this.textViewId = textViewId;
        //this.dialog = dialog;
        newlist = listData;
        newlist = new ArrayList<>(this.listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_state_data, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;


    }


    public void onBindViewHolder(ViewHolder holder, int position) {

        //final MyItem myItem = listData[position];
        final MyItem myItem = listData.get(position);

        holder.textView_stateName.setText(listData.get(position).getName());
        holder.textView_stateId.setText(String.valueOf(listData.get(position).getId()));
        /*holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextName.setText(myItem.getName());
                textViewId.setText(String.valueOf(myItem.getId()));
                dialog.dismiss();
              //  Toast.makeText(view.getContext(),"Clicked on "+myItem.getName(),Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<MyItem>filteredList = new ArrayList<>();



                if ( charSequence.length() == 0 || charSequence == null) {
                    filteredList.addAll(newlist);
                }
                else
                    {
                        filteredList.clear();

                        String filterPattern = charSequence.toString().toLowerCase().trim();
                        //editTextName.setText(filterPattern);
                       // Toast.makeText(getContext(),filterPattern,Toast.LENGTH_SHORT).show();

                        for (MyItem item : newlist) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;

                //return null;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listData.clear();
                listData.addAll((List)filterResults.values);
                notifyDataSetChanged();
            }
        };
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textView_stateId, textView_stateName;

        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textView_stateId = (TextView) itemView.findViewById(R.id.textView_Id);
            this.textView_stateName = (TextView) itemView.findViewById(textView_name);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            recyclerViewItemClickListener.clickOnItem(listData.get(this.getAdapterPosition()));
        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(MyItem data);
    }


}