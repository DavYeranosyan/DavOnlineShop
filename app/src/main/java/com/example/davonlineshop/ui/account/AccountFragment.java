package com.example.davonlineshop.ui.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.account.authentication.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {


    SharedPreferences sharedPreferences;
    Button logout;
    TextView textView;
    DatabaseReference databaseReference;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());




        Log.e("my", "onCreateView: " + sharedPreferences.getString("email", ""));
        if (sharedPreferences.getString("email", "").equals("")) {
            ((MainActivity) requireActivity()).replaceFragments(LoginFragment.class);
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            Query query = databaseReference.orderByChild("email").equalTo(sharedPreferences.getString("email", ""));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            textView = root.findViewById(R.id.text);
                            textView.setText("Login in account -> " + user.getName() + " " + user.getSurname());
                            logout = root.findViewById(R.id.logOut);
                            logout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sharedPreferences.edit().putString("email", "").apply();
                                    ((MainActivity) requireActivity()).replaceFragments(LoginFragment.class);
                                }
                            });
                            break;
                        }
                    } else {
                        ((MainActivity) requireActivity()).replaceFragments(LoginFragment.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return root;
    }

}