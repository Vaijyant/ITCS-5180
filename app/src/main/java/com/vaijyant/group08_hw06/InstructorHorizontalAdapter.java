package com.vaijyant.group08_hw06;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.vaijyant.group08_hw06.Models.Instructor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by vaijy on 2017-11-05.
 */

public class InstructorHorizontalAdapter extends RecyclerView.Adapter<InstructorHorizontalAdapter.InstructorViewHolder> {
    private ArrayList<Instructor> instructorList;
    Activity activity;
    static int lastClickPosition = -1;
    Instructor clickedInstructor;

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

    public void setClickedInstructor(Instructor instructor){
        clickedInstructor = instructor;
    }
    public Instructor getClickedInstructor(){

        return clickedInstructor;
    }


    InstructorHorizontalAdapter(ArrayList<Instructor> instructorList) {
        this.instructorList = instructorList;
        lastClickPosition  = -1;
        clickedInstructor = null;
    }

    @Override
    public int getItemCount() {
        return instructorList.size();
    }

    @Override
    public void onBindViewHolder(final InstructorViewHolder viewHolder, int i) {
        viewHolder.radioButton.setChecked(false);
        if (i == lastClickPosition) {
            viewHolder.radioButton.setChecked(true);
        }

        final Instructor instructor = instructorList.get(i);
        viewHolder.lblInstructorName.setText(instructor.getFirstName() + " " + instructor.getLastName());

        byte[] outImage = instructor.getInstructorImage();
        ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
        Bitmap bitmapImage = BitmapFactory.decodeStream(imageStream);

        ImageView image = viewHolder.imageView;
        image.setImageBitmap(bitmapImage);




    }

    @Override
    public InstructorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.instructor_item_cc, parent, false);

        return new InstructorViewHolder(itemView);
    }

    public class InstructorViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView lblInstructorName;
        protected RadioButton radioButton;

        public InstructorViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            lblInstructorName = (TextView) v.findViewById(R.id.lblInstructorName);
            radioButton = (RadioButton) v.findViewById(R.id.radioButton);
            radioButton.setChecked(false);

            final View.OnClickListener l = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastClickPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, instructorList.size());
                    setClickedInstructor(instructorList.get(lastClickPosition));
                }
            };
            radioButton.setOnClickListener(l);

        }
    }
}