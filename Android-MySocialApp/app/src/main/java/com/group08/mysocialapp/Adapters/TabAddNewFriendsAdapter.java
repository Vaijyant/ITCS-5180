package com.group08.mysocialapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.group08.mysocialapp.FireBaseManager;
import com.group08.mysocialapp.HomeScreenActivity;
import com.group08.mysocialapp.Models.User;
import com.group08.mysocialapp.R;

import java.util.ArrayList;

public class TabAddNewFriendsAdapter extends RecyclerView.Adapter<TabAddNewFriendsAdapter.TabAddNewFriendsViewHolder> {
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

    public TabAddNewFriendsAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public void onBindViewHolder(TabAddNewFriendsViewHolder viewHolder, final int position) {
        final User user = userList.get(position);
        Log.d(TAG, "onBindViewHolder: " + position);
        viewHolder.lblFriendName_tab2.setText(user.getFirstName()+" "+user.getLastName());
        viewHolder.imgBtnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                User sender = HomeScreenActivity.currentUser;
                User reciever = user;

                FireBaseManager.friendRequest(sender, reciever);
            }
        });
    }

    @Override
    public TabAddNewFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.tab_add_new_friends_item, parent, false);

        return new TabAddNewFriendsViewHolder(itemView);
    }

    public static class TabAddNewFriendsViewHolder extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab2;
        ImageButton imgBtnAddFriend;

        public TabAddNewFriendsViewHolder(View v) {
            super(v);

            setIsRecyclable(false);
            lblFriendName_tab2 = (TextView) itemView.findViewById(R.id.lblFriendName_tab2);
            imgBtnAddFriend = (ImageButton) itemView.findViewById(R.id.imgBtnAddFriend);
        }
    }
}