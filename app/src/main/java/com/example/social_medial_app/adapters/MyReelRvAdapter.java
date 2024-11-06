package com.example.social_medial_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.Models.Reel;
import com.example.social_medial_app.databinding.MyPostRvDesignBinding;
import com.example.social_medial_app.databinding.MyReelRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyReelRvAdapter extends RecyclerView.Adapter<MyReelRvAdapter.ViewHolder> {
private final Context context;
private final ArrayList<Reel> reelList;


public MyReelRvAdapter(Context context, ArrayList<Reel> reelList) {
    this.context = context;
    this.reelList = reelList;
}

@NonNull
@Override
public MyReelRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(context);
    MyReelRvDesignBinding binding = MyReelRvDesignBinding.inflate(inflater, parent, false);

    return new ViewHolder(binding);
}

@Override
public void onBindViewHolder(MyReelRvAdapter.ViewHolder holder, int position) {
    // Bind your data here
//        return
//        holder.binding.postImage=

    Glide.with(context).load(reelList.get(position).videoUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.binding.postImage);
}

@Override
public int getItemCount() {
    return reelList.size();
}

public static class ViewHolder extends RecyclerView.ViewHolder {
    private final MyReelRvDesignBinding binding;

    public ViewHolder(MyReelRvDesignBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
}