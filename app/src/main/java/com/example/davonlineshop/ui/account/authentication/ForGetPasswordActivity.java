package com.example.davonlineshop.ui.account.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.davonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForGetPasswordActivity extends AppCompatActivity {
    EditText editText;
    Button sendMessage;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_get_password);
        editText = findViewById(R.id.emailText);
        sendMessage = findViewById(R.id.sendMess);
        firebaseAuth = FirebaseAuth.getInstance();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Please sign in in your email and change password", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "This email is not registered in this application", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                SendMail sendMail = new SendMail(getApplicationContext(), /*editText.getText().toString()*/"dav.yeranosyan@gmail.com", "Forget Password", "Forget Password");
                sendMail.execute();
            }
        });
    }
}