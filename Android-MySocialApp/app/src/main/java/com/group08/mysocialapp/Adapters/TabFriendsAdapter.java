package com.group08.mysocialapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.group08.mysocialapp.FireBaseManager;
import com.group08.mysocialapp.FriendWallActivity;
import com.group08.mysocialapp.HomeScreenActivity;
import com.group08.mysocialapp.MainActivity;
import com.group08.mysocialapp.Models.User;
import com.group08.mysocialapp.R;

import java.util.ArrayList;

import static com.group08.mysocialapp.FireBaseManager.removeFriend;

/**
 * Created by vaijy on 2017-11-19.
 */

public class TabFriendsAdapter extends RecyclerView.Adapter<TabFriendsAdapter.TabFriendsViewHolder> {


    ArrayList<User> userList;
    Activity activity;
    String TAG = "VT";

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public TabFriendsAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(TabFriendsViewHolder viewHolder, final int position) {
        final User user = userList.get(position);
        Log.d(TAG, "onBindViewHolder: " + position);
        viewHolder.lblFriendName_tab1.setText(user.getFirstName() + " " + user.getLastName());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, FriendWallActivity.class);
                intent.putExtra("friend", user);
                activity.finish();
                activity.startActivity(intent);


            }
        });


        viewHolder.imgBtnRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFriend(HomeScreenActivity.currentUser, user);
            }
        });
    }

    @Override
    public TabFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.tab_friends_item, parent, false);

        return new TabFriendsViewHolder(itemView);
    }

    // View Holders ================================================================================
    // Holder Class >>> Tab 1 ======================================================================
    // =============================================================================================
    public static class TabFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab1;
        ImageButton imgBtnRemoveFriend;

        public TabFriendsViewHolder(View v) {
            super(v);

            setIsRecyclable(false);

            lblFriendName_tab1 = (TextView) itemView.findViewById(R.id.lblFriendName_tab1);
            imgBtnRemoveFriend = (ImageButton) itemView.findViewById(R.id.imgBtnRemoveFriend);
        }
    }
}
