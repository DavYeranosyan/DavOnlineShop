package com.example.davonlineshop.ui.account.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.davonlineshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextView textView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textView = findViewById(R.id.text1);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference  = FirebaseDatabase.getInstance().getReference().child("users");
        if (firebaseAuth.getCurrentUser() != null){
            Query query = databaseReference.orderByChild("email").equalTo(firebaseAuth.getCurrentUser().getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Model model = child.getValue(Model.class);
                            textView.setText("Welcome " + model.getName() + " " + model.getSurname());
                        }
                    }else{
                        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void logOut(View view) {
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}