/**
 * Assignment: Homework 02
 * FileName: CreateNewContact.java
 * Author: Vaijyant Tomar
 *
 * */


package example.com.hw2_groups08;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import java.util.Calendar;
import java.util.Date;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateNewContact extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int CAMERA_PIC_REQUEST = 1888;
    private ImageButton btnAddPhoto;
    private Bitmap image;
    private int mode;
    private EditText birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        btnAddPhoto = (ImageButton) findViewById(R.id.btnAddPhoto);
        birthday = (EditText) findViewById(R.id.editBirthday);
        btnAddPhoto.setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        birthday.setOnClickListener(this);
        birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Calendar rightNow = Calendar.getInstance();
                    Calendar minCal = Calendar.getInstance();
                    minCal.set(1850, Calendar.JANUARY, 1);
                    DatePickerDialog dpd = new DatePickerDialog(CreateNewContact.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear+1)
                                    +"-"+String.valueOf(dayOfMonth);
                            ((EditText)findViewById(R.id.editBirthday)).setText(date);
                        }
                    },
                            rightNow.get(Calendar.YEAR),
                            rightNow.get(Calendar.MONTH),
                            rightNow.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                    dpd.getDatePicker().setMinDate(minCal.getTimeInMillis());
                }
            }

        });

        Intent intent = getIntent();
        if(intent != null){
            mode = intent.getIntExtra("mode", 0);
            Contact editContact = (Contact) intent.getSerializableExtra("contact");
            if( mode == R.id.btnEditContact) {
                image = intent.getParcelableExtra("image");
                if(image == null)
                    ((ImageButton) findViewById(R.id.btnAddPhoto)).setImageResource(R.drawable.add_photo);
                else {
                    ((ImageButton) findViewById(R.id.btnAddPhoto)).setImageBitmap(image);
                }
                ((EditText)findViewById(R.id.editFirst)).setText(editContact.getFirst());
                ((EditText)findViewById(R.id.editLast)).setText(editContact.getLast());
                ((EditText)findViewById(R.id.editCompany)).setText(editContact.getCompany());
                ((EditText)findViewById(R.id.editPhone)).setText(editContact.getPhone());
                ((EditText)findViewById(R.id.editEmail)).setText(editContact.getEmail());
                ((EditText)findViewById(R.id.editUrl)).setText(editContact.getUrl());
                ((EditText)findViewById(R.id.editAddress)).setText(editContact.getAddress());
                ((EditText)findViewById(R.id.editBirthday)).setText(editContact.getBirthday());
                ((EditText)findViewById(R.id.editNickname)).setText(editContact.getNickname());
                ((EditText)findViewById(R.id.editFacebookProfileURL)).setText(editContact.getFacebookURL());
                ((EditText)findViewById(R.id.editTwiterProfileURL)).setText(editContact.getTwitterURL());
                ((EditText)findViewById(R.id.editSkype)).setText(editContact.getSkype());
                ((EditText)findViewById(R.id.editYouTubeChannel)).setText(editContact.getYouTubeChannel());

            }
        }
    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // this.editText.setText();
    }

    @Override
    public void onClick(View view) {
        Log.d("vt", "onClick: "+view.getId());
        switch(view.getId()){

            case R.id.editBirthday:
                Calendar rightNow = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) +"-"+String.valueOf(monthOfYear+1)
                                +"-"+String.valueOf(dayOfMonth);
                        ((EditText)findViewById(R.id.editBirthday)).setText(date);
                    }
                },
                        rightNow.get(Calendar.YEAR),
                        rightNow.get(Calendar.MONTH),
                        rightNow.get(Calendar.DAY_OF_MONTH));
                Calendar minCal = Calendar.getInstance();
                minCal.set(1850, Calendar.JANUARY, 1);
                dpd.getDatePicker().setMinDate(minCal.getTimeInMillis());
                dpd.show();

                break;

            case R.id.btnAddPhoto:
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                break;

            case R.id.btnSave:
                String first = ((EditText)findViewById(R.id.editFirst)).getText().toString();
                String last = ((EditText)findViewById(R.id.editLast)).getText().toString();
                String company = ((EditText)findViewById(R.id.editCompany)).getText().toString();
                String phone = ((EditText)findViewById(R.id.editPhone)).getText().toString();
                String email = ((EditText)findViewById(R.id.editEmail)).getText().toString();
                String url = ((EditText)findViewById(R.id.editUrl)).getText().toString();
                String address = ((EditText)findViewById(R.id.editAddress)).getText().toString();
                String birthday = ((EditText)findViewById(R.id.editBirthday)).getText().toString();
                String nickname = ((EditText)findViewById(R.id.editNickname)).getText().toString();
                String facebookURL = ((EditText)findViewById(R.id.editFacebookProfileURL)).getText().toString();
                String twitterURL = ((EditText)findViewById(R.id.editTwiterProfileURL)).getText().toString();
                String skype = ((EditText)findViewById(R.id.editSkype)).getText().toString();
                String youTubeChannel = ((EditText)findViewById(R.id.editYouTubeChannel)).getText().toString();


                //validations here
                boolean goodToSave = true;

                if(first.isEmpty()){
                    Toast.makeText(getApplicationContext(), first+"First Name required.",
                            Toast.LENGTH_SHORT).show();
                    goodToSave = false;
                }
                else if(last.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Last Name required.",
                            Toast.LENGTH_SHORT).show();
                    goodToSave = false;
                }
                else if(! phone.matches("(\\+[0-9]+)|([0-9]+)") ){
                    goodToSave = false;
                    Toast.makeText(getApplicationContext(),
                            "Invalid Phone number format", Toast.LENGTH_SHORT).show();
                }
                if(!birthday.isEmpty()){
                    String []split = birthday.split("-");
                    int year = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int date = Integer.parseInt(split[2]);

                    Date setDate = new Date(year-1900, month-1, date);
                    Date checkDate = new Date(1850-1900, 1-1, 1);

                    if (setDate.before(checkDate)){
                        goodToSave = false;
                        Toast.makeText(getApplicationContext(),
                                "Birthdate cannot be before January 01, 1850",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if(!email.isEmpty()){
                    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        goodToSave = false;
                        Toast.makeText(getApplicationContext(),
                                "Invalid email address", Toast.LENGTH_SHORT).show();
                    }
                }

                //if validations are good to save
                if(goodToSave){
                    Contact c = new Contact(image,
                            first,
                            last,
                            company,
                            phone,
                            email,
                            url,
                            address,
                            birthday,
                            nickname,
                            facebookURL,
                            twitterURL,
                            skype,
                            youTubeChannel);

                    if(mode == R.id.btnEditContact){
                        Toast.makeText(getApplicationContext(), "Contact Updated!",
                                Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("editContact",c);
                        returnIntent.putExtra("image",image);
                        setResult(Activity.RESULT_OK, returnIntent);
                    }
                    else {
                        Contact.contactList.add(c);
                        Toast.makeText(getApplicationContext(), "Contact Saved!",
                                Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            image = (Bitmap) data.getExtras().get("data");
            btnAddPhoto.setImageBitmap(image);
        }
    }
}
