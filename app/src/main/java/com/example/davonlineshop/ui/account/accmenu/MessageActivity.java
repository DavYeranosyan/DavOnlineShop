package com.example.davonlineshop.ui.account.accmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Chat;
import com.example.davonlineshop.model.Message;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MessageActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    LinearLayout messageLayout;
    boolean messageNull = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);

        messageLayout = findViewById(R.id.message_label_first);
        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(sharedPreferences.getString("email", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        if (user.getType() == Type.ADMIN) {
                            messageLayout.setVisibility(View.GONE);
                            messageNull = false;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LinearLayout layoutFullScreen = findViewById(R.id.layout_fullscreen);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
        Query query1 = databaseReference;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Chat message = child.getValue(Chat.class);
                        messageNull = true;
                        LinearLayout linearLayout  =new LinearLayout(getApplication());
                        ImageView image = new ImageView(getApplication());
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getSharedPreferences("userId", Context.MODE_PRIVATE).edit();
                                preferences.putString("userId", message.getFrom_id());
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
                            }
                        });
                        TextView name_surname = new TextView(getApplication());
                        name_surname.setText(searchUser(message.getFrom_id()));
                        name_surname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getSharedPreferences("userId", Context.MODE_PRIVATE).edit();
                                preferences.putString("userId", message.getFrom_id());
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
                            }
                        });
                        linearLayout.addView(image);
                        linearLayout.addView(name_surname);
                        storageReference = firebaseStorage.getReference().child("profile_images/" + message.getFrom_id());
                        Task<Uri> url =  storageReference.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                            Picasso.get().load(fileUrl).into(image);
                                        }
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (!messageNull){
            TextView textView = findViewById(R.id.textMessageIsEmpty);
            textView.setText("Հաղորդագրություներ չկան։");
            textView.setTextColor(Color.BLACK);
        }





    }

    public String searchUser(String user_id){
        String[] name = {""};
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference1.orderByChild("id").equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        name[0] = user.getName() + " " +user.getSurname();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return name[0];
    }
    public void clickMess(View view) {
        startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
    }
}