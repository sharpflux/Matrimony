package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.PostsModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    Context context;

    private LayoutInflater layoutInflater;
    ArrayList<PostsModel> list;


    public PostsAdapter(Context context, ArrayList<PostsModel> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_posts, parent, false);
        PostsAdapter.ViewHolder viewHolder = new PostsAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final PostsModel item = list.get(position);

        holder.circleImage_profilePic.setImageURI(list.get(position).getUri_profilePic());
        holder.imageView_postPic.setImageURI(list.get(position).getUri_postPic());
        holder.textView_userId.setText(list.get(position).getUserId());
        holder.textView_likes.setText(list.get(position).getLikes());
        //holder.textView_postId.setText(list.get(position).getPostId());
        holder.textView_postTime.setText(list.get(position).getPostTime());



    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{



        public CircleImageView circleImage_profilePic;
        public ImageView imageView_menu, imageView_postPic, imageView_like;
        public  TextView textView_userId, textView_likes, textView_postTime;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_likes = itemView.findViewById(R.id.textView_likes);
            this.textView_postTime = itemView.findViewById(R.id.textView_postTime);
            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            this.imageView_postPic = itemView.findViewById(R.id.imageView_postPic);
            this.imageView_like = itemView.findViewById(R.id.imageView_like);
            this.imageView_menu = itemView.findViewById(R.id.imageView_menu);


        }
    }
}
