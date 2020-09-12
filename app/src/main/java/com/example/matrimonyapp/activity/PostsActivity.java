package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.PostsAdapter;
import com.example.matrimonyapp.modal.PostsModel;

import java.util.ArrayList;

public class PostsActivity extends AppCompatActivity {

    RecyclerView recyclerView_posts;
    ArrayList<PostsModel> postsModelArrayList;
    PostsAdapter postsAdapter;




    View include_toolbar;
    TextView textView_toolbarHeader;
    ImageView imageView_back;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        include_toolbar = findViewById(R.id.include_toolbar);
        textView_toolbarHeader = findViewById(R.id.textView_toolbarHeader);
        imageView_back = findViewById(R.id.imageView_back);

        recyclerView_posts = findViewById(R.id.recyclerView_posts);


        textView_toolbarHeader.setText("Posts");

        postsModelArrayList = new ArrayList<PostsModel>();




        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Uri uri_profilePic = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower2));
        Uri uri_postPic=uri_profilePic;
        for(int i=0; i<9; i++)
        {
            PostsModel postsModel= new PostsModel("@yourUserId", "ABC", "150 likes","101","1 hour ago", uri_profilePic, uri_postPic);
            postsModelArrayList.add(postsModel);

        }



        postsAdapter = new PostsAdapter(this,postsModelArrayList);
        recyclerView_posts.setAdapter(postsAdapter);
        recyclerView_posts.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_posts.setLayoutManager(mLayoutManager);





    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }
}
