package example.com.hw3_groups08;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected static ArrayList<String> keywords = new ArrayList<String>();
    protected static ArrayList<String> results = new ArrayList<String>();
    String TAG = "vt";
    int success = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.imgBtnAddRemove).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        ViewGroup row = (ViewGroup) view.getParent();

        if (row.getClass().toString().contains("TableRow")) {
            EditText editWord = (EditText) row.getChildAt(0);
            ImageButton imgBtnAddRemove = (ImageButton) row.getChildAt(1);

            if (imgBtnAddRemove.getTag().equals("add")) {
                String word = editWord.getText().toString();
                if (keywords.size() == 20) {
                    Toast.makeText(this, "Can not specify more than 20 words at a time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!word.isEmpty()) {
                    if (keywords.contains(word)) {
                        Toast.makeText(this, "\"" + word + "\" is already specified.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    keywords.add(word);
                    //Create new components
                    TableLayout tblLayout = (TableLayout) findViewById(R.id.tblLayout);
                    TableRow tblRowNew = new TableRow(this);
                    tblRowNew.setId(View.generateViewId());
                    EditText editWordNew = new EditText(this);
                    editWordNew.setId(View.generateViewId());
                    ImageButton imgBtnARNew = new ImageButton(this);

                    TableRow.LayoutParams tableRowParms = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
                    tblRowNew.setLayoutParams(tableRowParms);

                    TableRow.LayoutParams editWordLayoutParams = (TableRow.LayoutParams) editWord.getLayoutParams();
                    editWordNew.setLayoutParams(editWordLayoutParams);

                    imgBtnARNew.setBackgroundColor(getResources().getColor(R.color.white));
                    TableRow.LayoutParams imgBtnARLayoutParams = (TableRow.LayoutParams) imgBtnAddRemove.getLayoutParams();

                    imgBtnARNew.setLayoutParams(imgBtnARLayoutParams);
                    imgBtnARNew.setImageResource(R.drawable.add);
                    imgBtnARNew.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imgBtnARNew.setTag("add");

                    tblRowNew.addView(editWordNew);
                    tblRowNew.addView(imgBtnARNew);

                    tblLayout.addView(tblRowNew);

                    editWordNew.requestFocus();
                    imgBtnARNew.setOnClickListener(this);

                    imgBtnAddRemove.setImageResource(R.drawable.remove);
                    imgBtnAddRemove.setTag("remove");
                } else {
                    Toast.makeText(this, "No word given.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (imgBtnAddRemove.getTag() == "remove") {
                TableLayout table = (TableLayout) findViewById(R.id.tblLayout);
                table.removeView(row);
                keywords.remove(((EditText) row.getChildAt(0)).getText().toString());
            }
        }
        // Search Button is clicked
        else if (view.getId() == R.id.btnSearch) {
            ((Button)findViewById(R.id.btnSearch)).setClickable(false);
            String mode;
            if (((CheckBox) findViewById(R.id.checkBox)).isChecked())
                mode = "case_sensitive";
            else
                mode = "case_insensitive";
            SearchTask st;
            for (String word : keywords) {
                st =new SearchTask();
                st.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, word, mode);

            }
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
            pb.setVisibility(View.VISIBLE);
        }
    }

    private class SearchTask extends AsyncTask<String, Integer, String> {
        ProgressBar pb;
        long readSize = -1;
        long fileSize = 0;
        final int CASE_SENSITIVE = 123;
        final int CASE_INSENSITIVE = 345;
        ArrayList<String> lines = new ArrayList<String>();

        @Override
        protected void onPreExecute() {
            pb = (ProgressBar) findViewById(R.id.progressBar);
            pb.setProgress(0);
            pb.setMax(100);

        }

        @Override
        protected String doInBackground(String... params) {
            String word = params[0];
            int mode = params[1].equals("case_sensitive") ? CASE_SENSITIVE : CASE_INSENSITIVE;
            try {
                InputStream is = ((AssetManager) MainActivity.this.getResources().getAssets()).open("textfile.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                fileSize = is.available();

                String readLine = "";
                while ((readLine = br.readLine()) != null) {
                    lines.add(readLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < lines.size(); i++) {
                String resultLine = createResultLine(lines.get(i), word, i, mode);
                if(!resultLine.isEmpty()) {
                    results.add(word + "::" + resultLine);
                }
            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] > pb.getProgress())
                pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            success++;
            if(success == keywords.size()) {
                Toast.makeText(MainActivity.this, "Search complete", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, WordsFound.class);
                startActivity(intent);
            }
        }

        String createResultLine(String line, String word, int lineNumber, int mode) {
            String resultLine = "";
            readSize += line.length();

            int progress = (int) (((double) readSize / (double) fileSize) * 100);
            publishProgress(progress);

            Pattern pattern = Pattern.compile(word);
            Matcher matcher= pattern.matcher(line);
            switch (mode) {
                case CASE_SENSITIVE:
                    pattern = Pattern.compile(word);
                    matcher = pattern.matcher(line);
                    break;
                case CASE_INSENSITIVE:
                    pattern = Pattern.compile(word.toUpperCase());
                    matcher = pattern.matcher(line.toUpperCase());
                    break;
            }
            if(matcher.find()){
                if(lineNumber==0){
                    //=====================================================first line
                    Log.d(TAG, "Line 0");
                    if(matcher.start()>10){
                        String lineAppend = line;
                        int start = lineNumber;
                        while(lineAppend.length() < 30+matcher.end()){
                            lineAppend = lineAppend+" "+lines.get(++start);
                        }
                        resultLine = "..." + lineAppend.substring(matcher.start() - 10, matcher.end() + 30 - (10 + word.length()))+"...";
                    }
                    else{
                        String lineAppend = line;
                        int start = lineNumber;
                        while(lineAppend.length() < 33){
                            lineAppend = lineAppend+" "+lines.get(++start);
                        }
                        resultLine = lineAppend.substring(0, 33)+"...";
                    }
                    Log.d(TAG, "Line 0: "+resultLine);
                }
                else if(lineNumber==lines.size()-1){
                    //=====================================================last line
                    if(matcher.start()>10){
                        int start = lineNumber;
                        if(matcher.end()+(line.length()-matcher.end())<33){
                            String lineAppend = line;
                            int change = 0;
                            while(matcher.start() + change < 10){
                                change+=lines.get(--start).length();
                                lineAppend = lines.get(start)+" "+lineAppend;
                            }
                            resultLine = "..." + lineAppend.substring(matcher.start() + change - 10, lineAppend.length());
                        }
                        else {
                            resultLine = "..." + line.substring(matcher.start() - 10, matcher.end() + 30 - (10 + word.length()));
                        }
                    }
                    else{
                        String lineAppend = line;
                        int change = 0;
                        int start = lineNumber;
                        while(matcher.start() + change < 10){
                            change+=lines.get(--start).length();
                            lineAppend = lines.get(start)+" "+lineAppend;
                        }
                        start = lineNumber;
                        while(lineAppend.length() < 30){
                            lineAppend = lineAppend+" "+lines.get(++start);
                        }
                        resultLine = "..." + lineAppend.substring(matcher.start() - 10 + change, matcher.end() + change + 30 - (10 + word.length()))+"...";
                    }
                }
                else{
                    //=====================================================all the other lines
                    if(matcher.start()>10){
                        String lineAppend = line;
                        int start = lineNumber;
                        while(lineAppend.length() < 30+matcher.end()){
                            lineAppend = lineAppend+" "+lines.get(++start);
                        }
                        resultLine = "..." + lineAppend.substring(matcher.start() - 10, matcher.end() + 30 - (10 + word.length()))+"...";
                    }
                    else{
                        String lineAppend = line;
                        int change = 0;
                        int start = lineNumber;
                        while(matcher.start() + change < 10){
                            change+=lines.get(--start).length();
                            lineAppend = lines.get(start)+" "+lineAppend;
                        }
                        start = lineNumber;
                        while((lineAppend.length() - change - matcher.end()) < 30 -(10+word.length())){
                            lineAppend = lineAppend+" "+lines.get(++start);
                        }
                        Log.d(TAG+"rest:", "word: "+word);
                        Log.d(TAG+"rest:", lineAppend);
                        resultLine = "..." + lineAppend.substring(matcher.start() - 10 + change, matcher.end() + change + 30 - (10 + word.length()))+"...";
                    }
                }
            }
            return resultLine;
        }
    }
}
