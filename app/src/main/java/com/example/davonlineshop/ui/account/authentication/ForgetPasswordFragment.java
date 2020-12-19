package com.example.davonlineshop.ui.account.authentication;

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

import com.example.davonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordFragment extends Fragment {
    EditText editText;
    Button sendMessage;
    FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View root = inflater.inflate(R.layout.fragmnet_forget_password, container, false);
        editText = root.findViewById(R.id.emailText);
        sendMessage = root.findViewById(R.id.sendMess);
        firebaseAuth = FirebaseAuth.getInstance();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Please sign in in your email and change password", Toast.LENGTH_LONG).show();
                            ((AccountActivity) requireActivity()).replaceFragments2(LoginFragment.class);
                        }else {
                            Toast.makeText(getContext(), "This email is not registered in this application", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                SendMail sendMail = new SendMail(getContext(), /*editText.getText().toString()*/"dav.yeranosyan@gmail.com", "Forget Password", "Forget Password");
                sendMail.execute();
            }
        });
        return root;
    }

}