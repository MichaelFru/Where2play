package com.example.goplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireVenueHelper;
import com.example.goplay.controller.NotificationWorker;

import java.util.concurrent.TimeUnit;


public class NotificationReceiver extends BroadcastReceiver {

    private FireVenueHelper fireVenueHelper;
    private Context context;

    public NotificationReceiver(Context context) {
        this.context=context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        fireVenueHelper = new FireVenueHelper(null);
        Log.d("NotificationReceiver", "Received action: " + action);

        if ("com.example.goplay.ACTION_YES".equals(action)) {
            Toast.makeText(context, "Good luck!, another reminder set in an hour", Toast.LENGTH_SHORT).show();
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build();
            WorkManager.getInstance(context).enqueue(workRequest);

        } else if ("com.example.goplay.ACTION_NO".equals(action)) {
            Toast.makeText(context, "You clicked NO!", Toast.LENGTH_SHORT).show();

        }
    }

}
