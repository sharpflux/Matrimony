package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSelectedAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.MarginDecoration;
import com.example.matrimonyapp.utils.PictureFacer;
import com.example.matrimonyapp.adapter.picture_Adapter;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleyMultipartRequest;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadSelectedActivity extends AppCompatActivity {

    TextView tvResult,textView_toolbar,txt_saveAndContinue;
    Bundle bundle;
    RecyclerView imageRecycler;
    ArrayList<PictureFacer> allpictures;
    ArrayList<PictureFacer> selectedItem = new ArrayList<>();
    ImageSelectedAdapter interestListAdapter;
    private Bitmap bitmap;
    private UserModel userModel;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_selected);
        allpictures = new ArrayList<>();
        userModel = CustomSharedPreference.getInstance(ImageUploadSelectedActivity.this).getUser();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(ImageUploadSelectedActivity.this);



        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        imageRecycler.setLayoutManager(mLayoutManager);


        textView_toolbar=findViewById(R.id.textView_toolbar);
        txt_saveAndContinue=findViewById(R.id.txt_saveAndContinue);


        txt_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogLoadingProgressBar.show();
                customDialogLoadingProgressBar.setCancelable(false);
                UploadImages();
            }
        });

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



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void UploadImages() {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_POST_IMAGEUPLOADMULTIPLE, new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            customDialogLoadingProgressBar.dismiss();
                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getBoolean("error")){
                                Toast.makeText(ImageUploadSelectedActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ImageUploadSelectedActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                Intent returnIntent = new Intent(ImageUploadSelectedActivity.this,EditProfileActivity.class);
                                returnIntent.putExtra("result", new Gson().toJson(selectedItem));
                                startActivity(returnIntent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        customDialogLoadingProgressBar.dismiss();
                        Toast.makeText(ImageUploadSelectedActivity.this,"Network Error !" +error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", userModel.getUserId());
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                int i = 0;
                for (PictureFacer item : allpictures) {
                    i++;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.fromFile(new File(item.getPicturePath())) );
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                        params.put("image_file"+i, new DataPart("image"+i+".jpg", getFileDataFromDrawable(bitmap) ));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQueue(volleyMultipartRequest);

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}