package com.example.davonlineshop.ui.account.authentication;

import android.app.ProgressDialog;
import android.content.Context;
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

    //    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.activity_login, container, true);
//        email = root.findViewById(R.id.emailLog);
//        password = root.findViewById(R.id.passwordLog);
//        firebaseAuth = FirebaseAuth.getInstance();
//        signIn = root.findViewById(R.id.signIn);
//        signIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
//                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
//                            } else {
//                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
//                                    SharedPreferences.Editor preferences = getActivity().getSharedPreferences(Email, Context.MODE_PRIVATE).edit();
//                                    String e = email.getText().toString().toLowerCase();
//                                    Log.e("my", "onComplete: " + e);
//                                    preferences.putString("email", e);
//                                    preferences.apply();
//                                    startActivity(new Intent(getActivity(), MainActivity.class));
//                                } else {
//                                    Toast.makeText(getContext(), "Email is invalid verified", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        signUp = root.findViewById(R.id.signUp);
//        signUp.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//                Fragment f = new RegisterActivity();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frag_acc, f);
//                transaction.commit();
//            }
//        });
//        forgetPass = root.findViewById(R.id.forget_password);
//        forgetPass.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
////                ((AccountActivity) requireActivity()).replaceFragments2(ForgetPasswordFragment.class);
//            }
//        });
//        return root;
//    }
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
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getApplicationContext(), "Ձեր հաշիվը վերիֆիկացված չէ։(\nԽնդրում ենք մուտք գործեք Ձեր Էլեկտրոնային փոստ Ձեզ մեր կողմից ւղարկվել է հղում սեղմեք հղման վրա եվ դուք կվերիֆիկացվեք", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
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
