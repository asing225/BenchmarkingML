package com.example.benchmarkingml_team4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import weka.core.Instances;

public class MainActivity extends AppCompatActivity {

    CheckBox knn, dt, lr, rf;
    EditText kvalue;
    SeekBar seek;
    Button classify;
    int trainSize = 0, testSize = 0, splitRatio = 1, k = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        knn = (CheckBox) findViewById(R.id.knn);
        dt = (CheckBox) findViewById(R.id.dt);
        lr = (CheckBox) findViewById(R.id.lr);
        rf = (CheckBox) findViewById(R.id.rf);
        kvalue = (EditText) findViewById(R.id.k_value);

        classify = (Button) findViewById(R.id.classify);
        seek = (SeekBar) findViewById(R.id.seekBar);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int newVal = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                newVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                splitRatio = seekBar.getProgress();
                if((splitRatio < 1) || (splitRatio > 99))
                {
                    Toast.makeText(MainActivity.this, "Please choose value between 1 and 100", Toast.LENGTH_LONG).show();
                }
                else
                {
                    trainSize = splitRatio;
                    testSize = 100 - trainSize;
                }
            }
        });

        final Intent passData = new Intent();
        classify.setOnClickListener(new View.OnClickListener() {
            Context context = MainActivity.this;
            @Override
            public void onClick(View v) {

                if (splitRatio == 0 || splitRatio == 100) {
                    Toast.makeText(MainActivity.this, "Please choose value between 1 and 100", Toast.LENGTH_LONG).show();
                }
                else {
                    ReadDataSet data = new ReadDataSet();
                    BufferedReader reader = data.readDataFile(context, R.raw.breastcancer);
                    Instances instance = null;
                    try {
                         instance = new Instances(reader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int algorithmCount = 0;
                    Toast.makeText(MainActivity.this, "Please wait for results.", Toast.LENGTH_SHORT).show();
                    if (knn.isChecked()) {
                        k = Integer.parseInt(kvalue.getText().toString());
                        algorithmCount++;
                        KNN knnAlgo = new KNN();
                        try {
                            knnAlgo.processKNN(instance, trainSize, testSize, k);
                            passData.putExtra("trainTime_knn", knnAlgo.getTrainTime());
                            passData.putExtra("testTime_knn", knnAlgo.getTestTime());
                            passData.putExtra("falsePositiveRate_knn", knnAlgo.getFalsePositiveRate());
                            passData.putExtra("falseNegativeRate_knn", knnAlgo.getFalseNegativeRate());
                            passData.putExtra("HTER_knn", knnAlgo.getHter());
                            passData.putExtra("trueNegativeRate_knn", knnAlgo.getTrueNegativeRate());
                            passData.putExtra("truePositiverate_knn", knnAlgo.getTruePositiveRate());
                            passData.putExtra("algoSummary_knn", knnAlgo.getTruePositiveRate());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (dt.isChecked()) {
                        algorithmCount++;

                    }
                    if (lr.isChecked()) {
                        algorithmCount++;
                        LogisticRegression lr= new LogisticRegression();
                        try {
                            lr.process(instance,trainSize,testSize);
                            //passData.putExtra("trainTime_knn", lr.getTpr());
                            //passData.putExtra("testTime_knn", lr.);
                            passData.putExtra("falsePositiveRate_lr", lr.getFpr());
                            passData.putExtra("falseNegativeRate_lr", lr.getFnr());
                            passData.putExtra("HTER_lr", lr.getHter());
                            passData.putExtra("trueNegativeRate_lr", lr.getTnr());
                            passData.putExtra("truePositiverate_lr", lr.getTpr());
                            //passData.putExtra("algoSummary_lr", );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (rf.isChecked()) {
                        algorithmCount++;

                    }
                    if (algorithmCount == 0) {
                        Toast.makeText(MainActivity.this, "Please select an algorithm.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, ResultPage.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
