package com.example.bestllm.ui.post;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.bestllm.R;
import com.example.bestllm.data.PostRepository;
import com.example.bestllm.models.Post;
import com.example.bestllm.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POST_ID = "post_id";
    private static final int REQUEST_EDIT_POST = 1001;

    private TextView textViewTitle, textViewBody, textViewAuthor, textViewDate;
    private TextView textViewVotes, textViewComments;
    private ChipGroup chipGroupTags;
    private ProgressBar progressBar;
    private View layoutContent;

    private PostRepository postRepository;
    private SessionManager sessionManager;
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postRepository = new PostRepository();
        sessionManager = new SessionManager(this);

        setupToolbar();
        initViews();
        loadPost();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post Details");
        }
    }

    private void initViews() {
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewBody = findViewById(R.id.textViewBody);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        textViewDate = findViewById(R.id.textViewDate);
        textViewVotes = findViewById(R.id.textViewVotes);
        textViewComments = findViewById(R.id.textViewComments);
        chipGroupTags = findViewById(R.id.chipGroupTags);
        progressBar = findViewById(R.id.progressBar);
        layoutContent = findViewById(R.id.layoutContent);
    }

    private void loadPost() {
        String postId = getIntent().getStringExtra(EXTRA_POST_ID);
        if (postId == null) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading(true);

        postRepository.getPost(postId, new PostRepository.PostCallback() {
            @Override
            public void onSuccess(Post post) {
                showLoading(false);
                currentPost = post;
                displayPost(post);
                invalidateOptionsMenu(); // Refresh menu to show/hide edit/delete
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(PostDetailActivity.this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void displayPost(Post post) {
        textViewTitle.setText(post.getTitle());
        textViewBody.setText(post.getBody());
        textViewAuthor.setText("by " + post.getAuthorName());
        
        // Format date
        if (post.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());
            textViewDate.setText(sdf.format(post.getCreatedAt().toDate()));
        }

        // Display vote count
        int netVotes = post.getNetVotes();
        String votesText = netVotes + " votes (" + post.getUpvotes() + " up, " + post.getDownvotes() + " down)";
        textViewVotes.setText(votesText);

        // Display comment count
        textViewComments.setText(post.getCommentCount() + " comments");

        // Display tags
        chipGroupTags.removeAllViews();
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            for (String tag : post.getTags()) {
                Chip chip = new Chip(this);
                chip.setText(tag);
                chip.setClickable(false);
                chip.setCheckable(false);
                chipGroupTags.addView(chip);
            }
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show edit/delete if user is the author
        if (currentPost != null && isCurrentUserAuthor()) {
            getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit) {
            handleEditPost();
            return true;
        } else if (id == R.id.action_delete) {
            handleDeletePost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentUserAuthor() {
        String currentUserId = sessionManager.getUserId();
        return currentPost != null && currentUserId != null && 
               currentUserId.equals(currentPost.getAuthorId());
    }

    private void handleEditPost() {
        Intent intent = new Intent(this, EditPostActivity.class);
        intent.putExtra(EditPostActivity.EXTRA_POST_ID, currentPost.getPostId());
        startActivityForResult(intent, REQUEST_EDIT_POST);
    }

    private void handleDeletePost() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Post")
                .setMessage("Are you sure you want to delete this post? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> confirmDeletePost())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDeletePost() {
        showLoading(true);

        postRepository.deletePost(currentPost.getPostId(), new PostRepository.DeleteCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PostDetailActivity.this, "Post deleted successfully", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(PostDetailActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_POST && resultCode == RESULT_OK) {
            // Reload the post to show updated data
            loadPost();
        }
    }
}

