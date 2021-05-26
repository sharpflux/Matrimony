package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSelectedAdapter;
import com.example.matrimonyapp.utils.MarginDecoration;
import com.example.matrimonyapp.utils.PictureFacer;
import com.example.matrimonyapp.adapter.picture_Adapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ImageUploadSelectedActivity extends AppCompatActivity {

    TextView tvResult,textView_toolbar;
    Bundle bundle;
    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ImageSelectedAdapter interestListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_selected);
        allpictures = new ArrayList<>();


        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        imageRecycler.setLayoutManager(mLayoutManager);


        textView_toolbar=findViewById(R.id.textView_toolbar);

        textView_toolbar.setText(getResources().getString(R.string.selected_photos));

        bundle = getIntent().getExtras();
        if(bundle!=null)
        {

            try {

               JSONArray jsonArray = new JSONArray(bundle.getString("result"));
               for (int i = 0; i < jsonArray.length(); i++) {
                    PictureFacer student = new Gson().fromJson(jsonArray.get(i).toString(), PictureFacer.class);
                    allpictures.add(student);
               }

               imageRecycler.setAdapter(new ImageSelectedAdapter(getApplicationContext(), allpictures));

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}