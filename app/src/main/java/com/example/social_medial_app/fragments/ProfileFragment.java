package com.example.social_medial_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_medial_app.Models.User;
import com.example.social_medial_app.R;
import com.example.social_medial_app.adapters.ViewPagerAdapter;
import com.example.social_medial_app.databinding.FragmentProfileBinding;
import com.example.social_medial_app.signUpActivity;
import com.example.social_medial_app.utils.Constants;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ViewPagerAdapter viewPagerAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        binding.editProfileButton.setOnClickListener(view -> {
            Intent signUp = new Intent(getActivity(), signUpActivity.class);
            signUp.putExtra("MODE", 1);
            requireActivity().startActivity(signUp);
        });

        // Initialize ViewPager2 and the adapter
        viewPagerAdapter = new ViewPagerAdapter(requireActivity());
        viewPagerAdapter.addFragment(new MyPostFragment(), "My Post");
        viewPagerAdapter.addFragment(new MyReelsFragment(), "My Reels");

        binding.viewPager.setAdapter(viewPagerAdapter);

        // Setup TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))
        ).attach();

        return binding.getRoot(); // Return the root view of the binding
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // To avoid memory leaks, set the binding to null when the view is destroyed
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection(Constants.USER_MODE)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        if (binding != null) {
                            binding.name.setText(user.Name);
                            binding.bio.setText(user.Email);
                            if (!user.Image.isEmpty()) {
                                Picasso.get().load(user.Image).into(binding.profileImage);
                            }
                        }
                    }
                });
    }
}
