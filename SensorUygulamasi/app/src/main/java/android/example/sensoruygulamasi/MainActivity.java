package android.example.sensoruygulamasi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView lightTextView, accTextView, infoTextView;
    SensorManager sensorManager;
    Sensor light_sensor;
    Sensor acc_sensor;
    boolean light_flag; // true disarda false cepte
    boolean acc_flag;  // true hareketli false hareketsiz
    double acc_previous, acc_current, acc_change;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("com.example.EXAMPLE_ACTION");
        registerReceiver(broadcastReceiver, filter);

        infoTextView = (TextView) findViewById(R.id.infoTextView);
        lightTextView = (TextView)  findViewById(R.id.lightTextView);
        accTextView = (TextView)  findViewById(R.id.accTextView);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        light_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT){
            float lux = sensorEvent.values[0];
            lightTextView.setText("Lux Value: " + lux );
            if(lux > 20)
                light_flag = true;
            else
                light_flag = false;
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acc_current = Math.sqrt(x*x + y*y + z*z);
            acc_change = Math.abs(acc_current - acc_previous);
            accTextView.setText("Total change in acc: "+ acc_change);
            acc_previous = acc_current;

            if(acc_change != 0)
                acc_flag = true;
            else
                acc_flag = false;

        }

        phoneState(light_flag, acc_flag);

    }

    public void phoneState(boolean light_flag,boolean acc_flag){
        if(light_flag == true && acc_flag == false){

            infoTextView.setText("Volume Down " + light_flag + acc_flag);
            sendBroadcast("VolumeDown");

        }

        else if(light_flag == false && acc_flag == true){
            infoTextView.setText("Volume Up " + light_flag + acc_flag);
            sendBroadcast("VolumeUp");
        }

        else{
            infoTextView.setText("Do Nothing " + light_flag + acc_flag);
            sendBroadcast("DoNothing");
        }
    }

    private void sendBroadcast(String string){
        Intent intent = new Intent("com.example.EXAMPLE_ACTION");
        intent.putExtra("com.example.EXTRA_TEXT", string);
        sendBroadcast(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receivedText = intent.getStringExtra("com.example.EXTRA_TEXT");
            infoTextView.setText(receivedText);
        }
    };




    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}