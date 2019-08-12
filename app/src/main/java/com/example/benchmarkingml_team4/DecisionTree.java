package com.example.benchmarkingml_team4;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.REPTree;
import weka.core.Instances;

public class DecisionTree {

    private double tpRate;
    private double tnRate;
    private double fpRate;
    private double fnRate;
    private double hter;
    private long totalRunTime;
    private long TrainTime;
    private long TestTime;

    // Calls the DecisionTree classifier and calculates the variables
    public void process(Instances context, int trainSize, int testSize) throws Exception {
        MLHelper helper = new MLHelper();
        Instances testSet = helper.setTestDate(context, trainSize, testSize);
        Instances trainSet = helper.setTrainData(context, trainSize, testSize);
        Classifier classifier = new REPTree();

        long startTrain = System.currentTimeMillis();
        classifier.buildClassifier(trainSet);
        long stopTrain = System.currentTimeMillis();
        setTrainTime(stopTrain-startTrain);
        long startTest=System.currentTimeMillis();
        Evaluation eval = new Evaluation(testSet);
        eval.evaluateModel(classifier, testSet);
        long stopTest=System.currentTimeMillis();
        setTestTime(stopTest- startTest);
        //Calculating total run time
        setTotalRunTime(getTrainTime() + getTestTime());
        System.out.println("Evaluate Breast Cancer Dataset using Decision Tree Algorithm using WEKA");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);
        setTpRate(eval.truePositiveRate(0));
        System.out.println("The True positive rate is " + getTpRate());
        setTnRate(eval.trueNegativeRate(0));
        System.out.println("The True negative rate is " + getTnRate());
        setFpRate(eval.falsePositiveRate(0));
        System.out.println("The False positive rate is " + getFpRate());
        setFnRate(eval.falseNegativeRate(0));
        System.out.println("The False negative rate is " + getFnRate());
        setHter((getFpRate() + getFnRate())/2);
        System.out.println("The HTE rate is " + getHter());
    }

    public double getTpRate() {
        return tpRate;
    }

    public void setTpRate(double tpr) {
        this.tpRate = tpr;
    }

    public double getTnRate() {
        return tnRate;
    }

    public void setTnRate(double tnr) {
        this.tnRate = tnr;
    }

    public double getFpRate() {
        return fpRate;
    }

    public void setFpRate(double fpr) {
        this.fpRate = fpr;
    }

    public double getFnRate() {
        return fnRate;
    }

    public void setFnRate(double fnr) {
        this.fnRate = fnr;
    }

    public double getHter() {
        return hter;
    }

    public void setHter(double hter) {
        this.hter = hter;
    }


    public long getTrainTime() {
        return TrainTime;
    }

    public void setTrainTime(long trainTime) {
        this.TrainTime = trainTime;
    }

    public long getTestTime() {
        return TestTime;
    }

    public void setTestTime(long testTime) {
        this.TestTime = testTime;
    }

    public long getTotalRunTime() {
        return totalRunTime;
    }

    public void setTotalRunTime(long runTime) {
        this.totalRunTime = runTime;
    }
}