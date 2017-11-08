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

import com.vaijyant.group08_hw06.Models.Course;
import com.vaijyant.group08_hw06.R;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vaijy on 2017-11-05.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private ArrayList<Course> courseList;
    private Activity activity;

    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public CourseAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    @Override
    public void onBindViewHolder(CourseViewHolder viewHolder, final int i) {
        final Course course = courseList.get(i);
        viewHolder.tvTitle.setText(course.getTitle());
        viewHolder.tvInstructor.setText(course.getInstructor());

        String schedule = course.getSchedDay().substring(0, 3) + " " + course.getSchedTime();
        viewHolder.tvSchedule.setText(schedule);

        if (course.getCourseImage() != null) {
            byte[] outImage = course.getCourseImage();
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
                        RealmResults<Course> result = realm.where(Course.class).equalTo("title", course.getTitle()).findAll();
                        result.deleteAllFromRealm();
                    }
                });
                Toast.makeText(activity, "Course \'" + course.getTitle() + "\' deleted.", Toast.LENGTH_SHORT).show();
                courseList.remove(i);
                notifyDataSetChanged();

                return false;
            }
        });


    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);

        return new CourseViewHolder(itemView);
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imgViewList;
        protected TextView tvTitle;
        protected TextView tvInstructor;
        protected TextView tvSchedule;

        public CourseViewHolder(View v) {
            super(v);
            imgViewList = (ImageView) v.findViewById(R.id.imgViewList);
            tvTitle = (TextView) v.findViewById(R.id.textViewList1);
            tvInstructor = (TextView) v.findViewById(R.id.textViewList2);
            tvSchedule = (TextView) v.findViewById(R.id.textViewList3);
        }
    }
}