package com.group08.mysocialapp;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group08.mysocialapp.Models.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    //GUI Components
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editDateOfBirth;
    EditText editPassword;
    EditText editConfirmPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editFirstName = (EditText) findViewById(R.id.editFirstName_sua);
        editLastName = (EditText) findViewById(R.id.editLastName_sua);
        editEmail = (EditText) findViewById(R.id.editEmail_sua);
        editDateOfBirth = (EditText) findViewById(R.id.editDateOfBirth_sua);
        editPassword = (EditText) findViewById(R.id.editPassword_sua);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword_sua);
        btnSignUp = (Button) findViewById(R.id.btnSignUp_sua);

        //Attaching events to GUI
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp_sua:
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String dateOfBirth = editDateOfBirth.getText().toString();
                String password = editPassword.getText().toString();
                String confirmPassword = editConfirmPassword.getText().toString();

                // >> Validations ==================================================================
                boolean valid = true;

                if (firstName.length() == 0) {
                    Toast.makeText(this, "Please provide first name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (lastName.length() == 0) {
                    Toast.makeText(this, "Please provide last name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (email.length() == 0) {
                    Toast.makeText(this, "Please provide email.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Please provide a valid email.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (dateOfBirth.length() == 0) {
                    Toast.makeText(this, "Please provide date of birth.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (password.length() < 6) {
                    Toast.makeText(this, "Minimum password length is 6.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(this, "Passwords do not match. Try again.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if(!valid)
                    return; // If anything is not valid return
                // >> End Validations ==============================================================

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setDateOfBirth(dateOfBirth);
                user.setPassword(password);

                FireBaseManager.createUser(user);

                Toast.makeText(this, "You are all set "+user.getFirstName()+"! Login to continue.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;

        }
    }
}
