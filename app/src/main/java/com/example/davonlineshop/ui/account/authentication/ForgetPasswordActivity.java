package com.example.davonlineshop.ui.account.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordActivity extends AppCompatActivity {
    EditText editText;
    Button sendMessage;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        editText = findViewById(R.id.emailText);
        sendMessage = findViewById(R.id.sendMess);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sendMessInMail(View view) {
        firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "Please sign in in your email and change password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "This email is not registered in this application", Toast.LENGTH_LONG).show();
                }
            }
        });
        SendMail sendMail = new SendMail(getApplicationContext(), editText.getText().toString(), "Forget Password", "Forget Password");
        sendMail.execute();
    }
}