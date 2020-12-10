package com.example.davonlineshop.ui.account.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.example.davonlineshop.ui.account.AccountFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    EditText email, password;
    FirebaseAuth firebaseAuth;
    public static final String Email = "email";
    Button signIn, signUp;

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
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                            } else {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    SharedPreferences.Editor preferences = getActivity().getSharedPreferences(Email, Context.MODE_PRIVATE).edit();
                                    preferences.putString("email", email.getText().toString()).apply();
                                    ((MainActivity) requireActivity()).replaceFragments(AccountFragment.class);
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
                ((MainActivity) requireActivity()).replaceFragments(RegisterFragment.class);
            }
        });
        return root;
    }

}