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

    public static void createUpdateApplicationUser(User user) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        String id = user.getId();
        user.setId(null);
        mDatabaseReference.child(id).setValue(user);

    }

    public static void publishPost(Post post) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        mDatabaseReference.push().setValue(post);

    }

    public static void friendRequest/*add friend*/(User sender, User receiver) {
        sender.addRequestTo(receiver.getId());
        receiver.addRequestFrom(sender.getId());

        createUpdateApplicationUser(sender); // update
        createUpdateApplicationUser(receiver); //update
    }


    public static void acceptFriendRequest(User currentUser, User friend) {

        currentUser.getRequestFrom().remove(friend.getId());
        currentUser.addFriends(friend.getId());

        friend.getRequestTo().remove(currentUser.getId());
        friend.addFriends(currentUser.getId());

        createUpdateApplicationUser(currentUser); // update
        createUpdateApplicationUser(friend); //update


    }

    public static void declineFriendRequest(User currentUser, User friend) {

        currentUser.getRequestFrom().remove(friend.getId());
        friend.getRequestTo().remove(currentUser.getId());

        createUpdateApplicationUser(currentUser); // update
        createUpdateApplicationUser(friend); //update

    }

    public static void deleteFriendRequest(User currentUser, User friend) {

        currentUser.getRequestTo().remove(friend.getId());
        friend.getRequestFrom().remove(currentUser.getId());

        createUpdateApplicationUser(currentUser); // update
        createUpdateApplicationUser(friend); //update

    }

    public static void removeFriend(User currentUser, User friend) {

        currentUser.getFriends().remove(friend.getId());
        friend.getFriends().remove(currentUser.getId());

        createUpdateApplicationUser(currentUser); // update
        createUpdateApplicationUser(friend); //update

    }

    public static void deletePost(Post post) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        String id = post.getId();
        mDatabaseReference.child(id).setValue(null);
    }
}
