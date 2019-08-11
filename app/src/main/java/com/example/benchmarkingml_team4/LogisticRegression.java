package com.example.benchmarkingml_team4;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
/*The following class is used to classify the breast cancer dataset and also evaluates the efficiency of the algorithm using
* various metrics such as RMSE,TAR,FAR,NAR,HTER and execution times
*/
public class LogisticRegression extends AppCompatActivity {
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
        long startTime = System.currentTimeMillis();
        Classifier classifier = new Logistic();
         //train the alogorithm with the training dataset
        classifier.buildClassifier(trainSet);
        //test the model using the testing dataset
        Evaluation eval = new Evaluation(testSet);
        eval.evaluateModel(classifier, testSet);
        /** Print the algorithm summary */
        System.out.println("Evaluate Breast Cancer Dataset using Logistic Regression");
        System.out.println(eval.toSummaryString());
        //System.out.println(classifier);
        //finding various parameters
        double tpr= eval.truePositiveRate(0);
        System.out.println("The True positive rate is " +tpr);
        double tnr= eval.trueNegativeRate(0);
        System.out.println("The True negative rate is " +tnr);
        double fpr  = eval.falsePositiveRate(0);
        System.out.println("The False positive rate is " +fpr);
        double fnr= eval.falseNegativeRate(0);
        System.out.println("The False negative rate is " +fnr);
        double hter=(fpr+fnr)/2;
        System.out.println("The HTE rate is " +hter);

        //finding the total time of execution for testing
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("The Execution Time is " + totalTime);
    }

}