package com.example.social_medial_app.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.ReelDgBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReelAdapter extends RecyclerView.Adapter<ReelAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Reel> reelList;

    public ReelAdapter(Context context, ArrayList<Reel> reelList) {
        this.context = context;
        this.reelList = reelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReelDgBinding binding = ReelDgBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reel reel = reelList.get(position);

        // Load profile image with null check
        if (reel.profileLink != null && !reel.profileLink.isEmpty()) {
            Picasso.get()
                    .load(reel.profileLink)
                    .placeholder(R.drawable.user)   // Default placeholder
                    .error(R.drawable.exclamation)  // Error image if loading fails
                    .into(holder.binding.profileImage);
        } else {
            holder.binding.profileImage.setImageResource(R.drawable.user);  // Default image if URL is invalid
        }

        // Set caption
        holder.binding.caption.setText(reel.caption);

        // Video handling
        holder.binding.videoView.setVideoPath(reel.videoUrl);
        holder.binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                holder.binding.progressBar.setVisibility(View.GONE);
                holder.binding.videoView.start();  // Start playing the video
            }
        });

        // Handle video playback errors
        holder.binding.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                // Show error UI or retry logic
                holder.binding.progressBar.setVisibility(View.GONE);
                holder.binding.videoView.setVisibility(View.GONE);
                // You can also log the error for further debugging
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reelList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ReelDgBinding binding;

        public ViewHolder(ReelDgBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
