package com.example.retia.don_speaker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by retia on 2017/08/22.
 */

public class AccountEntity {
    @SerializedName("display_name")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }
}
