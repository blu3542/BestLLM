package com.example.bestllm.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.models.User;
import com.example.bestllm.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewReputation;
    private ProgressBar progressBar;
    private Button buttonBack;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
        loadProfile();
    }

    private void initViews() {
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewReputation = findViewById(R.id.textViewReputation);
        progressBar = findViewById(R.id.progressBar);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> finish());
    }

    private void loadProfile() {
        String userId = sessionManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);

        authRepository.getUserProfile(userId, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                displayProfile(user);
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProfile(User user) {
        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());
        textViewReputation.setText(String.valueOf(user.getReputationScore()));
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        textViewName.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewEmail.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewReputation.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}