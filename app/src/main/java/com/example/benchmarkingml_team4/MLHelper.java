package com.example.benchmarkingml_team4;

import java.util.Random;
import weka.core.Instances;

public class MLHelper {

    public Instances setTrainData(Instances data, int trainSize, int testSize) {
        int classIndex = data.numAttributes() - 1;
        int dataSize = data.size();
        data.setClassIndex(classIndex);
        data.randomize(new Random(1));
        Instances trainData = new Instances(data, 0, (trainSize*dataSize)/100);
        return trainData;
    }

    public Instances setTestDate(Instances data, int trainSize, int testSize) {
        int classIndex = data.numAttributes() - 1;
        int dataSize = data.size();
        data.setClassIndex(classIndex);
        data.randomize(new Random(1));
        Instances testData = new Instances(data, (trainSize*dataSize)/100, (testSize*dataSize)/100);
        return testData;
    }
}