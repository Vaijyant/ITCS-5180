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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group08.mysocialapp.Models.Post;
import com.group08.mysocialapp.Models.User;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    TextView lblFirstName;
    ImageButton imgBtnFriendsList;
    ImageButton imgBtnPost;
    EditText editPost;
    RecyclerView rvPosts_hsa;
    LinearLayoutManager mLinearLayoutManager;


    GoogleApiClient mGoogleApiClient;

    String TAG = "VT";

    // Firebase instance variables
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mFirebaseAdapter;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;


    private String mUsername;

    // Holder Class ================================================================================
    // =============================================================================================
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView lblPostUserFirstName;
        TextView lblPostTime;
        TextView lblPostMessage;

        public PostViewHolder(View v) {
            super(v);
            lblPostUserFirstName = (TextView) itemView.findViewById(R.id.lblPostUserFirstName);
            lblPostTime = (TextView) itemView.findViewById(R.id.lblPostTime);
            lblPostMessage = (TextView) itemView.findViewById(R.id.lblPostMessage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsername = mFirebaseUser.getDisplayName();

        Toast.makeText(this, "Signed in as " + mFirebaseUser.getEmail() + ".", Toast.LENGTH_SHORT).show();

        //Initialize GUI Component
        lblFirstName = (TextView) findViewById(R.id.lblFirstName);
        imgBtnFriendsList = (ImageButton) findViewById(R.id.imgBtnFriendsList);
        editPost = (EditText) findViewById(R.id.editPost);
        imgBtnPost = (ImageButton) findViewById(R.id.imgBtnPost);


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange: " + user.toString());

                    if (userSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        lblFirstName.setText(user.getFirstName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.toString());
            }
        });

        //Sign out
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        Log.d(TAG, "before recycler view: ");

        // Initialize RecyclerView.
        rvPosts_hsa = (RecyclerView) findViewById(R.id.rvPosts_hsa);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        rvPosts_hsa.setLayoutManager(mLinearLayoutManager);


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
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final PostViewHolder viewHolder, int position, Post post) {
                Log.d(TAG, "onBindViewHolder: " + position);
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

        /*mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvPosts_hsa.scrollToPosition(positionStart);
                }
            }
        });*/

        rvPosts_hsa.setAdapter(mFirebaseAdapter);

        //Adapter stuff ends =======================================================================


        //Attach Listeners
        imgBtnFriendsList.setOnClickListener(this);
        imgBtnPost.setOnClickListener(this);
        editPost.setOnClickListener(this);

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
            case R.id.imgBtnFriendsList:
                Intent intent = new Intent(this, ManageFriendsActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.imgBtnPost:
                String postText = editPost.getText().toString().trim();
                if (postText.length() == 0) {
                    Toast.makeText(this, "Please write a message to post.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Post post = new Post();
                post.setFirstName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().split(" ")[0]);
                post.setPostingTime((new Date()).toString());
                post.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                post.setPost(postText);
                editPost.setText("");

                FireBaseManager.publishPost(post);
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.actionBtn).setIcon(R.drawable.logout);
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

    void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

}
