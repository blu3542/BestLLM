package com.example.bestllm.data;

import android.util.Log;

import com.example.bestllm.models.Prompt;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PromptRepository {
    private static final String TAG = "PromptRepository";
    private static final String COLLECTION_PROMPTS = "prompts";
    private final FirebaseFirestore db;

    public PromptRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // ---- Callbacks ----
    public interface PromptCallback { void onSuccess(Prompt prompt); void onError(String error); }
    public interface PromptListCallback { void onSuccess(List<Prompt> prompts); void onError(String error); }
    public interface DeleteCallback { void onSuccess(); void onError(String error); }
    public interface TagListCallback { void onSuccess(List<String> tags); void onError(String error); }

    // ---- Create ----
    public void createPrompt(String title, String text, List<String> tags, String authorId, String authorName, PromptCallback cb) {
        if (title == null || title.trim().isEmpty()) {
            cb.onError("Prompt title is required");
            return;
        }
        if (text == null || text.trim().isEmpty()) {
            cb.onError("Prompt text is required");
            return;
        }
        if (tags == null || tags.isEmpty()) {
            cb.onError("At least one tag is required");
            return;
        }
        // Soft limit to keep UI snappy; adjust as you wish.
        if (text.length() > 5000) {
            cb.onError("Prompt must be 5000 characters or less");
            return;
        }
        if (title.length() > 100) {
            cb.onError("Title must be 100 characters or less");
            return;
        }

        String promptId = db.collection(COLLECTION_PROMPTS).document().getId();
        Prompt p = new Prompt(promptId, title.trim(), text.trim(), tags, authorId, authorName);

        db.collection(COLLECTION_PROMPTS)
                .document(promptId)
                .set(p)
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Prompt created: " + promptId);
                    cb.onSuccess(p);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "createPrompt failed", e);
                    cb.onError("Failed to create prompt: " + e.getMessage());
                });
    }

    // ---- Read one ----
    public void getPrompt(String promptId, PromptCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .document(promptId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Prompt p = doc.toObject(Prompt.class);
                        if (p != null) cb.onSuccess(p);
                        else cb.onError("Failed to parse prompt data");
                    } else {
                        cb.onError("Prompt not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getPrompt failed", e);
                    cb.onError("Failed to fetch prompt: " + e.getMessage());
                });
    }

    // ---- Update ----
    public void updatePrompt(String promptId, String newTitle, String newText, List<String> newTags, PromptCallback cb) {
        if (newTitle == null || newTitle.trim().isEmpty()) {
            cb.onError("Prompt title is required");
            return;
        }
        if (newText == null || newText.trim().isEmpty()) {
            cb.onError("Prompt text is required");
            return;
        }
        if (newTags == null || newTags.isEmpty()) {
            cb.onError("At least one tag is required");
            return;
        }
        if (newText.length() > 5000) {
            cb.onError("Prompt must be 5000 characters or less");
            return;
        }
        if (newTitle.length() > 100) {
            cb.onError("Title must be 100 characters or less");
            return;
        }

        db.collection(COLLECTION_PROMPTS)
                .document(promptId)
                .update(
                        "title", newTitle.trim(),
                        "text", newText.trim(),
                        "tags", newTags,
                        "updatedAt", Timestamp.now()
                )
                .addOnSuccessListener(v -> getPrompt(promptId, cb))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "updatePrompt failed", e);
                    cb.onError("Failed to update prompt: " + e.getMessage());
                });
    }

    // ---- Delete ----
    public void deletePrompt(String promptId, DeleteCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .document(promptId)
                .delete()
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Prompt deleted: " + promptId);
                    cb.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "deletePrompt failed", e);
                    cb.onError("Failed to delete prompt: " + e.getMessage());
                });
    }

    // ---- Lists ----
    public void getPromptsByAuthor(String authorId, PromptListCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .whereEqualTo("authorId", authorId)
                .get()
                .addOnSuccessListener(sn -> {
                    List<Prompt> out = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);
                        out.add(p);
                    }
                    // Sort newest first in memory (avoid composite index requirement)
                    out.sort((a, b) -> {
                        if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                        if (a.getCreatedAt() == null) return 1;
                        if (b.getCreatedAt() == null) return -1;
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    });
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getPromptsByAuthor failed", e);
                    cb.onError("Failed to fetch prompts: " + e.getMessage());
                });
    }

    public void getAllPromptsRecent(PromptListCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(sn -> {
                    List<Prompt> out = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);
                        out.add(p);
                    }
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getAllPromptsRecent failed", e);
                    cb.onError("Failed to fetch prompts: " + e.getMessage());
                });
    }

    public void getPromptsByTag(String tag, PromptListCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .whereArrayContains("tags", tag)
                .get()
                .addOnSuccessListener(sn -> {
                    List<Prompt> out = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);
                        out.add(p);
                    }
                    // newest first
                    out.sort((a, b) -> {
                        if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                        if (a.getCreatedAt() == null) return 1;
                        if (b.getCreatedAt() == null) return -1;
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    });
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getPromptsByTag failed", e);
                    cb.onError("Failed to fetch prompts: " + e.getMessage());
                });
    }

    // ---- Search (basic, like PostRepository) ----
    public void searchPrompts(String searchQuery, PromptListCallback cb) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            getAllPromptsRecent(cb);
            return;
        }
        String q = searchQuery.trim().toLowerCase();

        db.collection(COLLECTION_PROMPTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(sn -> {
                    List<Prompt> out = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);
                        boolean titleMatch = p.getTitle() != null && p.getTitle().toLowerCase().contains(q);
                        boolean textMatch = p.getText() != null && p.getText().toLowerCase().contains(q);
                        if (titleMatch || textMatch) out.add(p);
                    }
                    Log.d(TAG, "Found " + out.size() + " prompts matching: " + searchQuery);
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "searchPrompts failed", e);
                    cb.onError("Failed to search prompts: " + e.getMessage());
                });
    }

    public void searchPromptsWithTags(String searchQuery, PromptListCallback cb) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            getAllPromptsRecent(cb);
            return;
        }
        String q = searchQuery.trim().toLowerCase();

        db.collection(COLLECTION_PROMPTS)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(sn -> {
                    List<Prompt> out = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);

                        boolean titleMatch = p.getTitle() != null && p.getTitle().toLowerCase().contains(q);
                        boolean textMatch = p.getText() != null && p.getText().toLowerCase().contains(q);
                        boolean tagMatch = false;
                        if (p.getTags() != null) {
                            for (String t : p.getTags()) {
                                if (t != null && t.toLowerCase().contains(q)) { tagMatch = true; break; }
                            }
                        }

                        if (titleMatch || textMatch || tagMatch) out.add(p);
                    }
                    Log.d(TAG, "Found " + out.size() + " prompts matching with tags: " + searchQuery);
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "searchPromptsWithTags failed", e);
                    cb.onError("Failed to search prompts: " + e.getMessage());
                });
    }

    // ---- Tags ----
    public void getAllTags(TagListCallback cb) {
        db.collection(COLLECTION_PROMPTS)
                .get()
                .addOnSuccessListener(sn -> {
                    List<String> tags = new ArrayList<>();
                    for (QueryDocumentSnapshot d : sn) {
                        Prompt p = d.toObject(Prompt.class);
                        if (p.getTags() != null) {
                            for (String t : p.getTags()) {
                                if (t != null && !tags.contains(t)) tags.add(t);
                            }
                        }
                    }
                    Log.d(TAG, "Found " + tags.size() + " unique prompt tags");
                    cb.onSuccess(tags);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "getAllTags failed", e);
                    cb.onError("Failed to fetch tags: " + e.getMessage());
                });
    }
}
