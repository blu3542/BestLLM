package com.example.bestllm.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.models.User;
import com.example.bestllm.ui.home.HomeActivity;
import com.example.bestllm.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    private AuthRepository authRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            navigateToHome();
            return;
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.textViewRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        findViewById(R.id.textViewForgotPassword).setOnClickListener(v -> {
            showForgotPasswordDialog();
        });
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        showLoading(true);

        authRepository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                showLoading(false);
                sessionManager.createSession(user.getUserId(), user.getName(), user.getEmail());
                Toast.makeText(LoginActivity.this, "Welcome back, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonLogin.setEnabled(!show);
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        builder.setMessage("Enter your USC email to receive a password reset link");

        final EditText input = new EditText(this);
        input.setHint("email@usc.edu");
        input.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setPadding(50, 20, 50, 20);
        builder.setView(input);

        builder.setPositiveButton("Send Reset Link", (dialog, which) -> {
            String email = input.getText().toString().trim();
            if (!email.isEmpty()) {
                handlePasswordReset(email);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void handlePasswordReset(String email) {
        showLoading(true);
        authRepository.resetPassword(email, new AuthRepository.PasswordResetCallback() {
            @Override
            public void onSuccess() {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Password reset email sent! Check your inbox.",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}