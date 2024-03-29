package com.example.testnoti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn_notify,btnUpdate,btnCancel;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 0;

    private static final String ACTION_UPDATED_NOTIFICATION ="com.example.testnoti.ACTION_UPDATED_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_notify = findViewById(R.id.notify);
        btnUpdate = findViewById(R.id.update);
        btnCancel = findViewById(R.id.cancel);
        createNotificationChannel();
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATED_NOTIFICATION));
        notficationButtonState(true,false,false);
        btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNotification();
            }
        });
    }

    public void updateNotification(){
        notficationButtonState(false,false,true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder builder = getNotificationBuilder();
       /* builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .setBigContentTitle("Notification Updated !"));*/
        builder.setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Here is the first one")
                        .addLine("This is the second one")
                        .addLine("Yay Last one")
                        .setBigContentTitle("LEARN MORE")
                        .setSummaryText("+1 more"));
        notificationManager.notify(NOTIFICATION_ID,builder.build());

    }

    public void cancelNotification(){
        notficationButtonState(true,false,false);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void sendNotification(){
        Intent updateIntent = new Intent(ACTION_UPDATED_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notiBuilder = getNotificationBuilder();
        notiBuilder.addAction(R.drawable.ic_update,"Update Notification",updatePendingIntent);
        notificationManager.notify(NOTIFICATION_ID,notiBuilder.build());
        notficationButtonState(false,true,true);
    }

    public void createNotificationChannel(){
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,"Mascot Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void notficationButtonState(Boolean isNotifiedEnabled,
                                        Boolean isUpdatedEnabled,
                                        Boolean isCancelEnabled)
    {
        btn_notify.setEnabled(isNotifiedEnabled);
        btnUpdate.setEnabled(isUpdatedEnabled);
        btnCancel.setEnabled(isCancelEnabled);
    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notiPendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notify")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentIntent(notiPendingIntent)
                .setAutoCancel(true);
        return notifyBuilder;
    }

    public class NotificationReceiver extends BroadcastReceiver{

        public NotificationReceiver(){
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
