package com.example.goplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("ACTION_YES".equals(action)) {
            Toast.makeText(context, "You clicked YES!", Toast.LENGTH_SHORT).show();
            // Handle YES action here (e.g., update Firestore, start activity)
        } else if ("ACTION_NO".equals(action)) {
            Toast.makeText(context, "You clicked NO!", Toast.LENGTH_SHORT).show();
            // Handle NO action here (e.g., dismiss notification)
        }
    }
}
