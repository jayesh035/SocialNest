package com.example.social_medial_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.social_medial_app.Models.Message;
import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.adapters.ChatAdapter;
import com.example.social_medial_app.databinding.ActivityChatBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private FirebaseFirestore db;
    private String reciver_id;
    private FirebaseAuth auth;

    // Member variable for adapter and message list
    private ChatAdapter adapter;
    private ArrayList<Message> messageList;
    private ListenerRegistration listenerRegistration; // Add this for listener registration

    private void setCurrentUser(String userEmail) {
        db = FirebaseFirestore.getInstance();
        db.collection(Constants.USER_MODE).whereEqualTo("Email", userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                reciver_id = queryDocumentSnapshots.getDocuments().get(0).getId();
                User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                if (user != null) {
                    // Load profile image
                    if (user.Image != null && !user.Image.isEmpty()) {
                        Picasso.get()
                                .load(user.Image)
                                .placeholder(R.drawable.user)  // Placeholder image while loading
                                .into(binding.profileImage);
                    } else {
                        Picasso.get()
                                .load(R.drawable.user)  // Default image for missing URL
                                .into(binding.profileImage);
                    }

                    // Set user name and status
                    binding.name.setText(user.Name);
                    binding.bio.setText("online");

                    // Call loadMessages after reciver_id is set
                    String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    loadMessages(uid, reciver_id);
                }
            }
        });
    }

    void loadMessages(String uid_par, String re_id_par) {
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList, this);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(adapter);

        String path = uid_par + re_id_par + Constants.CHAT;
        int comparisonResult = re_id_par.compareToIgnoreCase(uid_par);
        if (comparisonResult < 0) {
            path = re_id_par + uid_par + Constants.CHAT;
        }

        // Set up a listener for real-time updates
        listenerRegistration = db.collection(path)
                .orderBy("timestamp") // Order by timestamp to show messages in the correct order
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(ChatActivity.this, "Error loading messages", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        TreeMap<Long, Message> map = new TreeMap<>();

                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            Message ms = ds.toObject(Message.class);
                            assert ms != null;
                            map.put(ms.timestamp, ms);
                        }

                        messageList.clear(); // Clear the current list

                        for (Message newMessage : map.values()) {
                            if (!messageList.contains(newMessage)) { // Check for duplicates
                                messageList.add(newMessage);
                            }
                        }

                        adapter.notifyDataSetChanged(); // Notify the adapter of changes
                        // Scroll to bottom after data is set
                        if (!messageList.isEmpty()) {
                            binding.recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent it = getIntent();
        String userEmail = it.getStringExtra("email");
        auth = FirebaseAuth.getInstance();

        setCurrentUser(userEmail);

        // Add OnFocusChangeListener to EditText for scrolling when clicked
        binding.editTextMessage.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Scroll to bottom when EditText is touched
                if (messageList.size() > 0)
                    binding.recyclerViewMessages.post(() ->
                            binding.recyclerViewMessages.smoothScrollToPosition(adapter.getItemCount() - 1)
                    );
            }
            return false; // Allow other events like clicks to be processed
        });

        binding.recyclerViewMessages.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    binding.recyclerViewMessages.postDelayed(() -> {
                        if (!messageList.isEmpty()) {
                            binding.recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                        }
                    }, 100);
                }
            }
        });

        binding.buttonSend.setOnClickListener(view -> {
            String message_text = binding.editTextMessage.getText().toString();
            long timestamp = System.currentTimeMillis();

            Message message = new Message(Objects.requireNonNull(auth.getCurrentUser()).getUid(), message_text, timestamp);
            String path = Objects.requireNonNull(auth.getCurrentUser()).getUid() + reciver_id + Constants.CHAT;
            int comparisonResult = reciver_id.compareToIgnoreCase(Objects.requireNonNull(auth.getCurrentUser()).getUid());
            if (comparisonResult < 0) {
                path = reciver_id + Objects.requireNonNull(auth.getCurrentUser()).getUid() + Constants.CHAT;
            }
            db.collection(path)
                    .add(message)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ChatActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                        binding.editTextMessage.setText("");

                        // Add new message to the list and notify adapter
//                        messageList.add(message); // Add the new message to the list
//                        adapter.notifyItemInserted(messageList.size() - 1); // Notify adapter of the new item

                        // Scroll to bottom after adding a new message
//                        binding.recyclerViewMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
                    });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove(); // Unregister listener
        }
    }
}
