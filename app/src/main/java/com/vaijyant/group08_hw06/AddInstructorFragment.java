package com.vaijyant.group08_hw06;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.vaijyant.group08_hw06.Models.Instructor;

import java.io.ByteArrayOutputStream;


public class AddInstructorFragment extends Fragment implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo;

    private OnFragmentInteractionListener mListener;

    public AddInstructorFragment() {
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
        return inflater.inflate(R.layout.fragment_add_instructor, container, false);
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
    public void onStart() {
        super.onStart();
        getView().findViewById(R.id.btnRegister_ai).setOnClickListener(this);
        getView().findViewById(R.id.imgBtnRegister_ai).setOnClickListener(this);
        getView().findViewById(R.id.btnReset_ai).setOnClickListener(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister_ai:
                String firstName = ((EditText)getView().findViewById(R.id.editRegFirstName_ai)).getText().toString();
                String lastName = ((EditText)getView().findViewById(R.id.editRegLastName_ai)).getText().toString();
                String email = ((EditText)getView().findViewById(R.id.editEmail_ai)).getText().toString();
                String personalWebsite = ((EditText)getView().findViewById(R.id.editRegPersonalWebsite_ai)).getText().toString();

                byte[] userImage = null;
                if(photo != null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    userImage = stream.toByteArray();
                }

                Instructor instructor = new Instructor();
                instructor.setFirstName(firstName);
                instructor.setLastName(lastName);
                instructor.setEmail(email);
                instructor.setPersonalWebsite(personalWebsite);
                instructor.setInstructorImage(userImage);

                mListener.addInstructor(instructor);

                break;

            case R.id.btnReset_ai:
                ((EditText)getView().findViewById(R.id.editRegFirstName_ai)).setText("");
                ((EditText)getView().findViewById(R.id.editRegLastName_ai)).setText("");
                ((EditText)getView().findViewById(R.id.editEmail_ai)).setText("");
                ((EditText)getView().findViewById(R.id.editRegPersonalWebsite_ai)).setText("");
                ((ImageButton)getView().findViewById(R.id.imgBtnRegister_ai)).setImageResource(android.R.drawable.ic_menu_camera);
                photo = null;

                break;

            case R.id.imgBtnRegister_ai:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        photo = null;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            ((ImageButton)getView().findViewById(R.id.imgBtnRegister_ai)).setImageBitmap(photo);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void addInstructor(Instructor instructor);
    }
}
