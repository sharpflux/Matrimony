package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>{

    private Context context;
    private List<NavigationItemListModel> activityListModels;

    public NavigationDrawerAdapter(Context context, List<NavigationItemListModel> activityListModels) {
        this.context = context;
        this.activityListModels = activityListModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        holder.text.setText(activityListModels.get(position).getTxt());
        holder.image.setImageResource(activityListModels.get(position).getImage());

        //Condition is applied to apply visibility to the given

        if (position == 3 || position == 6){
            holder.view.setVisibility(View.VISIBLE);
        }
        else {
            holder.view.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return activityListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        View view;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.text);
            image = itemView.findViewById(R.id.image);
            view = itemView.findViewById(R.id.view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Context context=view.getContext();

            final Intent intent;
            switch (getAdapterPosition()) {
                /*case 0:
                    intent = new Intent(context, Review_CartActivity.class);
                    context.startActivity(intent);
                    break;

                case 1:
                    intent = new Intent(context, MyOrderActivity.class);
                    context.startActivity(intent);
                    break;

                case 3:
                    intent = new Intent(context, Coupon.class);
                    context.startActivity(intent);
                    break;

                case 6:
                    SharedPrefManager.getInstance(context).logout();
                    break;

                default:
                    intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;*/
                case 6:
                    CustomSharedPreference.getInstance(context).logout();
                    break;
            }


        }
    }
}

