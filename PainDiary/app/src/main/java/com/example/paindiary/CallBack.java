package com.example.paindiary;

public interface CallBack<T> {

    void onSucess(T painRecord);
    void onFailure();
}