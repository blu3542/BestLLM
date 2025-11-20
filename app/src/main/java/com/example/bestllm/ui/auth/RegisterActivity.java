package com.example.bestllm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.models.User;
import com.example.bestllm.ui.profile.EditProfileActivity;
import com.example.bestllm.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextEmail, editStudentID, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private ProgressBar progressBar;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editStudentID = findViewById(R.id.editStudentId);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.textViewLogin).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void setupListeners() {
        buttonRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String studentId = editStudentID.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!studentId.matches("\\d{10}")) {
            Toast.makeText(this, "Student ID must be exactly 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        authRepository.register(name, email, studentId, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                sessionManager.createSession(user.getUserId(), user.getName(), user.getEmail());
                Toast.makeText(RegisterActivity.this, "Welcome to BestLLM, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                navigateToProfileSetup();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonRegister.setEnabled(!show);
    }

    private void navigateToProfileSetup() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("SETUP_MODE", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}