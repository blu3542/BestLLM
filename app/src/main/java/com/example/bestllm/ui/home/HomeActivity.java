package com.example.bestllm.ui.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    private PostRepository postRepository;
    private AuthRepository authRepository;
    private SessionManager sessionManager;

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
    }

    private void loadPosts() {
        showLoading(true);

        postRepository.getAllPostsRecent(new PostRepository.PostListCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                showLoading(false);
                swipeRefreshLayout.setRefreshing(false);
                postAdapter.updatePosts(posts);
                
                if (posts.isEmpty()) {
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
        });
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
