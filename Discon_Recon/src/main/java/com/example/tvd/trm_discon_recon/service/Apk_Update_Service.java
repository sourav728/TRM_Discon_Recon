package com.example.tvd.trm_discon_recon.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.tvd.trm_discon_recon.invoke.ApkNotification;
import com.example.tvd.trm_discon_recon.invoke.ChangeDateNotification;
import com.example.tvd.trm_discon_recon.invoke.CrashNotification;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

public class Apk_Update_Service extends Service {
    FunctionCall functionCall;

    public Apk_Update_Service() {
    }

    @Override
    public void onCreate() {
        functionCall = new FunctionCall();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start_version_check();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start_check_date();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start_crash_report();
                    }
                },1000*3);
            }
        }, 1000 * 3);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stop_version_check();
        stop_check_date();
        //stop_crash_report();
    }

    private void start_version_check() {
        functionCall.logStatus("Version_receiver Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ApkNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Version_receiver Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
        } else functionCall.logStatus("Version_receiver Already running..");
    }

    private void stop_version_check() {
        functionCall.logStatus("Version_receiver Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ApkNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Version_receiver Stopping..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        } else functionCall.logStatus("Version_receiver is not yet started..");
    }

    public void start_check_date()
    {
        functionCall.logStatus("Date Comparison Started..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ChangeDateNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Date Checking Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
        } else functionCall.logStatus("Date Checking is Already running..");

    }

    public void stop_check_date()
    {
        functionCall.logStatus("Date Comparison Started..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ChangeDateNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Date Checking Stopping..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        } else functionCall.logStatus("Date Checking is not yet started..");

    }
    public void start_crash_report()
    {

        functionCall.logStatus("Crash Report Started..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), CrashNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Crash Report Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
        } else functionCall.logStatus("Crash Report is Already running..");
    }

   /* public void stop_crash_report()
    {

            functionCall.logStatus("Crash Report Stopped..");
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), CrashNotification.class);
            boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
            if (!alarmRunning) {
                functionCall.logStatus("Crash Report Stopped..");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }
            } else functionCall.logStatus("Crash Report is Already Stopped..");
    }*/
}
