package com.example.bestllm.models;

import com.google.firebase.Timestamp;

public class Comment {
    private String commentId;
    private String postId;
    private String authorId;
    private String authorName;
    private String title; // optional
    private String body;
    private int upvotes;
    private int downvotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Comment() { }

    public Comment(String commentId, String postId, String authorId, String authorName, String title, String body) {
        this.commentId = commentId;
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.body = body;
        this.upvotes = 0;
        this.downvotes = 0;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public String getCommentId() { return commentId; }
    public void setCommentId(String commentId) { this.commentId = commentId; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public int getUpvotes() { return upvotes; }
    public void setUpvotes(int upvotes) { this.upvotes = upvotes; }

    public int getDownvotes() { return downvotes; }
    public void setDownvotes(int downvotes) { this.downvotes = downvotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getNetVotes() { return upvotes - downvotes; }
}
