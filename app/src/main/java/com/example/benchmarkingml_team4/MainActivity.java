package com.example.benchmarkingml_team4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import weka.core.Instances;

public class MainActivity extends AppCompatActivity {

    CheckBox knn, dt, lr, rf;
    EditText kvalue;
    SeekBar seek;
    Button classify;
    int trainSize = 0, testSize = 0, splitRatio = 1, k = 2;
    String writedata = "";

    OutputStreamWriter outputStreamWriter = null;

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

        final Intent intent = new Intent(MainActivity.this, ResultPage.class);
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
                            intent.putExtra("test", knnAlgo.getAlgoSummary());
                            writedata = writedata + knnAlgo.getAlgoSummary() + "\r\n";
                            writedata = writedata + Double.toString(knnAlgo.getFalseNegativeRate()) + "\n\r";
                            writedata = writedata + Double.toString(knnAlgo.getFalsePositiveRate()) + "\n\r";
                            writedata = writedata + Double.toString(knnAlgo.getHter()) + "\n\r";
                            writedata = writedata + Double.toString(knnAlgo.getTrueNegativeRate()) + "\n\r";
                            writedata = writedata + Double.toString(knnAlgo.getTruePositiveRate()) + "\n\r";
                            writedata = writedata + Long.toString(knnAlgo.getTrainTime()) + "\n\r";
                            writedata = writedata + Long.toString(knnAlgo.getTestTime()) + "\n\r";
                            writeToFile(writedata, MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (dt.isChecked()) {
                        algorithmCount++;
                        DecisionTree decisionTree = new DecisionTree();
                        //getting values from decision tree class
                        try {
                            StringBuffer sb= new StringBuffer("\n");
                            sb.append(System.getProperty("line.separator"));
                            writedata = new String();
                            decisionTree.process(instance, trainSize, testSize);
                            intent.putExtra("result", decisionTree.getAlgoSummary());
                            writedata = writedata + decisionTree.getAlgoSummary() + "\n";
                            writedata = writedata + Double.toString(decisionTree.getTpRate())+ "\n" ;
                            writedata = writedata + Double.toString(decisionTree.getTnRate()) + "\n";
                            writedata = writedata + Double.toString(decisionTree.getFnRate()) + "\n";
                            writedata = writedata + Double.toString(decisionTree.getFpRate()) + "\n";
                            writedata = writedata + Double.toString(decisionTree.getHter()) + "\n";
                            writedata = writedata + Long.toString(decisionTree.getTrainTime())+ "\n";
                            writedata = writedata + Long.toString(decisionTree.getTestTime())+ "\n";
                            writedata = writedata + Long.toString(decisionTree.getTotalRunTime()) + "\n";
                            writeToFile(writedata, MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (lr.isChecked()) {
                        algorithmCount++;
                        LogisticRegression lr= new LogisticRegression();
                        try {
                            writedata = new String();
                            lr.process(instance,trainSize,testSize);
                            intent.putExtra("result", lr.getAlgoSummary());
                            writedata = writedata + lr.getAlgoSummary() + "\r\n";
                            writedata = writedata + Double.toString(lr.getTpr()) + "\r\n";
                            writedata = writedata + Double.toString(lr.getTnr()) + "\r\n";
                            writedata = writedata + Double.toString(lr.getFnr()) + "\r\n";
                            writedata = writedata + Double.toString(lr.getFpr()) + "\r\n";
                            writedata = writedata + Double.toString(lr.getHter()) + "\r\n";
                            writedata = writedata + Long.toString(lr.getTrainTime()) + "\r\n";
                            writedata = writedata + Long.toString(lr.getTestTime()) + "\r\n";
                            writedata = writedata + Long.toString(lr.getExecutionTime()) + "\r\n";
                            writeToFile(writedata, MainActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (rf.isChecked()) {
                        algorithmCount++;
                        Randomforest rf = new Randomforest();
                        try{
                            writedata = new String();
                            rf.process_RF(instance,trainSize,testSize);
                            intent.putExtra("result", rf.getAlgoSummary());
                            writedata = writedata + "\n" + rf.getAlgoSummary() + "\r\n " ;
                            writedata = writedata + "\r" + Double.toString(rf.getTruePositiveRate()) + "\r\n";
                            writedata = writedata + "\n" + Double.toString(rf.getTrueNegativeRate()) + "\r\n";
                            writedata = writedata + "\t" + Double.toString(rf.getTrueNegativeRate()) + "\r\n";
                            writedata = writedata + "\n" + Double.toString(rf.getFalseNegativeRate()) + "\r\n";
                            writedata = writedata + "\n" + Double.toString(rf.getFalsePositiveRate()) + "\r\n";
                            writedata = writedata + "\n" + Double.toString(rf.getHter()) + "\r\n";
                            writedata = writedata + "\n" + Long.toString(rf.getTrainTime()) + "\r\n";
                            writedata = writedata + "\n" + Long.toString(rf.getTestTime()) + "\r\n";
                            writedata = writedata + "\n" + Double.toString(rf.getExecution_Time()) + "\r\n";
                            writeToFile(writedata, MainActivity.this);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

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

    private void writeToFile(String data, Context context) {
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("logfile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
