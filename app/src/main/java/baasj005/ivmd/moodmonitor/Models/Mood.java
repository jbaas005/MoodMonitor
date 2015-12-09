package baasj005.ivmd.moodmonitor.Models;

import java.util.Date;

/**
 * Created by Jesse on 8-12-2015.
 */
public class Mood {
    private int moodLevel;
    private float temperature;
    private double longitude;
    private double latitude;
    private double speed;
    private Date moodDate;

    public Mood(){

    }

    public Mood(int moodLevel, float temperature, double longitude, double latitude, double speed) {
        this.moodLevel = moodLevel;
        this.temperature = temperature;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    public void setMoodLevel(int moodLevel) {
        this.moodLevel = moodLevel;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMoodDate(Date moodDate){
        this.moodDate = moodDate;
    }

    public Date getMoodDate(){
        return this.moodDate;
    }
}
