package com.alexjreyes.activity4;

public class SensorEventLog {

    public String timestamp;
    public int count;

    public SensorEventLog(int count, String timestamp) {
        this.count = count;
        this.timestamp = timestamp;
    }
}
