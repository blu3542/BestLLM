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

    private final List<Comment> items = new ArrayList<>();
    private final OnVoteClick voteCb;

    public CommentAdapter(OnVoteClick voteCb) {
        this.voteCb = voteCb;
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView author, body, votes, btnUp, btnDown;
        VH(View v) {
            super(v);
            author = v.findViewById(R.id.textAuthor);
            body   = v.findViewById(R.id.textBody);
            votes  = v.findViewById(R.id.textVotes);
            btnUp  = v.findViewById(R.id.btnUp);
            btnDown= v.findViewById(R.id.btnDown);
        }
    }
}
