package baasj005.ivmd.moodmonitor.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import baasj005.ivmd.moodmonitor.MainActivity;
import baasj005.ivmd.moodmonitor.R;

/**
 * Created by Jesse on 8-12-2015.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.print("Fired background event");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.notification_icon);
        notificationBuilder.setContentTitle("Rate your mood!");
        notificationBuilder.setContentText("Click here to rate your mood!");
        Intent intentResult = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intentResult);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
    }
}
