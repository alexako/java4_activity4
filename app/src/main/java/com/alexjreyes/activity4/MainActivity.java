package com.alexjreyes.activity4;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public int numShakes;
    public TextView numShakesTV;
    public TextView logsTV;
    public LinearLayout bg;
    private SensorManager sensorManager;
    private Sensor sensor;
    private List<SensorEventLog> logs;

    private float accel; //Apart from gravity
    private float accelCurrent; // current acceleration including gravity;
    private float accelLast; // Last acceleration including gravity;

    private final String FILENAME = "activity4-"
            + String.valueOf(new Timestamp(System.currentTimeMillis()))
            + ".csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numShakesTV = findViewById(R.id.numShakes);
        bg = findViewById(R.id.bg);
        logsTV = findViewById(R.id.logs);

        logs = new ArrayList<>();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        numShakes = 0;

        if (sensor != null) {
            sensorManager.registerListener(this,
                    sensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }

    }

    public void updateUI(SensorEventLog log) {
        numShakesTV.setText(Integer.toString(numShakes));
        logsTV.setText(String.valueOf(log.count) + " shakes at " + log.timestamp);

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);

        bg.setBackgroundColor(colors.get(getRandomNumber()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        accelLast = accelCurrent;
        accelCurrent = (float) Math.sqrt((double) (x* x + y * y + z * z));
        float delta = accelCurrent - accelLast;
        accel = accel * 0.9f + delta; // low-pass filter

        if (accel > 11) {
            numShakes += 1;
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SensorEventLog log = new SensorEventLog(numShakes, String.valueOf(timestamp));
            Log.d("Sensor change event", String.valueOf(timestamp));
            logs.add(log);
            updateUI(log);
        }
    }

    public void writeToFile(View view) throws IOException {
        CSVHandler csvHandler = new CSVHandler(FILENAME);

        if (!new File(FILENAME).exists()) {
            csvHandler.writeLine(Arrays.asList("Timestamp", "Trigger count"));
        }

        for (SensorEventLog log: logs) {
            List<String> entry = new ArrayList<>();
            entry.add(log.timestamp);
            entry.add(String.valueOf(log.count));
            csvHandler.writeLine(entry);
        }

        csvHandler.setFileContents(csvHandler.getCSVString());
        csvHandler.writeToFile(this, Context.MODE_APPEND);
        Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }

    public void readFile(View view) {
        FileHandler fileHandler = new FileHandler(FILENAME);
        String contents = fileHandler.getFileContents(this);
        Toast.makeText(this, contents, Toast.LENGTH_SHORT).show();
    }

    private int getRandomNumber() {
        Random r = new Random();
        return r.nextInt((2 - 0) + 1);
    }
}
