package com.example.goplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireUserHelper;
import com.example.goplay.FireVenueHelper;
import com.example.goplay.controller.NotificationWorker;
import com.example.goplay.FireUserHelper;

import java.util.concurrent.TimeUnit;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FireVenueHelper fireVenueHelper = new FireVenueHelper(null);
        FireUserHelper fireUserHelper = new FireUserHelper(null);

        String action = intent.getAction();
        Log.d("NotificationReceiver", "Received action: " + action);

        if (NotificationWorker.ACTION_YES.equals(action)) {
            Toast.makeText(context, "Good luck! Another reminder set in an hour", Toast.LENGTH_SHORT).show();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build();
            WorkManager.getInstance(context).enqueue(workRequest);

        } else if (NotificationWorker.ACTION_NO.equals(action)) {

                Toast.makeText(context, "Leaving venue!", Toast.LENGTH_SHORT).show();

                FireUserHelper.getCurrentVenueId(new FireUserHelper.FBVenueIdCallback() {
                    @Override
                    public void onVenueIdReceived(String venueId) {
                        if (venueId != null) {
                            fireVenueHelper.decVenuePlayers(venueId, documentSnapshot -> {

                            });
                            fireUserHelper.removePlayingVenue(FBAuthHelper.getCurrentUser().getUid());
                        } else {
                            Log.d("NotificationReceiver", "No current venue found.");
                        }
                    }
                });

            }
    }
}
