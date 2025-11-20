package com.example.bestllm.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestllm.R;
import com.example.bestllm.models.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.VH> {

    public interface OnVoteClick {
        void onVote(Comment c, int value);
    }

    public interface OnEditClick {
        void onEdit(Comment c);
    }

    public interface OnDeleteClick {
        void onDelete(Comment c);
    }

    private final List<Comment> items = new ArrayList<>();
    private final OnVoteClick voteCb;
    private final OnEditClick editCb;
    private final OnDeleteClick deleteCb;
    private final String currentUserId;

    public CommentAdapter(OnVoteClick voteCb, OnEditClick editCb, OnDeleteClick deleteCb, String currentUserId) {
        this.voteCb = voteCb;
        this.editCb = editCb;
        this.deleteCb = deleteCb;
        this.currentUserId = currentUserId;
    }

    public void submit(List<Comment> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Comment c = items.get(position);
        h.author.setText(c.getAuthorName());
        h.body.setText(c.getBody());
        h.votes.setText(String.valueOf(c.getNetVotes()));
        h.btnUp.setOnClickListener(v -> voteCb.onVote(c, 1));
        h.btnDown.setOnClickListener(v -> voteCb.onVote(c, -1));

        // Show/hide title
        if (c.getTitle() != null && !c.getTitle().trim().isEmpty()) {
            h.title.setText(c.getTitle());
            h.title.setVisibility(android.view.View.VISIBLE);
        } else {
            h.title.setVisibility(android.view.View.GONE);
        }

        // Show edit/delete buttons only for comment author
        boolean isAuthor = currentUserId != null && currentUserId.equals(c.getAuthorId());
        if (isAuthor) {
            h.layoutActions.setVisibility(android.view.View.VISIBLE);
            h.btnEdit.setOnClickListener(v -> editCb.onEdit(c));
            h.btnDelete.setOnClickListener(v -> deleteCb.onDelete(c));
        } else {
            h.layoutActions.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView author, title, body, votes, btnUp, btnDown, btnEdit, btnDelete;
        android.view.ViewGroup layoutActions;
        VH(View v) {
            super(v);
            author = v.findViewById(R.id.textAuthor);
            title  = v.findViewById(R.id.textTitle);
            body   = v.findViewById(R.id.textBody);
            votes  = v.findViewById(R.id.textVotes);
            btnUp  = v.findViewById(R.id.btnUp);
            btnDown= v.findViewById(R.id.btnDown);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
            layoutActions = v.findViewById(R.id.layoutCommentActions);
        }
    }
}
