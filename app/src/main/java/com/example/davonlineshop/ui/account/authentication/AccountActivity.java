package com.example.davonlineshop.ui.account.authentication;


import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.davonlineshop.R;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_account);
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void replaceFragments2(Class fragmentClass) {
//        Fragment fragment = null;
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (IllegalAccessException | java.lang.InstantiationException e) {
//            e.printStackTrace();
//        }
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frag_acc, fragment);
//        transaction.commit();
//    }
}