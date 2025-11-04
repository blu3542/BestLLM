package com.example.bestllm.models;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private String postId;
    private String title;
    private String body;
    private List<String> tags;
    private String authorId;
    private String authorName;
    private int upvotes;
    private int downvotes;
    private int commentCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Empty constructor required for Firestore
    public Post() {
        this.tags = new ArrayList<>();
    }

    public Post(String postId, String title, String body, List<String> tags, String authorId, String authorName) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.authorId = authorId;
        this.authorName = authorName;
        this.upvotes = 0;
        this.downvotes = 0;
        this.commentCount = 0;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    // Getters and Setters
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Utility method to get net votes
    public int getNetVotes() {
        return upvotes - downvotes;
    }
}

