package com.example.matrimonyapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ExpandableListAdapter;
import com.example.matrimonyapp.adapter.GalleryAdapter;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogChangeProfilePic;
import com.example.matrimonyapp.customViews.CustomNavigationView;
import com.example.matrimonyapp.fragment.ReligiousDetailsFragment;
import com.example.matrimonyapp.modal.ExpandedMenuModel;
import com.example.matrimonyapp.modal.NavigationDrawer;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.SingleImage;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.BlurBackground;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.internal.Blur;

public class MyProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 1, SELECT_FILE = 0, ACTIVITY_CONSTANT = 2;
    ImageView imageView_home, imageView_search, imageView_message, imageView_like, imageView_myProfile, imageView_addNew;
    TextView textView_welcomeUserName, textView_profileName, textView_editProfile, textView_editProfile2, textView_addBio, textView_setPreferences,
            textView_changeProfilePhoto;

    RelativeLayout relativeLayout_changeProfilePic;
    LinearLayout linearlayout_sharePhoto;
    CustomDialogChangeProfilePic customDialogChangeProfilePic;

    public String selfieString;

    ArrayList<Uri> mArrayUri;
    GridView gvGallery;
    Uri uri;
    GalleryAdapter galleryAdapter;
    Bitmap bitmap, O;
    GestureDetector gestureDetector;
    GestureDetectorCompat gestureDetector_gridView;

    CircleImageView circleImageView_profilePic, circleImageView_headerProfilePic;
    public static final int RequestPermissionCode = 1;

    Intent intent_camera;
    ArrayList<String> selectedImageList;

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;


    ArrayList<ExpandedMenuModel> arrayList_expandedMenuModel;


    private Toolbar toolbar;
    private RecyclerView drawerrecyview;


    //Navigation
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CustomNavigationView customNavigationView;
    private DrawerLayout drawerLayout;

    private SimpleGestureFilter detector;
    UserModel userModel;
    ViewPager2 viewPager2_singleImage;
    ArrayList<SingleImage>  arrayList_singleImage;
    Handler sliderHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        init();



        imageView_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, GalleryActivity.class);
                intent.putExtra("noOfSelectedImages",mArrayUri.size());
                startActivityForResult(intent, ACTIVITY_CONSTANT);
            }
        });

        EnableRuntimePermission();




        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower3)
        +"/"+this.getResources().getResourceTypeName(R.drawable.flower3)
        +"/"+this.getResources().getResourceEntryName(R.drawable.flower3));



        imageView_myProfile.setImageResource(R.drawable.filled_profile); // highlight MyProfile Tab

        arrayList_singleImage = new ArrayList<SingleImage>();


        navigation();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("GetProfilePic");


        customDialogChangeProfilePic = new CustomDialogChangeProfilePic(MyProfileActivity.this);




        gestureDetector = new GestureDetector(this,new Gesture());

        galleryAdapter = new GalleryAdapter(this, mArrayUri,MyProfileActivity.this,getResources());
        gvGallery.setAdapter(galleryAdapter);
        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                .getLayoutParams();
        mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()

        if(mArrayUri.size()>0)
        {
            linearlayout_sharePhoto.setVisibility(View.GONE);
        }
        else
        {
            linearlayout_sharePhoto.setVisibility(View.VISIBLE);
        }
        setGridViewHeight(gvGallery,3);


        gestureDetector_gridView = new GestureDetectorCompat(getApplicationContext(),new MyGestureListener());
        gestureDetector_gridView.setIsLongpressEnabled(true);


        onClickListener();
    }

    private void init(){

        gvGallery = findViewById(R.id.gridView_gallery);
        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_message= findViewById(R.id.imageView_message);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);

        textView_profileName = findViewById(R.id.textView_profileName);
        textView_editProfile = findViewById(R.id.textView_editProfile);
        textView_editProfile2 = findViewById(R.id.textView_editProfile2);
        textView_addBio = findViewById(R.id.textView_addBio);
        textView_setPreferences = findViewById(R.id.textView_setPreferences);
        textView_changeProfilePhoto = findViewById(R.id.textView_changeProfilePhoto);
        relativeLayout_changeProfilePic = findViewById(R.id.relativeLayout_changeProfilePic);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);

        viewPager2_singleImage= findViewById(R.id.viewPager2_singleImage);
        imageView_addNew= findViewById(R.id.imageView_addNew);
        linearlayout_sharePhoto= findViewById(R.id.linearlayout_sharePhoto);
        mArrayUri = new ArrayList<Uri>();
        selectedImageList = new ArrayList<String>();

        userModel = CustomSharedPreference.getInstance(MyProfileActivity.this).getUser();


        textView_profileName.setText(userModel.getFullName());

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

    }


    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                   // Toast.makeText(MyProfileActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(MyProfileActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void onClickListener() {

        onClickNewActivity(imageView_home, MyProfileActivity.this, HomeActivity.class);
        onClickNewActivity(imageView_search, MyProfileActivity.this, SetPreferencesActivity.class);
        onClickNewActivity(imageView_message, MyProfileActivity.this, DirectMessagesActivity.class);
        onClickNewActivity(imageView_like, MyProfileActivity.this, InterestActivity.class);
        onClickNewActivity(textView_editProfile, MyProfileActivity.this, MainActivity.class);
        onClickNewActivity(textView_editProfile2, MyProfileActivity.this, MainActivity.class);
        onClickNewActivity(textView_setPreferences, MyProfileActivity.this, SetPreferencesActivity.class);


        relativeLayout_changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilepicDialog();
            }
        });
        textView_changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilepicDialog();
            }
        });



    }

    private void changeProfilepicDialog() {
        customDialogChangeProfilePic.show();

        customDialogChangeProfilePic.textView_addFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogChangeProfilePic.dismiss();
                intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, REQUEST_CAMERA);
            }
        });


        customDialogChangeProfilePic.textView_addFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogChangeProfilePic.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode)
            {
                case SELECT_FILE:
                    onSelectFromGalleryResult(data);
                    break;
                case REQUEST_CAMERA:
                    onCaptureImageResult(data);
                    break;
                case ACTIVITY_CONSTANT:
                     onGalleryActivityResult(data);
                    break;
            }


        }

    }

    private void onGalleryActivityResult(Intent data) {
        Bundle bundle = data.getExtras();

        if(bundle !=null)
        {
            selectedImageList.clear();
            String [] images = bundle.getStringArray("selectedImageList");

            for(int i=0; i<images.length; i++)
            {
                Uri t_uri = Uri.fromFile(new File(images[i]));//FileProvider.getUriForFile(MyProfileActivity.this, "com.example.matrimonyapp", new File(images[i]));
                mArrayUri.add(t_uri);
            }
            if(mArrayUri.size()>0)
        {
            linearlayout_sharePhoto.setVisibility(View.GONE);
        }
        else
        {
            linearlayout_sharePhoto.setVisibility(View.VISIBLE);
        }
            galleryAdapter.notifyDataSetChanged();
            setGridViewHeight(gvGallery,3);

            Toast.makeText(MyProfileActivity.this, "Recv. " + mArrayUri.size()
                    + "\n No " + bundle.getInt("no"), Toast.LENGTH_SHORT).show();
        }

    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bm, 0, 0, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        }
        circleImageView_profilePic.setImageBitmap(bm);
