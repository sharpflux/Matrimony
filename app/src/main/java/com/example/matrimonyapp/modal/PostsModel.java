package com.example.matrimonyapp.modal;

import android.net.Uri;

public class PostsModel {

    String userId, userName, likes, postTime, postId;
    Uri uri_profilePic, uri_postPic;

    public PostsModel(String userId, String userName, String likes, String postId, String postTime, Uri uri_profilePic, Uri uri_postPic) {
        this.userId = userId;
        this.userName = userName;
        this.likes = likes;
        this.postId = postId;
        this.postTime = postTime;
        this.uri_profilePic = uri_profilePic;
        this.uri_postPic = uri_postPic;

    }

    public PostsModel() {

    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public Uri getUri_profilePic() {
        return uri_profilePic;
    }

    public void setUri_profilePic(Uri uri_profilePic) {
        this.uri_profilePic = uri_profilePic;
    }


    public Uri getUri_postPic() {
        return uri_postPic;
    }

    public void setUri_postPic(Uri uri_postPic) {
        this.uri_postPic = uri_postPic;
    }
}
