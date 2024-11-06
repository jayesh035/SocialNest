package com.example.social_medial_app.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.Post;
import com.example.social_medial_app.databinding.MyPostRvDesignBinding;
import com.example.social_medial_app.databinding.MyReelRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPostRvAdapter extends RecyclerView.Adapter<MyPostRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postList;

    public MyPostRvAdapter(Context context, ArrayList<Post> postList) {
        this.context = context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MyPostRvDesignBinding binding = MyPostRvDesignBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind your data here
//        return
//        holder.binding.postImage=
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.postImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MyPostRvDesignBinding binding;

        public ViewHolder(MyPostRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
