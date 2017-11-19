package com.group08.mysocialapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group08.mysocialapp.Models.Post;
import com.group08.mysocialapp.Models.User;

/**
 * Created by vaijy on 2017-11-16.
 */

public class FireBaseManager {

    static DatabaseReference mDatabaseReference;

    static String TAG = "VT";

    public static void createApplicationUser(User user) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        String id = user.getId();
        user.setId("");
        mDatabaseReference.child(id).setValue(user);

    }

    public static void publishPost(Post post) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        mDatabaseReference.push().setValue(post);

    }
}
