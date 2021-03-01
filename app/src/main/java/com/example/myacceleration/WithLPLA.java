package com.example.myacceleration;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.kircherelectronics.fsensor.observer.SensorSubject;
import com.kircherelectronics.fsensor.sensor.FSensor;
import com.kircherelectronics.fsensor.sensor.acceleration.LowPassLinearAccelerationSensor;

import java.util.Arrays;

/**
 * This activity allows to display the acceleration's value with the low pass linear acceleration
 * filter.
 * Cette activité permet d'afficher la valeur de l'accélération avec le filtre low pass linear
 * acceleration.
 * @link https://github.com/KalebKE/AccelerationExplorer/wiki/Low-Pass-Filter-Linear-Acceleration
 */
public class WithLPLA extends AppCompatActivity {

    /**
     * Max values
     */
    private float maxX;
    private float maxY;
    private float maxZ;

    /**
     * TextView
     */
    private TextView textViewMaxX;
    private TextView textViewMaxY;
    private TextView textViewMaxZ;
    private TextView x;
    private TextView y;
    private TextView z;
    private TextView hz;

    /**
     * The library that allows to use the low pass linear acceleration filter.
     * La librairie qui permet d'utiliser le filtre "low pass linear acceleration"
     */
    private FSensor fSensor;

    /**
     * The observer that allows to perform action when the values of the acceleration changed
     * L'observer qui permet de faire une action quand les valeurs de l'accélération changent
     */
    private final SensorSubject.SensorObserver sensorObserver = new SensorSubject.SensorObserver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(float[] values) {
            Log.e("DEBUGG", "event : "+ Arrays.toString(values.clone()));
            float angleX = (float) (values[0]);
            float angleY = (float) (values[1]);
            float angleZ = (float) (values[2]);
            x.setText("X : "+angleX);
            y.setText("Y : "+angleY);
            z.setText("Z : "+angleZ);
            hz.setText("Hz : "+values[3]);

            if(Math.abs(angleX) > maxX){
                textViewMaxX.setText("Max X : "+angleX);
                maxX =  Math.abs(angleX);
            }
            if(Math.abs(angleY) > maxY){
                textViewMaxY.setText("Max Y : "+angleY);
                maxY = Math.abs(angleY);
            }
            if(Math.abs(angleZ) > maxZ){
                textViewMaxZ.setText("Max Z : "+angleZ);
                maxZ = Math.abs(angleZ);
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_computation);

        // set fSensor
        this.fSensor = new LowPassLinearAccelerationSensor(this);
        ((LowPassLinearAccelerationSensor) this.fSensor).setFSensorLpfLinearAccelerationTimeConstant((float) 0.18);
        ((LowPassLinearAccelerationSensor) this.fSensor).setSensorDelay(SensorManager.SENSOR_DELAY_UI);
        this.fSensor.register(sensorObserver);
        this.fSensor.start();

        // init graphical component
        this.x = findViewById(R.id.x);
        this.y = findViewById(R.id.y);
        this.z = findViewById(R.id.z);
        this.hz = findViewById(R.id.hz);
        this.textViewMaxX = findViewById(R.id.max_x);
        this.textViewMaxY = findViewById(R.id.max_y);
        this.textViewMaxZ = findViewById(R.id.max_z);

        // init max
        this.maxX = 0;
        this.maxY = 0;
        this.maxZ = 0;

        // On the click of the restart button, max values become 0.
        // Au clique sur le bouton restart, les valeurs max reviennent à 0.
        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(v -> {
            maxX = 0;
            maxY = 0;
            maxZ = 0;
            hz.setText("Hz : example*");
            textViewMaxX.setText("Max X : 0");
            textViewMaxY.setText("Max Y : 0");
            textViewMaxZ.setText("Max Z : 0");
            Log.e("EXAMPLE", "change");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fSensor = new LowPassLinearAccelerationSensor (this);
        fSensor.register(sensorObserver);
        fSensor.start();
    }

    @Override
    protected void onPause() {
        fSensor.unregister(sensorObserver);
        fSensor.stop();

        super.onPause();
    }
}