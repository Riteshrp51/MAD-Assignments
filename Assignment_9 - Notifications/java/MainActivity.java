package com.example.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_HIGH_ID = "high_priority_channel";
    private static final String CHANNEL_DEFAULT_ID = "default_priority_channel";
    private static final int NOTIFICATION_ID_DEMO = 101;

    private NotificationManagerCompat notificationManager;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannels();

        setupButtons();
    }

    private void setupButtons() {
        Button btnPermission = findViewById(R.id.btnPermission);
        Button btnHighPriority = findViewById(R.id.btnHighPriority);
        Button btnDefaultPriority = findViewById(R.id.btnDefaultPriority);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnPermission.setOnClickListener(v -> checkAndRequestPermission());

        btnHighPriority.setOnClickListener(v -> showHighPriorityNotification());

        btnDefaultPriority.setOnClickListener(v -> showDefaultNotification());

        btnUpdate.setOnClickListener(v -> updateNotification());

        btnCancel.setOnClickListener(v -> cancelNotification());
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // High Priority Channel (Heads-up)
            NotificationChannel highChannel = new NotificationChannel(
                    CHANNEL_HIGH_ID,
                    "High Priority Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            highChannel.setDescription("Used for urgent alerts that pop up.");

            // Default Priority Channel
            NotificationChannel defaultChannel = new NotificationChannel(
                    CHANNEL_DEFAULT_ID,
                    "Default Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            defaultChannel.setDescription("Used for standard app notifications.");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(highChannel);
                manager.createNotificationChannel(defaultChannel);
            }
        }*
    }

    private void checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            } else {
                Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission not required for this Android version", Toast.LENGTH_SHORT).show();
        }
    }

    private void showHighPriorityNotification() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this, "Please grant permission first", Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_HIGH_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Urgent Action!")
                .setContentText("This is a high priority notification.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID_DEMO, builder.build());
    }

    private void showDefaultNotification() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this, "Please grant permission first", Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_DEFAULT_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("New Message")
                .setContentText("This is a regular notification in the drawer.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(202, builder.build());
    }

    private void updateNotification() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Toast.makeText(this, "Please grant permission first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Updating notification with ID 101 (The high priority one)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_HIGH_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Updated Alert!")
                .setContentText("The content of this notification has been changed.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID_DEMO, builder.build());
        Toast.makeText(this, "Notification Updated", Toast.LENGTH_SHORT).show();
    }

    private void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID_DEMO);
        Toast.makeText(this, "Notification ID 101 Cancelled", Toast.LENGTH_SHORT).show();
    }
}