package com.example.social_medial_app.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.social_medial_app.Models.Post;

import java.util.ArrayList;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Post>> postList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<ArrayList<Post>> getPostList() {
        return postList;
    }

    public void setPostList(ArrayList<Post> posts) {
        postList.setValue(posts);
    }
}