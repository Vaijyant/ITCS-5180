package com.vaijyant.group08_hw06.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vaijyant.group08_hw06.MainActivity;
import com.vaijyant.group08_hw06.Models.User;
import com.vaijyant.group08_hw06.R;

import io.realm.Realm;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.btnLogin).setOnClickListener(this);
        getView().findViewById(R.id.lblSignUp).setOnClickListener(this);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragment.OnFragmentInteractionListener) {
            mListener = (LoginFragment.OnFragmentInteractionListener) context;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                String userName = ((EditText) getView().findViewById(R.id.editUserName)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.editPassword)).getText().toString();

                if(userName.length() == 0){
                    Toast.makeText(getActivity(), "Please enter username.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(password.length() == 0){
                    Toast.makeText(getActivity(), "Please enter password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Realm realm = Realm.getDefaultInstance();
                User user = realm.where(User.class).equalTo("userName", userName).findFirst();

                if (user == null || !password.equals(user.getPassword())) {
                    Toast.makeText(getActivity(), "Invalid username or password.", Toast.LENGTH_SHORT).show();
                    return;
                }
                MainActivity.LOGGED_IN = true;
                Toast.makeText(getActivity(), "Logged in as \'" + user.getUserName() + "\'.", Toast.LENGTH_SHORT).show();
                mListener.gotoCourseManager();
                break;
            case R.id.lblSignUp:
                mListener.gotoRegisterFragment();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void gotoCourseManager();
        void gotoRegisterFragment();
    }
}
