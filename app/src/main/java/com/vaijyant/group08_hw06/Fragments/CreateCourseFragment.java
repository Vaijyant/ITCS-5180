package com.vaijyant.group08_hw06.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.vaijyant.group08_hw06.Adapter.InstructorHorizontalAdapter;
import com.vaijyant.group08_hw06.Models.Course;
import com.vaijyant.group08_hw06.Models.Instructor;
import com.vaijyant.group08_hw06.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class CreateCourseFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private CreateCourseFragment.OnFragmentInteractionListener mListener;

    EditText editTitle_cm;
    RecyclerView recyclerView;
    Spinner spinDay;
    EditText editTimeHrs;
    EditText editTimeMin;
    Spinner spinTime;
    Spinner spinSemester;
    RadioGroup rgCreditHrs;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    Button btnReset;
    Button btnCreate;
    Realm realm;
    InstructorHorizontalAdapter adapter;

    public CreateCourseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_course, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        editTitle_cm = (EditText) getView().findViewById(R.id.editTitle_cm);
        recyclerView = getView().findViewById(R.id.rvInstructorList_cm);


        spinDay = ((Spinner) getView().findViewById(R.id.spinDay));
        spinDay.setOnItemSelectedListener(this);
        List<String> days = new ArrayList<String>();
        days.add("Select");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        ArrayAdapter<String> daysDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, days);
        daysDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDay.setAdapter(daysDataAdapter);

        editTimeHrs = (EditText) getView().findViewById(R.id.editTimeHrs);
        editTimeMin = (EditText) getView().findViewById(R.id.editTimeMin);

        spinTime = ((Spinner) getView().findViewById(R.id.spinTime));
        spinDay.setOnItemSelectedListener(this);
        List<String> time = new ArrayList<String>();
        time.add("AM");
        time.add("PM");
        ArrayAdapter<String> timeDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, time);
        daysDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinTime.setAdapter(timeDataAdapter);

        rgCreditHrs = getView().findViewById(R.id.rgCreditHrs);
        rb1 = getView().findViewById(R.id.rb1);
        rb2 = getView().findViewById(R.id.rb2);
        rb3 = getView().findViewById(R.id.rb3);

        spinSemester = ((Spinner) getView().findViewById(R.id.spinSemester));
        spinSemester.setOnItemSelectedListener(this);
        List<String> semesters = new ArrayList<String>();
        semesters.add("Select Semester");
        semesters.add("Fall");
        semesters.add("Spring");
        semesters.add("Summer");
        ArrayAdapter<String> semestersDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, semesters);
        semestersDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinSemester.setAdapter(semestersDataAdapter);

        btnReset = getView().findViewById(R.id.btnReset);
        btnReset.setOnClickListener(this);
        btnCreate = getView().findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(this);

        // Recycler View handling
        realm = Realm.getDefaultInstance();
        ArrayList<Instructor> instructorList = (ArrayList<Instructor>) realm.copyFromRealm(realm.where(Instructor.class).findAll());
        initializeRecyclerView(instructorList);

        if(instructorList.size() > 0) {
            btnCreate.setEnabled(true);
            getView().findViewById(R.id.lblNoInstructors).setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateCourseFragment.OnFragmentInteractionListener) {
            mListener = (CreateCourseFragment.OnFragmentInteractionListener) context;
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.btnReset:
                resetCreateCourseFragment();
                break;

            case R.id.btnCreate:

                boolean hrsIsInt = editTimeHrs.getText().toString().matches("^[0-9]+$");
                boolean minIsInt = editTimeMin.getText().toString().matches("^[0-9]+$");
                int hrs = -1, min = -1;
                if (hrsIsInt)
                    hrs = Integer.parseInt(editTimeHrs.getText().toString());
                if (minIsInt)
                    min = Integer.parseInt(editTimeMin.getText().toString());

                // =================================================================================
                // Validations =====================================================================
                boolean valid = true;
                if (editTitle_cm.length() == 0) {
                    Toast.makeText(getActivity(), "Please specify a title.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if(adapter.getClickedInstructor() == null){
                    Toast.makeText(getActivity(), "Please specify an Instructor.", Toast.LENGTH_SHORT).show();
                    valid = false;

                }else if (spinDay.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please specify a day.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (hrs < 1 || hrs > 12) {
                    Toast.makeText(getActivity(), "Please specify hours in range 1 - 12.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (min < 0 || min > 60) {
                    Toast.makeText(getActivity(), "Please specify minutes in range 0 - 60.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (rgCreditHrs.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please select credit hours.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (spinSemester.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please specify a semester.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                //==================================================================================
                if (!valid) {
                    return;
                }

                Course course = new Course();
                course.setTitle(editTitle_cm.getText().toString());

                Instructor instructor = adapter.getClickedInstructor();
                String name = instructor.getFirstName() + " " + instructor.getLastName();
                course.setInstructor(name);

                course.setSchedDay(spinDay.getSelectedItem().toString());
                course.setSchedTime(hrs + ":" + min + " " + spinTime.getSelectedItem().toString());
                course.setCourseImage(instructor.getInstructorImage());

                mListener.addCourse(course);

                Toast.makeText(getActivity(), "New course \'" + course.getTitle() + "\' created.", Toast.LENGTH_SHORT).show();
                resetCreateCourseFragment();
                break;
        }

    }

    public void initializeRecyclerView(ArrayList<Instructor> list) {

        RecyclerView recList = getActivity().findViewById(R.id.rvInstructorList_cm);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new InstructorHorizontalAdapter(list);
        adapter.setActivity(getActivity());
        recList.setAdapter(adapter);
        recList.setLayoutManager(llm);


    }

    public void resetCreateCourseFragment(){
        editTitle_cm.setText("");
        spinDay.setSelection(0);
        editTimeHrs.setText("");
        editTimeMin.setText("");
        spinTime.setSelection(0);
        rgCreditHrs.clearCheck();
        spinSemester.setSelection(0);
        ArrayList<Instructor> instructorList = (ArrayList<Instructor>) realm.copyFromRealm(realm.where(Instructor.class).findAll());
        initializeRecyclerView(instructorList);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void addCourse(Course course);
    }
}