/*

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");
*/

    }


    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(MyProfileActivity.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(MyProfileActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(MyProfileActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }
    private void onCaptureImageResult(Intent data) {


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();


        Bitmap newBitmap = Bitmap.createBitmap(thumbnail.getWidth(), thumbnail.getHeight(), thumbnail.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(thumbnail, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);


        /*thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        ImageUrl= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);*/
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // img_banner_profile_placeholder.setImageBitmap(thumbnail);

        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        //circleImageView_profilePic.setImageBitmap(imageBitmap);
/*
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");*/


    }

    private void onClickNewActivity(View view, final Context context, final Class goToClass) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, goToClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });


    }


  private void navigation()
  {
      drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
      expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
      final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

      //arrayList_expandedMenuModel = new ArrayList<>();
        View view_header = navigationView.getHeaderView(0);

        circleImageView_headerProfilePic = view_header.findViewById(R.id.circleImageView_profilePic);
        textView_welcomeUserName = view_header.findViewById(R.id.textView_welcomeUserName);
        TextView textView_emailId= view_header.findViewById(R.id.textView_emailId);


        Glide.with(MyProfileActivity.this)
              .load(URLs.MainURL+userModel.getProfilePic())
              .placeholder(R.drawable.default_profile)
              .into(circleImageView_headerProfilePic);

      textView_welcomeUserName.setText(userModel.getFullName());
      textView_emailId.setText(userModel.getEmailId());

      CustomNavigationView customNavigationView = new CustomNavigationView(MyProfileActivity.this,
              drawerLayout, expandableList, navigationView);

      customNavigationView.createNavigation();


      findViewById(R.id.imageView_menu).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


              if(drawerLayout.isDrawerOpen(navigationView))
              {
                  drawerLayout.closeDrawers();
              }
              else {
                  drawerLayout.openDrawer(navigationView);
              }

          }
      });

      //prepareListData();


      // setting list adapter

