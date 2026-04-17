package com.example.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText editFilename, editContent;
    private Button btnSave, btnLoad, btnDelete;
    private TextView txtStatus;

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

        // Initialize UI components
        editFilename = findViewById(R.id.editFilename);
        editContent = findViewById(R.id.editContent);
        btnSave = findViewById(R.id.btnSave);
        btnLoad = findViewById(R.id.btnLoad);
        btnDelete = findViewById(R.id.btnDelete);
        txtStatus = findViewById(R.id.txtStatus);

        // Set Click Listeners
        btnSave.setOnClickListener(v -> saveData());
        btnLoad.setOnClickListener(v -> loadData());
        btnDelete.setOnClickListener(v -> deleteData());
    }

    private void saveData() {
        String filename = editFilename.getText().toString().trim();
        String content = editContent.getText().toString();

        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileOutputStream fos = openFileOutput(filename, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
            txtStatus.setText("Status: File '" + filename + "' saved successfully!");
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            txtStatus.setText("Status: Error saving file!");
            Toast.makeText(this, "Error saving file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        String filename = editFilename.getText().toString().trim();

        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename to load", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileInputStream fis = openFileInput(filename);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            editContent.setText(sb.toString().trim());
            txtStatus.setText("Status: File '" + filename + "' loaded.");
            Toast.makeText(this, "Loaded successfully", Toast.LENGTH_SHORT).show();

        } catch (java.io.FileNotFoundException e) {
            txtStatus.setText("Status: File not found!");
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            txtStatus.setText("Status: Error loading file!");
            Toast.makeText(this, "Error loading file", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteData() {
        String filename = editFilename.getText().toString().trim();

        if (filename.isEmpty()) {
            Toast.makeText(this, "Please enter a filename to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean deleted = deleteFile(filename);
        if (deleted) {
            editFilename.setText("");
            editContent.setText("");
            txtStatus.setText("Status: File '" + filename + "' deleted.");
            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
        } else {
            txtStatus.setText("Status: Failed to delete or file doesn't exist.");
            Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
        }
    }
}