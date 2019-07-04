package com.alexjreyes.activity4;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileHandler {

    private String filename;
    private String fileContents;

    public FileHandler(String filename) {
        this.filename = filename;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
    }

    public boolean writeToFile(Context context, int writeMode) {

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, writeMode);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public File openFile(Context context) {
        File directory = context.getFilesDir();
        return new File(directory, filename);
    }

    public String getFileContents(Context context) {
        return getFileContents(openFile(context));
    }

    public String getFileContents(File file) {
        FileInputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
        } catch (Exception ex) {
            Log.e("File read error: ", ex.getMessage());
        }

        return stringBuilder.toString();
    }
}