package com.example.notifcustomlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    Button btn_notif ,btn_simple_notif,btn_expandable_notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_notif = findViewById(R.id.btn_notif);
        btn_simple_notif = findViewById(R.id.btn_simple_notif);
        btn_expandable_notif = findViewById(R.id.btn_expandable_notif);
        btn_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomSmallNotification();
            }
        });
        btn_simple_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimpleNotif();

            }
        });

        btn_expandable_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExpandableNotif();
            }
        });

    }



    private void showSimpleNotif() {

        String channelId = "channel_id";
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                this.getResources(),
                                R.drawable.ic_launcher_background))
                        .setContentTitle("title")
                        .setContentText("content")
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000})
                        .setSound(uri);


        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, notificationBuilder.build());
        } else {

            NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
            notificationManager1.notify(1, notificationBuilder.build());

        }

    }

    private void showCustomSmallNotification() {


        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_small);
        String strtitle ="customnotificationtitle";
        String strtext = "customnotificationtext";


        Intent SecondIntent = new Intent(getApplicationContext(), SecondActivity.class);
        SecondIntent.putExtra("REQUEST_CODE", 1);
        SecondIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent imgright = PendingIntent.getActivity(getApplicationContext(), 0, SecondIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent dismissIntent = new Intent(getApplicationContext(), ThirdActivity.class);
        dismissIntent.putExtra("REQUEST_CODE", 2);
        dismissIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent imgleft = PendingIntent.getActivity(getApplicationContext(), 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.imgright, imgright);
        remoteViews.setOnClickPendingIntent(R.id.imgleft, imgleft);





        String channelId = "channel_id";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this ,channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setTicker("customnotificationticker")
                .setAutoCancel(true)
                .setContent(remoteViews);

        remoteViews.setImageViewResource(R.id.imgleft,R.drawable.ic_launcher_background);
        remoteViews.setImageViewResource(R.id.imgright,R.drawable.ic_launcher_background);
        remoteViews.setTextViewText(R.id.title,"customnotificationtitle");
        remoteViews.setTextViewText(R.id.text,"customnotificationtext");
        // Create Notification Manager
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
            notificationManager1.notify(1, notificationBuilder.build());
        }


    }


    private void showExpandableNotif() {


        String channelId = "channel_id";
        RemoteViews remoteCollapsedViews = new RemoteViews(getPackageName(), R.layout.custom_normal);
        RemoteViews remoteExpandedViews = new RemoteViews(getPackageName(), R.layout.custom_expanded);

        //start this(MainActivity) on by Tapping notification
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent mainPIntent = PendingIntent.getActivity(this, 0,
                mainIntent, PendingIntent.FLAG_ONE_SHOT);

        //creating notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        //start intent on notification tap (SecondActivity)
        notificationBuilder.setContentIntent(mainPIntent);
        //custom style
        notificationBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle()); // show time in the right
        notificationBuilder.setCustomContentView(remoteCollapsedViews);
        notificationBuilder.setCustomBigContentView(remoteExpandedViews);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManagerCompat.createNotificationChannel(channel);
            notificationManagerCompat.notify(1, notificationBuilder.build());
        } else {
            NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
            notificationManager1.notify(1, notificationBuilder.build());
        }
    }

}