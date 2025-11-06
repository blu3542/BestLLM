package com.example.bestllm.ui.prompt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bestllm.R;
import com.example.bestllm.models.Prompt;
import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PromptAdapter extends RecyclerView.Adapter<PromptAdapter.VH> {

    public interface Actions {
        void onShare(Prompt p);
        void onEdit(Prompt p);
        void onDelete(Prompt p);
    }

    private final List<Prompt> items = new ArrayList<>();
    private final Actions actions;
    private final String currentUserId;

    public PromptAdapter(String currentUserId, Actions actions) {
        this.currentUserId = currentUserId;
        this.actions = actions;
    }

    public void submit(List<Prompt> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_prompt, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Context ctx = h.itemView.getContext();
        Prompt p = items.get(position);

        h.text.setText(p.getText());
        h.author.setText(p.getAuthorName() == null ? "Unknown" : p.getAuthorName());

        Timestamp ts = p.getCreatedAt();
        Date when = ts != null ? ts.toDate() : new Date();
        DateFormat df = android.text.format.DateFormat.getMediumDateFormat(ctx);
        h.date.setText(df.format(when));

        h.btnShare.setOnClickListener(v -> actions.onShare(p));

        boolean isOwner = p.getAuthorId() != null && p.getAuthorId().equals(currentUserId);
        h.btnEdit.setVisibility(isOwner ? View.VISIBLE : View.GONE);
        h.btnDelete.setVisibility(isOwner ? View.VISIBLE : View.GONE);

        h.btnEdit.setOnClickListener(v -> actions.onEdit(p));
        h.btnDelete.setOnClickListener(v -> actions.onDelete(p));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView text, author, date;
        ImageButton btnShare, btnEdit, btnDelete;
        VH(@NonNull View v) {
            super(v);
            text     = v.findViewById(R.id.tvPromptText);
            author   = v.findViewById(R.id.tvAuthorName);
            date     = v.findViewById(R.id.tvDate);
            btnShare = v.findViewById(R.id.btnSharePrompt);
            btnEdit  = v.findViewById(R.id.btnEditPrompt);
            btnDelete= v.findViewById(R.id.btnDeletePrompt);
        }
    }
}

