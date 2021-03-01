package com.example.myacceleration;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Main activity of the application which displays acceleration information
 * Activité principal de l'application qui permet d'afficher les informations sur l'accélération.
 */
public class WithoutComputation extends AppCompatActivity implements SensorEventListener {

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
     * The sensor
     */
    private Sensor accelerometer;

    /**
     * Contains the accelerometer' values
     */
    private float[] mGravity;

    /**
     * Manager
     */
    private SensorManager mSensorManager;

    /**
     * Button that set to 0 the values
     */
    private Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_without_computation);

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
                hz.setText("Hz :");
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
        mGravity = event.values;
        float angleX = (float) (getDeviceAngles()[0]);
        float angleY = (float) (getDeviceAngles()[1]);
        float angleZ = (float) (getDeviceAngles()[2]);
        x.setText("X : "+angleX);
        y.setText("Y : "+angleY);
        z.setText("Z : "+angleZ);

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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * This method allows to find the acceleration for each acceleration.
     * Cette méthode permet de trouver l'accélération pour chaque axe.
     * @return An array which contains the information of the acceleration for each axes.
     *         Un tableau contenant les informations de l'accélération pour chaque axe.
     */
    public float[] getDeviceAngles() {
        return mGravity.clone();
    }



}