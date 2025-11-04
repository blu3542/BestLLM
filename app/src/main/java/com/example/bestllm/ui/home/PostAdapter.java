package com.example.bestllm.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.bestllm.R;
import com.example.bestllm.models.Post;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private OnPostClickListener listener;

    public interface OnPostClickListener {
        void onPostClick(Post post);
    }

    public PostAdapter(List<Post> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post, listener);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private MaterialCardView cardView;
        private TextView textViewTitle;
        private TextView textViewBody;
        private TextView textViewAuthor;
        private TextView textViewDate;
        private TextView textViewVotes;
        private TextView textViewComments;
        private ChipGroup chipGroupTags;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewBody = itemView.findViewById(R.id.textViewBody);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewVotes = itemView.findViewById(R.id.textViewVotes);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            chipGroupTags = itemView.findViewById(R.id.chipGroupTags);
        }

        public void bind(Post post, OnPostClickListener listener) {
            textViewTitle.setText(post.getTitle());
            
            // Truncate body for preview
            String body = post.getBody();
            if (body.length() > 150) {
                body = body.substring(0, 150) + "...";
            }
            textViewBody.setText(body);
            
            textViewAuthor.setText(post.getAuthorName());
            
            // Format date
            if (post.getCreatedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                textViewDate.setText(sdf.format(post.getCreatedAt().toDate()));
            }
            
            // Display vote count
            int netVotes = post.getNetVotes();
            textViewVotes.setText(String.valueOf(netVotes));
            
            // Display comment count
            textViewComments.setText(String.valueOf(post.getCommentCount()));
            
            // Display tags (limit to first 3 for preview)
            chipGroupTags.removeAllViews();
            if (post.getTags() != null && !post.getTags().isEmpty()) {
                int tagCount = Math.min(post.getTags().size(), 3);
                for (int i = 0; i < tagCount; i++) {
                    Chip chip = new Chip(itemView.getContext());
                    chip.setText(post.getTags().get(i));
                    chip.setClickable(false);
                    chip.setCheckable(false);
                    chipGroupTags.addView(chip);
                }
                
                // Add "more" indicator if there are more tags
                if (post.getTags().size() > 3) {
                    Chip chip = new Chip(itemView.getContext());
                    chip.setText("+" + (post.getTags().size() - 3) + " more");
                    chip.setClickable(false);
                    chip.setCheckable(false);
                    chipGroupTags.addView(chip);
                }
            }
            
            cardView.setOnClickListener(v -> listener.onPostClick(post));
        }
    }
}

