package example.com.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Calculator extends AppCompatActivity  {

    private double memory = 0.0;
    private char operationMem = Character.MIN_VALUE;
    private boolean operatorSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

    }
    protected void onNumberClick(View view){
        int n = Integer.parseInt(view.getTag().toString());
        TextView disp = (TextView)findViewById(R.id.textView);

        if(operatorSet) {
            disp.setText(""+0);
            operatorSet = false;
        }

        if(disp.getText().toString().endsWith(".")) {
            disp.append("" + n);
            return;
        }

        if(disp.getText().toString().equals("-"))
            disp.append(""+n);
        else if(Double.parseDouble(disp.getText().toString()) == 0.0)
            disp.setText(""+n);
        else
            disp.append(""+n);
    }
    protected void onOperatorClick(View view) {
        TextView disp = (TextView)findViewById(R.id.textView);
        if(operatorSet)
            return;
        if(Double.parseDouble(disp.getText().toString()) == 0.0 && view.getTag().toString().equals("-")){
            disp.setText("-");
            return;
        }
        operatorSet = true;
        double ans = 0;

        if(operationMem == Character.MIN_VALUE){
            operationMem = view.getTag().toString().charAt(0);
                    memory = Double.parseDouble(disp.getText().toString());
                    }
                    else{
                    double current = Double.parseDouble(disp.getText().toString());
                    switch(operationMem) {
                    case '+':
                    ans = memory + current;
                    break;
                    case '-':
                    ans = memory - current;
                    break;
                    case '*':
                    ans = memory * current;
                    break;
                    case '/':
                    ans = memory / current;
                    break;
                    }
                    if(view.getTag().toString().charAt(0)=='='){
                    memory = ans;
                    String st = ans+"";
                    disp.setText((st.lastIndexOf('.') == st.length()-2
                    && st.lastIndexOf('0') == st.length()-1)
                    ?
                    st.substring(0,st.lastIndexOf('.'))
                    :
                    st);
                    operationMem = Character.MIN_VALUE;
                    operatorSet = false;
                    return;
                    }
                    memory = ans;
                    operationMem = view.getTag().toString().charAt(0);
                    String st = ans+"";
                    disp.setText((st.lastIndexOf('.') == st.length()-2 && st.lastIndexOf('0') == st.length()-1)
                    ?
                    st.substring(0,st.lastIndexOf('.'))
                    :
                    st);
                    }
                    }

protected void onACClick(View view){
        TextView disp = (TextView)findViewById(R.id.textView);
        disp.setText(""+0);
        memory = 0.0;
        operationMem = Character.MIN_VALUE;
        operatorSet = false;
        }

protected void onDotClick(View view){
        if(operatorSet)
        return;
        TextView disp = (TextView)findViewById(R.id.textView);
        if ( disp.getText().toString().contains(".")){
        return;
        }
        disp.setText(disp.getText().toString().concat("."));
        }
        }