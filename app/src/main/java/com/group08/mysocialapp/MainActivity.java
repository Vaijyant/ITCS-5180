package com.group08.mysocialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import static com.group08.mysocialapp.FireBaseManager.createAccount;
import static com.group08.mysocialapp.FireBaseManager.validLogin;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String TAG = "VT";
    private static final int RC_SIGN_IN = 9001;

    //GUI Component
    EditText editUsername;
    EditText editPassword;
    Button btnLogin;
    SignInButton signInButton;
    TextView lblSignUp;

    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //Initialize GUI Component
        editUsername = (EditText) findViewById(R.id.editUsername);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        signInButton = (SignInButton) findViewById(R.id.signInButton);
        {
            TextView textView = (TextView) signInButton.getChildAt(0);
            textView.setText("Sign in with Google");
        }
        lblSignUp = (TextView) findViewById(R.id.lblSignUp);

        //Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Attach Listeners
        btnLogin.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        lblSignUp.setOnClickListener(this);

        checkSession();

    }

    void checkSession() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(MainActivity.this, "Signed in as " + user.getEmail() + ".", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();
                if (validLogin(username, password)) {
                    Intent intent = new Intent(this, HomeScreenActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid Login.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.signInButton:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.lblSignUp:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
        }
    }

    void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            if(!FireBaseManager.googleAcctExist(acct)){
                createAccount(acct);
            }
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);

            Toast.makeText(this, ""+acct.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
