package com.example.bestllm.data;

import android.util.Log;

import com.example.bestllm.models.Vote;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class VoteRepository {
    private static final String TAG = "VoteRepository";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface VoidCallback { void onSuccess(); void onError(String e); }

    private DocumentReference postDoc(String postId) {
        return db.collection("posts").document(postId);
    }
//    private DocumentReference postVoteDoc(String postId, String userId) {
//        return postDoc(postId).collection("postVotes").document(userId);
//    }
    private DocumentReference postVoteDoc(String postId, String userId) {
        return db.collection("posts").document(postId)
                .collection("postVotes").document(userId); // userId == FirebaseAuth UID
    }
    private DocumentReference commentDoc(String postId, String commentId) {
        return postDoc(postId).collection("comments").document(commentId);
    }
//    private DocumentReference commentVoteDoc(String postId, String commentId, String userId) {
//        return commentDoc(postId, commentId).collection("commentVotes").document(userId);
//    }

    private DocumentReference commentVoteDoc(String postId, String commentId, String userId) {
        return db.collection("posts").document(postId)
                .collection("comments").document(commentId)
                .collection("commentVotes").document(userId); // UID
    }
    /** Toggle a vote on a post. value ∈ {1, -1} */
    public void votePost(String postId, String userId, int value, VoidCallback cb) {
        if (value != 1 && value != -1) { cb.onError("Invalid vote value"); return; }

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference myVoteDoc = postVoteDoc(postId, userId);
                    DocumentReference post = postDoc(postId);

                    int deltaUp = 0, deltaDown = 0;
                    DocumentSnapshot snap = transaction.get(myVoteDoc);

                    if (snap.exists()) {
                        Long current = snap.getLong("value");
                        if (current != null && current.intValue() == value) {
                            // cancel the same vote
                            transaction.delete(myVoteDoc);
                            if (value == 1) deltaUp = -1; else deltaDown = -1;
                        } else {
                            // switch vote
                            transaction.set(myVoteDoc, new Vote(value));
                            if (value == 1) { deltaUp = 1; deltaDown = -1; }
                            else { deltaUp = -1; deltaDown = 1; }
                        }
                    } else {
                        // new vote
                        transaction.set(myVoteDoc, new Vote(value));
                        if (value == 1) deltaUp = 1; else deltaDown = 1;
                    }

                    if (deltaUp != 0) transaction.update(post, "upvotes", FieldValue.increment(deltaUp));
                    if (deltaDown != 0) transaction.update(post, "downvotes", FieldValue.increment(deltaDown));
                    transaction.update(post, "updatedAt", Timestamp.now());
                    return null;
                }).addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> { Log.e(TAG, "votePost failed", e); cb.onError(e.getMessage()); });
    }

    /** Toggle a vote on a comment. value ∈ {1, -1} */
    public void voteComment(String postId, String commentId, String userId, int value, VoidCallback cb) {
        if (value != 1 && value != -1) { cb.onError("Invalid vote value"); return; }

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference myVoteDoc = commentVoteDoc(postId, commentId, userId);
                    DocumentReference comment = commentDoc(postId, commentId);

                    int deltaUp = 0, deltaDown = 0;
                    DocumentSnapshot snap = transaction.get(myVoteDoc);

                    if (snap.exists()) {
                        Long current = snap.getLong("value");
                        if (current != null && current.intValue() == value) {
                            transaction.delete(myVoteDoc);
                            if (value == 1) deltaUp = -1; else deltaDown = -1;
                        } else {
                            transaction.set(myVoteDoc, new Vote(value));
                            if (value == 1) { deltaUp = 1; deltaDown = -1; }
                            else { deltaUp = -1; deltaDown = 1; }
                        }
                    } else {
                        transaction.set(myVoteDoc, new Vote(value));
                        if (value == 1) deltaUp = 1; else deltaDown = 1;
                    }

                    if (deltaUp != 0) transaction.update(comment, "upvotes", FieldValue.increment(deltaUp));
                    if (deltaDown != 0) transaction.update(comment, "downvotes", FieldValue.increment(deltaDown));
                    transaction.update(comment, "updatedAt", Timestamp.now());
                    return null;
                }).addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
