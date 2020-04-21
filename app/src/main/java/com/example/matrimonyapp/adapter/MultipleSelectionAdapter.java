package com.example.matrimonyapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.MyItem;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectionAdapter extends RecyclerView.Adapter<MultipleSelectionAdapter.ViewHolder>
        implements Filterable {

    public ArrayList<MyItem> listData,newlist;
    public  RecyclerViewCheckedChangekListener recyclerViewCheckedChangekListener;
    EditText editTextName;
    TextView textViewId;
    // Dialog dialog;

    public MultipleSelectionAdapter(ArrayList<MyItem> listData, EditText editTextName,
                                    RecyclerViewCheckedChangekListener listener)// , Dialog dialog
    {

        this.listData = listData;
        recyclerViewCheckedChangekListener = listener;
        this.editTextName = editTextName;
        //this.textViewId = textViewId;
        //this.dialog = dialog;
        newlist = listData;
        newlist = new ArrayList<>(this.listData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_multiple_selection, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;


    }


    public void onBindViewHolder(final ViewHolder holder, final int position) {


        final MyItem myItem = listData.get(position);

        holder.checkbox_itemName.setText(listData.get(position).getName());
        holder.textView_stateId.setText(String.valueOf(listData.get(position).getId()));



        if(listData.get(position).isChecked())
        {
            holder.checkbox_itemName.setChecked(true);
        }
        else
        {
            holder.checkbox_itemName.setChecked(false);
        }

        holder.checkbox_itemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    listData.get(position).setChecked(b);
                    //notifyDataSetChanged();



            }
        });


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


/*                boolean flag=true;
                for(int i=0; i<listData.size(); i++)
                {
                    if(!listData.get(i).isChecked())
                    {
                        flag = false;
                    }
                }
                if(flag == false)
                {
                    C
                }*/
                notifyDataSetChanged();
                recyclerViewCheckedChangekListener.checkedChange(true);
            }
        };
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CheckBox checkbox_itemName;

        public TextView textView_stateId;

        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textView_stateId = (TextView) itemView.findViewById(R.id.textView_Id);
            this.checkbox_itemName = (CheckBox) itemView.findViewById(R.id.checkbox_itemName);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            this.setIsRecyclable(false);
            //itemView.setli(this);

            checkbox_itemName.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            recyclerViewCheckedChangekListener.checkedChange(listData.get(this.getAdapterPosition()).isChecked());
        }



/*        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            recyclerViewCheckedChangekListener.checkedChange(b);
        }*/
    }
    public interface RecyclerViewCheckedChangekListener {
        void checkedChange(boolean b);
    }



}