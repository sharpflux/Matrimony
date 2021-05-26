package com.example.matrimonyapp.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.GalleryItemDisplayActivity;
import com.example.matrimonyapp.fragment.GallaryFragment;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;


import java.util.ArrayList;

/**
 * Author CodeBoy722
 * <p>
 * An adapter for populating RecyclerView with items representing folders that contain images
 */
public class pictureFolderAdapter extends RecyclerView.Adapter<pictureFolderAdapter.FolderHolder> {
    int LAUNCH_SECOND_ACTIVITY = 101;

    private ArrayList<imageFolder> folders;
    private GallaryFragment folderContx;

    public pictureFolderAdapter(ArrayList<imageFolder> folders, GallaryFragment folderContx) {
        this.folders = folders;
        this.folderContx = folderContx;
    }

    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.item_gallery_folder, parent, false);
        return new FolderHolder(cell);

    }

    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final imageFolder folder = folders.get(position);

        Glide.with(folderContx)
                .load(folder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.folderPic);

        //setting the number of images
        String text = "" + folder.getFolderName();
        String folderSizeString = "(" + folder.getNumberOfPics() + ")  ";
        holder.folderName.setText(folderSizeString+text);

        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(folderContx.getActivity(), GalleryItemDisplayActivity.class);
                i.putExtra("folderName", folder.getFolderName());
                i.putExtra("folderPath", folder.getPath());
                folderContx.startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
               //listenToClick.onPicClicked(folder.getPath(), folder.getFolderName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder {
        ImageView folderPic;
        TextView folderName;
        //set textview for foldersize


        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }

}
