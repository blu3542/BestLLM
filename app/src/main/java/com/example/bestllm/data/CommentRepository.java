package com.example.bestllm.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bestllm.models.Comment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private static final String TAG = "CommentRepository";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface CommentCallback { void onSuccess(Comment c); void onError(String e); }
    public interface CommentListCallback { void onSuccess(List<Comment> list); void onError(String e); }
    public interface VoidCallback { void onSuccess(); void onError(String e); }

    private CollectionReference commentsRef(String postId) {
        return db.collection("posts").document(postId).collection("comments");
    }

    public void addComment(String postId, String authorId, String authorName, String title, String body, CommentCallback cb) {
        String id = commentsRef(postId).document().getId();
        Comment c = new Comment(id, postId, authorId, authorName, title, body);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference postDoc = db.collection("posts").document(postId);
                    DocumentReference commentDoc = commentsRef(postId).document(id);
                    transaction.set(commentDoc, c);
                    transaction.update(postDoc, "commentCount", FieldValue.increment(1), "updatedAt", Timestamp.now());
                    return null;
                }).addOnSuccessListener(unused -> cb.onSuccess(c))
                .addOnFailureListener(e -> { Log.e(TAG, "addComment failed", e); cb.onError(e.getMessage()); });
    }

    public void editComment(String postId, String commentId, String title, String body, CommentCallback cb) {
        commentsRef(postId).document(commentId)
                .update("title", title,
                        "body", body,
                        "updatedAt", Timestamp.now())
                .addOnSuccessListener(unused -> getComment(postId, commentId, cb))
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void deleteComment(String postId, String commentId, VoidCallback cb) {
        db.runTransaction((Transaction.Function<Void>) transaction -> {
                    DocumentReference postDoc = db.collection("posts").document(postId);
                    DocumentReference commentDoc = commentsRef(postId).document(commentId);
                    transaction.delete(commentDoc);
                    transaction.update(postDoc, "commentCount", FieldValue.increment(-1), "updatedAt", Timestamp.now());
                    return null;
                }).addOnSuccessListener(unused -> cb.onSuccess())
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void getCommentsForPost(String postId, CommentListCallback cb) {
        commentsRef(postId).orderBy("createdAt", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(sn -> {
                    List<Comment> out = new ArrayList<>();
                    for (DocumentSnapshot d : sn.getDocuments()) {
                        Comment c = d.toObject(Comment.class);
                        if (c != null) out.add(c);
                    }
                    cb.onSuccess(out);
                })
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }

    public void getComment(String postId, String commentId, CommentCallback cb) {
        commentsRef(postId).document(commentId).get()
                .addOnSuccessListener(d -> cb.onSuccess(d.toObject(Comment.class)))
                .addOnFailureListener(e -> cb.onError(e.getMessage()));
    }
}
