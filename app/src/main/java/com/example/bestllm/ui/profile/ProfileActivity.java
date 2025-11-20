package com.example.bestllm.ui.profile;

import android.content.Intent;
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

    private TextView textViewName, textViewEmail, textViewDepartment, textViewSchool, textViewReputation;
    private ProgressBar progressBar;
    private Button buttonBack, buttonEditProfile;

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
        textViewDepartment = findViewById(R.id.textViewDepartment);
        textViewSchool = findViewById(R.id.textViewSchool);
        textViewReputation = findViewById(R.id.textViewReputation);
        progressBar = findViewById(R.id.progressBar);
        buttonBack = findViewById(R.id.buttonBack);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> finish());
        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfile();
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

        String department = user.getDepartment();
        textViewDepartment.setText(department != null && !department.isEmpty() ? department : "Not set");

        String school = user.getSchool();
        textViewSchool.setText(school != null && !school.isEmpty() ? school : "Not set");

        textViewReputation.setText(String.valueOf(user.getReputationScore()));
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        textViewName.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewEmail.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewDepartment.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewSchool.setVisibility(show ? View.GONE : View.VISIBLE);
        textViewReputation.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}