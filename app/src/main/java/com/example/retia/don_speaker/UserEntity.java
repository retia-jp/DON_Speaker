package com.example.retia.don_speaker;

import com.google.gson.annotations.*;

/**
 * Created by retia on 2017/07/06.
 */

public class UserEntity {
    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}