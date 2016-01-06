package baasj005.ivmd.moodmonitor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Date;

import baasj005.ivmd.moodmonitor.models.Mood;
import baasj005.ivmd.moodmonitor.tasks.MoodPostTask;
import baasj005.ivmd.moodmonitor.notification.NotificationReceiver;
import okhttp3.Response;

public class RatingActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor thermometer;
    private LocationManager locationManager;
    private float temperature;
    private RatingBar moodRatingBar;
    private AsyncTask<Mood, Void, Response> task;
    private final static long NOTIFICATION_INTERVAL = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        setLocationListener();
        thermometer = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        sensorManager.registerListener(this, thermometer, SensorManager.SENSOR_DELAY_NORMAL);
        setScheduledNotificationTask();

        moodRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        Button moodButton = (Button) findViewById(R.id.moodButton);

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task = new MoodPostTask().execute(getMoodData());
                try {
                    if (task.get().isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Your mood was sent successfully", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(RatingActivity.this, MainActivity.class);
                startActivity(intent);
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
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, thermometer, SensorManager.SENSOR_DELAY_NORMAL);
        setLocationListener();
    }

    private void setScheduledNotificationTask() {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), NOTIFICATION_INTERVAL, pendingIntent);
    }


    private void setLocationListener() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, Criteria.ACCURACY_FINE, this);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private Location getLastLocation() {
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(getApplicationContext(), "Enable GPS", Toast.LENGTH_LONG).show();
            return null;
        }
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }

    private Mood getMoodData() {
        Mood mood = new Mood();
        Location location = getLastLocation();
        if (location != null) {
            mood.setLatitude(location.getLatitude());
            mood.setLongitude(location.getLongitude());
            mood.setSpeed(location.getSpeed());
        }
        mood.setTemperature(temperature);
        mood.setMoodLevel(moodRatingBar.getRating());
        mood.setMoodDate(new Date().getTime());
        return mood;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE) {
            temperature = event.values[0];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
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
