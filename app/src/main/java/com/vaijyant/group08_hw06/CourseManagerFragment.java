package com.vaijyant.group08_hw06;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vaijyant.group08_hw06.Models.Course;
import com.vaijyant.group08_hw06.Models.Instructor;

import java.util.ArrayList;

import io.realm.Realm;

public class CourseManagerFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;

    public CourseManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_manager, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CourseManagerFragment.OnFragmentInteractionListener) {
            mListener = (CourseManagerFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.imgBtnAddCourse).setOnClickListener(this);

        Realm realm = Realm.getDefaultInstance();
        ArrayList<Course> courseList = (ArrayList<Course>) realm.copyFromRealm(realm.where(Course.class).findAll());
        Toast.makeText(getActivity(), courseList.size()+" course(s) available.", Toast.LENGTH_SHORT).show();
        initializeRecyclerView(courseList);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnAddCourse:
                mListener.gotoCreateCourseFragment();
                break;
        }
    }

    public void initializeRecyclerView(ArrayList<Course> list) {

        RecyclerView recList = getActivity().findViewById(R.id.rvCourseManager_cm);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        CourseAdapter adapter = new CourseAdapter(list);
        adapter.setActivity(getActivity());
        recList.setAdapter(adapter);
        recList.setLayoutManager(llm);


    }

    public interface OnFragmentInteractionListener {
        void gotoCreateCourseFragment();
    }
}
