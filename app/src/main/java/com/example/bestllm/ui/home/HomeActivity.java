package com.example.bestllm.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.ui.auth.LoginActivity;
import com.example.bestllm.ui.profile.ProfileActivity;
import com.example.bestllm.utils.SessionManager;

public class HomeActivity extends AppCompatActivity {

    private TextView textViewWelcome;
    private Button buttonProfile, buttonLogout;

    private SessionManager sessionManager;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        authRepository = new AuthRepository();

        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        initViews();
        setupListeners();
        displayWelcome();
    }

    private void initViews() {
        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonProfile = findViewById(R.id.buttonProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
    }

    private void setupListeners() {
        buttonProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> handleLogout());
    }

    private void displayWelcome() {
        String userName = sessionManager.getUserName();
        if (userName != null) {
            textViewWelcome.setText("Welcome, " + userName + "!");
        }
    }

    private void handleLogout() {
        authRepository.logout();
        sessionManager.logout();
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}