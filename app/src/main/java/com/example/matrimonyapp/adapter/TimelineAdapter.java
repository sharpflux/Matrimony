package com.example.matrimonyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.SignalRMessagesActivity;
import com.example.matrimonyapp.activity.ViewProfileActivity;
import com.example.matrimonyapp.activity.ViewProfileDetailsActivity;
import com.example.matrimonyapp.fragment.PersonalDetailsFragment;
import com.example.matrimonyapp.listener.OnSwipeTouchListener;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.modal.UsersConnectionModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
//import com.jackandphantom.androidlikebutton.AndroidLikeButton;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {


    Context context;
    private static int pos;
    private LayoutInflater layoutInflater;
    ArrayList<TimelineModel> list;

    ImageView imageView_like;
    boolean bool_like=false;
    boolean bool_favorite=false;
    Display display;
    //private Bitmap bitmap;
    //private LinearLayout.LayoutParams params;//= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);;


    private UserModel userModel;

    public TimelineAdapter(Context context, ArrayList<TimelineModel> list, Display display)
    {
        this.context = context;
        this.list = list;
        this.display = display;
        this.userModel = CustomSharedPreference.getInstance(context).getUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_profiles, parent, false);
        TimelineAdapter.ViewHolder viewHolder = new TimelineAdapter.ViewHolder(listItem);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final TimelineModel item = list.get(position);


        holder.relativeLayout.setOnTouchListener(new OnSwipeTouchListener(context){
            @Override
            public void onDoubleTaps() {
                super.onDoubleTaps();

               /* holder.imageView_doubleTapFav.setVisibility(View.VISIBLE);

                holder.imageView_doubleTapFav.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.imageView_doubleTapFav.setVisibility(View.INVISIBLE);
                    }
                },400);*/

            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
/*
                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {

                        holder.relativeLayout.animate().alpha(1f).setDuration(400).translationX(0);
                    }
                },400);


               */



            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

/*
                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(-size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                },400);*/

               /* list.remove(position);
                notifyDataSetChanged();
*/
            }
        });


        holder.imageView_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Point size = new Point();
                display.getSize(size);


                holder.relativeLayout.animate().alpha(0.2f).setDuration(400).translationX(-size.x+10);
                holder.relativeLayout.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.remove(position);
                        notifyDataSetChanged();
                    }
                },400);

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(item.getUserId(),String.valueOf(UsersConnectionModel.REJECTED),"0");
            }
        });


        holder.textView_userId.setText(list.get(position).getUserName());
        holder.textView_userName.setText(list.get(position).getUserName());
        holder.textView_userAge.setText(list.get(position).getUserAge()+" "+context.getResources().getString(R.string.years));
        holder.textView_userQualification.setText(list.get(position).getUserQualification());
        holder.textView_userCity.setText(list.get(position).getUserCity());
        holder.textView_userMaritalStatus.setText(list.get(position).getUserMaritalStatus());
        holder.textView_userReligion.setText(list.get(position).getUserReligion());
        holder.textView_userHeight.setText(list.get(position).getUserHeight()+" cms");

        holder.textView_userBio.setText(Html.fromHtml("<b>"+list.get(position).getUserName()+"</b>  "
                +" "+list.get(position).getUserEmail()+" "));


        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop()
                .placeholder(R.color.quantum_grey100)
                .into(holder.circleImage_profilePic);



    RequestBuilder thubnail = Glide.with(context).load(URLs.MainURL+list.get(position).getProfilePic());
/*        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.color.quantum_grey100)
                .into(holder.imageView_profilePic);*/

        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(URLs.MainURL+list.get(position).getProfilePic())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.color.quantum_grey100)
                .error(R.drawable.default_profile)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false; // important to return false so the error placeholder can be placed
                    }


                })
                .into(holder.imageView_profilePic);


        holder.textView_userQualification.setText(list.get(position).getUserQualification());

        holder.textView_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ViewProfileDetailsActivity.class);
                intent.putExtra("userId", item.getUserId());
                intent.putExtra("userName", item.getUserName());
                intent.putExtra("userProfilePic", item.getProfilePic());
                intent.putExtra("userQualification", item.getUserQualification());
                intent.putExtra("userOccupation", item.getUserOccupation());
                intent.putExtra("userCompany", item.getUserCompany());
                intent.putExtra("userAge", item.getUserAge());
                context.startActivity(intent);

            }
        });


        holder.imageView_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, SignalRMessagesActivity.class);
                intent.putExtra("connectionId", "0");
                intent.putExtra("toUserId", list.get(position).getUserId());
                intent.putExtra("toUserName", list.get(position).getUserName());
                intent.putExtra("toUserProfilePic", list.get(position).getProfilePic());
                context.startActivity(intent);

            }
        });



        if (Integer.parseInt(list.get(position).getFavorites())>0)
        {

            holder.imageView_favorite.setChecked(true);
        }
        if (Integer.parseInt(list.get(position).getInterested())>0)
        {
            holder.imageView_interests.setChecked(true);
        }
        if (Integer.parseInt(list.get(position).getRejected())>0)
        {
            holder.relativeLayout.setVisibility(View.GONE);
        }






        holder.imageView_interests.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState)
                {
                    if (Integer.parseInt(list.get(position).getInterested())==0) {
                        AsyncTaskLoad runner = new AsyncTaskLoad();
                        runner.execute(item.getUserId(), String.valueOf(UsersConnectionModel.INTERESTED), "0");
                    }
                }
                else
                {
                    AsyncTaskLoad runner = new AsyncTaskLoad();
                    runner.execute(item.getUserId(),"0", String.valueOf(UsersConnectionModel.INTERESTED));
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                holder.imageView_doubleTapFav.setVisibility(View.GONE);

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
                holder.imageView_doubleTapFav.setVisibility(View.VISIBLE);


                holder.imageView_doubleTapFav.setActiveImage(R.drawable.red_heart);
                holder.imageView_doubleTapFav.setInactiveImage(R.drawable.red_heart);
                holder.imageView_doubleTapFav.setColors(ContextCompat.getColor(context, R.color.red_heart),ContextCompat.getColor(context, R.color.yellow_favorite));
                holder.imageView_doubleTapFav.playAnimation();

            }
        });
