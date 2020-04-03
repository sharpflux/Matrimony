package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.HorizontalImageAdapter;
import com.example.matrimonyapp.adapter.ProfileTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.modal.SingleImage;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewProfileActivity extends AppCompatActivity {

    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_myProfile,
                imageView_back;
    ArrayList<Uri> mArrayUri;

    Uri uri;

    boolean follow_flag=false;

    RecyclerView recyclerView_horizontalImages;
    ArrayList<SingleImage> arrayList_horizontalImage;
    HorizontalImageAdapter horizontalImageAdapter;


    TabLayout tabLayout_details;
    CustomViewPager viewPager_details;
    TextView textView_follow, textView_following;
    LinearLayout linearLayout_privateAccount;
    View view_include_onFollow, view_include_unfollowed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        mArrayUri = new ArrayList<Uri>();


        textView_follow = findViewById(R.id.textView_follow);
        textView_following = findViewById(R.id.textView_following);
        linearLayout_privateAccount = findViewById(R.id.linearLayout_privateAccount);
        view_include_onFollow = findViewById(R.id.include_onFollowingProfile);
        view_include_unfollowed = findViewById(R.id.include_unfollowedProfile);
        viewPager_details = findViewById(R.id.viewPager_details);
        tabLayout_details = findViewById(R.id.tabLayout_details);

/*        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);*/
        imageView_back = findViewById(R.id.imageView_back);
        //imageView_home.setColorFilter(ContextCompat.getColor(this,R.color.project_color));
        recyclerView_horizontalImages = findViewById(R.id.recyclerView_horizontalImages);

       // imageView_menu.setVisibility(View.GONE);


        //tabLayout_details.addTab(new TabLayout.Tab(),0,true);


        imageView_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       /* imageView_myProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProfileActivity.this, MyProfileActivity.class);
                startActivity(intent);
            }
        });*/


        viewPager_details.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_details.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProfileTabLayoutAdapter profileTabLayoutAdapter = new ProfileTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_details.getTabCount());
        viewPager_details.setAdapter(profileTabLayoutAdapter);



        viewPager_details.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_details));

        tabLayout_details.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager_details.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower2));

        //imageView_myProfile.setColorFilter(ContextCompat.getColor(this,R.color.project_color));


        arrayList_horizontalImage = new ArrayList<SingleImage>();

        for(int i=0; i<9; i++) {
            mArrayUri.add(uri);
            SingleImage singleImage = new SingleImage(uri.toString());
            arrayList_horizontalImage.add(singleImage);

        }

        /*gestureDetector = new GestureDetector(this,new MyProfileActivity.Gesture());*/

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower1)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower1)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower1));

        arrayList_horizontalImage.get(0).setUri(uri.toString());
        arrayList_horizontalImage.get(8).setUri(uri.toString());

        horizontalImageAdapter = new HorizontalImageAdapter(this, arrayList_horizontalImage);
        LinearLayoutManager horizontalLayourManager = new LinearLayoutManager(ViewProfileActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_horizontalImages.setLayoutManager(horizontalLayourManager);
        recyclerView_horizontalImages.setAdapter(horizontalImageAdapter);


        textView_following.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(follow_flag == true)
                {

                    view_include_onFollow.setVisibility(View.GONE);
                    view_include_unfollowed.setVisibility(View.VISIBLE);
                    follow_flag=false;
                }
            }
        });
        textView_follow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(follow_flag == false)
                {
                    view_include_onFollow.setVisibility(View.VISIBLE);
                    view_include_unfollowed.setVisibility(View.GONE);
                    follow_flag=true;
                }
                else
                {
                    view_include_onFollow.setVisibility(View.GONE);
                    view_include_unfollowed.setVisibility(View.INVISIBLE);
                    follow_flag=false;

                }


            }
        });



/*        galleryAdapter = new GalleryAdapter(this,mArrayUri,ViewProfileActivity.this,getResources());
        gvGallery.setAdapter(galleryAdapter);
        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                .getLayoutParams();
        mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()


        setGridViewHeight(gvGallery,3);*/


    }


}
