package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.RegisterTransporterModel;

import java.util.ArrayList;

public class RegisterTransporterAdapter extends RecyclerView.Adapter<RegisterTransporterAdapter.ViewHolder> {


    Context context;
    private int pos;
    private LayoutInflater layoutInflater;
    ArrayList<RegisterTransporterModel> list;



    public RegisterTransporterAdapter(Context context, ArrayList<RegisterTransporterModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_register_transporter, parent, false);
        RegisterTransporterAdapter.ViewHolder viewHolder = new RegisterTransporterAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final RegisterTransporterModel item = list.get(position);
        holder.editText_vehicleType.setText(list.get(position).getVehicleType());
        holder.editText_capacity.setText(list.get(position).getCapacity());
        holder.editText_rate.setText(list.get(position).getRate());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public  TextView editText_vehicleType, editText_capacity, editText_rate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.editText_vehicleType = itemView.findViewById(R.id.editText_vehicleType);
            this.editText_capacity = itemView.findViewById(R.id.editText_capacity);
            this.editText_rate = itemView.findViewById(R.id.editText_rate);

        }
    }
}