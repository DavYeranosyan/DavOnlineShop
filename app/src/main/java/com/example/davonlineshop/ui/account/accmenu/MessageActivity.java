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

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Message;
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
    LinearLayout layoutFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);

        layoutFullScreen = findViewById(R.id.layout_Fullscreen_message);
        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        searchMessages();


    }

    public void searchMessages() {
        String user_id = sharedPreferences.getString("user_id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot child : snapshot.getChildren()) {
                        Message message = child.getValue(Message.class);
                        if (message.getId_one().equals(user_id)) {
                            layoutFullScreen.removeAllViews();
                            searchUser(message.getId_two());
                        } else if (message.getId_two().equals(user_id)) {
                            layoutFullScreen.removeAllViews();
                            searchUser(message.getId_one());
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchUser(String user_id) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference1.orderByChild("id").equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        LinearLayout newLayout = new LinearLayout(getApplication());
                        newLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 120);
                        newLayout.setPadding(25, 25, 25, 35);
                        layoutParams1.setMargins(20,0,0,0);
                        ImageView image = new ImageView(getApplication());
                        TextView name_surname = new TextView(getApplication());
                        name_surname.setText(user.getName() + " " + user.getSurname());
                        name_surname.setTextColor(Color.BLACK);
                        name_surname.setGravity(Gravity.START);
                        name_surname.setTextSize(20);
                        newLayout.addView(image, layoutParams);
                        newLayout.addView(name_surname, layoutParams1);
                        layoutFullScreen.addView(newLayout);

                        String pathPhoto = user.getId();
                        storageReference = firebaseStorage.getReference().child("profile_images/" + pathPhoto);
                        Task<Uri> url = storageReference.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                            Picasso.get().load(fileUrl).into(image);
                                        }
                                    }
                                });
                        name_surname.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getSharedPreferences("Worker", Context.MODE_PRIVATE).edit();
                                preferences.putString("user_id", user.getId());
                                preferences.putString("user_email", user.getEmail());
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getSharedPreferences("Worker", Context.MODE_PRIVATE).edit();
                                preferences.putString("user_id", user.getId());
                                preferences.putString("user_email", user.getEmail());
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}