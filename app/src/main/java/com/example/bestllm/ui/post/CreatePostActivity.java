package com.example.bestllm.ui.post;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.example.bestllm.R;
import com.example.bestllm.data.PostRepository;
import com.example.bestllm.models.Post;
import com.example.bestllm.utils.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatePostActivity extends AppCompatActivity {

    private TextInputEditText editTextTitle, editTextBody, editTextTags;
    private Button buttonCreatePost, buttonCancel;
    private ProgressBar progressBar;

    private PostRepository postRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        postRepository = new PostRepository();
        sessionManager = new SessionManager(this);

        setupToolbar();
        initViews();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Post");
        }
    }

    private void initViews() {
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextBody = findViewById(R.id.editTextBody);
        editTextTags = findViewById(R.id.editTextTags);
        buttonCreatePost = findViewById(R.id.buttonCreatePost);
        buttonCancel = findViewById(R.id.buttonCancel);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        buttonCreatePost.setOnClickListener(v -> handleCreatePost());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void handleCreatePost() {
        String title = editTextTitle.getText().toString().trim();
        String body = editTextBody.getText().toString().trim();
        String tagsInput = editTextTags.getText().toString().trim();

        // Validate inputs
        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            editTextTitle.requestFocus();
            return;
        }

        if (title.length() > 200) {
            editTextTitle.setError("Title must be 200 characters or less");
            editTextTitle.requestFocus();
            return;
        }

        if (body.isEmpty()) {
            editTextBody.setError("Body is required");
            editTextBody.requestFocus();
            return;
        }

        // Parse tags (comma-separated)
        List<String> tags = parseTags(tagsInput);

        // Get current user info
        String userId = sessionManager.getUserId();
        String userName = sessionManager.getUserName();

        if (userId == null || userName == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);

        postRepository.createPost(title, body, tags, userId, userName, new PostRepository.PostCallback() {
            @Override
            public void onSuccess(Post post) {
                showLoading(false);
                Toast.makeText(CreatePostActivity.this, "Post created successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(CreatePostActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> parseTags(String tagsInput) {
        if (tagsInput.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonCreatePost.setEnabled(!show);
        buttonCancel.setEnabled(!show);
        editTextTitle.setEnabled(!show);
        editTextBody.setEnabled(!show);
        editTextTags.setEnabled(!show);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

