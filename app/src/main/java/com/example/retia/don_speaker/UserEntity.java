package com.example.retia.don_speaker;

import com.google.gson.annotations.*;

/**
 * Created by retia on 2017/07/06.
 */

public class UserEntity {
    @SerializedName("id")
    private long id;
    @SerializedName("content")
    private String content;
    @SerializedName("account")
    private AccountEntity account;

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public AccountEntity getAccount() {
        return account;
    }
}
