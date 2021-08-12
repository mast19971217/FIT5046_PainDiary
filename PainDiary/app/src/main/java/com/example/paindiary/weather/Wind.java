package com.example.paindiary.weather;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;
}
