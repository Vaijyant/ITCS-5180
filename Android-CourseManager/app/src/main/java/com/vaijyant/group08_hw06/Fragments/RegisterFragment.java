package com.vaijyant.group08_hw06.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vaijyant.group08_hw06.Models.User;
import com.vaijyant.group08_hw06.R;

import java.io.ByteArrayOutputStream;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        getView().findViewById(R.id.imgBtnRegister).setOnClickListener(this);
        getView().findViewById(R.id.btnRegister).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragment.OnFragmentInteractionListener) {
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnRegister:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.btnRegister:
                String firstName = ((EditText) getView().findViewById(R.id.editRegFirstName)).getText().toString();
                String lastName = ((EditText) getView().findViewById(R.id.editRegLastName)).getText().toString();
                String userName = ((EditText) getView().findViewById(R.id.editRegUserName)).getText().toString();
                String password = ((EditText) getView().findViewById(R.id.editRegPassword)).getText().toString();


                // Validations =====================================================================

                boolean valid = true;
                if(firstName.length() == 0){
                    Toast.makeText(getActivity(), "Please specify first name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                else if(lastName.length() == 0){
                    Toast.makeText(getActivity(), "Please specify last name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                else if(userName.length() == 0){
                    Toast.makeText(getActivity(), "Please specify username.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                else if(password.length() < 8){
                    if(password.length() == 0 )
                        Toast.makeText(getActivity(), "Please specify password.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Minimum password length is 8.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
                else if(photo == null){
                    Toast.makeText(getActivity(), "Please specify profile picture.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if(!valid){
                    return;
                }
                // =================================================================================

                byte[] userImage = null;
                if (photo != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                }

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUserName(userName);
                user.setPassword(password);
                user.setUserImage(userImage);

                mListener.register(user);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photo = null;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ((ImageButton) getView().findViewById(R.id.imgBtnRegister)).setImageBitmap(photo);
        }
    }

    /***********************************************************************************************
     * Interfaces
     */
    public interface OnFragmentInteractionListener {
        void register(User user);
    }
}
