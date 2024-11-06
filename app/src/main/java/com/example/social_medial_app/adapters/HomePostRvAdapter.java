package com.example.social_medial_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.HomePost;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.HomePostRvDesignBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePostRvAdapter extends RecyclerView.Adapter<HomePostRvAdapter.ViewHolder> {
    private static final String TAG = "HomePostRvAdapter"; // For logging
    private Context context;
    private ArrayList<HomePost> postList;

    public HomePostRvAdapter(Context context, ArrayList<HomePost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public HomePostRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomePostRvDesignBinding binding = HomePostRvDesignBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostRvAdapter.ViewHolder holder, int position) {
        // Ensure the position is valid
        if (position < 0 || position >= postList.size()) {
            return; // Prevent any IndexOutOfBoundsException
        }

        HomePost post = postList.get(position);

        // Load post image
        String imageUrl = post.postUrl; // Get the image URL
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)  // Placeholder while loading
                    .error(R.drawable.exclamation)  // Error image if loading fails
                    .into(holder.binding.postImage);
        } else {
            // Load a default image if the URL is null or empty
            Log.w(TAG, "Post image URL is empty for position: " + position);
            holder.binding.postImage.setImageResource(R.drawable.loading);
        }

        // Load profile image
        String postProfile = post.profileImage; // Get the profile image URL
        if (!TextUtils.isEmpty(postProfile)) {
            Picasso.get()
                    .load(postProfile)
                    .placeholder(R.drawable.user)  // Placeholder while loading
                    .error(R.drawable.exclamation)  // Error image if loading fails
                    .into(holder.binding.postProfileImage);
        } else {
            // Load a default image if the URL is null or empty
            Log.w(TAG, "Profile image URL is empty for position: " + position);
            holder.binding.postProfileImage.setImageResource(R.drawable.user);
        }

        // Set text for profile name and description
        holder.binding.postProfileName.setText(post.profileName != null ? post.profileName : "Unknown");
        holder.binding.caption.setText(post.caption != null ? post.caption : "No description available.");

        holder.binding.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(post.isLiked)
                {
                    holder.binding.likeBtn.setImageResource(R.drawable.like);

                }
                else
                {
                    holder.binding.likeBtn.setImageResource(R.drawable.filled_like);
                }
                post.setLiked(!post.isLiked);

            }
        });

        holder.binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePost(post);
            }
        });

        String text = TimeAgo.using(Long.parseLong(post.uploadTime));
        holder.binding.uploadTime.setText(text);


    }
private  void sharePost(HomePost post)
{
    Intent shareIntent=new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    String shareText = "Check out this post:\n" + post.caption + "\n" + post.postUrl;
    shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
    // Start the share activity
    context.startActivity(Intent.createChooser(shareIntent, "Share post via"));
}

    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0; // Prevent NullPointerException
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private HomePostRvDesignBinding binding;

        public ViewHolder(HomePostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
