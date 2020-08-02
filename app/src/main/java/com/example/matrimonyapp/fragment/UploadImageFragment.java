package com.example.matrimonyapp.fragment;


import android.app.Activity;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.GalleryAdapter;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadImageFragment extends Fragment {


    View view;
    private TextView textView_uploadImage, textView_saveAndContinue;

    private ImageView imageView_back, imageView_upload, imageView_addNew;

    private int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_SELECT_IMAGE = 100;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "GalleryUtil";
    ArrayList<Uri> mArrayUri;
    String currentPhotoPath;
    File photoFile = null;

    TextView textView_profileName;

    ImageView imageView;

    Bundle bundle;

    final int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;


    public UploadImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_image, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imageView_back = ((MainActivity) getActivity()).findViewById(R.id.imageView_back);
        TextView tv = ((MainActivity) getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Upload Image");

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
            }
        });


        mArrayUri= new ArrayList<Uri>();
        //textView_uploadImage = view.findViewById(R.id.textView_upload);
        imageView_upload = view.findViewById(R.id.profile_image);
        imageView_addNew = view.findViewById(R.id.imageView_addNew);

        gvGallery = view.findViewById(R.id.gridView_gallery);

        textView_profileName = view.findViewById(R.id.textView_profileName);

//        textView_profileName.setText(bundle.getString("fullName"));

        imageView_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an Intent with action as ACTION_PICK
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, RESULT_SELECT_IMAGE);



/*
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra("crop","true");
            intent.putExtra("aspectX",0);
            intent.putExtra("aspectY",0);
            try{
                intent.putExtra("return-data",true);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"),RESULT_SELECT_IMAGE);
            }
            catch (ActivityNotFoundException e)
            {}
*/


            }
        });


        imageView_addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);


               /*
                Intent intent = new Intent(getContext(), MultiPhotoSelectActivity.class);
                startActivity(intent);
               */


            }
        });

        /*textView_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
*/
        /*                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,RESULT_SELECT_IMAGE);*/
        /*
                //startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

                //Create an Intent with action as ACTION_PICK
                Intent intent = new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, RESULT_SELECT_IMAGE);
                // Launching the Intent


            }

        });*/


        return view;

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data!=null) {
            Uri selectedImage;
            switch (requestCode) {
                case RESULT_SELECT_IMAGE:
                    //data.getData returns the content URI for the selected Image

                    /*selectedImage = data.getData();
                    imageView_upload.setImageURI(selectedImage);*/

                    Bundle extras = data.getExtras();
                    if(extras!=null)
                    {
                        Bitmap photo = extras.getParcelable("data");
                        imageView_upload.setImageBitmap(photo);
                    }


                    break;
                case PICK_IMAGE_MULTIPLE:

                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    imagesEncodedList = new ArrayList<String>();

                    if(data.getData()!=null){

                        Uri mImageUri=data.getData();

                        // Get the cursor
                        Cursor cursor = getContext().getContentResolver().query(mImageUri,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imageEncoded  = cursor.getString(columnIndex);
                        cursor.close();
                        if(mArrayUri==null)
                        {}

                        mArrayUri.add(mImageUri);
                        galleryAdapter = new GalleryAdapter(getContext(),mArrayUri,getActivity(),getResources());
                        gvGallery.setAdapter(galleryAdapter);
                        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                .getLayoutParams();
                        mlp.setMargins(0, 5, 0, 1);

                    }
                    else
                    {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();


                            for (int i = 0; i < mClipData.getItemCount(); i++) {

                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();


                                try {

                                    InputStream imageStream = getContext().getContentResolver().openInputStream(uri);
                                    Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);
                                    bitmapImage = getResizedBitmap(bitmapImage,400);
                                    uri = getImageUri(getContext(),bitmapImage);

                                }
                                catch (Exception exception)
                                {
                                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imageEncoded  = cursor.getString(columnIndex);
                                imagesEncodedList.add(imageEncoded);
                                cursor.close();

                                galleryAdapter = new GalleryAdapter(getContext(),mArrayUri,getActivity(),getResources());
                                gvGallery.setAdapter(galleryAdapter);
                                gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                        .getLayoutParams();
                                mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()


                                setGridViewHeight(gvGallery,3);

                            }
                            Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                        }
                    }


                    break;


            }


        }


    }

    public Uri getImageUri(Context context, Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize)
    {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width/(float)height;
        if(bitmapRatio>1)
        {
            width = maxSize;
            height = (int)(width/bitmapRatio);
        }
        else{
            height = maxSize;
            width = (int)(height*bitmapRatio);

        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}