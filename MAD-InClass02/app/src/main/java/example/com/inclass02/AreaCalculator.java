/*
* Assignment: In Class Assignment 2
* File Name : AreaCalculator.java
* Name      : Vaijyant Tomar, Prerana Singh
* */
package example.com.inclass02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AreaCalculator extends AppCompatActivity {

    static int choice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_calculator);

        final EditText length1 = (EditText) findViewById(R.id.length1);
        final EditText length2 = (EditText)  findViewById(R.id.length2);

        ImageButton tri = (ImageButton)findViewById(R.id.tri);
        ImageButton squ = (ImageButton)findViewById(R.id.squ);
        ImageButton cir = (ImageButton)findViewById(R.id.cir);

        Button calculate = (Button) findViewById(R.id.calculate);
        Button clear = (Button) findViewById(R.id.clear);

        final TextView select = (TextView) findViewById(R.id.select);
        final TextView result = (TextView) findViewById(R.id.result);

        tri.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(findViewById(R.id.blockLen2).getVisibility() == View.INVISIBLE)
                    findViewById(R.id.blockLen2).setVisibility(View.VISIBLE);
                select.setText("Triangle");
                result.setText("");
                choice = 1;
            }
        });

        squ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                select.setText("Square");
                result.setText("");
                findViewById(R.id.blockLen2).setVisibility(View.INVISIBLE);
                choice = 2;
            }
        });

        cir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                select.setText("Circle");
                result.setText("");
                findViewById(R.id.blockLen2).setVisibility(View.INVISIBLE);
                choice = 3;
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double area = 0;

                switch(choice){
                    case 1: //triangle
                        if(length1.getText().toString().isEmpty() || length2.getText().toString().isEmpty())
                            break;
                        double height = Double.parseDouble(length1.getText().toString());
                        double base = Double.parseDouble(length2.getText().toString());
                        area = 0.5 * base * height;
                        break;
                    case 2: //square
                        if(length1.getText().toString().isEmpty())
                            break;
                        double side = Double.parseDouble(length1.getText().toString());
                        area = side * side;
                        break;
                    case 3: //circle
                        if(length1.getText().toString().isEmpty())
                            break;
                        double radius = Double.parseDouble(length1.getText().toString());
                        area =  3.1416 * radius * radius;
                        break;
                }
                result.setText(area+"");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                result.setText("");
                length1.setText("");
                length2.setText("");
                select.setText("Select a shape");
                findViewById(R.id.blockLen2).setVisibility(View.VISIBLE);
                choice = 0;

            }
        });

    }

}
