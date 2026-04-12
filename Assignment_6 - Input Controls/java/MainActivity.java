package com.example.assignment_6_inputcontrols;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    RadioGroup rgGender;
    Spinner spinner;
    ToggleButton toggleBtn;
    CheckBox cbTerms;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        rgGender = findViewById(R.id.rgGender);
        spinner = findViewById(R.id.spinnerCountry);
        toggleBtn = findViewById(R.id.toggleBtn);
        cbTerms = findViewById(R.id.cbTerms);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Spinner Data
        String[] countries = {"India", "USA", "UK", "Canada"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> {

            if (!cbTerms.isChecked()) {
                Toast.makeText(this, "Accept Terms First", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString();

            int id = rgGender.getCheckedRadioButtonId();
            String gender = "Not Selected";
            if (id != -1) {
                RadioButton rb = findViewById(id);
                gender = rb.getText().toString();
            }

            String country = spinner.getSelectedItem().toString();
            String notification = toggleBtn.isChecked() ? "ON" : "OFF";

            String result = "Name: " + name +
                    "\nGender: " + gender +
                    "\nCountry: " + country +
                    "\nNotifications: " + notification;

            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        });
    }
}