holder.imageView_favorite.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if(buttonState)
                {
                    if (Integer.parseInt(list.get(position).getFavorites())==0) {
                        AsyncTaskLoad runner = new AsyncTaskLoad();
                        runner.execute(item.getUserId(), String.valueOf(UsersConnectionModel.FAVORITE), "0");
                    }
                }
                else
                {
                    AsyncTaskLoad runner = new AsyncTaskLoad();
                    runner.execute(item.getUserId(),"0",String.valueOf(UsersConnectionModel.FAVORITE));
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                holder.imageView_doubleTapFav.setVisibility(View.GONE);

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {
                holder.imageView_doubleTapFav.setVisibility(View.VISIBLE);


                holder.imageView_doubleTapFav.setActiveImage(R.drawable.starfilled);
                holder.imageView_doubleTapFav.setInactiveImage(R.drawable.starfilled);
                holder.imageView_doubleTapFav.setColors(ContextCompat.getColor(context, R.color.yellow_favorite),ContextCompat.getColor(context, R.color.green_dark));
                holder.imageView_doubleTapFav.playAnimation();

            }
        });


    }


    class AsyncTaskLoad extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String[] params) {

            insertDetails(params[0],params[1],params[2]);

            return params[0];
        }
    }

    private void insertDetails(final String toUserId, final String connectionType, final String removeConnectionType)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_USER_CONNECTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("message").equals("Success") &&
                                    !jsonObject.getBoolean("error"))
                            {
                                //getDetails();

                                //Toast.makeText(context,"REQUEST SENT!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                //Toast.makeText(context,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("ConnectChoiceId","0");
                params.put("FromUserId",userModel.getUserId());
                params.put("ToUserId",toUserId);
                params.put("ConntectionTypeId", connectionType);
                params.put("RemoveFromConntectionTypeId", removeConnectionType);
                params.put("FullName", userModel.getFullName());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);





    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_userId;
        public TextView textView_userName;
        public TextView textView_userQualification;
        public TextView textView_userOccupation;
        public TextView textView_userReligion;
        public TextView textView_userMaritalStatus;
        public TextView textView_userCity;
        public TextView textView_userBio;
        public TextView textView_userAge;
        public TextView textView_userHeight;
        public TextView textView_favorites;
        public TextView textView_interests;
        public ImageView imageView_profilePic;
        public SparkButton imageView_interests;
        public SparkButton imageView_favorite;
        public SparkButton imageView_doubleTapFav;
        public SparkButton imageView_reject;
        public SparkButton imageView_messages;
        RelativeLayout  linearLayout_favorites,  linearLayout_interests;
        RelativeLayout linearLayout_cancel, linearLayout_message;
        public de.hdodenhof.circleimageview.CircleImageView circleImage_profilePic;
        public RelativeLayout relativeLayout;

        ProgressBar progressBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
            this.textView_userId = itemView.findViewById(R.id.textView_userId);
            this.textView_userName = itemView.findViewById(R.id.textView_userName);
            this.textView_userAge = itemView.findViewById(R.id.textView_userAge);
            this.textView_userHeight = itemView.findViewById(R.id.textView_userHeight);
            this.circleImage_profilePic = itemView.findViewById(R.id.circleImage_profilePic);
            this.textView_userQualification = itemView.findViewById(R.id.textView_userQualification);
            this.textView_userReligion = itemView.findViewById(R.id.textView_userReligion);
            this.textView_userMaritalStatus = itemView.findViewById(R.id.textView_userMaritalStatus);
            this.textView_userCity = itemView.findViewById(R.id.textView_userCity);
            this.textView_userBio = itemView.findViewById(R.id.textView_userBio);
            this.imageView_profilePic = itemView.findViewById(R.id.imageView_profilePic);
            this.imageView_reject = itemView.findViewById(R.id.imageView_reject);
           // this.imageView_like = itemView.findViewById(R.id.imageView_like);
            this.imageView_favorite = itemView.findViewById(R.id.imageView_favorite);
            this.imageView_doubleTapFav = itemView.findViewById(R.id.imageView_doubleTapFav);
            this.linearLayout_cancel = itemView.findViewById(R.id.linearLayout_cancel);
            this.linearLayout_favorites = itemView.findViewById(R.id.linearLayout_favorites);
            this.linearLayout_message = itemView.findViewById(R.id.linearLayout_message);
            this.imageView_messages = itemView.findViewById(R.id.imageView_message);
            this.linearLayout_interests = itemView.findViewById(R.id.linearLayout_interests);
            this.imageView_interests = itemView.findViewById(R.id.spark_button);
            this.textView_interests = itemView.findViewById(R.id.textView_interests);
            this.textView_favorites = itemView.findViewById(R.id.textView_favorites);
            this.progressBar=itemView.findViewById(R.id.progressBar);


            setIsRecyclable(false);

        }
    }


}
