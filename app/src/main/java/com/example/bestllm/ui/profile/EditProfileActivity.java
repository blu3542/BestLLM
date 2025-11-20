package com.example.bestllm.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.models.User;
import com.example.bestllm.ui.home.HomeActivity;
import com.example.bestllm.utils.SessionManager;
import com.example.bestllm.utils.Validators;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText editTextName, editTextEmail, editTextStudentId;
    private TextInputEditText editTextDepartment, editTextSchool, editTextBio, editTextBirthDate, editTextPassword;
    private TextInputLayout layoutName, layoutDepartment, layoutSchool, layoutPassword;
    private Button buttonSaveProfile, buttonSkip;
    private ProgressBar progressBar;
    private TextView textViewTitle, textViewSubtitle;

    private AuthRepository authRepository;
    private SessionManager sessionManager;
    private User currentUser;
    private boolean isSetupMode = false;
    private Calendar selectedBirthDate;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        isSetupMode = getIntent().getBooleanExtra("SETUP_MODE", false);

        initViews();
        loadUserProfile();
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewSubtitle = findViewById(R.id.textViewSubtitle);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        editTextSchool = findViewById(R.id.editTextSchool);
        editTextBio = findViewById(R.id.editTextBio);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextPassword = findViewById(R.id.editTextPassword);

        layoutName = findViewById(R.id.layoutName);
        layoutDepartment = findViewById(R.id.layoutDepartment);
        layoutSchool = findViewById(R.id.layoutSchool);
        layoutPassword = findViewById(R.id.layoutPassword);

        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);
        buttonSkip = findViewById(R.id.buttonSkip);
        progressBar = findViewById(R.id.progressBar);

        if (isSetupMode) {
            // Setup mode: allow editing name, department, school; hide password field
            textViewTitle.setText("Profile Setup");
            textViewSubtitle.setText("Complete your profile information");
            buttonSaveProfile.setText("Create Profile");
            buttonSkip.setVisibility(View.VISIBLE);
            buttonSkip.setOnClickListener(v -> navigateToHome());
            layoutPassword.setVisibility(View.GONE);
        } else {
            // Edit mode: lock name, department, school; show password field
            textViewTitle.setText("Edit Profile");
            textViewSubtitle.setText("Update your bio, birth date, or password");
            buttonSaveProfile.setText("Save Changes");
            editTextName.setEnabled(false);
            editTextDepartment.setEnabled(false);
            editTextSchool.setEnabled(false);
            layoutName.setHint("Name (read-only)");
            layoutDepartment.setHint("Department (read-only)");
            layoutSchool.setHint("School (read-only)");
            layoutPassword.setVisibility(View.VISIBLE);
        }

        buttonSaveProfile.setOnClickListener(v -> handleSaveProfile());
        editTextBirthDate.setOnClickListener(v -> showDatePicker());
    }

    private void loadUserProfile() {
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
                currentUser = user;
                populateFields();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void populateFields() {
        if (currentUser != null) {
            editTextName.setText(currentUser.getName());
            editTextEmail.setText(currentUser.getEmail());
            editTextStudentId.setText(currentUser.getStudentId());

            if (currentUser.getDepartment() != null && !currentUser.getDepartment().isEmpty()) {
                editTextDepartment.setText(currentUser.getDepartment());
            }

            if (currentUser.getSchool() != null && !currentUser.getSchool().isEmpty()) {
                editTextSchool.setText(currentUser.getSchool());
            }

            if (currentUser.getBio() != null && !currentUser.getBio().isEmpty()) {
                editTextBio.setText(currentUser.getBio());
            }

            if (currentUser.getBirthDate() != null) {
                Date birthDate = currentUser.getBirthDate().toDate();
                editTextBirthDate.setText(dateFormat.format(birthDate));

                selectedBirthDate = Calendar.getInstance();
                selectedBirthDate.setTime(birthDate);
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = selectedBirthDate != null ? selectedBirthDate : Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedBirthDate = Calendar.getInstance();
                    selectedBirthDate.set(year, month, dayOfMonth);
                    editTextBirthDate.setText(dateFormat.format(selectedBirthDate.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void handleSaveProfile() {
        String bio = editTextBio.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (isSetupMode) {
            // In setup mode, validate and save name, department, school
            String name = editTextName.getText().toString().trim();
            String department = editTextDepartment.getText().toString().trim();
            String school = editTextSchool.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (department.isEmpty()) {
                Toast.makeText(this, "Department cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (school.isEmpty()) {
                Toast.makeText(this, "School cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setName(name);
            currentUser.setDepartment(department);
            currentUser.setSchool(school);
        }

        // Bio and birth date can always be updated
        currentUser.setBio(bio);

        if (selectedBirthDate != null) {
            currentUser.setBirthDate(new Timestamp(selectedBirthDate.getTime()));
        }

        // Update password if provided (only in edit mode)
        if (!isSetupMode && !password.isEmpty()) {
            if (!Validators.isValidPassword(password)) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }
            updatePasswordAndProfile(password);
        } else {
            updateProfile();
        }
    }

    private void updatePasswordAndProfile(String newPassword) {
        showLoading(true);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            firebaseUser.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateProfile();
                        } else {
                            showLoading(false);
                            String error = task.getException() != null ?
                                    task.getException().getMessage() : "Failed to update password";
                            Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            showLoading(false);
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        showLoading(true);
        authRepository.updateUserProfile(currentUser, new AuthRepository.ProfileUpdateCallback() {
            @Override
            public void onSuccess() {
                showLoading(false);
                sessionManager.createSession(currentUser.getUserId(), currentUser.getName(), currentUser.getEmail());

                String message = isSetupMode ? "Profile created successfully!" : "Profile updated successfully!";
                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();

                if (isSetupMode) {
                    navigateToHome();
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonSaveProfile.setEnabled(!show);
        if (isSetupMode) {
            buttonSkip.setEnabled(!show);
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
