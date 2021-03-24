package com.example.davonlineshop.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.davonlineshop.R;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        String s = sharedPreferences.getString("email", "");
        Log.d("my", "onCreateView: "+ s);

        return root;
    }


}