package com.example.bestllm.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.bestllm.R;
import com.example.bestllm.data.AuthRepository;
import com.example.bestllm.data.PostRepository;
import com.example.bestllm.models.Post;
import com.example.bestllm.ui.auth.LoginActivity;
import com.example.bestllm.ui.post.CreatePostActivity;
import com.example.bestllm.ui.post.PostDetailActivity;
import com.example.bestllm.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements PostAdapter.OnPostClickListener {

    private static final int REQUEST_CREATE_POST = 1001;
    private static final int REQUEST_VIEW_POST = 1002;

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabCreatePost;
    
    // Search and filter UI elements
    private TextInputEditText editTextSearch;
    private MaterialButtonToggleGroup toggleGroupSort;
    private MaterialButton buttonSortRecent;
    private MaterialButton buttonSortVotes;
    private MaterialButton buttonFilterTags;

    private PostRepository postRepository;
    private AuthRepository authRepository;
    private SessionManager sessionManager;
    
    // Filter state
    private String currentSearchQuery = "";
    private boolean sortByVotes = false;
    private List<String> selectedTags = new ArrayList<>();
    private List<String> allTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postRepository = new PostRepository();
        authRepository = new AuthRepository();
        sessionManager = new SessionManager(this);

        setupToolbar();
        initViews();
        setupRecyclerView();
        setupListeners();
        loadPosts();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("BestLLM - Home");
        }
    }

    private void initViews() {
        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        progressBar = findViewById(R.id.progressBar);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fabCreatePost = findViewById(R.id.fabCreatePost);
        
        // Search and filter UI elements
        editTextSearch = findViewById(R.id.editTextSearch);
        toggleGroupSort = findViewById(R.id.toggleGroupSort);
        buttonSortRecent = findViewById(R.id.buttonSortRecent);
        buttonSortVotes = findViewById(R.id.buttonSortVotes);
        buttonFilterTags = findViewById(R.id.buttonFilterTags);
        
        // Set default sort option
        toggleGroupSort.check(R.id.buttonSortRecent);
    }

    private void setupRecyclerView() {
        postAdapter = new PostAdapter(new ArrayList<>(), this);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPosts.setAdapter(postAdapter);
    }

    private void setupListeners() {
        fabCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent, REQUEST_CREATE_POST);
        });

        swipeRefreshLayout.setOnRefreshListener(this::loadPosts);
        
        // Search functionality
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                currentSearchQuery = s.toString();
                performSearch();
            }
        });
        
        // Sort functionality
        toggleGroupSort.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                sortByVotes = (checkedId == R.id.buttonSortVotes);
                loadPosts();
            }
        });
        
        // Tag filter functionality
        buttonFilterTags.setOnClickListener(v -> showTagFilterDialog());
        
        // Load all tags for filtering
        loadAllTags();
    }

    private void loadPosts() {
        showLoading(true);

        PostRepository.PostListCallback callback = new PostRepository.PostListCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                
                // Apply tag filtering if any tags are selected
                List<Post> filteredPosts = filterPostsByTags(posts);
                
                postAdapter.updatePosts(filteredPosts);
                
                if (filteredPosts.isEmpty()) {
                    String emptyMessage = selectedTags.isEmpty() && currentSearchQuery.isEmpty()
                            ? "No posts yet.\nBe the first to create one!"
                            : "No posts match your search criteria.";
                    textViewEmpty.setText(emptyMessage);
                    textViewEmpty.setVisibility(View.VISIBLE);
                    recyclerViewPosts.setVisibility(View.GONE);
                } else {
                    textViewEmpty.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(HomeActivity.this, error, Toast.LENGTH_LONG).show();
            }
        };

        // Determine which method to call based on current state
        if (!currentSearchQuery.isEmpty()) {
            // Search mode
            postRepository.searchPostsWithTags(currentSearchQuery, callback);
        } else if (sortByVotes) {
            // Sort by votes
            postRepository.getAllPostsByVotes(callback);
        } else {
            // Default: sort by recent
            postRepository.getAllPostsRecent(callback);
        }
    }

    private void performSearch() {
        // Debounce search to avoid too many queries
        if (currentSearchQuery.length() >= 2 || currentSearchQuery.isEmpty()) {
            loadPosts();
        }
    }

    private List<Post> filterPostsByTags(List<Post> posts) {
        if (selectedTags.isEmpty()) {
            return posts;
        }

        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTags() != null) {
                boolean hasMatchingTag = false;
                for (String selectedTag : selectedTags) {
                    if (post.getTags().contains(selectedTag)) {
                        hasMatchingTag = true;
                        break;
                    }
                }
                if (hasMatchingTag) {
                    filteredPosts.add(post);
                }
            }
        }
        return filteredPosts;
    }

    private void loadAllTags() {
        postRepository.getAllTags(new PostRepository.TagListCallback() {
            @Override
            public void onSuccess(List<String> tags) {
                allTags.clear();
                allTags.addAll(tags);
            }

            @Override
            public void onError(String error) {
                // Silently fail for tags loading
            }
        });
    }

    private void showTagFilterDialog() {
        if (allTags.isEmpty()) {
            Toast.makeText(this, "No tags available", Toast.LENGTH_SHORT).show();
            return;
        }

//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_tag_filter, null);
        View dialogView = findViewById(android.R.id.content);
        ChipGroup chipGroupFilterTags = dialogView.findViewById(R.id.chipGroupFilterTags);
        MaterialButton buttonClearFilter = dialogView.findViewById(R.id.buttonClearFilter);
        MaterialButton buttonApplyFilter = dialogView.findViewById(R.id.buttonApplyFilter);

        // Add chips for all tags
        chipGroupFilterTags.removeAllViews();
        for (String tag : allTags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCheckable(true);
            chip.setChecked(selectedTags.contains(tag));
            chipGroupFilterTags.addView(chip);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        buttonClearFilter.setOnClickListener(v -> {
            selectedTags.clear();
            updateFilterButtonText();
            loadPosts();
            dialog.dismiss();
        });

        buttonApplyFilter.setOnClickListener(v -> {
            selectedTags.clear();
            for (int i = 0; i < chipGroupFilterTags.getChildCount(); i++) {
                Chip chip = (Chip) chipGroupFilterTags.getChildAt(i);
                if (chip.isChecked()) {
                    selectedTags.add(chip.getText().toString());
                }
            }
            updateFilterButtonText();
            loadPosts();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateFilterButtonText() {
        if (selectedTags.isEmpty()) {
            buttonFilterTags.setText("Filter by Tag");
        } else {
            buttonFilterTags.setText("Filtered (" + selectedTags.size() + ")");
        }
    }

    private void showLoading(boolean show) {
        if (!swipeRefreshLayout.isRefreshing()) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onPostClick(Post post) {
        Intent intent = new Intent(this, PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.getPostId());
        startActivityForResult(intent, REQUEST_VIEW_POST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            loadPosts();
            return true;
        } else if (id == R.id.action_my_posts) {
            showMyPosts();
            return true;
        } else if (id == R.id.action_logout) {
            handleLogout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMyPosts() {
        String userId = sessionManager.getUserId();
        if (userId == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        postRepository.getPostsByAuthor(userId, new PostRepository.PostListCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                showLoading(false);
                postAdapter.updatePosts(posts);
                
                if (posts.isEmpty()) {
                    textViewEmpty.setText("You haven't created any posts yet");
                    textViewEmpty.setVisibility(View.VISIBLE);
                    recyclerViewPosts.setVisibility(View.GONE);
                } else {
                    textViewEmpty.setVisibility(View.GONE);
                    recyclerViewPosts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                showLoading(false);
                Toast.makeText(HomeActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleLogout() {
        authRepository.logout();
        sessionManager.logout();
        
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CREATE_POST || requestCode == REQUEST_VIEW_POST) && resultCode == RESULT_OK) {
            // Refresh the posts list
            loadPosts();
        }
    }
}
