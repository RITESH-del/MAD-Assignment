package com.example.sensorapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor accelerometer, lightSensor, proximitySensor;
    TextView accelText, lightText, proximityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accelText = findViewById(R.id.accelText);
        lightText = findViewById(R.id.lightText);
        proximityText = findViewById(R.id.proximityText);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register listeners
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            lightText.setText("Light sensor not available");
        }

        if (proximitySensor != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            proximityText.setText("Proximity sensor not available");
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        sensorManager.unregisterListener(this);
    }

@Override
    public void onSensorChanged(SensorEvent event){
    Log.d("SENSOR_DEBUG", "Sensor type: " + event.sensor.getType());

        switch (event.sensor.getType()){ // or 1

            case Sensor.TYPE_ACCELEROMETER:
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                accelText.setText(String.format(Locale.US, "Accelerometer:\nX: %.2f \nY: %.2f \nZ: %.2f", x, y, z));
                break;
            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                lightText.setText(String.format(Locale.US, "Light: %.2f lx", light));
                break;
            case Sensor.TYPE_PROXIMITY:
                float proximity = event.values[0];
                proximityText.setText(String.format(Locale.US, "Proximity: %.2f", proximity));
                break;
    }}

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can leave empty
    }
}