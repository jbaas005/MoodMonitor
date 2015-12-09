package baasj005.ivmd.moodmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import java.util.Date;

import baasj005.ivmd.moodmonitor.Models.Mood;
import baasj005.ivmd.moodmonitor.Resources.MoodResource;
import baasj005.ivmd.moodmonitor.ScheduledTasks.NotificationReceiver;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private float temperature;
    private double longitude;
    private double latitude;
    private double speed;
    private Button moodButton;
    private RatingBar moodRatingBar;
    private MoodResource moodResource;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private final int NOTIFICATION_INTERVAL = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            moodResource = new MoodResource();
            setLocationListener();
            setTemperatureListener();
            setScheduledNotificationTask();
            moodRatingBar = (RatingBar) findViewById(R.id.ratingBar);
            moodButton = (Button) findViewById(R.id.moodButton);
            moodButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMoodData();
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){
        try {
            super.onPause();
            sensorManager.unregisterListener(this);
            locationManager.removeUpdates(this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        setTemperatureListener();
        setLocationListener();
    }

    private void setScheduledNotificationTask(){
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), NOTIFICATION_INTERVAL, pendingIntent);
    }


    private void setTemperatureListener(){
        Sensor thermometer = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(this, thermometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setLocationListener(){
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, Criteria.ACCURACY_FINE, this);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }

    private void sendMoodData(){
        Mood mood = new Mood();
        mood.setLatitude(latitude);
        mood.setLongitude(longitude);
        mood.setTemperature(temperature);
        mood.setSpeed(speed);
        mood.setMoodLevel(moodRatingBar.getNumStars());
        mood.setMoodDate(new Date());
        moodResource.doRestPost(mood);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            temperature = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        speed = location.getSpeed();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
