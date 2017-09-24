package example.com.hw3_groups08;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

public class WordsFound extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "vt" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_found);
        int colors[] = {Color.RED, Color.BLUE, Color.GREEN};
        ArrayList<String> words = MainActivity.keywords;
        ArrayList<String> results = MainActivity.results;

        TableLayout tblLayout = (TableLayout) findViewById(R.id.tblLayoutResult);
        TextView viewResult = new TextView(this);
        if(results.size()==0){
            TableRow tblRow = new TableRow(this);
            viewResult.setText(getResources().getString(R.string.noresult));
            tblRow.addView(viewResult);
            tblLayout.addView(tblRow);
        }
        else{
            for(String result : results){
                String exploded[] = result.split("::");
                String word = exploded[0];
                String sentence = exploded[1];
                Log.d(TAG+"set", "word: "+word);
                Log.d(TAG+"set", "Sentence: "+sentence);
                int color = colors[words.indexOf(word)%3];
                viewResult = new TextView(this);

                SpannableStringBuilder sb = new SpannableStringBuilder(sentence);
                ForegroundColorSpan fcs = new ForegroundColorSpan(color);
                String upperSentence = sentence.toUpperCase();
                String upperWord = word.toUpperCase();

                sb.setSpan(fcs, upperSentence.indexOf(upperWord),
                        (upperSentence.toUpperCase()).indexOf(upperWord)+upperWord.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                viewResult.setText(sb);

                TableRow tblRow = new TableRow(this);
                tblRow.addView(viewResult);
                tblLayout.addView(tblRow);

            }
        }
        findViewById(R.id.btnFinish).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnFinish){
            MainActivity.keywords.clear();
            MainActivity.results.clear();
            finishAffinity();
        }
    }
}
