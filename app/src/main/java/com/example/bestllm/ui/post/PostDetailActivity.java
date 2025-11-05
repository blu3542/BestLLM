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
// NEW: repos, adapter, widgets
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.example.bestllm.data.CommentRepository;
import com.example.bestllm.data.VoteRepository;
import com.example.bestllm.models.Comment;
import com.example.bestllm.ui.post.CommentAdapter; // or com.example.bestllm.adapters.CommentAdapter if you moved it


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

    // NEW: Workstream-3 state
    private String postId;                       // set in loadPost()
    private VoteRepository voteRepo;
    private CommentRepository commentRepo;

    // NEW: Comments UI
    private RecyclerView recyclerComments;
    private CommentAdapter commentAdapter;
    private MaterialButton btnUpvote, btnDownvote, btnSubmitComment;
    private TextInputEditText editCommentBody;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_post_detail);
//
//        postRepository = new PostRepository();
//        sessionManager = new SessionManager(this);
//
//        setupToolbar();
//        initViews();
//        loadPost();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postRepository = new PostRepository();
        sessionManager = new SessionManager(this);

        // NEW: init repos
        voteRepo = new VoteRepository();
        commentRepo = new CommentRepository();

        setupToolbar();
        initViews();   // (we’ll hook up new views inside initViews)
        loadPost();    // (we’ll set postId here and then wire listeners)
    }


    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post Details");
        }
    }

//    private void initViews() {
//        textViewTitle = findViewById(R.id.textViewTitle);
//        textViewBody = findViewById(R.id.textViewBody);
//        textViewAuthor = findViewById(R.id.textViewAuthor);
//        textViewDate = findViewById(R.id.textViewDate);
//        textViewVotes = findViewById(R.id.textViewVotes);
//        textViewComments = findViewById(R.id.textViewComments);
//        chipGroupTags = findViewById(R.id.chipGroupTags);
//        progressBar = findViewById(R.id.progressBar);
//        layoutContent = findViewById(R.id.layoutContent);
//    }

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

        // NEW: comments + vote controls
        recyclerComments  = findViewById(R.id.recyclerComments);
        btnUpvote         = findViewById(R.id.btnUpvote);
        btnDownvote       = findViewById(R.id.btnDownvote);
        btnSubmitComment  = findViewById(R.id.btnSubmitComment);
        editCommentBody   = findViewById(R.id.editCommentBody);

        if (recyclerComments != null) {
            recyclerComments.setLayoutManager(new LinearLayoutManager(this));
            commentAdapter = new CommentAdapter((c, value) -> {
                if (!ensureReadyForVoting()) return;
                voteRepo.voteComment(postId, c.getCommentId(), currentUserId(), value,
                        new VoteRepository.VoidCallback() {
                            @Override public void onSuccess() { refreshComments(); }
                            @Override public void onError(String e) { Toast.makeText(PostDetailActivity.this, e, Toast.LENGTH_SHORT).show(); }
                        });
            });
            recyclerComments.setAdapter(commentAdapter);
        }
    }


//    private void loadPost() {
//        String postId = getIntent().getStringExtra(EXTRA_POST_ID);
//        if (postId == null) {
//            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//
//        showLoading(true);
//
//        postRepository.getPost(postId, new PostRepository.PostCallback() {
//            @Override
//            public void onSuccess(Post post) {
//                showLoading(false);
//                currentPost = post;
//                displayPost(post);
//                invalidateOptionsMenu(); // Refresh menu to show/hide edit/delete
//            }
//
//            @Override
//            public void onError(String error) {
//                showLoading(false);
//                Toast.makeText(PostDetailActivity.this, error, Toast.LENGTH_LONG).show();
//                finish();
//            }
//        });
//    }
    private void loadPost() {
        String idFromIntent = getIntent().getStringExtra(EXTRA_POST_ID);
        if (idFromIntent == null) {
            Toast.makeText(this, "Invalid post ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // NEW: cache it for voting/comment calls
        postId = idFromIntent;

        showLoading(true);

        postRepository.getPost(postId, new PostRepository.PostCallback() {
            @Override
            public void onSuccess(Post post) {
                showLoading(false);
                currentPost = post;
                displayPost(post);
                invalidateOptionsMenu();

                // NEW: now that postId/currentPost are valid, wire the click listeners
                setupVotingAndCommentsUi();   // sets button listeners
                refreshComments();            // loads comments list
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

    // NEW: set button listeners after post is loaded
    private void setupVotingAndCommentsUi() {
        if (btnUpvote != null) {
            btnUpvote.setOnClickListener(v -> {
                if (!ensureReadyForVoting()) return;
                voteRepo.votePost(postId, currentUserId(), 1, cbRefreshPost());
            });
        }
        if (btnDownvote != null) {
            btnDownvote.setOnClickListener(v -> {
                if (!ensureReadyForVoting()) return;
                voteRepo.votePost(postId, currentUserId(), -1, cbRefreshPost());
            });
        }
        if (btnSubmitComment != null) {
            btnSubmitComment.setOnClickListener(v -> {
                String body = String.valueOf(editCommentBody.getText()).trim();
                if (body.isEmpty()) {
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ensureReadyForVoting()) return;
                commentRepo.addComment(postId, currentUserId(), currentUserName(), null, body,
                        new CommentRepository.CommentCallback() {
                            @Override public void onSuccess(Comment c) {
                                editCommentBody.setText("");
                                refreshComments();
                                // We already have currentPost; if you want to re-fetch for counts:
                                refreshPost();
                            }
                            @Override public void onError(String e) {
                                Toast.makeText(PostDetailActivity.this, e, Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }
    }

    // NEW: defensively refresh post (if you have a repository call for a single post)
    private void refreshPost() {
        if (postId == null) return;
        postRepository.getPost(postId, new PostRepository.PostCallback() {
            @Override public void onSuccess(Post post) {
                currentPost = post;
                displayPost(post);
            }
            @Override public void onError(String error) {
                // optional: Toast or ignore
            }
        });
    }

    // NEW: load comments list
    private void refreshComments() {
        if (postId == null || recyclerComments == null || commentAdapter == null) return;
        commentRepo.getCommentsForPost(postId, new CommentRepository.CommentListCallback() {
            @Override public void onSuccess(java.util.List<Comment> list) {
                commentAdapter.submit(list);
            }
            @Override public void onError(String e) {
                Toast.makeText(PostDetailActivity.this, e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // NEW: simple guards to prevent crashes on null ids
    private boolean ensureReadyForVoting() {
        if (postId == null || postId.trim().isEmpty()) {
            Toast.makeText(this, "Missing post", Toast.LENGTH_SHORT).show();
            return false;
        }
        String uid = currentUserId();
        if (uid == null || uid.trim().isEmpty()) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private VoteRepository.VoidCallback cbRefreshPost() {
        return new VoteRepository.VoidCallback() {
            @Override public void onSuccess() { refreshPost(); }
            @Override public void onError(String e) { Toast.makeText(PostDetailActivity.this, e, Toast.LENGTH_SHORT).show(); }
        };
    }

    // If your app uses FirebaseAuth directly, you can swap to FirebaseAuth.getInstance().getUid().
//    private String currentUserId() {
//        return sessionManager != null ? sessionManager.getUserId() : null;
//    }
    private String currentUserId() {
        return com.google.firebase.auth.FirebaseAuth.getInstance().getUid();
    }

    private String currentUserName() {
        String name = sessionManager != null ? sessionManager.getUserName() : null;
        return (name != null && !name.isEmpty()) ? name : "You";
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

