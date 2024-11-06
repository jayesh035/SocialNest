package com.example.social_medial_app.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.databinding.SearchRvDesignBinding;
import com.example.social_medial_app.utils.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class SearchRvAdapter extends RecyclerView.Adapter<SearchRvAdapter.ViewHolder> {

    Context context;
    ArrayList<User> userList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public SearchRvAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public SearchRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchRvDesignBinding binding = SearchRvDesignBinding.inflate(LayoutInflater.from(context), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRvAdapter.ViewHolder holder, int position) {

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
        db = FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        final Boolean[] isFollow = {false};
        db.collection(Objects.requireNonNull(auth.getCurrentUser()).getUid()+Constants.FOLLOW).whereEqualTo("Email",user.Email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
           if(!queryDocumentSnapshots.getDocuments().isEmpty())
           {
               holder.binding.followBtn.setText("Unfollow");
               isFollow[0] =true;
           }
            }
        });
        holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();

            if(isFollow[0])
            {
                db.collection(uid+Constants.FOLLOW).whereEqualTo("Email",user.Email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                   db.collection(uid+Constants.FOLLOW).document(queryDocumentSnapshots.getDocuments().get(0).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                     isFollow[0]=false;
                     holder.binding.followBtn.setText("Follow");
                       }
                   });
                    }
                });
            }
            else
            {
                db.collection(uid+Constants.FOLLOW).add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        holder.binding.followBtn.setText("Unfollow");
                        isFollow[0]=true;
                    }
                });

            }

            }
        });
        holder.binding.name.setText(user.Name);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SearchRvDesignBinding binding;

        public ViewHolder(SearchRvDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
