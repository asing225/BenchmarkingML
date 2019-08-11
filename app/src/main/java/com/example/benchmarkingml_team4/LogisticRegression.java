package com.example.benchmarkingml_team4;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;

/*The following class is used to classify the breast cancer dataset and also evaluates the efficiency of the algorithm using
* various metrics such as RMSE,TAR,FAR,NAR,HTER and execution times
*/
public class LogisticRegression extends AppCompatActivity {
    private double tpr;
    private double tnr;
    private double fpr;
    private double fnr;
    private double hter;
    private long TrainTime;
    private long TestTime;
    private long ExecutionTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

    }
    /**
     * This method is used to classify the Breast Cancer dataset into recurrence and non-recurrence events
     * The classifier used is Logistic Regression
     * @param context
     */
    public void process(Instances context, int trainSize, int testSize) throws Exception {
        MLHelper helper = new MLHelper();
        Instances testSet = helper.setTestDate(context, trainSize, testSize);
        Instances trainSet = helper.setTrainData(context, trainSize, testSize);
        /** Classifier here is Logistic Regression */
        //trainSet.setClassIndex(10);
        Classifier classifier = new Logistic();
         //train the alogorithm with the training dataset
        long startTrain = System.currentTimeMillis();
        classifier.buildClassifier(trainSet);
        long stopTrain = System.currentTimeMillis();
         setTrainTime(stopTrain-startTrain);
        //test the model using the testing dataset
        long startTest=System.currentTimeMillis();
        Evaluation eval = new Evaluation(testSet);
        eval.evaluateModel(classifier, testSet);
        long stopTest=System.currentTimeMillis();
        /** Print the algorithm summary */
         setTestTime(stopTest- startTest);
         setExecutionTime(getTrainTime() + getTestTime());
        System.out.println("Evaluate Breast Cancer Dataset using Logistic Regression");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);

        //finding various parameters
         setTpr(eval.truePositiveRate(0));
        System.out.println("The True positive rate is " + getTpr());
         setTnr(eval.trueNegativeRate(0));
        System.out.println("The True negative rate is " + getTnr());
         setFpr(eval.falsePositiveRate(0));
        System.out.println("The False positive rate is " + getFpr());
         setFnr(eval.falseNegativeRate(0));
        System.out.println("The False negative rate is " + getFnr());
         setHter((getFpr() + getFnr())/2);
        System.out.println("The HTE rate is " + getHter());

        //finding the total time of execution for testimg

    }

    public double getTpr() {
        return tpr;
    }

    public void setTpr(double tpr) {
        this.tpr = tpr;
    }

    public double getTnr() {
        return tnr;
    }

    public void setTnr(double tnr) {
        this.tnr = tnr;
    }

    public double getFpr() {
        return fpr;
    }

    public void setFpr(double fpr) {
        this.fpr = fpr;
    }

    public double getFnr() {
        return fnr;
    }

    public void setFnr(double fnr) {
        this.fnr = fnr;
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
        TrainTime = trainTime;
    }

    public long getTestTime() {
        return TestTime;
    }

    public void setTestTime(long testTime) {
        TestTime = testTime;
    }

    public long getExecutionTime() {
        return ExecutionTime;
    }

    public void setExecutionTime(long executionTime) {
        ExecutionTime = executionTime;
    }
}