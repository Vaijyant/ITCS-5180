package com.group08.mysocialapp.Models;

import java.io.Serializable;

/**
 * Created by vaijy on 2017-11-18.
 */

public class Post implements Serializable {
    String id;
    String userId;
    String firstName;
    String postingTime;
    String post;

    public Post() {
    }

    public Post(String userId, String firstName, String postingTime, String post) {
        this.userId = userId;
        this.firstName = firstName;
        this.postingTime = postingTime;
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPostingTime() {
        return postingTime;
    }

    public void setPostingTime(String postingTime) {
        this.postingTime = postingTime;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
