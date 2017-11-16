package com.group08.mysocialapp;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group08.mysocialapp.Models.User;

/**
 * Created by vaijy on 2017-11-16.
 */

public class FireBaseManager {

    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    static boolean googleAcctExist(GoogleSignInAccount acct) {


        return false;
    }

    static boolean validLogin(String username, String password) {


        return false;
    }

    static void createAccount(GoogleSignInAccount acct) {
        User user = new User();
        user.setFirstName(acct.getGivenName());
        user.setLastName(acct.getFamilyName());
        user.setEmail(acct.getEmail());

        createUser(user);
    }

    static void createUser(User user) {
        user.set_id(mDatabase.child("users").push().getKey());
        mDatabase.child("users").child(user.get_id()).setValue(user);
    }
}
