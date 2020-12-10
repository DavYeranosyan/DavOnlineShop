//package com.example.davonlineshop.ui.account.authentication;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.davonlineshop.R;
//import com.example.davonlineshop.ui.account.AccountFragment;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class LoginActivity extends AppCompatActivity {
//    EditText email, password;
//    FirebaseAuth firebaseAuth;
//    public static final String Email = "email";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        email = findViewById(R.id.emailLog);
//        password = findViewById(R.id.passwordLog);
//        firebaseAuth = FirebaseAuth.getInstance();
//    }
//
//    public void signIn(View view) {
//        if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
//            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if (!task.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//                    } else {
//                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                            SharedPreferences.Editor preferences = getSharedPreferences(Email, Context.MODE_PRIVATE).edit();
//                            preferences.putString("email", email.getText().toString()).apply();
//                            startActivity(new Intent(getApplicationContext(), AccountFragment.class));
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Email is invalid verified", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void signUp(View view) {
//        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//    }
//
//    public void inClickForget(View view) {
//        startActivity(new Intent(LoginActivity.this, ForGetPasswordActivity.class));
//    }
//}