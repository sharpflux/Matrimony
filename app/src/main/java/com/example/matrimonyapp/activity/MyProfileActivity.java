package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.GalleryAdapter;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_myProfile;
    TextView textView_editProfile;

    ArrayList<Uri> mArrayUri;
    GridView gvGallery;
    Uri uri;
    GalleryAdapter galleryAdapter;
    Bitmap bitmap, O;
    GestureDetector gestureDetector;
    GestureDetectorCompat gestureDetector_gridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mArrayUri = new ArrayList<Uri>();
        gvGallery = findViewById(R.id.gridView_gallery);
        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);

        textView_editProfile = findViewById(R.id.textView_editProfile);


        imageView_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        imageView_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, DirectMessagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, InterestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        //uri = Uri.parse(getResources().getDrawable(R.drawable.noimage).toString());

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower3)
        +"/"+this.getResources().getResourceTypeName(R.drawable.flower3)
        +"/"+this.getResources().getResourceEntryName(R.drawable.flower3));

        imageView_myProfile.setColorFilter(ContextCompat.getColor(this,R.color.project_color));


        /*BitmapDrawable BD = (BitmapDrawable)imageView_myProfile.getDrawable();
        bitmap = BD.getBitmap();

        O = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        for(int i=0; i<bitmap.getWidth(); i++)
        {
            for (int j=0; j<bitmap.getHeight(); j++)
            {
                int p = bitmap.getPixel(i,j);
                int b = Color.LTGRAY;

                int x=0;
                int y=0;
                b=b+150;
                O.setPixel(i,j,Color.argb(Color.alpha(p),x,y,b));

            }

        }
        imageView_myProfile.setImageBitmap(O);*/


        textView_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });




        for(int i=0; i<7; i++) {
            mArrayUri.add(uri);
        }

        gestureDetector = new GestureDetector(this,new Gesture());

        galleryAdapter = new GalleryAdapter(this,mArrayUri,MyProfileActivity.this,getResources());
        gvGallery.setAdapter(galleryAdapter);
        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                .getLayoutParams();
        mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()


        setGridViewHeight(gvGallery,3);


        gestureDetector_gridView = new GestureDetectorCompat(getApplicationContext(),new MyGestureListener());
        gestureDetector_gridView.setIsLongpressEnabled(true);


    }

    void setGridViewHeight(GridView gridView, int noOfColumns)
    {

        ListAdapter gridViewAdapter = gridView.getAdapter();


        if(gridViewAdapter == null)
            return;

        else
        {
            int totalHt;
            int items = gridViewAdapter.getCount();
            int rows;

            View listItem = gridViewAdapter.getView(0,null,gridView);
            listItem.measure(0,0);
            totalHt = listItem.getMeasuredHeight();

            float x;

            if(items > noOfColumns)
            {
                x = items / noOfColumns;

                if(items % noOfColumns != 0 )
                {
                    rows = (int)(x+1);

                }
                else
                {
                    rows = (int)x;
                }
                totalHt *= rows;
                totalHt += gridView.getVerticalSpacing() * rows;

            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHt;
            gridView.setLayoutParams(params);

        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);



        }
    }

    class Gesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onSingleTapUp(MotionEvent ev) {
            return false;
        }

        public void onLongPress(MotionEvent ev) {



        }


        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {


            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }
    }

}
