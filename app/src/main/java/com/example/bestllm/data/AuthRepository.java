package com.example.bestllm.data;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.bestllm.models.User;
import com.example.bestllm.utils.Validators;

public class AuthRepository {
    private static final String TAG = "AuthRepository";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public void register(String name, String email, String password, AuthCallback callback) {
        if (!Validators.isValidName(name)) {
            callback.onError("Please enter a valid name");
            return;
        }

        if (!Validators.isValidUSCEmail(email)) {
            callback.onError("Please use a valid USC email (@usc.edu)");
            return;
        }

        if (!Validators.isValidPassword(password)) {
            callback.onError("Password must be at least 6 characters");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            User user = new User(firebaseUser.getUid(), name, email);
                            createUserDocument(user, callback);
                        }
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Registration failed";
                        Log.e(TAG, "Registration failed", task.getException());
                        callback.onError(error);
                    }
                });
    }

    private void createUserDocument(User user, AuthCallback callback) {
        db.collection("users")
                .document(user.getUserId())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User document created successfully");
                    callback.onSuccess(user);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating user document", e);
                    callback.onError("Failed to create user profile: " + e.getMessage());
                });
    }

    public void login(String email, String password, AuthCallback callback) {
        if (!Validators.isValidUSCEmail(email)) {
            callback.onError("Please use a valid USC email (@usc.edu)");
            return;
        }

        if (!Validators.isValidPassword(password)) {
            callback.onError("Password must be at least 6 characters");
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            fetchUserData(firebaseUser.getUid(), callback);
                        }
                    } else {
                        String error = task.getException() != null ?
                                task.getException().getMessage() : "Login failed";
                        Log.e(TAG, "Login failed", task.getException());
                        callback.onError(error);
                    }
                });
    }

    private void fetchUserData(String userId, AuthCallback callback) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User profile not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user data", e);
                    callback.onError("Failed to fetch user data: " + e.getMessage());
                });
    }

    public void logout() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void getUserProfile(String userId, AuthCallback callback) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onError("Error fetching profile: " + e.getMessage());
                });
    }
}