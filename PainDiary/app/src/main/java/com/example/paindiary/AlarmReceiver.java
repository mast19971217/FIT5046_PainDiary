package com.example.paindiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


            ToastUtil.newToast(context,"Please Enter your Pain Record!");


    }
}