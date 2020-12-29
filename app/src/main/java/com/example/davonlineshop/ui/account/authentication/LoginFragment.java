package com.example.davonlineshop.ui.account.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class LoginFragment extends Fragment {
    EditText email, password;
    FirebaseAuth firebaseAuth;
    public static final String Email = "email";
    Button signIn, signUp, forgetPass;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        email = root.findViewById(R.id.emailLog);
        password = root.findViewById(R.id.passwordLog);
        firebaseAuth = FirebaseAuth.getInstance();
        signIn = root.findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                            } else {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    SharedPreferences.Editor preferences = getActivity().getSharedPreferences(Email, Context.MODE_PRIVATE).edit();
                                    String e = email.getText().toString().toLowerCase();
                                    Log.e("my", "onComplete: " + e);
                                    preferences.putString("email", e);
                                    preferences.apply();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                } else {
                                    Toast.makeText(getContext(), "Email is invalid verified", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        signUp = root.findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                ((AccountActivity) requireActivity()).replaceFragments2(RegisterFragment.class);
                RegisterFragment f = new RegisterFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frag_acc, f);
                transaction.commit();
                Toast.makeText(getContext(), "sign up", Toast.LENGTH_LONG).show();
            }
        });
        forgetPass = root.findViewById(R.id.forget_password);
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                ((AccountActivity) requireActivity()).replaceFragments2(ForgetPasswordFragment.class);
                Toast.makeText(getContext(), "forget pass", Toast.LENGTH_LONG).show();
            }
        });
        return root;
    }

}