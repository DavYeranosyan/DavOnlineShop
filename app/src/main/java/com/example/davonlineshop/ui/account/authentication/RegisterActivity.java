package com.example.davonlineshop.ui.account.authentication;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
    ProgressDialog spinner;


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
        spinner = new ProgressDialog(RegisterActivity.this);
        spinner.setTitle("Խնդրում ենք սպասել...");
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void saveMe(View view) {
        spinner.show();
        if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        spinner.cancel();
                        Toast.makeText(getApplicationContext(), "Ինչոր բան այն չէ։", Toast.LENGTH_LONG).show();
                    } else {
                        spinner.show();
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    String nameF = name.getText().toString();
                                    String surnameF = surname.getText().toString();
                                    String emailF = email.getText().toString().toLowerCase();
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
                                    getAlert(true);
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                }
            });
        }else    spinner.cancel();
    }

    public void getAlert(boolean ok){
        if (ok){
            spinner.cancel();
            AlertDialog.Builder builderB = new AlertDialog.Builder(RegisterActivity.this);
            builderB.setMessage("Շնորհակալություն հենց Մեզ ընտրելու համար։\nԻսկ հիմա խնդրում ենք Ձեզ մուտք գործեք Ձեր Էլ․ փոստ, մենք Ձեզ ուղարկել ենք հաղորդագրություն որում կա հղում, սեղմեք հղման վրա և Դուք կվերիֆիկացվեք և կարող եք մուտք գործել Ձեր նոր հաշիվ։").setNegativeButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }).setPositiveButton("Բարի։)", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            });
            AlertDialog alertDialog = builderB.create();
            alertDialog.show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return true;
    }

}