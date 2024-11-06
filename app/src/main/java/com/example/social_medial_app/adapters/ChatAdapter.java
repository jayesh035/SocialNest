package com.example.social_medial_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.Message;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.ChatRvDesignBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Message>messageList;
    private Context context;
    private FirebaseAuth auth;
//    private FirebaseFirestore db;
    private ChatRvDesignBinding binding;
    public ChatAdapter(ArrayList<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ChatRvDesignBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
//        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        String currentUser= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        Message message=messageList.get(position);

        holder.binding.messageText.setText(message.message_text);
        holder.binding.timestamp.setText(formatTimeStamp(message.timestamp));
        if (message.senderId.equals(currentUser)) {
            // Current user is the sender
            holder.binding.messageText.setBackgroundResource(R.drawable.message_bubble_sent);
            holder.binding.messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.binding.timestamp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.binding.timestamp.setTextColor(context.getResources().getColor(android.R.color.holo_purple)); // White for sent messages

            // Align the message to the right (end)
            ConstraintLayout.LayoutParams messageParams = (ConstraintLayout.LayoutParams) holder.binding.messageText.getLayoutParams();
            messageParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID; // Align to the right
            messageParams.startToStart = ConstraintLayout.LayoutParams.UNSET; // Unset left alignment
            holder.binding.messageText.setLayoutParams(messageParams);

            // Align the timestamp to the right (end) within the message bubble
            ConstraintLayout.LayoutParams timestampParams = (ConstraintLayout.LayoutParams) holder.binding.timestamp.getLayoutParams();
            timestampParams.endToEnd = R.id.message_text; // Align timestamp to the end of message text
            timestampParams.startToStart = ConstraintLayout.LayoutParams.UNSET; // Unset left alignment
            holder.binding.timestamp.setLayoutParams(timestampParams);

        } else {
            // Current user is the receiver
            holder.binding.messageText.setBackgroundResource(R.drawable.message_bubble_recived);
            holder.binding.messageText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.timestamp.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.binding.timestamp.setTextColor(context.getResources().getColor(android.R.color.holo_purple)); // Black for received messages

            // Align the message to the left (start)
            ConstraintLayout.LayoutParams messageParams = (ConstraintLayout.LayoutParams) holder.binding.messageText.getLayoutParams();
            messageParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID; // Align to the left
            messageParams.endToEnd = ConstraintLayout.LayoutParams.UNSET; // Unset right alignment
            holder.binding.messageText.setLayoutParams(messageParams);

            // Align the timestamp to the left (start) within the message bubble
            ConstraintLayout.LayoutParams timestampParams = (ConstraintLayout.LayoutParams) holder.binding.timestamp.getLayoutParams();
            timestampParams.startToStart = R.id.message_text; // Align timestamp to the start of message text
            timestampParams.endToEnd = ConstraintLayout.LayoutParams.UNSET; // Unset right alignment
            holder.binding.timestamp.setLayoutParams(timestampParams);
        }


    }

    private String formatTimeStamp(long currentTimeMillis) {


        // Convert milliseconds to LocalDateTime
        // Check if device is running API level 26 or higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // For devices running API level 26 or higher
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
            return dateTime.format(formatter);
        } else {
            // For older devices
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(new Date(currentTimeMillis));
        }
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
       private final ChatRvDesignBinding binding;

        public ViewHolder(ChatRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
