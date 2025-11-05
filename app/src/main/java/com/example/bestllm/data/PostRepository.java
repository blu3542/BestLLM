package com.example.bestllm.data;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.bestllm.models.Post;

import java.util.ArrayList;
import java.util.List;

public class PostRepository {
    private static final String TAG = "PostRepository";
    private static final String COLLECTION_POSTS = "posts";
    private FirebaseFirestore db;

    public PostRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public interface PostCallback {
        void onSuccess(Post post);
        void onError(String error);
    }

    public interface PostListCallback {
        void onSuccess(List<Post> posts);
        void onError(String error);
    }

    public interface DeleteCallback {
        void onSuccess();
        void onError(String error);
    }

    /**
     * Create a new post in Firestore
     */
    public void createPost(String title, String body, List<String> tags, String authorId, String authorName, PostCallback callback) {
        // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            callback.onError("Title is required");
            return;
        }
        if (body == null || body.trim().isEmpty()) {
            callback.onError("Body is required");
            return;
        }
        if (title.length() > 200) {
            callback.onError("Title must be 200 characters or less");
            return;
        }

        // Generate new document ID
        String postId = db.collection(COLLECTION_POSTS).document().getId();
        
        Post post = new Post(postId, title.trim(), body.trim(), tags, authorId, authorName);

