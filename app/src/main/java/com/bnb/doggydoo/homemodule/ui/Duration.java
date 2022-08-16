package com.bnb.doggydoo.homemodule.ui;

//public class Duration {
//}

import com.google.gson.annotations.SerializedName;

public class Duration{
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @SerializedName("text")
    private String text;

    @SerializedName("value")
    private int value;
}
