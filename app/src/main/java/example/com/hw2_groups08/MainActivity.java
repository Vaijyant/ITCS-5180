/**
 * Assignment: Homework 02
 * FileName: MainActivity.java
 * AuthorS: Vaijyant Tomar
 */

package example.com.hw2_groups08;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        findViewById(R.id.btnCreateContact).setOnClickListener(this);
        findViewById(R.id.btnEditContact).setOnClickListener(this);
        findViewById(R.id.btnDeleteContact).setOnClickListener(this);
        findViewById(R.id.btnDisplayContact).setOnClickListener(this);
        findViewById(R.id.btnFinish).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.btnCreateContact:
                intent = new Intent(MainActivity.this, CreateNewContact.class);
                intent.putExtra("mode", view.getId());
                startActivity(intent);
                break;

            case R.id.btnEditContact:
            case R.id.btnDeleteContact:
            case R.id.btnDisplayContact:
                intent = new Intent(MainActivity.this, ContactList.class);
                intent.putExtra("mode", view.getId());
                startActivity(intent);
            break;

            case R.id.btnFinish:
                finish();
                System.exit(0);
                break;
        }
    }
}
