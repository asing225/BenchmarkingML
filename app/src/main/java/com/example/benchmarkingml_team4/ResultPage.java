package com.example.benchmarkingml_team4;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ResultPage extends AppCompatActivity {


    TextView heading;
    String out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        heading = (TextView)findViewById(R.id.result);
        heading.setMovementMethod(new ScrollingMovementMethod());
        InputStream inputStream = null;
        try {
            inputStream = this.openFileInput("logfile.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if ( inputStream != null ) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                try {
                    if (!((receiveString = bufferedReader.readLine()) != null))
                        break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stringBuilder.append(receiveString+"\n");
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out = stringBuilder.toString();
        }
        heading.setText(out);
        //String[] r = out.split("(?=\\p{Upper})");
        System.out.println(out);

    }
}
