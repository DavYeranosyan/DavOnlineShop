package com.example.davonlineshop.ui.account.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    EditText name, surname, email, age, password;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        password = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void saveMe(View view) {
        if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    } else {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String nameF = name.getText().toString();
                                    String surnameF = surname.getText().toString();
                                    String emailF = email.getText().toString().toLowerCase();
                                    Log.e("my", "onComplete: " + email.getText().toString());
                                    String ageF = age.getText().toString();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("users").push();
                                    User model = new User();
                                    model.setId(databaseReference.getKey());
                                    model.setName(nameF);
                                    model.setSurname(surnameF);
                                    model.setEmail(emailF);
                                    model.setType(Type.USER);
                                    model.setAge(Integer.parseInt(ageF));
                                    databaseReference.setValue(model);
                                    Toast.makeText(getApplicationContext(), "Werryyy Gooood", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    public void signIn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}