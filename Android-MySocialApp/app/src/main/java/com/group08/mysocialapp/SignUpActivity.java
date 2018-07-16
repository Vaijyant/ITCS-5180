package com.group08.mysocialapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.group08.mysocialapp.Models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "vt";

    //GUI Components
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editDateOfBirth;
    EditText editPassword;
    EditText editConfirmPassword;
    Button btnSignUp;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
                    editFirstName.setError("Please provide first name.");
                    valid = false;
                } else if (lastName.length() == 0) {
                    editLastName.setError("Please provide last name.");
                    valid = false;
                } else if (email.length() == 0) {
                    editEmail.setError("Please provide email.");
                    valid = false;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editEmail.setError("Please provide a valid email.");
                    valid = false;
                } else if (dateOfBirth.length() == 0) {
                    editDateOfBirth.setError("Please provide date of birth.");
                    valid = false;
                } else if (calculateAge(dateOfBirth) < 13) {
                    Toast.makeText(this, "Ineligible. You should be older than 13 years.", Toast.LENGTH_SHORT).show();
                    valid = false;
                } else if (!validPassword(password)) {
                    valid = false;
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(this, "Passwords do not match. Try again.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }

                if (!valid)
                    return; // If anything is not valid return, otherwise (if valid) continue
                // >> End Validations ==============================================================

                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setDateOfBirth(dateOfBirth);
                user.setPassword(password);

                createFirebaseUser(user);

                break;
        }
    }


    boolean validPassword(String password) {
        if (password.length() < 6) {
            editPassword.setError("Password should be more than 5 characters in length.");
            return false;
        }

        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            editPassword.setError("Password should contain at least one upper case alphabet.");
            return false;
        }

        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            editPassword.setError("Password should contain at least one lower case alphabet.");
            return false;
        }

        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            editPassword.setError("Password should contain at least one number.");
            return false;
        }

        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars)) {
            editPassword.setError("Password should contain at least one special character.");
            return false;
        }
        return true;
    }

    float calculateAge(String dateOfBirth) {
        int age = 0;
        try {
            Date date1 = new SimpleDateFormat("mm/dd/yyyy").parse(dateOfBirth);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);

            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            int month1 = now.get(Calendar.MONTH);
            int month2 = dob.get(Calendar.MONTH);
            if (month2 > month1) {
                age--;
            } else if (month1 == month2) {
                int day1 = now.get(Calendar.DAY_OF_MONTH);
                int day2 = dob.get(Calendar.DAY_OF_MONTH);
                if (day2 > day1) {
                    age--;
                }
            }
        } catch (ParseException e) {
            editDateOfBirth.setError("Please enter date in mm/dd/yyyy format");
            e.printStackTrace();
        }
        return age;
    }

    void createFirebaseUser(final User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser createdUser = FirebaseAuth.getInstance().getCurrentUser();

                    //Save user to database
                    User firebaseUser = new User();
                    firebaseUser.setId(createdUser.getUid());
                    firebaseUser.setFirstName(user.getFirstName());
                    firebaseUser.setLastName(user.getLastName());
                    firebaseUser.setEmail(user.getEmail());
                    firebaseUser.setDateOfBirth(user.getDateOfBirth());
                    firebaseUser.setPassword(user.getPassword());

                    // Saving in Application - Database
                    FireBaseManager.createUpdateApplicationUser(firebaseUser);


                    // Updating created user in Firebase Authentication
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getFirstName() + " " + user.getLastName())
                            .build();

                    createdUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated. id: " + createdUser.getUid());

                                        //Update GUI
                                        Toast.makeText(SignUpActivity.this, "You are all set " + createdUser.getDisplayName() + "! Login to continue.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        finish();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(intent);
                                    }
                                }
                            });


                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        editPassword.setError("Weak Password.");
                        editPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editPassword.setError("Weak Password.");
                        editPassword.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editEmail.setError("Email is already taken.");
                        editEmail.requestFocus();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}