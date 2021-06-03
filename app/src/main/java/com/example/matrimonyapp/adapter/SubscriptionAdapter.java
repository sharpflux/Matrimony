package com.example.matrimonyapp.adapter;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.MyItem;
import com.example.matrimonyapp.modal.SingleImage;
import com.example.matrimonyapp.modal.SubscriptionModel;
import com.example.matrimonyapp.utils.PayPalClientIdConfiguration;


import java.math.BigDecimal;
import java.util.ArrayList;

import in.codeshuffle.typewriterview.TypeWriterView;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder > {

    //ImageView imageView;
    ArrayList<SubscriptionModel> arrayList_subscriptionModel;
    ViewPager2 viewPager2_subscription;
    public SelectPlanClickListener selectPlanClickListener;




    public SubscriptionAdapter(ArrayList<SubscriptionModel> arrayList_subscriptionModel, ViewPager2 viewPager2_subscription, SelectPlanClickListener selectPlanClickListener) {
        this.arrayList_subscriptionModel = arrayList_subscriptionModel;
        this.viewPager2_subscription = viewPager2_subscription;
        this.selectPlanClickListener = selectPlanClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pager_subscription,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.textView_packName.setText(arrayList_subscriptionModel.get(position).getPackName());
        holder.textView_amount.setText(arrayList_subscriptionModel.get(position).getAmount());

        if(position == arrayList_subscriptionModel.size() - 1)
        {
            viewPager2_subscription.post(runnable);
        }
        
/*        holder.textView_selectThisPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
    }


    @Override
    public int getItemCount() {
        return arrayList_subscriptionModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView_packName, textView_amount, textView_packType,
                 textView_description, textView_selectThisPlan;

        TypeWriterView textView_tagLine;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_packName = itemView.findViewById(R.id.textView_packName);
            textView_tagLine = itemView.findViewById(R.id.textView_tagLine);
            textView_amount = itemView.findViewById(R.id.textView_amount);
//            textView_packType = itemView.findViewById(R.id.textView_packType);
            textView_description = itemView.findViewById(R.id.textView_description);
            textView_selectThisPlan = itemView.findViewById(R.id.textView_selectThisPlan);

            textView_selectThisPlan.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            selectPlanClickListener.onPlanSelect(arrayList_subscriptionModel.get(this.getAdapterPosition()));
        }

    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2_subscription.setCurrentItem(0);
            //arrayList_singleImage.addAll(arrayList_singleImage);
        }
    };



    public interface SelectPlanClickListener {
        void onPlanSelect(SubscriptionModel data);
    }




}
