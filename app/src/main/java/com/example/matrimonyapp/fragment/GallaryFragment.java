package com.example.matrimonyapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.utils.imageFolder;
import com.example.matrimonyapp.utils.pictureFolderAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class GallaryFragment extends Fragment {


    int LAUNCH_SECOND_ACTIVITY = 101;

    private static final String TAG = "MainActivity";
    RecyclerView folderRecycler;
    TextView empty;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private View view;


    public GallaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_gallary, container, false);

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        //____________________________________________________________________________________
        empty =  view.findViewById(R.id.empty);
        folderRecycler = view.findViewById(R.id.folderRecycler);
        folderRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        ArrayList<imageFolder> folds = getPicturePaths();
        if (folds.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
        } else {
            RecyclerView.Adapter folderAdapter = new pictureFolderAdapter(folds, this);
            folderRecycler.setAdapter(folderAdapter);
        }

//        changeStatusBarColor();

        return view;
    }

    private ArrayList<imageFolder> getPicturePaths() {
        ArrayList<imageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allVideouri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Log.d(TAG, "getPicturePaths: " + allVideouri.getPath());


        Log.d(TAG, "getPicturePaths: LAD" + new Gson().toJson(new File(allVideouri.getPath())));


        String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID};
        Cursor cursor = getContext().getContentResolver().query(allVideouri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do {
                imageFolder folds = new imageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection_image = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor_image = getContext().getContentResolver().query(allImagesuri, projection_image, null, null, null);
        try {
            if (cursor_image != null) {
                cursor_image.moveToFirst();
            }
            do {
                imageFolder folds = new imageFolder();
                String name = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor_image.getString(cursor_image.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                //String folderpaths =  datapath.replace(name,"");
                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                folderpaths = folderpaths + folder + "/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);

                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                    folds.addpics();
                    picFolders.add(folds);
                } else {
                    for (int i = 0; i < picFolders.size(); i++) {
                        if (picFolders.get(i).getPath().equals(folderpaths)) {
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            } while (cursor_image.moveToNext());
            cursor_image.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < picFolders.size(); i++) {
            Log.d("picture folders", picFolders.get(i).getFolderName() + " and path = " + picFolders.get(i).getPath() + " " + picFolders.get(i).getNumberOfPics());
        }

        //reverse order ArrayList
       /* ArrayList<imageFolder> reverseFolders = new ArrayList<>();

        for(int i = picFolders.size()-1;i > reverseFolders.size()-1;i--){
            reverseFolders.add(picFolders.get(i));
        }*/

        return picFolders;
    }


    /**
     * Default status bar height 24dp,with code API level 24
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void changeStatusBarColor() {
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.black));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                 getActivity(). setResult(Activity.RESULT_OK, returnIntent);
                getActivity(). finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult




}
