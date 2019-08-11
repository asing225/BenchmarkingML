package com.example.benchmarkingml_team4;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;
import weka.core.neighboursearch.NearestNeighbourSearch;

public class KNN {

    public void processKNN(Instances data, int trainSize, int testSize, int k) throws Exception {
        MLHelper helper = new MLHelper();
        Instances testSet = helper.setTestDate(data, trainSize, testSize);
        Instances trainSet = helper.setTrainData(data, trainSize, testSize);

//        NearestNeighbourSearch knn_helper = new NearestNeighbourSearch() {
//            @Override
//            public Instance nearestNeighbour(Instance instance) throws Exception {
//                return null;
//            }
//
//            @Override
//            public Instances kNearestNeighbours(Instance instance, int i) throws Exception {
//                return null;
//            }
//
//            @Override
//            public double[] getDistances() throws Exception {
//                return new double[0];
//            }
//
//            @Override
//            public void update(Instance instance) throws Exception {
//
//            }
//
//            @Override
//            public String getRevision() {
//                return null;
//            }
//        };
        //LinearNNSearch knnHelper = new LinearNNSearch(trainSet);
        //Instances nearestinstances = knnHelper.kNearestNeighbours(trainSet, k);
        //knn_helper.kNearestNeighbours(trainSet, k);
//        Classifier ibk = new IBk();
//        Evaluation eval = new Evaluation(trainSet);
//
//        ibk.buildClassifier(data);
        //double class1 = ibk.classifyInstance(trainSet);
    }
}
