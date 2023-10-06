package com.example.mytask.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class UserId {

    @Exclude
    public String Userid;

    public <T extends UserId> T withId(@NonNull final String id) {
        this.Userid = id;
        return (T) this;

    }
}
