package com.example.bestllm.models;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Prompt {
    private String promptId;
    private String title;             // title for the shared prompt
    private String text;              // the actual LLM prompt
    private List<String> tags;
    private String authorId;
    private String authorName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Empty constructor required for Firestore
    public Prompt() {
        this.tags = new ArrayList<>();
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public Prompt(String promptId, String title, String text, List<String> tags, String authorId, String authorName) {
        this.promptId = promptId;
        this.title = title;
        this.text = text;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdAt = Timestamp.now();
        this.updatedAt = Timestamp.now();
    }

    public String getPromptId() { return promptId; }
    public void setPromptId(String promptId) { this.promptId = promptId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}

