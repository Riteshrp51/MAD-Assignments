package com.example.backgroundimagechange;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ConstraintLayout mainLayout = findViewById(R.id.main);
        Button btnImg1 = findViewById(R.id.btn_img1);
        Button btnImg2 = findViewById(R.id.btn_img2);
        Button btnImg3 = findViewById(R.id.btn_img3);

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnImg1.setOnClickListener(v -> mainLayout.setBackgroundResource(R.drawable.dilbahar));
        btnImg2.setOnClickListener(v -> mainLayout.setBackgroundResource(R.drawable.dilbahar1));
        btnImg3.setOnClickListener(v -> mainLayout.setBackgroundResource(R.drawable.dilbahar5));
    }
}