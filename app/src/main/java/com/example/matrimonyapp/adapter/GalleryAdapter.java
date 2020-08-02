package com.example.matrimonyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.PostsActivity;
import com.example.matrimonyapp.customViews.CustomDialogImagePreview;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    Activity activity;
    private Context context;
    private int pos;
    private LayoutInflater inflater;
    private ImageView ivGallery;
    ArrayList<Uri> mArrayUri;
    Resources resources;
    CustomDialogImagePreview customDialogImagePreview;



    public GalleryAdapter(Context context, ArrayList<Uri> mArrayUri, Activity activity, Resources resources) {

        this.context = context;
        this.mArrayUri = mArrayUri;
        this.activity = activity;
        this.resources = resources;

    }

    @Override
    public int getCount() {
        return mArrayUri.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayUri.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        pos = position;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.gv_item, parent, false);

        ivGallery = (ImageView) itemView.findViewById(R.id.ivGallery);

        ivGallery.setImageURI(mArrayUri.get(position));



        /*ivGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                 CustomDialogImagePreview customDialogImagePreview =
                        new CustomDialogImagePreview(ctx, mArrayUri.get(position));



                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    customDialogImagePreview.cancel();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {


                    customDialogImagePreview.show();
                    Bitmap map=takeScreenShot(activity);

                    Bitmap fast=fastblur(map, 10);

                    final Drawable draw=new BitmapDrawable(resources ,fast);


                    customDialogImagePreview.getWindow().setBackgroundDrawable(draw);


                    */
        /*WindowManager.LayoutParams lp = customDialogImagePreview.getWindow().getAttributes();
                    lp.dimAmount = 0.5f;
                    customDialogImagePreview.getWindow().setAttributes(lp);
                    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
        /*
                    customDialogImagePreview.getWindow().setDimAmount(0.5f);
                    customDialogImagePreview.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                }





                return true;
            }
        });*/



                /*new CustomDialogImagePreview(ctx, mArrayUri.get(position));

        Bitmap map=takeScreenShot(activity);

        Bitmap fast=fastblur(map, 10);

        final Drawable draw=new BitmapDrawable(resources ,fast);


        customDialogImagePreview.getWindow().setBackgroundDrawable(draw);
*/



                ivGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(context, PostsActivity.class);
                        context.startActivity(intent);

                    }
                });

                ivGallery.setFocusableInTouchMode(true);

       /* ivGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = motionEvent.getActionMasked();

                customDialogImagePreview = new CustomDialogImagePreview(ctx, mArrayUri.get(position));


                if(action == MotionEvent.ACTION_DOWN)
                {
                    Bitmap map =takeScreenShot(activity);

                    Bitmap fast=fastblur(map, 10);

                    final Drawable draw=new BitmapDrawable(resources ,fast);


                    customDialogImagePreview.getWindow().setBackgroundDrawable(draw);


                    customDialogImagePreview.show();


                }
                if(customDialogImagePreview.isShowing())
                {
                    view.getParent().requestDisallowInterceptTouchEvent(true);

                    if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
                    {


                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        customDialogImagePreview.dismiss();
                        return true;

                }

                }


                return false;
            }


        });*/



        ivGallery.setOnLongClickListener(new View.OnLongClickListener() {


            @Override
            public boolean onLongClick(View view) {


                CustomDialogImagePreview customDialogImagePreview =
                        new CustomDialogImagePreview(context, mArrayUri.get(position));
                customDialogImagePreview.show();


                Bitmap map=takeScreenShot(activity);

                Bitmap fast=fastblur(map, 10);

                final Drawable draw=new BitmapDrawable(resources ,fast);


                customDialogImagePreview.getWindow().setBackgroundDrawable(draw);

                //customDialogImagePreview.getWindow().setDimAmount(0.5f);
                //customDialogImagePreview.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                return false;
            }
        });

        return itemView;
    }

    private static Bitmap takeScreenShot(Activity activity)
    {


        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        //b.setHasAlpha(true);
//        b.setDensity(1);
        view.destroyDrawingCache();
        return b;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
       //bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Color.RED);
        ColorFilter colorFilter = new LightingColorFilter(0xFF7F7F7F,0x00000000);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        return (bitmap);
    }



}