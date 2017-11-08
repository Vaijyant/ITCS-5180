package com.vaijyant.group08_hw06;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.vaijyant.group08_hw06.Models.Instructor;
import java.util.ArrayList;

import io.realm.Realm;

public class InstructorListFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    public InstructorListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructor_list, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.imgBtnAddInstructor).setOnClickListener(this);

        Realm realm = Realm.getDefaultInstance();
        ArrayList<Instructor> instructorList = (ArrayList<Instructor>) realm.copyFromRealm(realm.where(Instructor.class).findAll());
        Toast.makeText(getActivity(), instructorList.size()+" instructor(s) available.", Toast.LENGTH_SHORT).show();

        initializeRecyclerView(instructorList);
    }

    public void initializeRecyclerView(ArrayList<Instructor> list) {

        RecyclerView recList = getActivity().findViewById(R.id.rvInstructorList_il);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        InstructorAdapter adapter = new InstructorAdapter(list);
        adapter.setActivity(getActivity());
        recList.setAdapter(adapter);
        recList.setLayoutManager(llm);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgBtnAddInstructor:
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddInstructorFragment(), "add_instructor")
                        .commit();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