        db.collection(COLLECTION_POSTS)
                .document(postId)
                .set(post)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Post created successfully: " + postId);
                    callback.onSuccess(post);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating post", e);
                    callback.onError("Failed to create post: " + e.getMessage());
                });
    }

    /**
     * Get a single post by ID
     */
    public void getPost(String postId, PostCallback callback) {
        db.collection(COLLECTION_POSTS)
                .document(postId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Post post = documentSnapshot.toObject(Post.class);
                        if (post != null) {
                            callback.onSuccess(post);
                        } else {
                            callback.onError("Failed to parse post data");
                        }
                    } else {
                        callback.onError("Post not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching post", e);
                    callback.onError("Failed to fetch post: " + e.getMessage());
                });
    }

    /**
     * Update an existing post
     */
    public void updatePost(String postId, String title, String body, List<String> tags, PostCallback callback) {
        // Validate inputs
        if (title == null || title.trim().isEmpty()) {
            callback.onError("Title is required");
            return;
        }
        if (body == null || body.trim().isEmpty()) {
            callback.onError("Body is required");
            return;
        }
        if (title.length() > 200) {
            callback.onError("Title must be 200 characters or less");
            return;
        }

        db.collection(COLLECTION_POSTS)
                .document(postId)
                .update(
                        "title", title.trim(),
                        "body", body.trim(),
                        "tags", tags,
                        "updatedAt", Timestamp.now()
                )
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Post updated successfully: " + postId);
                    // Fetch the updated post to return
                    getPost(postId, callback);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating post", e);
                    callback.onError("Failed to update post: " + e.getMessage());
                });
    }

    /**
     * Delete a post
     */
    public void deletePost(String postId, DeleteCallback callback) {
        db.collection(COLLECTION_POSTS)
                .document(postId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Post deleted successfully: " + postId);
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting post", e);
                    callback.onError("Failed to delete post: " + e.getMessage());
                });
    }

    /**
     * Get all posts by a specific author
     */
    public void getPostsByAuthor(String authorId, PostListCallback callback) {
        db.collection(COLLECTION_POSTS)
                .whereEqualTo("authorId", authorId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    Log.d(TAG, "Fetched " + posts.size() + " posts for author: " + authorId);
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching posts by author", e);
                    callback.onError("Failed to fetch posts: " + e.getMessage());
                });
    }

    /**
     * Get all posts ordered by creation date (for homepage)
     */
    public void getAllPostsRecent(PostListCallback callback) {
        db.collection(COLLECTION_POSTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    Log.d(TAG, "Fetched " + posts.size() + " posts");
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching all posts", e);
                    callback.onError("Failed to fetch posts: " + e.getMessage());
                });
    }

    /**
     * Get posts by tag
     */
    public void getPostsByTag(String tag, PostListCallback callback) {
        db.collection(COLLECTION_POSTS)
                .whereArrayContains("tags", tag)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    Log.d(TAG, "Fetched " + posts.size() + " posts with tag: " + tag);
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching posts by tag", e);
                    callback.onError("Failed to fetch posts: " + e.getMessage());
                });
    }

    /**
     * Get all posts ordered by most upvotes (for sorting)
     */
    public void getAllPostsByVotes(PostListCallback callback) {
        db.collection(COLLECTION_POSTS)
                .orderBy("upvotes", Query.Direction.DESCENDING)
                .orderBy("createdAt", Query.Direction.DESCENDING) // Secondary sort by date
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        posts.add(post);
                    }
                    Log.d(TAG, "Fetched " + posts.size() + " posts sorted by votes");
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching posts by votes", e);
                    callback.onError("Failed to fetch posts: " + e.getMessage());
                });
    }

    /**
     * Search posts by title or body content
     */
    public void searchPosts(String searchQuery, PostListCallback callback) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            getAllPostsRecent(callback);
            return;
        }

        String query = searchQuery.trim().toLowerCase();
        
        // Note: Firestore doesn't support full-text search natively
        // This is a basic implementation that gets all posts and filters locally
        // For production, consider using Algolia or similar search service
        db.collection(COLLECTION_POSTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        
                        // Check if title or body contains search query
                        boolean titleMatch = post.getTitle() != null &&
                                post.getTitle().toLowerCase().contains(query);
                        boolean bodyMatch = post.getBody() != null &&
                                post.getBody().toLowerCase().contains(query);
                        
                        if (titleMatch || bodyMatch) {
                            posts.add(post);
                        }
                    }
                    Log.d(TAG, "Found " + posts.size() + " posts matching query: " + searchQuery);
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching posts", e);
                    callback.onError("Failed to search posts: " + e.getMessage());
                });
    }

    /**
     * Search posts by title, body, or tags
     */
    public void searchPostsWithTags(String searchQuery, PostListCallback callback) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            getAllPostsRecent(callback);
            return;
        }

        String query = searchQuery.trim().toLowerCase();
        
        db.collection(COLLECTION_POSTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        
                        // Check if title, body, or tags contain search query
                        boolean titleMatch = post.getTitle() != null &&
                                post.getTitle().toLowerCase().contains(query);
                        boolean bodyMatch = post.getBody() != null &&
                                post.getBody().toLowerCase().contains(query);
                        
                        boolean tagMatch = false;
                        if (post.getTags() != null) {
                            for (String tag : post.getTags()) {
                                if (tag.toLowerCase().contains(query)) {
                                    tagMatch = true;
                                    break;
                                }
                            }
                        }
                        
                        if (titleMatch || bodyMatch || tagMatch) {
                            posts.add(post);
                        }
                    }
                    Log.d(TAG, "Found " + posts.size() + " posts matching query with tags: " + searchQuery);
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error searching posts with tags", e);
                    callback.onError("Failed to search posts: " + e.getMessage());
                });
    }

    /**
     * Get all unique tags from posts
     */
    public void getAllTags(TagListCallback callback) {
        db.collection(COLLECTION_POSTS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> allTags = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post post = document.toObject(Post.class);
                        if (post.getTags() != null) {
                            for (String tag : post.getTags()) {
                                if (!allTags.contains(tag)) {
                                    allTags.add(tag);
                                }
                            }
                        }
                    }
                    Log.d(TAG, "Found " + allTags.size() + " unique tags");
                    callback.onSuccess(allTags);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching tags", e);
                    callback.onError("Failed to fetch tags: " + e.getMessage());
                });
    }

    public interface TagListCallback {
        void onSuccess(List<String> tags);
        void onError(String error);
    }
}

