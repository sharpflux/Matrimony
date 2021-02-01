package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.HorizontalImageAdapter;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.ProfileTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.modal.SingleImage;
import com.example.matrimonyapp.utils.BlurImage;
import com.example.matrimonyapp.volley.URLs;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class ViewProfileActivity extends AppCompatActivity {

    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_profilePic,
                imageView_back;
    ArrayList<Uri> mArrayUri;

    Uri uri;
    private Bundle bundle;

    boolean follow_flag=false;

    //RecyclerView recyclerView_horizontalImages;
    //ArrayList<SingleImage> arrayList_horizontalImage;
    ArrayList<SingleImage>  arrayList_singleImage;
    //HorizontalImageAdapter horizontalImageAdapter;

    RelativeLayout relativeLayout_image;
    private TextView textView_nameAgeTitle, textView_designationName, textView_companyName, textView_highestQualificationInstitute;

    TabLayout tabLayout_details;
    CustomViewPager viewPager_details;
    TextView textView_follow, textView_following;
    LinearLayout linearLayout_privateAccount;
    View view_include_onFollow, view_include_unfollowed;

    private String userId="0";

    ViewPager2 viewPager2_singleImage;
    Handler sliderHandler = new Handler();

    private int BLUR_PRECENTAGE = 50;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);


        init();

        bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            userId = bundle.getString("userId");
            textView_nameAgeTitle.setText(bundle.getString("userName")+", "+bundle.getString("userAge")+"yrs");
            textView_designationName.setText(bundle.getString("userOccupation"));
            textView_companyName.setText(bundle.getString("userCompany"));
            textView_highestQualificationInstitute.setText(bundle.getString("userQualification"));

            Glide.with(ViewProfileActivity.this)
                    .load(URLs.MainURL+bundle.getString("userProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.color.quantum_grey100)
                    .into(imageView_profilePic);

        }





        imageView_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        viewPager_details.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_details.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProfileTabLayoutAdapter profileTabLayoutAdapter = new ProfileTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_details.getTabCount(), userId,"ViewDetails");
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
       /* Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView_profilePic.setImageBitmap(BlurImage.fastblur(bitmap,1f,BLUR_PRECENTAGE));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                imageView_profilePic.setImageResource(R.mipmap.ic_launcher);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        imageView_profilePic.setTag(target);
*/
        //Picasso.get().load(uri).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(target);
        //imageView_myProfile.setColorFilter(ContextCompat.getColor(this,R.color.project_color));

        arrayList_singleImage = new ArrayList<SingleImage>();

        ArrayList <String> sliderImages = new ArrayList<>();
        for(int i=0; i<5; i++)
        {
            //mArrayUri.add(uri);
            //SingleImage singleImage = new SingleImage(uri.toString());
            sliderImages.add(bundle.getString("userProfilePic"));

        }

        viewPager2_singleImage.setAdapter(new ImageSliderAdapter(ViewProfileActivity.this,
                sliderImages,viewPager2_singleImage));

        viewPager2_settings();

/*

        arrayList_horizontalImage = new ArrayList<SingleImage>();

        for(int i=0; i<9; i++) {
            mArrayUri.add(uri);
            SingleImage singleImage = new SingleImage(uri.toString());
            arrayList_horizontalImage.add(singleImage);

        }

        */
/*gestureDetector = new GestureDetector(this,new MyProfileActivity.Gesture());*//*


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
*/


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




    private void init() {
        mArrayUri = new ArrayList<Uri>();

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        textView_follow = findViewById(R.id.textView_follow);
        textView_following = findViewById(R.id.textView_following);
        linearLayout_privateAccount = findViewById(R.id.linearLayout_privateAccount);
        view_include_onFollow = findViewById(R.id.include_onFollowingProfile);
        view_include_unfollowed = findViewById(R.id.include_unfollowedProfile);
        viewPager_details = findViewById(R.id.viewPager_details);
        tabLayout_details = findViewById(R.id.tabLayout_details);
        textView_nameAgeTitle = findViewById(R.id.textView_nameAgeTitle);
        textView_designationName = findViewById(R.id.textView_designationName);
        textView_companyName = findViewById(R.id.textView_companyName);
        textView_companyName = findViewById(R.id.textView_companyName);
        textView_highestQualificationInstitute = findViewById(R.id.textView_highestQualificationInstitute);


/*        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);*/
        imageView_back = findViewById(R.id.imageView_back);
        //imageView_home.setColorFilter(ContextCompat.getColor(this,R.color.project_color));
        imageView_profilePic = findViewById(R.id.imageView_profilePic);
        //recyclerView_horizontalImages = findViewById(R.id.recyclerView_horizontalImages);
        viewPager2_singleImage = findViewById(R.id.viewPager2_singleImage);
        relativeLayout_image = findViewById(R.id.relativeLayout_image);


    }


    public void viewPager2_settings()
    {
        viewPager2_singleImage.setCurrentItem(1);

        viewPager2_singleImage.setClipToPadding(false);
        viewPager2_singleImage.setClipChildren(false);
        viewPager2_singleImage.setOffscreenPageLimit(3);
        viewPager2_singleImage.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });


        viewPager2_singleImage.setPageTransformer(compositePageTransformer);

        viewPager2_singleImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });


    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2_singleImage.setCurrentItem(viewPager2_singleImage.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sliderHandler.postDelayed(sliderRunnable,3000);

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }
}
