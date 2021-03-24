package com.example.davonlineshop.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragmentForManager extends Fragment {

    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    boolean bool;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_for_manager, container, false);
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(sharedPreferences.getString("email", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getValue(User.class).getType().equals(Type.ADMIN) || child.getValue(User.class).getType().equals(Type.MANAGER)) {
                            bool = true;
                            Log.d("my", "hasav1"+bool);
                        }
                        break;
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        if (!bool) {
//            root = inflater.inflate(R.layout.fragment_dashboard, container, false);
////            sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
////            String s = sharedPreferences.getString("email", "");
////            Log.d("my", "onCreateView: " + s);
//        } else {
//            root = inflater.inflate(R.layout.fragment_dashboard_for_manager, container, false);
////            sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
////            String s = sharedPreferences.getString("email", "");
////            Log.d("my", "onCreateView: " + s);
//        }
        return root;
    }


}