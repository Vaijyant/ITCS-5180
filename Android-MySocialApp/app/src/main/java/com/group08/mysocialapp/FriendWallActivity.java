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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group08.mysocialapp.Models.Post;
import com.group08.mysocialapp.Models.User;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FriendWallActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //GUI Components
    TextView lblFirstName;
    ImageButton imgBtnHome;
    TextView lblPost;
    RecyclerView rvPosts_fwa;


    //Other Stuff
    String TAG = "VT";

    GoogleApiClient mGoogleApiClient;

    //Recycler View
    LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Post, FriendPostViewHolder> mFirebaseAdapter;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String mUsername;

    User friend;

    // Holder Class ================================================================================
    // =============================================================================================
    public static class FriendPostViewHolder extends RecyclerView.ViewHolder {
        TextView lblPostUserFirstName;
        TextView lblPostTime;
        TextView lblPostMessage;
        ImageButton imgBtnDelete;

        public FriendPostViewHolder(View v) {
            super(v);
            lblPostUserFirstName = (TextView) itemView.findViewById(R.id.lblPostUserFirstName);
            lblPostTime = (TextView) itemView.findViewById(R.id.lblPostTime);
            lblPostMessage = (TextView) itemView.findViewById(R.id.lblPostMessage);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_wall);

        Intent intent = getIntent();
        friend = (User) intent.getSerializableExtra("friend");

        //Initialize GUI
        lblFirstName = findViewById(R.id.lblFirstName);
        imgBtnHome = findViewById(R.id.imgBtnHome_fwa);
        lblPost = findViewById(R.id.lblPost_fwa);
        rvPosts_fwa = findViewById(R.id.rvPosts_fwa);

        lblFirstName.setText(friend.getFirstName());
        lblPost.setText(friend.getFirstName() + "\'s Posts");

        //Sign out
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        // Initialize RecyclerView.
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvPosts_fwa.setLayoutManager(mLinearLayoutManager);
        rvPosts_fwa.setHasFixedSize(false);


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
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Post, FriendPostViewHolder>(options) {
            @Override
            public FriendPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new FriendPostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final FriendPostViewHolder viewHolder, int position, final Post post) {
                Log.d(TAG, "onBindViewHolder: " + position);

                if(!post.getUserId().equals(friend.getId())){
                    viewHolder.itemView.setVisibility(View.GONE);
                    return;
                }

                viewHolder.lblPostUserFirstName.setText(post.getFirstName());
                viewHolder.lblPostTime.setText(getPrettyDate(post.getPostingTime()));
                viewHolder.lblPostMessage.setText(post.getPost());
            }

            public String getPrettyDate(String createdDate) {
                String prettyDate = "";

                //"Sun Nov 19 00:02:40 EST 2017"
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                Date date = null;
                PrettyTime p = new PrettyTime();
                try {
                    date = formatter.parse(createdDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                prettyDate = p.format(new Date(date.getTime()));

                return prettyDate;

            }
        };

        rvPosts_fwa.setAdapter(mFirebaseAdapter);
        Log.d(TAG, "onCreate: rvPosts_fwa adapter set");
        //Adapter stuff ends =======================================================================

        //Attach Events
        imgBtnHome.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnHome_fwa:
                Intent intent = new Intent(this, HomeScreenActivity.class);
                startActivity(intent);
                break;
        }


    }

    //Menu code ====================================================================================

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //Menu End= ====================================================================================
}