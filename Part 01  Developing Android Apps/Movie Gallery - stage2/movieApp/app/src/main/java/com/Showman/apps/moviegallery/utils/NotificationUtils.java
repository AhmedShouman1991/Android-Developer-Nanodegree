package com.Showman.apps.moviegallery.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.Showman.apps.moviegallery.MainActivity;
import com.Showman.apps.moviegallery.R;


public class NotificationUtils {

    public static final int NEW_MOVIES_NOTIFICATION_ID = 50015;
    public static final String MOVIES_NOTIFICATION_CHANNEL_ID = "MoviesChannel";
    public static final int PENDING_INTENT_ID = 500500;

    public static void notifyUserOfNewWeather(Context context) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                   MOVIES_NOTIFICATION_CHANNEL_ID,
                    "Movies Gallery Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        Bitmap largeIcon = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_reel);

        String notificationTitle = context.getString(R.string.app_name);

        String notificationText = context.getString(R.string.notify_msg);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,MOVIES_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_reel)
                .setLargeIcon(largeIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, PENDING_INTENT_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);


        notificationManager.notify(NEW_MOVIES_NOTIFICATION_ID, notificationBuilder.build());

    }
}
