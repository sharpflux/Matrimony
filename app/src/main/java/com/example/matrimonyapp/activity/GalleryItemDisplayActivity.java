package com.example.matrimonyapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.matrimonyapp.R;
import com.example.matrimonyapp.utils.MarginDecoration;
import com.example.matrimonyapp.utils.PictureFacer;
import com.example.matrimonyapp.adapter.picture_Adapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class GalleryItemDisplayActivity extends AppCompatActivity {

    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ProgressBar load;
    String foldePath;
    Button okbutton;
    TextView folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_item_display);

        okbutton = findViewById(R.id.okbutton);
        folderName = findViewById(R.id.foldername);
        folderName.setText(getIntent().getStringExtra("folderName"));

        foldePath = getIntent().getStringExtra("folderPath");
        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);


        if (allpictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);
            allpictures = getAllImagesByFolder(foldePath);

            Collections.sort(allpictures);
            Collections.reverse(allpictures);

            imageRecycler.setAdapter(new picture_Adapter(allpictures, GalleryItemDisplayActivity.this));
            load.setVisibility(View.GONE);
        } else {

        }
        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.okbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent returnIntent = new Intent();
                returnIntent.putExtra("result", new Gson().toJson(selectedItem));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();*/

                Intent returnIntent = new Intent(GalleryItemDisplayActivity.this,ImageUploadSelectedActivity.class);
                returnIntent.putExtra("result", new Gson().toJson(selectedItem));
                startActivity(returnIntent);
                finish();
            }
        });

        filterdata("Image");
    }


    public ArrayList<PictureFacer> getAllImagesByFolder(String path) {
        ArrayList<PictureFacer> images = new ArrayList<>();
        Uri allimagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = GalleryItemDisplayActivity.this.getContentResolver().query(allimagesuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor.moveToFirst();
            do {
                PictureFacer pic = new PictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                File fileObject = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Long fileModified = fileObject.lastModified();
                pic.setDateTime(new Date(fileModified));
                pic.setType("Image");
                images.add(pic);
            } while (cursor.moveToNext());
            cursor.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection_video = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE};
        Cursor cursor_video = GalleryItemDisplayActivity.this.getContentResolver().query(allVideosuri, projection_video, MediaStore.Video.Media.DATA + " like ? ", new String[]{"%" + path + "%"}, null);
        try {
            cursor_video.moveToFirst();
            do {
                PictureFacer pic = new PictureFacer();

                pic.setPicturName(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));

                pic.setPictureSize(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));

                File fileObject = new File(cursor_video.getString(cursor_video.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                Long fileModified = fileObject.lastModified();
                pic.setDateTime(new Date(fileModified));
                Log.d("TAG", "getAllImagesByFolder: " + fileModified);
                pic.setType("Video");
                images.add(pic);
            } while (cursor_video.moveToNext());
            cursor_video.close();
            ArrayList<PictureFacer> reSelection = new ArrayList<>();
            for (int i = images.size() - 1; i > -1; i--) {
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return images;
    }

    public void onClick(PictureFacer file, boolean isselect) {
        if (isselect) {
            selectedItem.add(file);
        } else {
            selectedItem.remove(file);
        }
        if (selectedItem.size() > 0) {
            okbutton.setVisibility(View.VISIBLE);

        } else {
            okbutton.setVisibility(View.GONE);
        }

        if (selectedItem.size() < 5) {

            okbutton.setVisibility(View.VISIBLE);

        } else {
            selectedItem.remove(file);
            Toast.makeText(GalleryItemDisplayActivity.this, "Sorry you can not select more than 5 Images..", Toast.LENGTH_SHORT).show();
            okbutton.setVisibility(View.GONE);
        }

//        System.out.println("Pdffile" + Gson().toJson(selectedFileModellist));
//        System.out.println("Pdffile99" + Gson().toJson(file));

    }


    void filterdata(String itemtype) {
        selectedItem.clear();
        okbutton.setVisibility(View.GONE);
        ArrayList<PictureFacer> fileModellist_filter = new ArrayList<>();
        for (PictureFacer item : allpictures) {
            if (itemtype.equals("Image")) {
                if (item.getType().equals("Image"))
                    fileModellist_filter.add(item);
            } else if (itemtype.equals("Video")) {
                if (item.getType().equals("Video"))
                    fileModellist_filter.add(item);
            } else {
                fileModellist_filter.add(item);
            }

        }
        imageRecycler.setAdapter(new picture_Adapter(fileModellist_filter, GalleryItemDisplayActivity.this));
    }


}
