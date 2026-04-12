package com.example.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Proper Fragment Implementation: Check if savedInstanceState is null
        // to avoid loading the fragment again on configuration changes (like rotation).
        if (savedInstanceState == null) {
            replaceFragment(new FirstFragment(), false);
        }

        findViewById(R.id.btn1).setOnClickListener(v -> replaceFragment(new FirstFragment(), true));
        findViewById(R.id.btn2).setOnClickListener(v -> replaceFragment(new SecondFragment(), true));
    }


    /**
     * Proper way to replace fragments with transitions and backstack management.
     */
    private void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        
        // Add custom animations for a "premium" feel
        transaction.setCustomAnimations(
            android.R.anim.fade_in, 
            android.R.anim.fade_out,
            android.R.anim.slide_in_left, 
            android.R.anim.slide_out_right
        );
        
        transaction.replace(R.id.fragment_container, fragment);
        
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        
        // Use setReorderingAllowed(true) for better lifecycle handling and animations
        transaction.setReorderingAllowed(true);
        transaction.commit();
    }
}