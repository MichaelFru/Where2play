import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.goplay.R;
import


public class NotificationWorker extends Worker {
    private static final String CHANNEL_ID = "play_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    public static final String ACTION_YES = "com.example.goplay.ACTION_YES";
    public static final String ACTION_NO = "com.example.goplay.ACTION_NO";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        showNotification();
        return Result.success();
    }

    private void showNotification() {
        Context context = getApplicationContext();
        createNotificationChannel(context);

        // **Intent for "Yes" button**
        Intent yesIntent = new Intent(context, NotificationActionReceiver.class);
        yesIntent.setAction(ACTION_YES);
        PendingIntent yesPendingIntent = PendingIntent.getBroadcast(context, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // **Intent for "No" button**
        Intent noIntent = new Intent(context, NotificationActionReceiver.class);
        noIntent.setAction(ACTION_NO);
        PendingIntent noPendingIntent = PendingIntent.getBroadcast(context, 1, noIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // **Build the notification**
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Join the Game?")
                .setContentText("A game is happening nearby. Do you want to join?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_yes, "Yes", yesPendingIntent)  // Yes button
                .addAction(R.drawable.ic_no, "No", noPendingIntent);    // No button

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Play Notifications", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
