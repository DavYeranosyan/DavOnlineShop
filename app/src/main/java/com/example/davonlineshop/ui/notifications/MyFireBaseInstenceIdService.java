package com.example.davonlineshop.ui.notifications;

import android.content.Intent;
import android.util.Log;

import com.example.davonlineshop.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstenceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
//        String srt = FirebaseInstanceId.getInstance().getToken();
//        Common.currentToken = srt;
//        Log.e("my",FirebaseInstanceId.getInstance().getToken());

    }
}
