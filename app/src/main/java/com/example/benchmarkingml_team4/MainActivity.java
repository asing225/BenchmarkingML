package com.example.benchmarkingml_team4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import Service.UploadToServer;
import weka.core.Instances;

public class MainActivity extends AppCompatActivity {

    CheckBox knn, dt, lr, rf;
    EditText kvalue;
    SeekBar seek;
    Button classify, Upload, View_Cloud;
    Button classify;
    Button GPU;
  
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
        Upload = (Button) findViewById(R.id.Upload_btn);
        View_Cloud = (Button) findViewById(R.id. Cloud_btn);

        GPU= (Button)findViewById(R.id.gpuButton);

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


        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                if (knn.isChecked() || dt.isChecked() || lr.isChecked() || rf.isChecked()) {
                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Log.i("msg","upload started");
                                }
                            });

                            int result = -1;

                            if (result == 1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("msg","upload complete");
                                    }
                                });
                            } else if (result == 0) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("msg","upload error");
                                    }
                                });
                            }
                        }
                    }).start();

                }

                else{
                    Toast.makeText(MainActivity.this, "Please select an algorithm", Toast.LENGTH_SHORT).show();
                }
            }


        });

        View_Cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (knn.isChecked() || dt.isChecked() || lr.isChecked() || rf.isChecked()) {
                    Uri uri = Uri.parse("https://colab.research.google.com/drive/1qtuJqSkMrF7TzO28FjoJ0UvnnuXpJ-Ls");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(MainActivity.this, "Please select an algorithm", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Intent intent = new Intent(MainActivity.this, ResultPage.class);

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
                            writedata = writedata + " KNN Algorithm" + knnAlgo.getAlgoSummary();
                            writedata = writedata + "\n True Positive Rate: \t" + knnAlgo.getTruePositiveRate();
                            writedata = writedata + "\n True Negative Rate: \t" + knnAlgo.getTrueNegativeRate();
                            writedata = writedata + "\n False Negative Rate: \t" + knnAlgo.getFalseNegativeRate();
                            writedata = writedata + "\n False Positive Rate: \t" + knnAlgo.getFalsePositiveRate();
                            writedata = writedata + "\n Hter: \t" + knnAlgo.getHter();
                            writedata = writedata + "\n Train Time: \t" + knnAlgo.getTrainTime();
                            writedata = writedata + "\n Test Time: \t" + knnAlgo.getTestTime();
                            writedata = writedata + "\n Total Execution Time: \t" + knnAlgo.getExecutionTime();
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
                            writedata = writedata + " Decision Tree \n" + decisionTree.getAlgoSummary() + "\n";
                            writedata = writedata + "\n True Positive Rate: \t" + decisionTree.getTpRate();
                            writedata = writedata + "\n True Negative Rate: \t" + decisionTree.getTnRate();
                            writedata = writedata + "\n False Negative Rate: \t" + decisionTree.getFnRate();
                            writedata = writedata + "\n False Positive Rate: \t" + decisionTree.getFpRate();
                            writedata = writedata + "\n Hter: \t" + decisionTree.getHter();
                            writedata = writedata + "\n Train Time: \t" + decisionTree.getTrainTime();
                            writedata = writedata + "\n Test Time: \t" + decisionTree.getTestTime();
                            writedata = writedata + "\n Total Execution Time: \t" + decisionTree.getTotalRunTime();
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
                            writedata = writedata + "Logistic Regression \n" +lr.getAlgoSummary();
                            writedata = writedata + "\n True Positive Rate: \t" + lr.getTpr();
                            writedata = writedata + "\n True Negative Rate: \t" + lr.getTnr();
                            writedata = writedata + "\n False Negative Rate: \t" + lr.getFnr();
                            writedata = writedata + "\n False Positive Rate: \t" + lr.getFpr();
                            writedata = writedata + "\n Hter: \t" + lr.getHter();
                            writedata = writedata + "\n Train Time: \t" + lr.getTrainTime();
                            writedata = writedata + "\n Test Time: \t" + lr.getTestTime();
                            writedata = writedata + "\n Total Execution Time: \t" + lr.getExecutionTime();
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
                            writedata = writedata + "Random Forest \n" + rf.getAlgoSummary() + "\r\n " ;
                            writedata = writedata + "\n True Positive Rate: \t" + rf.getTruePositiveRate();
                            writedata = writedata + "\n True Negative Rate: \t" + rf.getTrueNegativeRate();
                            writedata = writedata + "\n False Negative Rate: \t" + rf.getFalseNegativeRate();
                            writedata = writedata + "\n False Positive Rate: \t" + rf.getFalsePositiveRate();
                            writedata = writedata + "\n Hter: \t" + rf.getHter();
                            writedata = writedata + "\n Train Time: \t" + rf.getTrainTime();
                            writedata = writedata + "\n Test Time: \t" + rf.getTestTime();
                            writedata = writedata + "\n Total Execution Time: \t" + rf.getExecution_Time();
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

    GPU.setOnClickListener(new View.OnClickListener() {
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
                if (rf.isChecked()) {
                    algorithmCount++;
                    Randomforest rf = new Randomforest();
                    try{
                        writedata = new String();
                        rf.process_RF(instance,trainSize,testSize);
                        intent.putExtra("result", rf.getAlgoSummary());
                        writedata = writedata + "Random Forest \n" + rf.getAlgoSummary() + "\r\n " ;
                        writedata = writedata + "\n True Positive Rate: \t" + rf.getTruePositiveRate();
                        writedata = writedata + "\n True Negative Rate: \t" + rf.getTrueNegativeRate();
                        writedata = writedata + "\n False Negative Rate: \t" + rf.getFalseNegativeRate();
                        writedata = writedata + "\n False Positive Rate: \t" + rf.getFalsePositiveRate();
                        writedata = writedata + "\n Hter: \t" + rf.getHter();
                        writedata = writedata + "\n Train Time: \t" + rf.getTrainTime();
                        writedata = writedata + "\n Test Time: \t" + rf.getTestTime();
                        writedata = writedata + "\n Total Execution Time: \t" + rf.getExecution_Time();
                        writeToFile(writedata, MainActivity.this);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (algorithmCount == 0) {
                        Toast.makeText(MainActivity.this, "Please select an algorithm.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, ResultPage.class);
                        startActivity(intent);
                    }

                }
                }}});
            }
                private void writeToFile (String data, Context context){
                    try {
                        outputStreamWriter = new OutputStreamWriter(context.openFileOutput("logfile.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(data);
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
            }
