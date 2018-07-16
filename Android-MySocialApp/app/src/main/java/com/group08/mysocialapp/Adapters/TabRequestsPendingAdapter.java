package com.group08.mysocialapp.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group08.mysocialapp.HomeScreenActivity;
import com.group08.mysocialapp.Models.User;
import com.group08.mysocialapp.R;

import java.util.ArrayList;

import static com.group08.mysocialapp.FireBaseManager.acceptFriendRequest;
import static com.group08.mysocialapp.FireBaseManager.declineFriendRequest;
import static com.group08.mysocialapp.FireBaseManager.deleteFriendRequest;

/**
 * Created by vaijy on 2017-11-19.
 */

public class TabRequestsPendingAdapter extends RecyclerView.Adapter<TabRequestsPendingAdapter.TabRequestsPendingViewHolder> {
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

    public TabRequestsPendingAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @Override
    public TabRequestsPendingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new TabRequestsPendingViewHolder(inflater.inflate(R.layout.tab_request_pending_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TabRequestsPendingViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        final User user = userList.get(position);
        viewHolder.lblFriendName_tab3.setText(user.getFirstName() + " " + user.getLastName());

        switch (user.getMarker()){
            case "sender":
                viewHolder.imgBtnDelete.setVisibility(View.GONE);
                break;
            case "receiver":
                viewHolder.imgBtnAccept.setVisibility(View.GONE);
                viewHolder.imgBtnDecline.setVisibility(View.GONE);
                break;
        }


        viewHolder.imgBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "You accepted friend request from "+user.getFirstName()+".", Toast.LENGTH_SHORT).show();
                acceptFriendRequest(HomeScreenActivity.currentUser, user);


            }
        });
        viewHolder.imgBtnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "You declined friend request from "+user.getFirstName()+".", Toast.LENGTH_SHORT).show();
                declineFriendRequest(HomeScreenActivity.currentUser, user);

            }
        });
        viewHolder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "You deleted friend request sent to "+user.getFirstName()+".", Toast.LENGTH_SHORT).show();
                deleteFriendRequest(HomeScreenActivity.currentUser, user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Holder Class >>> Tab 3 ======================================================================
    // =============================================================================================
    public static class TabRequestsPendingViewHolder extends RecyclerView.ViewHolder {
        TextView lblFriendName_tab3;
        ImageButton imgBtnAccept;
        ImageButton imgBtnDecline;
        ImageButton imgBtnDelete;

        public TabRequestsPendingViewHolder(View v) {
            super(v);

            setIsRecyclable(false);

            lblFriendName_tab3 = (TextView) itemView.findViewById(R.id.lblFriendName_tab3);
            imgBtnAccept = (ImageButton) itemView.findViewById(R.id.imgBtnAccept);
            imgBtnDecline = (ImageButton) itemView.findViewById(R.id.imgBtnDecline);
            imgBtnDelete = (ImageButton) itemView.findViewById(R.id.imgBtnDelete);
        }
    }
}
