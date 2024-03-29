package com.example.davonlineshop.ui.account.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.davonlineshop.AdminActivity;
import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.account.AccountFragment;
import com.example.davonlineshop.ui.home.infoactivity.ActivityPersonWorker;
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
import com.squareup.picasso.Picasso;


public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    FirebaseAuth firebaseAuth;
    public static final String Email = "email";
    Button signIn, signUp, forgetPass;
    ProgressDialog spinner;
    DatabaseReference databaseReference;
    FirebaseAuth auth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailLog);
        password = findViewById(R.id.passwordLog);
        firebaseAuth = FirebaseAuth.getInstance();
        spinner = new ProgressDialog(LoginActivity.this);
        spinner.setTitle("Կատարվում է հարցում խնդրում ենք սպասել...");
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    public void signUp(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void signIn(View view) {
//        spinner.show();
        if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
//                        spinner.cancel();
                        Toast.makeText(getApplicationContext(), "Ինչոր բան այն չէ։", Toast.LENGTH_LONG).show();
                    } else {
                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                            spinner.show();
                            SharedPreferences.Editor preferences = getSharedPreferences(Email, Context.MODE_PRIVATE).edit();
                            String e = email.getText().toString().toLowerCase();
                            preferences.putString("email", e);
                            getUserId(email.getText().toString(), preferences);
                            preferences.apply();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            AlertDialog.Builder builderB = new AlertDialog.Builder(LoginActivity.this);
                            builderB.setTitle("Ձեր հաշիվը վերիֆիկացված չէ։(").setMessage("Խնդրում ենք մուտք գործեք Ձեր Էլեկտրոնային փոստ Ձեզ մեր կողմից ուղարկվել է հղում սեղմեք հղման վրա և դուք կվերիֆիկացվեք").setNegativeButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).setPositiveButton("Բարի։)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = builderB.create();
                            alertDialog.show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Ինչոր բան այն չէ։", Toast.LENGTH_LONG).show();
        }
    }

    public void getUserId(String email, SharedPreferences.Editor pref) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        String id = user.getId();
                        pref.putString("user_id", id);
                        pref.apply();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void forgetPassword(View view) {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }
}
