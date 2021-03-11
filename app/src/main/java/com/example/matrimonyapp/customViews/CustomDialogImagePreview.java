package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matrimonyapp.R;



public class CustomDialogImagePreview extends Dialog implements View.OnClickListener {

/*
    public CustomRecyclerViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomRecyclerViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }*/

    Activity activity;
    public Context context;
    public Dialog dialog;
    TextView title, textView_customDialogName;

    boolean bool_like;

    Uri previewImage;

    /*GalleryAdapter adapter;*/

    ImageView imageView_previewImage, imageView_like;
    de.hdodenhof.circleimageview.CircleImageView circleImage_customDialogProfile;


    public CustomDialogImagePreview(Context context, Uri previewImage) {
        super(context);
        this.context = context;
       // this.adapter = adapter;
        bool_like =false;
        this.previewImage = previewImage;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.dialog_on_image_long_press);
        setTitle("Image... ");


        setCanceledOnTouchOutside(true);
       // getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        imageView_like = findViewById(R.id.imageView_like);
        circleImage_customDialogProfile = findViewById(R.id.circleImage_customDialogProfile);
        textView_customDialogName = findViewById(R.id.textView_customDialogName);
        imageView_previewImage = findViewById(R.id.imageView_previewImage);

        imageView_previewImage.setImageURI(previewImage);


        imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_like==false)
                {
                    imageView_like.setBackgroundResource(R.drawable.red_heart);
                    bool_like = true;
                }
                else
                {
                    imageView_like.setBackgroundResource(R.drawable.black_heart);
                    bool_like = false;
                }
            }
        });


    }


    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.txt_cancel:
                dismiss();
                break;
            default:
                break;
        }*/
        dismiss();
    }

    private void blurBackground()
    {
/*
        //take Screenshot
        Bitmap bitmap = Screenshot.getInstance().takeScreenshotForScreen((Activity)context);

        //set blurring factor and heighth width of screenshot
        BlurFactor blurFactor = new BlurFactor();
        blurFactor.height = bitmap.getHeight();
        blurFactor.width = bitmap.getWidth();
        blurFactor.color = context.getResources().getColor(R.color.transparent_bg);

        //blurred image
        Bitmap blurBitmap = Blur.of(context, bitmap, blurFactor);
        //convert blurred image into drawable
        Drawable drawable = new BitmapDrawable(context.getResources(), blurBitmap);

        //set blurred screenshot to background
        getWindow().setBackgroundDrawable(drawable);*/

    }


}
