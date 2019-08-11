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
public class LogisticRegression extends AppCompatActivity {

    /** file names are defined*/
    public static final String TRAINING_DATA_SET_FILENAME="breast-cancer.arff";
    public static final String TESTING_DATA_SET_FILENAME="breast-cancer.arff";

    //public static final String PREDICTION_DATA_SET_FILENAME="weather.nominal-confused.arff";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

    }

    public static Instances getDataSet(String fileName) throws IOException {

        int classIdx = 1;
        /** the arffloader to load the arff file */
        ArffLoader loader = new ArffLoader();

        /** load the traing data */
        loader.setSource(LogisticRegression.class.getResourceAsStream("/" + fileName));
        /**
         * we can also set the file like loader3.setFile(new
         * File("test-confused.arff"));
         */
        Instances dataSet = loader.getDataSet();
        /** set the index based on the data given in the arff files */
        dataSet.setClassIndex(classIdx);
        return dataSet;
    }

    /**
     * This method is used to process the input and return the statistics.
     */
    public static void process() throws Exception {

        Instances trainingDataSet = getDataSet(TRAINING_DATA_SET_FILENAME);
        Instances testingDataSet = getDataSet(TESTING_DATA_SET_FILENAME);
        /** Classifier here is Linear Regression */
        Classifier classifier = new Logistic();
        /** */
        classifier.buildClassifier(trainingDataSet);
        /**
         * train the alogorithm with the training data and evaluate the
         * algorithm with testing data
         */
        Evaluation eval = new Evaluation(trainingDataSet);
        eval.evaluateModel(classifier, testingDataSet);
        /** Print the algorithm summary */
        System.out.println("** Logistic Regression Evaluation with Datasets **");
        System.out.println(eval.toSummaryString());
        System.out.println(classifier);

        Instance predicationDataSet = getDataSet(TESTING_DATA_SET_FILENAME).lastInstance();
        double value = classifier.classifyInstance(predicationDataSet);
        /** Prediction Output */
        System.out.println(value);
    }

}