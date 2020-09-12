package com.example.matrimonyapp.activity;

import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.HorizontalProgressBarAdapter;
import com.example.matrimonyapp.modal.SingleImage;

import java.util.ArrayList;

public class ViewImageActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Handler handler;
    ArrayList<SingleImage> arrayList_horizontalImage;
    ImageView imageView_galleryImage;
    RecyclerView recyclerView_progressBar;


    Uri uri;
    Bundle bundle;
    HorizontalProgressBarAdapter horizontalProgressBarAdapter;
    AsyncTaskProgressBar asyncTaskProgressBar;

    int position=0;
    int progressBarWidth=0;
    int progressStatus=0;
    int i=0;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);


        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        //progressBar = findViewById(R.id.progressBar);
        imageView_galleryImage = findViewById(R.id.imageView_galleryImage);
        recyclerView_progressBar = findViewById(R.id.recyclerView_horizontalProgressBar);
        handler = new Handler();


        bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        arrayList_horizontalImage = (ArrayList<SingleImage>)bundle.getSerializable("arrayList");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        horizontalProgressBarAdapter = new HorizontalProgressBarAdapter(getApplicationContext(),
                position, arrayList_horizontalImage.size(),size.x);
        LinearLayoutManager horizontalLayourManager = new LinearLayoutManager(ViewImageActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_progressBar.setLayoutManager(horizontalLayourManager);
        recyclerView_progressBar.setAdapter(horizontalProgressBarAdapter);


        asyncTaskProgressBar =  new AsyncTaskProgressBar();
        asyncTaskProgressBar.execute();


    }
    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

    public class AsyncTaskProgressBar extends AsyncTask{


        @Override
        protected String doInBackground(Object[] objects) {

            String resp="";


            for(i=position; i<arrayList_horizontalImage.size(); i++) {


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView_galleryImage.setImageURI(Uri.parse(arrayList_horizontalImage.get(i).getUri()));
                    }
                });


                try {
                    progressStatus = 0;
                    while (progressStatus < 150) {

                        Thread.sleep(30);
                        progressStatus = progressStatus + 1;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {


                                ((ProgressBar)recyclerView_progressBar.getChildAt(i)).setProgress(progressStatus);

                               // progressBar.setProgress(progressStatus);
                            }
                        });
                    }
                    //finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            finish();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Object o) {
            super.onCancelled(o);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}
