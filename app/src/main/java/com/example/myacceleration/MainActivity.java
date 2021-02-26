package com.example.myacceleration;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

/**
 * Main activity of the application which displays acceleration information
 * Activité principal de l'application qui permet d'afficher les informations sur l'accélération.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // TODO compute the hz

    private float maxX;
    private float maxY;
    private float maxZ;
    private TextView textViewMaxX;
    private TextView textViewMaxY;
    private TextView textViewMaxZ;
    private TextView x;
    private TextView y;
    private TextView z;
    private TextView hz;
    private Sensor accelerometer;
    private float[] mGravity;
    private SensorManager mSensorManager;
    private Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Set the sensor
        this.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mSensorManager.registerListener(this, this.accelerometer, SensorManager.SENSOR_DELAY_UI);

        // On the click of the restart button, max values become 0.
        // Au clique sur le bouton restart, les valeurs max reviennent à 0.
        this.restart = findViewById(R.id.restart);
        this.restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxX = 0;
                maxY = 0;
                maxZ = 0;
                textViewMaxX.setText("Max X : 0");
                textViewMaxY.setText("Max Y : 0");
                textViewMaxZ.setText("Max Z : 0");
            }
        });
    }

    /**
     * When the sensor receives new value it goes here do the method.
     * Quand le capteur recoit une nouvelle valeur, on vient ici faire la méthode.
     * @param event SensorEvent which contains the new values
     *              SensorEvent qui contient les nouvelles valeurs.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.e("DEBUGG", "event : "+ Arrays.toString(event.values.clone()));
        mGravity = event.values;
        float angleX = (float) (getDeviceAngles()[0]*9.80665);
        float angleY = (float) (getDeviceAngles()[1]*9.80665);
        float angleZ = (float) (getDeviceAngles()[2]*9.80665);
        x.setText("X : "+Math.abs(angleX));
        y.setText("Y : "+Math.abs(angleY));
        z.setText("Z : "+Math.abs(angleZ));

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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * This method allows to find the acceleration for each acceleration.
     * Cette méthode permet de trouver l'accélération pour chaque axe.
     * @return An array which contains the information of the acceleration for each axes.
     *         Un tableau contenant les informations de l'accélération pour chaque axe.
     */
    public float[] getDeviceAngles() {
        float[] g = mGravity.clone();
        //double normOfG = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);
        // Normalize the accelerometer vector
        //g[0] = (float) (g[0] / normOfG);
        //g[1] = (float) (g[1] / normOfG);
        //g[2] = (float) (g[2] / normOfG);
        return g;
    }



}