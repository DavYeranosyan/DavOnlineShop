package com.example.davonlineshop;

import com.example.davonlineshop.service.ApiService;
import com.example.davonlineshop.ui.notifications.RetrofitClient;

public class Common {
    public static  String currentToken = "";
    public static String urls = "https://fcm.googleapis.com/";

    public static ApiService getFCMClient(){
        return RetrofitClient.getRetrofit(urls).create(ApiService.class);
    }
}
