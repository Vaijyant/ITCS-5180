/**
 * Assignment: Homework 02
 * FileName: ContactList.java
 * AuthorS: Vaijyant Tomar
 */

package example.com.hw2_groups08;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactList extends AppCompatActivity implements View.OnClickListener{

    private int mode;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contact_list);

        resetContactIds();

        ScrollView sv = new ScrollView(this);
        setContentView(sv);
        GridLayout gl = new GridLayout(this);



        gl.setRowCount(Contact.contactList.size());
        gl.setColumnCount(2);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", R.id.btnDisplayContact);
        int i = 0;
        for(Contact contact : Contact.contactList){

            RelativeLayout rl = new RelativeLayout(this);
            ImageView image = new ImageView(this);
            TextView labelName = new TextView(this);
            TextView labelPhone = new TextView(this);

            //setting Unique Ids for dynamically generated components
            rl.setId(i*100 + 1);
            image.setId(i*100 + 2);
            labelName.setId(i*100 + 3);
            labelPhone.setId(i*100 + 4);
            i++;

            if( !(contact.getIds().split(",").length == 4)) {
                contact.setComponetId(rl.getId());
                contact.setComponetId(image.getId());
                contact.setComponetId(labelName.getId());
                contact.setComponetId(labelPhone.getId());
            }

            image.setOnClickListener(this);
            labelName.setOnClickListener(this);
            labelPhone.setOnClickListener(this);

            //layouts
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(200,200);
            parms.setMargins(30, 0, 20, 0);
            image.setLayoutParams(parms);

            RelativeLayout.LayoutParams labeNameLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            labeNameLayoutParams.setMargins(50, 40, 0, 0);
            labelName.setLayoutParams(labeNameLayoutParams);

            RelativeLayout.LayoutParams labelPhoneLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            labelPhoneLayoutParams.setMargins(50, 10, 0, 0); // llp.setMargins(left, top, right, bottom);
            labelPhoneLayoutParams.addRule(RelativeLayout.BELOW, labelName.getId());
            labelPhone.setLayoutParams(labelPhoneLayoutParams);

            //Contents
            if(contact.getImage() == null)
                image.setImageResource(R.drawable.default_image);
            else
                image.setImageBitmap(contact.getImage());
            labelName.setText(contact.getFirst()+" "+contact.getLast());
            labelPhone.setText(contact.getPhone());

            rl.addView(labelName);
            rl.addView(labelPhone);

            gl.addView(image);
            gl.addView(rl);
        }
        sv.addView(gl);
    }

    @Override
    public void onClick(final View view) {
        final View viewForInnerClass = view;
        switch(mode){

            case R.id.btnEditContact:
                Intent intent = new Intent(this, CreateNewContact.class);
                contact = Contact.getContact(viewForInnerClass.getId());

                Contact.contactList.remove(contact);
                intent.putExtra("mode", R.id.btnEditContact);
                intent.putExtra("contact", contact);
                intent.putExtra("image", contact.getImage());
                startActivityForResult(intent,1);
                break;


            case R.id.btnDeleteContact:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you really want to delete this?")
                        .setTitle("Delete Contact");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Contact contact = Contact.getContact(viewForInnerClass.getId());
                        String name = contact.getFirst()+" "+contact.getLast();
                        Contact.contactList.remove(contact);
                        Toast.makeText(getApplicationContext(), "Deleted "+name,
                                Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intentRefresh = new Intent(ContactList.this, ContactList.class);
                        intentRefresh.putExtra("mode", R.id.btnDeleteContact);
                        startActivity(intentRefresh);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });

                AlertDialog alert =builder.create();
                alert.show();
                break;
            case R.id.btnDisplayContact:
                contact = Contact.getContact(view.getId());
                Intent detailedContactIntent = new Intent(ContactList.this, DetailedContact.class);
                detailedContactIntent.putExtra("contact", contact);
                detailedContactIntent.putExtra("image", contact.getImage());
                Log.d("VT", "onClick: "+contact);
                startActivity(detailedContactIntent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Contact editContact = (Contact)intent.getSerializableExtra("editContact");
                Bitmap image = intent.getParcelableExtra("image");
                editContact.setImage(image);
                Contact.contactList.add(editContact);

                Intent intentRefresh = new Intent(this, ContactList.class);
                intentRefresh.putExtra("mode", R.id.btnEditContact);
                startActivity(intentRefresh);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Contact.contactList.add(contact);
            }
        }
    }
    void resetContactIds(){
        for(Contact contact : Contact.contactList){
            contact.setIdsEmpty();
        }
    }
}
