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

public class EditPromptActivity extends AppCompatActivity {

    public static final String EXTRA_PROMPT_ID = "prompt_id";

    private TextInputEditText editTextPrompt, editTextTags;
    private Button buttonUpdate, buttonCancel;
    private CircularProgressIndicator progress;

    private final PromptRepository promptRepo = new PromptRepository();
    private SessionManager session;
    private String promptId;
    private Prompt currentPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prompt);

        session = new SessionManager(this);

        setupToolbar();
        initViews();
        setupListeners();
        loadPrompt();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Prompt");
        }
    }

    private void initViews() {
        editTextPrompt = findViewById(R.id.editTextPrompt);
        editTextTags   = findViewById(R.id.editTextTags);
        buttonUpdate   = findViewById(R.id.buttonUpdatePrompt);
        buttonCancel   = findViewById(R.id.buttonCancel);
        progress       = findViewById(R.id.progressBar);
    }

    private void setupListeners() {
        buttonUpdate.setOnClickListener(v -> handleUpdate());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void loadPrompt() {
        promptId = getIntent().getStringExtra(EXTRA_PROMPT_ID);
        if (promptId == null) {
            Toast.makeText(this, "Invalid prompt ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);
        promptRepo.getPrompt(promptId, new PromptRepository.PromptCallback() {
            @Override public void onSuccess(Prompt prompt) {
                showLoading(false);
                currentPrompt = prompt;

                String currentUserId = session.getUserId();
                if (currentUserId == null || !currentUserId.equals(prompt.getAuthorId())) {
                    Toast.makeText(EditPromptActivity.this, "You can only edit your own prompts", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                // populate
                editTextPrompt.setText(prompt.getText());
                if (prompt.getTags() != null && !prompt.getTags().isEmpty()) {
                    editTextTags.setText(String.join(", ", prompt.getTags()));
                }
            }

            @Override public void onError(String error) {
                showLoading(false);
                Toast.makeText(EditPromptActivity.this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void handleUpdate() {
        String text = safe(editTextPrompt);
        String rawTags = safe(editTextTags);

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

        showLoading(true);
        promptRepo.updatePrompt(promptId, text, parseTags(rawTags), new PromptRepository.PromptCallback() {
            @Override public void onSuccess(Prompt prompt) {
                showLoading(false);
                Toast.makeText(EditPromptActivity.this, "Prompt updated!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
            @Override public void onError(String error) {
                showLoading(false);
                Toast.makeText(EditPromptActivity.this, error, Toast.LENGTH_LONG).show();
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
        buttonUpdate.setEnabled(!show);
        buttonCancel.setEnabled(!show);
        editTextPrompt.setEnabled(!show);
        editTextTags.setEnabled(!show);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}

