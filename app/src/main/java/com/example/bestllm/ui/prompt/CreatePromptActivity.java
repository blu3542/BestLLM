package com.example.bestllm.ui.prompt;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bestllm.R;
import com.example.bestllm.data.PromptRepository;
import com.example.bestllm.models.Prompt;
import com.example.bestllm.utils.SessionManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePromptActivity extends AppCompatActivity {

    private TextInputEditText editTextTitle, editTextPrompt, editTextTags;
    private Button buttonCreate, buttonCancel;
    private CircularProgressIndicator progress;

    private final PromptRepository promptRepo = new PromptRepository();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_prompt);

        session = new SessionManager(this);

        setupToolbar();
        initViews();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Share Prompt");
        }
    }

    private void initViews() {
        editTextTitle  = findViewById(R.id.editTextTitle);
        editTextPrompt = findViewById(R.id.editTextPrompt);
        editTextTags   = findViewById(R.id.editTextTags);
        buttonCreate   = findViewById(R.id.buttonCreatePrompt);
        buttonCancel   = findViewById(R.id.buttonCancel);
        progress       = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        buttonCreate.setOnClickListener(v -> handleCreate());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void handleCreate() {
        String title = safe(editTextTitle);
        String text = safe(editTextPrompt);
        String tagsRaw = safe(editTextTags);

        if (title.isEmpty()) {
            editTextTitle.setError("Title is required");
            editTextTitle.requestFocus();
            return;
        }
        if (title.length() > 100) {
            editTextTitle.setError("Title must be 100 characters or less");
            editTextTitle.requestFocus();
            return;
        }
        if (text.isEmpty()) {
            editTextPrompt.setError("Prompt text is required");
            editTextPrompt.requestFocus();
            return;
        }
        if (text.length() > 5000) {
            editTextPrompt.setError("Prompt must be 5000 characters or less");
            editTextPrompt.requestFocus();
            return;
        }

        List<String> tags = parseTags(tagsRaw);
        if (tags.isEmpty()) {
            editTextTags.setError("At least one tag is required");
            editTextTags.requestFocus();
            return;
        }

        String uid = session.getUserId();
        String name = session.getUserName();
        if (uid == null || name == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);
        promptRepo.createPrompt(title, text, tags, uid, name, new PromptRepository.PromptCallback() {
            @Override public void onSuccess(Prompt prompt) {
                showLoading(false);
                Toast.makeText(CreatePromptActivity.this, "Prompt shared!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override public void onError(String error) {
                showLoading(false);
                Toast.makeText(CreatePromptActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<String> parseTags(String raw) {
        if (raw.isEmpty()) return new ArrayList<>();
        List<String> out = new ArrayList<>();
        for (String s : Arrays.asList(raw.split(","))) {
            String t = s.trim();
            if (!t.isEmpty() && !out.contains(t)) out.add(t);
        }
        return out;
    }

    private String safe(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void showLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonCreate.setEnabled(!show);
        buttonCancel.setEnabled(!show);
        editTextTitle.setEnabled(!show);
        editTextPrompt.setEnabled(!show);
        editTextTags.setEnabled(!show);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