/*
      expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
          @Override
          public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
              //Log.d("DEBUG", "submenu item clicked");
              return false;
          }
      });
      expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
          @Override
          public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
              //Log.d("DEBUG", "heading clicked");
              return false;
          }
      });*/
  }
    /*private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });




    }*/
    private void setToolbar() {

/*        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
       // actionBar.setTitle("");*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
       /* findViewById(R.id.imageView_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Click", "keryu");

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });*/

    }




    void setGridViewHeight(GridView gridView, int noOfColumns)
    {

        ListAdapter gridViewAdapter = gridView.getAdapter();


        if(gridViewAdapter == null || mArrayUri.size()==0)
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


    void insertProfilePic()
    {



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_PROFILEPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                             //   getDetails();
                                getProfilePic();
                                //Picasso.get().(userModel.getProfilePic()).into(circleImageView_profilePic);

                               // userModel.setProfilePic(URLs.MainURL+jsonObject.getString("ImageUrl"));
                                Toast.makeText(MyProfileActivity.this,"Profile Pic updated successfully!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(MyProfileActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyProfileActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("UserId",userModel.getUserId());
                params.put("ProfileImage",selfieString);


                return params;
            }
        };

        VolleySingleton.getInstance(MyProfileActivity.this).addToRequestQueue(stringRequest);



    }

    void getProfilePic()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFILEPIC + "UserId=" + userModel.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()==1)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {

                                    //ImageUrl - 0 or 1 :- default img
                                    if(!(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("1")
                                        && !(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("0"))
                                    {
                                        //to clear cache
                                        Picasso.get().invalidate(URLs.MainURL + jsonObject.getString("ImageUrl"));
                                        Picasso.get().load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .error(R.drawable.default_profile)
                                                .placeholder(R.drawable.default_profile)
                                                .into(circleImageView_profilePic);

                                        userModel.setProfilePic(URLs.MainURL+jsonObject.getString("ImageUrl"));


                                    }
                                    else{
                                        userModel.setProfilePic("");
                                    }

                                }
                                else
                                {
                                    Toast.makeText(MyProfileActivity.this, "Error while loading Profile pic! ", Toast.LENGTH_SHORT).show();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        VolleySingleton.getInstance(MyProfileActivity.this).addToRequestQueue(stringRequest);

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if(params[0].equals("InsertProfilePic"))
            {
                insertProfilePic();
            }
            if(params[0].equals("GetProfilePic"))
            {
                getProfilePic();
            }

            return null;
        }
    }


}
