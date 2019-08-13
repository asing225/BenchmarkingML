package com.example.benchmarkingml_team4;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
//Using GPU functionality
public class RandomForestG {

    private long Train_Time, Test_Time;
    private double False_Positive_Rate, False_Negative_Rate, hter, True_Positive_Rate, True_Negative_Rate,Execution_Time;
    private String algoSummary;

    public void process_RF(Instances data, int trainSize, int testSize) throws Exception {

        MLHelper helper = new MLHelper();
        Instances trainSet = helper.setTrainData(data, trainSize, testSize);
        Instances testSet = helper.setTestDate(data, trainSize, testSize);
        long startTrainTime = System.currentTimeMillis();
        Classifier RF = new RandomForest();
        long endTrainTime = 0, startTestTime = 0;
        Evaluation eval = null;
        try{
            RF.buildClassifier(trainSet);
            endTrainTime = System.currentTimeMillis();
            startTestTime = System.currentTimeMillis();
            eval = new Evaluation(testSet);
            eval.evaluateModel(RF, testSet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTestTime = System.currentTimeMillis();
        Train_Time = endTrainTime- startTrainTime;
        Test_Time =endTestTime- startTestTime ;
        False_Positive_Rate = eval.falsePositiveRate(0);
        False_Negative_Rate = eval.falseNegativeRate(0);
        hter = (False_Positive_Rate + False_Negative_Rate) / 2;
        True_Positive_Rate = eval.truePositiveRate(0);
        True_Negative_Rate = eval.trueNegativeRate(0);
        algoSummary = eval.toSummaryString();
        Execution_Time = getTrainTime()+getTestTime();

    }

    public long getTrainTime() {
        return Train_Time;
    }

    public long getTestTime() {
        return Test_Time;
    }

    public double getFalsePositiveRate() {
        return False_Negative_Rate;
    }

    public double getFalseNegativeRate() {
        return False_Negative_Rate;
    }

    public double getHter() {
        return hter;
    }

    public double getTruePositiveRate() {
        return True_Positive_Rate;
    }

    public double getTrueNegativeRate() {
        return True_Negative_Rate;
    }
    public double getExecution_Time(){
        return Execution_Time;
    }
    public String getAlgoSummary() {
        return algoSummary;
    }
}
