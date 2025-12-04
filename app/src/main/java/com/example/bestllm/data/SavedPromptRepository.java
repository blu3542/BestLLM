package com.example.bestllm.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashSet;
import java.util.Set;

public class SavedPromptRepository {
    private static final String TAG = "SavedPromptRepository";
    private static final String COLLECTION_USERS = "users";
    private static final String SUBCOLLECTION_SAVED_PROMPTS = "savedPrompts";
    
    private final FirebaseFirestore db;
    private ListenerRegistration savedPromptsListener;
    private final MutableLiveData<Set<String>> savedPromptIdsLiveData = new MutableLiveData<>();

    public SavedPromptRepository() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Toggle saved status of a prompt for a user
     * If the prompt is saved, it will be unsaved (deleted)
     * If the prompt is not saved, it will be saved (created)
     */
    public void toggleSavedPrompt(String userId, String promptId, ToggleCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onError("User ID is required");
            return;
        }
        if (promptId == null || promptId.isEmpty()) {
            callback.onError("Prompt ID is required");
            return;
        }

        // Check if prompt is already saved
        db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_SAVED_PROMPTS)
                .document(promptId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Prompt is saved, unsave it (delete)
                        unsavePrompt(userId, promptId, callback);
                    } else {
                        // Prompt is not saved, save it (create)
                        savePrompt(userId, promptId, callback);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking saved prompt status", e);
                    callback.onError("Failed to check saved status: " + e.getMessage());
                });
    }

    private void savePrompt(String userId, String promptId, ToggleCallback callback) {
        db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_SAVED_PROMPTS)
                .document(promptId)
                .set(new SavedPrompt(promptId))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Prompt saved: " + promptId + " for user: " + userId);
                    callback.onSuccess(true); // true = saved
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving prompt", e);
                    callback.onError("Failed to save prompt: " + e.getMessage());
                });
    }

    private void unsavePrompt(String userId, String promptId, ToggleCallback callback) {
        db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_SAVED_PROMPTS)
                .document(promptId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Prompt unsaved: " + promptId + " for user: " + userId);
                    callback.onSuccess(false); // false = unsaved
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error unsaving prompt", e);
                    callback.onError("Failed to unsave prompt: " + e.getMessage());
                });
    }

    /**
     * Observe saved prompt IDs for a user as LiveData
     * Returns a LiveData that emits a Set of prompt IDs that are saved by the user
     */
    public LiveData<Set<String>> observeSavedPromptIds(String userId) {
        if (userId == null || userId.isEmpty()) {
            savedPromptIdsLiveData.setValue(new HashSet<>());
            return savedPromptIdsLiveData;
        }

        // Remove existing listener if any
        if (savedPromptsListener != null) {
            savedPromptsListener.remove();
        }

        // Set up real-time listener
        savedPromptsListener = db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_SAVED_PROMPTS)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Error listening to saved prompts", error);
                        savedPromptIdsLiveData.setValue(new HashSet<>());
                        return;
                    }

                    Set<String> savedIds = new HashSet<>();
                    if (snapshot != null) {
                        for (QueryDocumentSnapshot doc : snapshot) {
                            savedIds.add(doc.getId());
                        }
                    }
                    savedPromptIdsLiveData.setValue(savedIds);
                    Log.d(TAG, "Saved prompts updated: " + savedIds.size() + " prompts");
                });

        return savedPromptIdsLiveData;
    }

    /**
     * Stop observing saved prompts (call this when no longer needed, e.g., in onDestroy)
     */
    public void stopObserving() {
        if (savedPromptsListener != null) {
            savedPromptsListener.remove();
            savedPromptsListener = null;
        }
    }

    /**
     * Get saved prompts for a user (one-time fetch, not LiveData)
     */
    public void getSavedPrompts(String userId, SavedPromptsCallback callback) {
        if (userId == null || userId.isEmpty()) {
            callback.onSuccess(new HashSet<>());
            return;
        }

        db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(SUBCOLLECTION_SAVED_PROMPTS)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Set<String> savedIds = new HashSet<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        savedIds.add(doc.getId());
                    }
                    callback.onSuccess(savedIds);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching saved prompts", e);
                    callback.onError("Failed to fetch saved prompts: " + e.getMessage());
                });
    }

    /**
     * Get saved prompts as a list of Prompt objects
     */
    public void getSavedPromptObjects(String userId, PromptRepository.PromptListCallback callback) {
        getSavedPrompts(userId, new SavedPromptsCallback() {
            @Override
            public void onSuccess(Set<String> savedIds) {
                if (savedIds.isEmpty()) {
                    callback.onSuccess(new java.util.ArrayList<>());
                    return;
                }

                // Fetch prompt objects for saved IDs
                PromptRepository promptRepo = new PromptRepository();
                promptRepo.getAllPromptsRecent(new PromptRepository.PromptListCallback() {
                    @Override
                    public void onSuccess(java.util.List<com.example.bestllm.models.Prompt> allPrompts) {
                        java.util.List<com.example.bestllm.models.Prompt> savedPrompts = new java.util.ArrayList<>();
                        for (com.example.bestllm.models.Prompt prompt : allPrompts) {
                            if (savedIds.contains(prompt.getPromptId())) {
                                savedPrompts.add(prompt);
                            }
                        }
                        // Sort by creation date (newest first)
                        savedPrompts.sort((a, b) -> {
                            if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                            if (a.getCreatedAt() == null) return 1;
                            if (b.getCreatedAt() == null) return -1;
                            return b.getCreatedAt().compareTo(a.getCreatedAt());
                        });
                        callback.onSuccess(savedPrompts);
                    }

                    @Override
                    public void onError(String error) {
                        callback.onError(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    // Callback interfaces
    public interface ToggleCallback {
        void onSuccess(boolean isSaved); // true if saved, false if unsaved
        void onError(String error);
    }

    public interface SavedPromptsCallback {
        void onSuccess(Set<String> savedPromptIds);
        void onError(String error);
    }

    // Simple data class for saved prompt document
    private static class SavedPrompt {
        private String promptId;
        private com.google.firebase.Timestamp savedAt;

        public SavedPrompt() {
            this.savedAt = com.google.firebase.Timestamp.now();
        }

        public SavedPrompt(String promptId) {
            this.promptId = promptId;
            this.savedAt = com.google.firebase.Timestamp.now();
        }

        public String getPromptId() {
            return promptId;
        }

        public void setPromptId(String promptId) {
            this.promptId = promptId;
        }

        public com.google.firebase.Timestamp getSavedAt() {
            return savedAt;
        }

        public void setSavedAt(com.google.firebase.Timestamp savedAt) {
            this.savedAt = savedAt;
        }
    }
}

