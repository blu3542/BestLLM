package com.example.bestllm.models;

import com.google.firebase.Timestamp;

public class Vote {
    private int value; // 1 or -1
    private Timestamp updatedAt;

    public Vote() { }

    public Vote(int value) {
        this.value = value;
        this.updatedAt = Timestamp.now();
    }

    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
