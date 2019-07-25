package com.jeeb.mycommunity.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.jeeb.mycommunity.authintication.LoginActivity;

import java.lang.ref.WeakReference;

public class MyNotificationService {
    private String TAG = "MyNotificationService";
    private static MyNotificationService newInstance;
    private WeakReference<Context> mWeakRefContext;
    private MyNotificationService(WeakReference<Context> weakRefContext) {
        mWeakRefContext = weakRefContext;
    }
    public static synchronized MyNotificationService getInstance(WeakReference<Context> context){
        if (newInstance == null){
            newInstance = new MyNotificationService(context);
        }
        return newInstance;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void displayNotifcation(String title, String message, int icon){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mWeakRefContext.get(),"channel_id");
        builder.setSmallIcon(icon);
        builder.setContentText(message);
        builder.setContentTitle(title);
        Intent intent = new Intent(mWeakRefContext.get(), LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mWeakRefContext.get(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) mWeakRefContext.get().getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager!= null){
            manager.notify(1,builder.build());
        }
    }
}
