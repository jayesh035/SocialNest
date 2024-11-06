package com.example.social_medial_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.StoryRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private StoryRvDesignBinding binding; // Removed unnecessary initialization here
    private Context context;
    private ArrayList<User> userList;

    public StoryAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = StoryRvDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, int position) {
        // Check if the image URL is not empty or null
        String imageUrl = userList.get(position).Image; // Get the image URL
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.user)  // Placeholder while loading
                    .error(R.drawable.exclamation)  // Error image if loading fails
                    .into(holder.binding.imageView3); // Load image into ImageView

        } else {
            // Load a default image if the URL is null or empty
            holder.binding.imageView3.setImageResource(R.drawable.user);
        }
        holder.binding.storyProfileName.setText(userList.get(position).Name);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private StoryRvDesignBinding binding;

        public ViewHolder(StoryRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding; // Store the binding for later use
        }
    }
}
