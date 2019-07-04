package com.alexjreyes.activity4;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVHandler extends FileHandler {

    private static final char DEFAULT_SEPARATOR = ',';
    StringBuilder stringBuilder;

    public CSVHandler(String filename) {
        super(filename);
        stringBuilder = new StringBuilder();
    }

    public void writeLine(List<String> values) throws IOException {
        writeLine(values, DEFAULT_SEPARATOR, ' ');
    }

    public void writeLine(List<String> values, char separators) throws IOException {
        writeLine(values, separators, ' ');
    }

    private String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;
    }

    public void writeLine(List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        for (String value : values) {
            if (!first) {
                stringBuilder.append(separators);
            }
            if (customQuote == ' ') {
                stringBuilder.append(followCVSformat(value));
            } else {
                stringBuilder.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        stringBuilder.append("\n");
    }

    public String getCSVString() {
        return stringBuilder.toString();
    }

    public Map<String, String> getCSVMap(File file) {
        String content = getFileContents(file);
        Map<String, String> result = new HashMap<>();
        String[] c = content.split("\n");

        String[] headers = c[0].split(",");

        for (String row: Arrays.copyOfRange(c, 1, c.length)) {
            String[] value = row.split(",");
            for (int i=0; i < headers.length; i++) {
                result.put(headers[i], value[i]);
            }
        }

        return result;
    }
}
