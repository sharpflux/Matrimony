package com.example.matrimonyapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.GalleryImagesAdapter;
import com.example.matrimonyapp.adapter.SelectedImageAdapter;
import com.example.matrimonyapp.modal.GalleryFolderModel;
import com.example.matrimonyapp.modal.GalleryImageModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int STORAGE_PERMISSION = 100;

    private String currentLanguage;

    //
    // Views
    RecyclerView imageRecyclerView, selectedImageRecyclerView;
    Spinner spinner_folderNames;
    Button done;
    ImageView imageView_back;
    TextView textView_noImageSelected;

    // ArrayLists
    ArrayList<GalleryFolderModel> galleryFolderModelArrayList;
    ArrayList<String> selectedImageList;

    //Adapter
    GalleryImagesAdapter galleryImagesAdapter;
    SelectedImageAdapter selectedImageAdapter;

    File imageFile;
    String newCameraImagePhotoPath;
    //int[] resImg = {R.drawable.ic_camera_white_30dp, R.drawable.ic_folder_white_30dp};
    String[] title = {"Camera", "Folder"};
    boolean boolean_folder;
    static int maxSelectedImages = 5;
    ArrayList<String> stringSelectedArrayList;

    UserModel userModel;
    StringBuilder stringBuilder_multipleImages;
    private int noOfSelectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        if (isStoragePermissionGranted()) {
            init();
            //getAllImages();

            getAllImagesByFolder();
            setImageList(0);

            setSelectedImageList();

            onClickListener();
            onSpinnerClick();


        }

        currentLanguage = getResources().getConfiguration().locale.getLanguage();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

    private boolean isSelectedImageMax() {

        if(selectedImageList.size()>=maxSelectedImages)
        {
            return true;
        }
        else return false;

    }

    private void onClickListener() {

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void init() {
        imageRecyclerView = findViewById(R.id.recycler_view);
        selectedImageRecyclerView = findViewById(R.id.selected_recycler_view);
        done = findViewById(R.id.done);
        spinner_folderNames = findViewById(R.id.spinner_folderNames);
        imageView_back = findViewById(R.id.imageView_back);
        textView_noImageSelected = findViewById(R.id.textView_noImageSelected);

        selectedImageList = new ArrayList<>();
        galleryFolderModelArrayList = new ArrayList<>();
        stringSelectedArrayList = new ArrayList<String>();
        stringBuilder_multipleImages = new StringBuilder();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            noOfSelectedImages = bundle.getInt("noOfSelectedImages");
            maxSelectedImages = maxSelectedImages - noOfSelectedImages;
        }
        userModel = CustomSharedPreference.getInstance(GalleryActivity.this).getUser();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringSelectedArrayList.clear();
                for (int i = 0; i < selectedImageList.size(); i++)
                {

                    /*Bitmap t_bitmap = BitmapFactory.decodeFile(selectedImageList.get(i));
                    Bitmap newBitmap = Bitmap.createBitmap(t_bitmap.getWidth(), t_bitmap.getHeight(), t_bitmap.getConfig());
                    Canvas canvas = new Canvas(newBitmap);
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(t_bitmap, 0, 0, null);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

                    stringSelectedArrayList.add( Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));*/


                    //Toast.makeText(getApplicationContext(), new File(selectedImageList.get(i)).getParentFile().getName(), Toast.LENGTH_LONG).show();
                }
                String [] str = new String[selectedImageList.size()] ;

                for(int i=0; i< selectedImageList.size(); i++)
                {
                    str[i] = selectedImageList.get(i);
                }

                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("InsertMultipleImages");

                Intent intent = new Intent();
                intent.putExtra("selectedImageList",str);
                intent.putExtra("no",str.length);
                //Toast.makeText(GalleryActivity.this, " No. " + selectedImageList.size(), Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }


    public void getFolderPosition(String imagePath){

        File file = new File(imagePath);
        String folderName = file.getParentFile().getName();

        int folderPosition=0;

        for (int i = 0; i < galleryFolderModelArrayList.size(); i++)
        {
            if(galleryFolderModelArrayList.get(i).getFolderName().equals(folderName))
            {
                folderPosition = i;
                break;


            }
        }

        for(int j=0; j<galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList().size(); j++)
        {
            if(galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList().get(j).getImagePath().equals(imagePath))
            {
                unSelectImage(folderPosition, j);
                //Toast.makeText(GalleryActivity.this, "FolderName - "+galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList().get(j).getImagePath(), Toast.LENGTH_SHORT).show();

                return;
            }
        }

    }

    public ArrayList<GalleryFolderModel> getAllImagesByFolder() {
        galleryFolderModelArrayList.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN; // original was - DATE_TAKEN
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < galleryFolderModelArrayList.size(); i++) {
                if (galleryFolderModelArrayList.get(i).getFolderName().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder) {

                ArrayList<GalleryImageModel> t_galleryImageModelArrayList = new ArrayList<>();
                t_galleryImageModelArrayList .addAll(galleryFolderModelArrayList.get(int_position).getGalleryImageModelArrayList());

                GalleryImageModel t_galleryImageModel = new GalleryImageModel();
                t_galleryImageModel.setImagePath(absolutePathOfImage);
                t_galleryImageModel.setSelected(false);
                t_galleryImageModelArrayList .add(t_galleryImageModel);
                galleryFolderModelArrayList.get(int_position).setGalleryImageModelArrayList(t_galleryImageModelArrayList);

            }
            else
            {
                ArrayList<GalleryImageModel> t_galleryImageModelArrayList = new ArrayList<>();
                GalleryImageModel t_galleryImageModel = new GalleryImageModel();
                t_galleryImageModel.setImagePath(absolutePathOfImage);
                t_galleryImageModel.setSelected(false);

                t_galleryImageModelArrayList.add(t_galleryImageModel);
                GalleryFolderModel t_galleryFolderModel = new GalleryFolderModel();
                t_galleryFolderModel.setFolderName(cursor.getString(column_index_folder_name));

                t_galleryFolderModel.setGalleryImageModelArrayList(t_galleryImageModelArrayList);

                galleryFolderModelArrayList.add(t_galleryFolderModel );


            }


        }


        for (int i = 0; i < galleryFolderModelArrayList.size(); i++)
        {
            Log.e("FOLDER", galleryFolderModelArrayList.get(i).getFolderName());
            for (int j = 0; j < galleryFolderModelArrayList.get(i).getGalleryImageModelArrayList().size(); j++)
            {
                Log.e("FILE", galleryFolderModelArrayList.get(i).getGalleryImageModelArrayList().get(j).getImagePath());
            }
        }

        Collections.sort(galleryFolderModelArrayList, GalleryFolderModel.folderModelComparator);

        ArrayList al_folderNames = new ArrayList();
        for(int i=0; i<galleryFolderModelArrayList.size(); i++)
        {
            al_folderNames.add(galleryFolderModelArrayList.get(i).getFolderName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(GalleryActivity.this, R.layout.support_simple_spinner_dropdown_item,al_folderNames);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_folderNames.setAdapter(dataAdapter);


        return galleryFolderModelArrayList;
    }


    public void setImageList(final int folderPosition) {
        imageRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        galleryImagesAdapter = new GalleryImagesAdapter(getApplicationContext(), galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList());
        imageRecyclerView.setAdapter(galleryImagesAdapter);

        galleryImagesAdapter.setOnItemClickListener(new GalleryImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
/*                if (position == 0) {
                    takePicture();
                }*/
/*                else if (position == 1)
                {
                    getPickImageIntent();
                } */
                //              else
                {
                    try {
                        if (!galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList().get(position).isSelected()) {
                            selectImage(folderPosition,position);
                        } else {
                            unSelectImage(folderPosition,position);
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException ed) {
                        ed.printStackTrace();
                    }
                }
            }
        });
        //setImagePickerList();
    }

    public void setSelectedImageList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        selectedImageRecyclerView.setLayoutManager(layoutManager);
        selectedImageAdapter = new SelectedImageAdapter(this, selectedImageList,
                getWindowManager().getDefaultDisplay());
        selectedImageRecyclerView.setAdapter(selectedImageAdapter);

    }
    private void onSpinnerClick() {

        spinner_folderNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                setImageList(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // start the image capture Intent
    public void takePicture() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Continue only if the File was successfully created;
        File photoFile = createImageFile();
        if (photoFile != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public File createImageFile() {
        // Create an image file name
        String dateTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + dateTime + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        newCameraImagePhotoPath = "file:" + imageFile.getAbsolutePath();
        return imageFile;
    }

    // Add image in selectedImageList
    public void selectImage(int folderPosition, int imagePosition) {
        // Check before add new item in ArrayList;
        if (!selectedImageList.contains(galleryFolderModelArrayList.get(folderPosition).
                getGalleryImageModelArrayList().get(imagePosition).getImagePath()))
        {

            if(!isSelectedImageMax())
            {
                galleryFolderModelArrayList.get(folderPosition).getGalleryImageModelArrayList()
                    .get(imagePosition).setSelected(true);
                selectedImageList.add(0, galleryFolderModelArrayList.get(folderPosition).
                        getGalleryImageModelArrayList().get(imagePosition).getImagePath());
                textView_noImageSelected.setVisibility(View.GONE);
            }
            else
            {
                Toast.makeText(GalleryActivity.this, "Can't share more than "+
                        maxSelectedImages+" media items", Toast.LENGTH_SHORT).show();
            }

            selectedImageAdapter.notifyDataSetChanged();
            galleryImagesAdapter.notifyDataSetChanged();
        }
    }

    // Remove image from selectedImageList
    public void unSelectImage(int folderPosition, int imagePosition) {
        for (int i = 0; i < selectedImageList.size(); i++){
            if (galleryFolderModelArrayList.get(folderPosition).
                    getGalleryImageModelArrayList().get(imagePosition).getImagePath() != null) {
                if (selectedImageList.get(i).equals(galleryFolderModelArrayList.get(folderPosition).
                        getGalleryImageModelArrayList().get(imagePosition).getImagePath()))
                {
                    galleryFolderModelArrayList.get(folderPosition).
                            getGalleryImageModelArrayList().get(imagePosition).setSelected(false);
                    selectedImageList.remove(i);
                    selectedImageAdapter.notifyDataSetChanged();
                    galleryImagesAdapter.notifyDataSetChanged();
                    if(selectedImageList.size()==0)
                    {
                        textView_noImageSelected.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }


    // Add Camera and Folder in ArrayList
   /* public void setImagePickerList() {
        for (int i = 0; i < resImg.length; i++) {
            GalleryImageModel galleryImageModel = new GalleryImageModel();
            //galleryImageModel.setResImg(resImg[i]);
            galleryImageModel.setTitle(title[i]);

            GalleryFolderModel t_galleryFolderModel = new GalleryFolderModel();

            ArrayList<GalleryImageModel> arrayList = new ArrayList<GalleryImageModel>();
            arrayList.add(galleryImageModel);
            t_galleryFolderModel.setGalleryImageModelArrayList(arrayList);
            galleryFolderModelArrayList.add(i, t_galleryFolderModel);
        }
        galleryImagesAdapter.notifyDataSetChanged();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (newCameraImagePhotoPath != null) {
                    addImage(newCameraImagePhotoPath);
                }
            }

        }
    }


    // add image in selectedImageList and imageList
    public void addImage(String filePath)
    {
        GalleryImageModel t_galleryImageModel = new GalleryImageModel();
        t_galleryImageModel.setImagePath(filePath);
        t_galleryImageModel.setSelected(true);
        //galleryFolderModelArrayList.add(2, t_galleryImageModel);
        selectedImageList.add(0, filePath);
        selectedImageAdapter.notifyDataSetChanged();
        galleryImagesAdapter.notifyDataSetChanged();
    }


    public boolean isStoragePermissionGranted() {
        int ACCESS_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if ((ACCESS_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
            return false;
        }
        return true;
    }



    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if(params[0].equals("InsertMultipleImages"))
            {
                insertImages();
            }
            if(params[0].equals("GetProfilePic"))
            {
                //getProfilePic();
            }

            return null;
        }
    }

    void insertImages()
    {


        // MultipleImages XML
        stringBuilder_multipleImages.append("<?xml version=\"1.0\" ?>");

        stringBuilder_multipleImages.append("<MultipleImages>");



        for(int i=0; i<selectedImageList.size(); i++)
        {

            Bitmap t_bitmap = BitmapFactory.decodeFile(selectedImageList.get(i));
            Bitmap newBitmap = Bitmap.createBitmap(t_bitmap.getWidth(), t_bitmap.getHeight(), t_bitmap.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(t_bitmap, 0, 0, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);


            String imageString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

            //stringBuilder_multipleImages.append("<Image>");

            //stringBuilder_multipleImages.append("<UserId>" + userModel.getUserId() + "</UserId>");
            stringBuilder_multipleImages.append("<ImageUrl>" + imageString+ "</ImageUrl>");


            //stringBuilder_multipleImages.append("</Image>");


        }

        stringBuilder_multipleImages.append("</MultipleImages>");





        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_MULTIPLEIMAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                                //   getDetails();
                                ///getProfilePic();

                                Toast.makeText(GalleryActivity.this,"Images uploaded successfully!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(GalleryActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GalleryActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("UserId",userModel.getUserId());
                params.put("xmlImages",stringBuilder_multipleImages.toString());


                return params;
            }
        };

        VolleySingleton.getInstance(GalleryActivity.this).addToRequestQueue(stringRequest);

    }

}
