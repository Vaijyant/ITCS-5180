package com.group08.mysocialapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group08.mysocialapp.Models.Post;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManageFriendsActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG ="VT" ;
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
    private FirebaseRecyclerAdapter<Post, TabFriendsViewHolder> mFirebaseAdapterTabFriends;
    private FirebaseRecyclerAdapter<Post, TabAddNewFriendsViewHolder> mFirebaseAdapterTabAddNewFriends;
    private FirebaseRecyclerAdapter<Post, TabRequestsPending> mFirebaseAdapterTabRequestsPending;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private String mUsername;
    private FirebaseUser mFirebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Initialize GUI components
        imgBtnHome_mfa = findViewById(R.id.imgBtnHome_mfa);
        rvTabFriends = findViewById(R.id.rvTabFriends);
        rvTabAddNewFriends = findViewById(R.id.rvTabAddNewFriends);
        rvTabRequestsPending = findViewById(R.id.rvTabRequestsPending);

        //initialize tabs
        mTabHost = (TabHost)findViewById(R.id.tabHost);
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
        switch (view.getId()){
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
    void initializeTab1(){
        String heading = "Friends";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabFriends);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        // Initialize RecyclerView.
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvTabFriends.setLayoutManager(mLinearLayoutManager);


        // Parser ==================================================================================
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "onCreate: " + mFirebaseDatabaseReference);
        SnapshotParser<Post> parser = new SnapshotParser<Post>() {
            @Override
            public Post parseSnapshot(DataSnapshot dataSnapshot) {
                Log.d(TAG, "parseSnapshot: " + dataSnapshot);
                Post post = dataSnapshot.getValue(Post.class);
                return post;
            }
        };

        // Adapter Class ===========================================================================
        DatabaseReference postsRef = mFirebaseDatabaseReference.child("posts");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsRef, parser)
                .build();


        // class Adapter begins
        mFirebaseAdapterTabFriends = new FirebaseRecyclerAdapter<Post, TabFriendsViewHolder>(options) {
            @Override
            public TabFriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new TabFriendsViewHolder(inflater.inflate(R.layout.tab_friends_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final TabFriendsViewHolder viewHolder, int position, Post post) {
                Log.d(TAG, "onBindViewHolder: " + position);
                viewHolder.lblFriendName_tab1.setText("");
                viewHolder.imgBtnRemoveFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewHolder.lblFriendName_tab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        rvTabFriends.setAdapter(mFirebaseAdapterTabFriends);

    }

    void initializeTab2(){
        String heading = "Add New Friends";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabAddNewFriends);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        // Initialize RecyclerView.
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvTabAddNewFriends.setLayoutManager(mLinearLayoutManager);


        // Parser ==================================================================================
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "onCreate: " + mFirebaseDatabaseReference);
        SnapshotParser<Post> parser = new SnapshotParser<Post>() {
            @Override
            public Post parseSnapshot(DataSnapshot dataSnapshot) {
                Log.d(TAG, "parseSnapshot: " + dataSnapshot);
                Post post = dataSnapshot.getValue(Post.class);
                return post;
            }
        };

        // Adapter Class ===========================================================================
        DatabaseReference postsRef = mFirebaseDatabaseReference.child("posts");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsRef, parser)
                .build();


        // class Adapter begins
        mFirebaseAdapterTabAddNewFriends = new FirebaseRecyclerAdapter<Post, TabAddNewFriendsViewHolder>(options) {
            @Override
            public TabAddNewFriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new TabAddNewFriendsViewHolder(inflater.inflate(R.layout.tab_add_new_friends_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final TabAddNewFriendsViewHolder viewHolder, int position, Post post) {
                Log.d(TAG, "onBindViewHolder: " + position);
                viewHolder.lblFriendName_tab2.setText("");
                viewHolder.imgBtnRemoveFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        rvTabAddNewFriends.setAdapter(mFirebaseAdapterTabAddNewFriends);

    }

    void initializeTab3(){
        String heading = "Requests Pending";
        TabHost.TabSpec mSpec = mTabHost.newTabSpec(heading);
        mSpec.setContent(R.id.tabRequestsPending);
        mSpec.setIndicator(heading);
        mTabHost.addTab(mSpec);

        // Initialize RecyclerView.
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvTabRequestsPending.setLayoutManager(mLinearLayoutManager);


        // Parser ==================================================================================
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, "onCreate: " + mFirebaseDatabaseReference);
        SnapshotParser<Post> parser = new SnapshotParser<Post>() {
            @Override
            public Post parseSnapshot(DataSnapshot dataSnapshot) {
                Log.d(TAG+"X", "parseSnapshot: " + dataSnapshot);
                Post post = dataSnapshot.getValue(Post.class);
                return post;
            }
        };

        // Adapter Class ===========================================================================
        DatabaseReference postsRef = mFirebaseDatabaseReference.child("posts");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsRef, parser)
                .build();


        // class Adapter begins
        mFirebaseAdapterTabRequestsPending = new FirebaseRecyclerAdapter<Post, TabRequestsPending>(options) {
            @Override
            public TabRequestsPending onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new TabRequestsPending(inflater.inflate(R.layout.tab_request_pending_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final TabRequestsPending viewHolder, int position, Post post) {
                Log.d(TAG, "onBindViewHolder: " + position);
                viewHolder.lblFriendName_tab3.setText("");
                viewHolder.imgBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                viewHolder.imgBtnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        rvTabRequestsPending.setAdapter(mFirebaseAdapterTabRequestsPending);

    }

    @Override
    public void onPause() {
        mFirebaseAdapterTabFriends.stopListening();
        mFirebaseAdapterTabAddNewFriends.stopListening();
        mFirebaseAdapterTabRequestsPending.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapterTabFriends.startListening();
        mFirebaseAdapterTabAddNewFriends.startListening();
        mFirebaseAdapterTabRequestsPending.startListening();
    }

    // View Holders ================================================================================
    // Holder Class >>> Tab 1 ======================================================================
    // =============================================================================================
    public static class TabFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab1;
        ImageButton imgBtnRemoveFriend;

        public TabFriendsViewHolder(View v) {
            super(v);
            lblFriendName_tab1 = (TextView) itemView.findViewById(R.id.lblFriendName_tab1);
            imgBtnRemoveFriend = (ImageButton) itemView.findViewById(R.id.imgBtnRemoveFriend);
        }
    }

    // Holder Class >>> Tab 2 ======================================================================
    // =============================================================================================
    public static class TabAddNewFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab2;
        ImageButton imgBtnRemoveFriend;

        public TabAddNewFriendsViewHolder(View v) {
            super(v);
            lblFriendName_tab2 = (TextView) itemView.findViewById(R.id.lblFriendName_tab2);
            imgBtnRemoveFriend = (ImageButton) itemView.findViewById(R.id.imgBtnAddFriend);
        }
    }

    // Holder Class >>> Tab 3 ======================================================================
    // =============================================================================================
    public static class TabRequestsPending extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab3;
        ImageButton imgBtnAccept;
        ImageButton imgBtnDecline;

        public TabRequestsPending(View v) {
            super(v);
            lblFriendName_tab3 = (TextView) itemView.findViewById(R.id.lblFriendName_tab3);
            imgBtnAccept = (ImageButton) itemView.findViewById(R.id.imgBtnAccept);
            imgBtnDecline = (ImageButton) itemView.findViewById(R.id.imgBtnDecline);
        }
    }
}
