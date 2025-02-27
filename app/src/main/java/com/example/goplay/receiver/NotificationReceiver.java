package com.example.goplay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.goplay.FBAuthHelper;
import com.example.goplay.FireVenueHelper;


public class NotificationReceiver extends BroadcastReceiver {

    private FireVenueHelper fireVenueHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        fireVenueHelper = new FireVenueHelper(null);
        Log.d("NotificationReceiver", "Received action: " + action);

        if ("com.example.goplay.ACTION_YES".equals(action)) {
            Toast.makeText(context, "You clicked YES!", Toast.LENGTH_SHORT).show();

        } else if ("com.example.goplay.ACTION_NO".equals(action)) {
            Toast.makeText(context, "You clicked NO!", Toast.LENGTH_SHORT).show();
        }
    }

}
