package com.example.matrimonyapp.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.matrimonyapp.activity.EditProfileActivity;
import com.example.matrimonyapp.activity.ImageUploadSelectedActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.PictureFacer;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadJobService  extends JobService {
    private static final String TAG = ImageUploadJobService.class.getSimpleName();
    private UserModel userModel;
    private Bitmap bitmap;
    ArrayList<PictureFacer> allpictures;
    JSONArray jsonArraySelectedImages;
    @Override
    public boolean onStartJob(final JobParameters params) {





        allpictures = new ArrayList<>();

        HandlerThread handlerThread = new HandlerThread("SomeOtherThread");
        handlerThread.start();
        userModel = CustomSharedPreference.getInstance(getApplicationContext()).getUser();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(TAG, "Running!!!!!!!!!!!!!");

                    String selectedImages=    params.getExtras().getString("selectedImages");
                    jsonArraySelectedImages = new JSONArray(selectedImages);
                    for (int i = 0; i < jsonArraySelectedImages.length(); i++) {
                        PictureFacer student = new Gson().fromJson(jsonArraySelectedImages.get(i).toString(), PictureFacer.class);
                        allpictures.add(student);
                    }
                    UploadImages(params);
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        });

        return true;
    }


    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void UploadImages(final JobParameters parameters) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URLs.URL_POST_IMAGEUPLOADMULTIPLE, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {

                    JSONObject obj = new JSONObject(new String(response.data));

                    if(obj.getBoolean("error")){
                        Toast.makeText(ImageUploadJobService.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ImageUploadJobService.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        jobFinished(parameters, false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ImageUploadJobService.this,"Network Error !" +error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
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
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(new File(item.getPicturePath())) );
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



    @Override
    public boolean onStopJob(final JobParameters params) {
        Log.d(TAG, "onStopJob() was called");
        return true;
    }
}
