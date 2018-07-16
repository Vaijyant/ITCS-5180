/**
 * Assignment: Homework 02
 * FileName: DetailedActivity.java
 * Authors: Vaijyant Tomar
 */

package example.com.hw2_groups08;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailedContact extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_contact);
        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");
        Log.d("vt1", "onCreate: "+contact);
        Bitmap image = intent.getParcelableExtra("image");
        if( image == null)
            ((ImageView)findViewById(R.id.imgPhoto)).setImageResource(R.drawable.default_image);
        else {
            contact.setImage(image);
            ((ImageView) findViewById(R.id.imgPhoto)).setImageBitmap(contact.getImage());
        }

        ((TextView)findViewById(R.id.viewFirst)).setText(contact.getFirst());
        ((TextView)findViewById(R.id.viewLast)).setText(contact.getLast());
        ((TextView)findViewById(R.id.viewCompany)).setText(contact.getCompany());
        ((TextView)findViewById(R.id.viewPhone)).setText(contact.getPhone());
        ((TextView)findViewById(R.id.viewEmail)).setText(contact.getEmail());

        TextView viewUrl = (TextView)findViewById(R.id.viewUrl);
        viewUrl.setText(contact.getUrl());

        ((TextView)findViewById(R.id.viewAddress)).setText(contact.getAddress());
        ((TextView)findViewById(R.id.viewBirthday)).setText(contact.getBirthday());
        ((TextView)findViewById(R.id.viewNickname)).setText(contact.getNickname());

        TextView viewFacebookProfileUrl = (TextView)findViewById(R.id.viewFacebookProfileURL);
        viewFacebookProfileUrl.setText(contact.getFacebookURL());

        TextView viewTwitterProfileURL = (TextView)findViewById(R.id.viewTwitterProfileURL);
        viewTwitterProfileURL.setText(contact.getTwitterURL());

        TextView viewSkype = (TextView)findViewById(R.id.viewSkype);
        viewSkype.setText(contact.getSkype());

        TextView viewYouTubeChannel = (TextView)findViewById(R.id.viewYouTubeChannel);
        viewYouTubeChannel.setText(contact.getYouTubeChannel());




        if(!viewUrl.getText().toString().isEmpty())
            viewUrl.setOnClickListener(this);
        if(!viewFacebookProfileUrl.getText().toString().isEmpty())
            viewFacebookProfileUrl.setOnClickListener(this);
        if(!viewTwitterProfileURL.getText().toString().isEmpty())
            viewTwitterProfileURL.setOnClickListener(this);
        if(!viewSkype.getText().toString().isEmpty())
            viewSkype.setOnClickListener(this);
        if(!viewUrl.getText().toString().isEmpty())
            viewYouTubeChannel.setOnClickListener(this);


        }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewUrl:
            case R.id.viewFacebookProfileURL:
            case R.id.viewTwitterProfileURL:
            case R.id.viewSkype:
            case R.id.viewYouTubeChannel:
                String url = ((TextView)findViewById(view.getId())).getText().toString();
                Intent browserIntent;
                if(url.startsWith("https://") || url.startsWith("http://"))
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                else
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+url));
                startActivity(browserIntent);
        }

    }
}

