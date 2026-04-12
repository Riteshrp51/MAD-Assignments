package com.example.assignment7_ratingbarprogressbar; // Use your actual package name

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize components
        RatingBar ratingBar = findViewById(R.id.simpleRatingBar);
        TextView tvRatingValue = findViewById(R.id.tvRatingValue);
        ProgressBar progressBar = findViewById(R.id.simpleProgressBar);
        Button btnIncrement = findViewById(R.id.btnIncrement);

        // 1. RatingBar Change Listener
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar rb, float rating, boolean fromUser) {
                tvRatingValue.setText("Rating: " + rating);
                Toast.makeText(MainActivity.this, "Stars: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        // 2. ProgressBar Manual Update
        btnIncrement.setOnClickListener(v -> {
            int current = progressBar.getProgress();
            if (current < 100) {
                progressBar.setProgress(current + 10);
            } else {
                Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show();
                progressBar.setProgress(0); // Reset after 100
            }
        });
    }
}