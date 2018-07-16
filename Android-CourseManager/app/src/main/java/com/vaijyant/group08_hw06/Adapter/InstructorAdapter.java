package com.vaijyant.group08_hw06.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaijyant.group08_hw06.Models.Instructor;
import com.vaijyant.group08_hw06.R;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vaijy on 2017-11-05.
 */

public class InstructorAdapter extends RecyclerView.Adapter<InstructorAdapter.InstructorViewHolder> {
    private ArrayList<Instructor> instructorList;
    private Activity activity;

    public ArrayList<Instructor> getInstructorList() {
        return instructorList;
    }

    public void setInstructorList(ArrayList<Instructor> instructorList) {
        this.instructorList = instructorList;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public InstructorAdapter(ArrayList<Instructor> instructorList) {
        this.instructorList = instructorList;
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    @Override
    public void onBindViewHolder(InstructorViewHolder viewHolder, final int i) {
        final Instructor instructor = instructorList.get(i);
        viewHolder.tvName.setText(instructor.getFirstName() + " " + instructor.getLastName());
        viewHolder.tvEmail.setText(instructor.getEmail());

        if(instructor.getInstructorImage() != null) {
            byte[] outImage = instructor.getInstructorImage();
            ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
            Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);

            ImageView image = viewHolder.imgViewList;
            image.setImageBitmap(bitmapImage);
        }

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Instructor> result = realm.where(Instructor.class).equalTo("email", instructor.getEmail()).findAll();
                        result.deleteAllFromRealm();
                    }
                });
                Toast.makeText(activity, "Instructor \'" + instructor.getFirstName() + " " + instructor.getLastName() + "\' deleted.", Toast.LENGTH_SHORT).show();
                instructorList.remove(i);
                notifyDataSetChanged();

                return false;
            }
        });


    }

    @Override
    public InstructorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);

        return new InstructorViewHolder(itemView);
    }

    public static class InstructorViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgViewList;
        protected TextView tvName;
        protected TextView tvEmail;

        public InstructorViewHolder(View v) {
            super(v);
            imgViewList = (ImageView) v.findViewById(R.id.imgViewList);
            tvName = (TextView) v.findViewById(R.id.textViewList1);
            tvEmail = (TextView) v.findViewById(R.id.textViewList2);
        }
    }
}