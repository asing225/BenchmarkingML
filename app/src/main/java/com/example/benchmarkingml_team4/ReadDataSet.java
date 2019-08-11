package com.example.benchmarkingml_team4;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ReadDataSet {

    private static String filename = "breastcancer.arff";

    public BufferedReader readDataFile(Context context, int fileId) {

        BufferedReader inputReader = null;
        InputStream is = context.getResources().openRawResource(fileId);
        try {
            inputReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return inputReader;
    }
}
