package com.group08.mysocialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group08.mysocialapp.Models.Post;
import com.group08.mysocialapp.Models.User;

import java.util.ArrayList;
import java.util.List;

import com.group08.mysocialapp.Adapters.*;

public class ManageFriendsActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "VT";
    // GUI components
    ImageButton imgBtnHome_mfa;
    TabHost mTabHost;
    RecyclerView rvTabFriends;
    RecyclerView rvTabAddNewFriends;
    RecyclerView rvTabRequestsPending;

    // GUI support
    LinearLayoutManager mLinearLayoutManager;

    // Firebase
    DatabaseReference mFirebaseDatabaseReference;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private String mUsername;
    private FirebaseUser mFirebaseUser;


    // Adapters
    TabFriendsAdapter tabFriendsAdapter;
    TabAddNewFriendsAdapter tabAddNewFriendsAdapter;
    TabRequestsPendingAdapter tabRequestsPendingAdapter;

    // adapter lists
    static ArrayList<User> userList1;
    static ArrayList<User> userList2;
    static ArrayList<User> userList3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Initialize GUI components
        imgBtnHome_mfa = findViewById(R.id.imgBtnHome_mfa);
        rvTabFriends = findViewById(R.id.rvTabFriends);


        //initialize tabs
        mTabHost = (TabHost) findViewById(R.id.tabHost);
        mTabHost.setup();
        initializeTab1();
        initializeTab2();
        initializeTab3();


        // Attach Listiners to GUI Components
        imgBtnHome_mfa.setOnClickListener(this);

        //Sign out
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionBtn:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = "anonymous";
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnHome_mfa:
                Intent intent = new Intent(this, HomeScreenActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    // Initializing tabs ===========================================================================
    void initializeTab1() {
        String heading = "Friends";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabFriends);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        userList1 = new ArrayList<>();
        // Initialize RecyclerView.
        rvTabFriends = findViewById(R.id.rvTabFriends);
        rvTabFriends.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tabFriendsAdapter = new TabFriendsAdapter(userList1);
        tabFriendsAdapter.setActivity(this);
       /* mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);*/
        rvTabFriends.setAdapter(tabFriendsAdapter);
        rvTabFriends.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = HomeScreenActivity.currentUser;

                List<String> friends = new ArrayList<String>();
                if (currentUser.getFriends() != null) {
                    friends.addAll(currentUser.getFriends());
                }

                userList1.clear();
                tabFriendsAdapter.notifyDataSetChanged();


                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User snapUser = userSnapshot.getValue(User.class);
                    snapUser.setId(userSnapshot.getKey());

                    String id;
                    id = userSnapshot.getKey();

                    if (friends.contains(snapUser.getId())) {
                        userList1.add(snapUser);
                        tabFriendsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void initializeTab2() {

        String heading = "Add New Friends";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabAddNewFriends);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        userList2 = new ArrayList<>();
        // Initialize RecyclerView.
        rvTabAddNewFriends = findViewById(R.id.rvTabAddNewFriends);
        rvTabAddNewFriends.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tabAddNewFriendsAdapter = new TabAddNewFriendsAdapter(userList2);
        tabAddNewFriendsAdapter.setActivity(this);
       /* mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);*/
        rvTabAddNewFriends.setAdapter(tabAddNewFriendsAdapter);
        rvTabAddNewFriends.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = HomeScreenActivity.currentUser;

                List<String> friends = new ArrayList<String>();
                if (currentUser.getFriends() != null) {
                    friends.addAll(currentUser.getFriends());
                }

                userList2.clear();
                tabAddNewFriendsAdapter.notifyDataSetChanged();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User snapUser = userSnapshot.getValue(User.class);
                    snapUser.setId(userSnapshot.getKey());

                    if (!currentUser.getId().equals(snapUser.getId())
                            && !friends.contains(snapUser.getId())) {
                        userList2.add(snapUser);
                        tabAddNewFriendsAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void initializeTab3() {
        String heading = "Requests Pending";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabRequestsPending);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        userList3 = new ArrayList<>();
        // Initialize RecyclerView.
        rvTabRequestsPending = findViewById(R.id.rvTabRequestsPending);
        rvTabRequestsPending.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        //mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        tabRequestsPendingAdapter = new TabRequestsPendingAdapter(userList3);
        tabRequestsPendingAdapter.setActivity(this);
       /* mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);*/
        rvTabRequestsPending.setAdapter(tabRequestsPendingAdapter);
        rvTabRequestsPending.setLayoutManager(mLinearLayoutManager);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = HomeScreenActivity.currentUser;

                List<String> requestFrom = new ArrayList<String>();
                if (currentUser.getRequestFrom() != null) {
                    requestFrom.addAll(currentUser.getRequestFrom());
                }
                List<String> requestTo = new ArrayList<String>();
                if (currentUser.getRequestTo() != null) {
                    requestTo.addAll(currentUser.getRequestTo());
                }

                userList3.clear();
                tabRequestsPendingAdapter.notifyDataSetChanged();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User snapUser = userSnapshot.getValue(User.class);
                    snapUser.setId(userSnapshot.getKey());

                    if ((!currentUser.getId().equals(snapUser.getId())) && (requestFrom.contains(snapUser.getId())
                            || requestTo.contains(snapUser.getId()))) {
                        if (requestFrom.contains(snapUser.getId())) {
                            User senderUser = new User(snapUser);
                            senderUser.setMarker("sender");
                            userList3.add(senderUser);
                            tabRequestsPendingAdapter.notifyDataSetChanged();
                        }
                        if (requestTo.contains(snapUser.getId())) {
                            User receiverUser = new User(snapUser);
                            receiverUser.setMarker("receiver");
                            userList3.add(receiverUser);
                            tabRequestsPendingAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
