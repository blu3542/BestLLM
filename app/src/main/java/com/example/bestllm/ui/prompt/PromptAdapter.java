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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

        h.title.setText(p.getTitle() != null ? p.getTitle() : "Untitled Prompt");
        h.text.setText(p.getText());
        h.author.setText(p.getAuthorName() == null ? "Unknown" : p.getAuthorName());

        // Display tags using Chips (similar to PostAdapter)
        h.chipGroupTags.removeAllViews();
        if (p.getTags() != null && !p.getTags().isEmpty()) {
            int tagCount = Math.min(p.getTags().size(), 3);
            for (int i = 0; i < tagCount; i++) {
                Chip chip = new Chip(ctx);
                chip.setText(p.getTags().get(i));
                chip.setClickable(false);
                chip.setCheckable(false);
                h.chipGroupTags.addView(chip);
            }
            
            // Add "more" indicator if there are more tags
            if (p.getTags().size() > 3) {
                Chip chip = new Chip(ctx);
                chip.setText("+" + (p.getTags().size() - 3) + " more");
                chip.setClickable(false);
                chip.setCheckable(false);
                h.chipGroupTags.addView(chip);
            }
        }

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
        TextView title, text, author, date;
        ChipGroup chipGroupTags;
        ImageButton btnShare, btnEdit, btnDelete;
        VH(@NonNull View v) {
            super(v);
            title         = v.findViewById(R.id.tvPromptTitle);
            text          = v.findViewById(R.id.tvPromptText);
            chipGroupTags = v.findViewById(R.id.chipGroupTags);
            author        = v.findViewById(R.id.tvAuthorName);
            date          = v.findViewById(R.id.tvDate);
            btnShare      = v.findViewById(R.id.btnSharePrompt);
            btnEdit       = v.findViewById(R.id.btnEditPrompt);
            btnDelete     = v.findViewById(R.id.btnDeletePrompt);
        }
    }
}

