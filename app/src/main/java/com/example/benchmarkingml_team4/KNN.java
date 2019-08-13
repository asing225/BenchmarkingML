package com.example.benchmarkingml_team4;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;

public class KNN {

    private long trainTime, testTime,executionTime;
    private double falsePositiveRate, falseNegativeRate, hter, truePositiveRate, trueNegativeRate;
    public String algoSummary;

    public void processKNN(Instances data, int trainSize, int testSize, int k) throws Exception {

        MLHelper helper = new MLHelper();
        Instances trainSet = helper.setTrainData(data, trainSize, testSize);
        Instances testSet = helper.setTestDate(data, trainSize, testSize);
        long startTrainTime = System.currentTimeMillis();
        Classifier ibk = new IBk(k);
        long endTrainTime = 0, startTestTime = 0;
        Evaluation eval = null;
        try{
            ibk.buildClassifier(trainSet);
            endTrainTime = System.currentTimeMillis();
            startTestTime = System.currentTimeMillis();
            eval = new Evaluation(testSet);
            eval.evaluateModel(ibk, testSet);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        long endTestTime = System.currentTimeMillis();
        trainTime = -(startTrainTime - endTrainTime);
        testTime = -(startTestTime - endTestTime);
        setExecutionTime(getTrainTime() + getTestTime());
        falsePositiveRate = eval.falsePositiveRate(0);
        falseNegativeRate = eval.falseNegativeRate(0);
        hter = (falsePositiveRate + falseNegativeRate) / 2;
        truePositiveRate = eval.truePositiveRate(0);
        trueNegativeRate = eval.trueNegativeRate(0);
        algoSummary = eval.toSummaryString();
    }

    public long getTrainTime() {
        return trainTime;
    }

    public long getTestTime() {
        return testTime;
    }

    public double getFalsePositiveRate() {
        return falsePositiveRate;
    }

    public double getFalseNegativeRate() {
        return falseNegativeRate;
    }

    public double getHter() {
        return hter;
    }

    public double getTruePositiveRate() {
        return truePositiveRate;
    }

    public double getTrueNegativeRate() {
        return trueNegativeRate;
    }

    public void setExecutionTime(long ExecutionTime) {
        executionTime = ExecutionTime;
    }

    public double getExecutionTime()
    {
        return executionTime;
    }


    public String getAlgoSummary() {
        //eval.toSummaryString("\nResults:\nCurrent Timestamp: "+format+"\n tpRate: "+tpRate + "\nTNR: "+tnRate+ "\nFPR: "+ fpRate + "\nFNR: "+ fnRate + " \nHTER: "+ hter +"\nTotal time training: "+ (totalRunTime) + " milliseconds" + "\nTotal time testing: "+ (TestTime) + " milliseconds", false);
        return algoSummary;
    }
}
