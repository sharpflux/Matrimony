package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;

public class HorizontalProgressBarAdapter extends RecyclerView.Adapter<HorizontalProgressBarAdapter.ViewHolder> {

    Intent intent;
    Context context;
    int progressBarPosition;
    int progressBarCount;
    int progressBarWidth;
    int width;

    public HorizontalProgressBarAdapter(Context context, int progressBarPosition, int progressBarCount,
                                        int width)
    {
        this.context = context;
        this.progressBarPosition = progressBarPosition;
        this.progressBarCount = progressBarCount;
        this.width = width;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_progress_bar,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        if(position<progressBarPosition)
            holder.progressBar.setProgress(150);

       // holder.progressBar.setProgress(Uri.parse(arrayList_horizontalImage.get(position).getUri()));

        progressBarWidth = (width/progressBarCount)-2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(progressBarWidth,4);
        holder.progressBar.setLayoutParams(lp);

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)holder.progressBar.getLayoutParams();
        /*mlp.setMarginStart(1);
        mlp.setMarginEnd(1);*/
        mlp.setMargins(2,20,2,0);

       /* holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(context, ViewImageActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("arrayList",(Serializable)arrayList_horizontalImage);
                bundle.putInt("position",position);

                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return progressBarCount;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }
}
