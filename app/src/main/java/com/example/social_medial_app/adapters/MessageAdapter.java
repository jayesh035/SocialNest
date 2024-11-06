package com.example.social_medial_app.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.ChatActivity;
import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.MessageRvDesignBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
   Context context;
   ArrayList<User>userList;

    public MessageAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    private MessageRvDesignBinding binding;
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=MessageRvDesignBinding.inflate(LayoutInflater.from(context),parent,false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        if (position < 0 || position >= userList.size()) {
            return; // Prevent any IndexOutOfBoundsException
        }

        User user = userList.get(position);

        // Load post image
        String imageUrl = user.Image; // Get the image URL
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)  // Placeholder while loading
                    .error(R.drawable.exclamation)  // Error image if loading fails
                    .into(holder.binding.profileImage);
        } else {
            // Load a default image if the URL is null or empty
            Log.w(TAG, "Post image URL is empty for position: " + position);
            holder.binding.profileImage.setImageResource(R.drawable.user);
        }

        holder.binding.name.setText(user.Name);
        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(context, ChatActivity.class);
                it.putExtra("email", user.Email);
               context.startActivity(it);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       private  MessageRvDesignBinding binding;

        public ViewHolder(MessageRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
