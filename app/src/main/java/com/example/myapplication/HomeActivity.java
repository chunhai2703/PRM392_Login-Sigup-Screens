package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get the username from the intent
        String username = getIntent().getStringExtra("USERNAME");

        // Initialize the TextView
        welcomeTextView = findViewById(R.id.welcome_text);

        // Set the username to the TextView
        welcomeTextView.setText("Welcome, " + username);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Clear the text when the app is minimized
        if (welcomeTextView != null) {
            welcomeTextView.setText("");
        }
    }
}
