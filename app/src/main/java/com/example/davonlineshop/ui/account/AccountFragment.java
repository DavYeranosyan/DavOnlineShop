package com.example.davonlineshop.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.davonlineshop.R;
import com.example.davonlineshop.ui.account.authentication.LoginActivity;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;

    public AccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}