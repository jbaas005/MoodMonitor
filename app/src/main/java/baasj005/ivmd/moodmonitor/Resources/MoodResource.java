package baasj005.ivmd.moodmonitor.Resources;

import baasj005.ivmd.moodmonitor.Models.Mood;

/**
 * Created by Jesse on 8-12-2015.
 */
public class MoodResource {

    public MoodResource(){

    }

    public void doRestPost(Mood mood){
        System.out.println("Mood level: " + mood.getMoodLevel());
        System.out.println("Temperature: " + mood.getTemperature());
        System.out.println("Longitude: " + mood.getLongitude());
        System.out.println("Latitude: " + mood.getLatitude());
        System.out.println("Speed: " + mood.getSpeed());
        System.out.println("Acceleration: " + mood.getAcceleration());
        System.out.println("Date and time: " + mood.getMoodDate().toString());
    }

    public void doRestGet(){

    }
}
