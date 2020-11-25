package com.example.davonlineshop.ui.notifications;

import android.content.Intent;

import com.example.davonlineshop.ui.Common;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstenceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        String srt = FirebaseInstanceId.getInstance().getToken();
        Common.currentToken = srt;
    }
}
