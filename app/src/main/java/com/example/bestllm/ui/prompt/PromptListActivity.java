package com.example.bestllm.ui.prompt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestllm.R;
import com.example.bestllm.data.PromptRepository;
import com.example.bestllm.models.Prompt;
import com.example.bestllm.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class PromptListActivity extends AppCompatActivity implements PromptAdapter.Actions {

    private RecyclerView recycler;
    private PromptAdapter adapter;
    private FloatingActionButton fab;
    private CircularProgressIndicator progress;

    private final PromptRepository promptRepo = new PromptRepository();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_list);

        session = new SessionManager(this);

        setupToolbar();
        initViews();
        loadPrompts();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Prompt Sharing");
        }
    }

    private void initViews() {
        recycler = findViewById(R.id.rvPrompts);
        progress = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fabSharePrompt);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PromptAdapter(session.getUserId(), this);
        recycler.setAdapter(adapter);

        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CreatePromptActivity.class)));
    }

    private void loadPrompts() {
        showLoading(true);
        promptRepo.getAllPromptsRecent(new PromptRepository.PromptListCallback() {
            @Override public void onSuccess(List<Prompt> prompts) {
                showLoading(false);
                adapter.submit(prompts != null ? prompts : new ArrayList<>());
            }
            @Override public void onError(String error) {
                showLoading(false);
                Toast.makeText(PromptListActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLoading(boolean show) {
        if (progress != null) progress.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
        if (recycler != null) recycler.setAlpha(show ? 0.3f : 1f);
        if (fab != null) fab.setEnabled(!show);
    }

    // ----- PromptAdapter.Actions -----
    @Override
    public void onShare(Prompt p) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,
                "LLM Prompt by " + (p.getAuthorName() == null ? "BestLLM user" : p.getAuthorName())
                        + ":\n\n" + p.getText());
        startActivity(Intent.createChooser(share, "Share Prompt"));
    }

    @Override
    public void onEdit(Prompt p) {
        Intent i = new Intent(this, EditPromptActivity.class);
        i.putExtra(EditPromptActivity.EXTRA_PROMPT_ID, p.getPromptId());
        startActivity(i);
    }

    @Override
    public void onDelete(Prompt p) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Prompt")
                .setMessage("Are you sure you want to delete this prompt?")
                .setPositiveButton("Delete", (d, w) -> {
                    showLoading(true);
                    promptRepo.deletePrompt(p.getPromptId(), new PromptRepository.DeleteCallback() {
                        @Override public void onSuccess() {
                            loadPrompts();
                            Toast.makeText(PromptListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        }
                        @Override public void onError(String error) {
                            showLoading(false);
                            Toast.makeText(PromptListActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload to reflect newly created/edited prompts
        loadPrompts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}

