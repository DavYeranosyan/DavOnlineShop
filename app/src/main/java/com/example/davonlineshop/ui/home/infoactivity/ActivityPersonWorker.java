package com.example.davonlineshop.ui.home.infoactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.ui.account.accmenu.OpenMessageActivity;
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

public class ActivityPersonWorker extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;

    ImageView photo_person, cell_image, message_image;
    TextView name_surname, number, message;
    Button cell;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_worker);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getSharedPreferences("Worker", MODE_PRIVATE);

        String pathPhoto = sharedPreferences.getString("user_id", "");
        photo_person = findViewById(R.id.imagePerson);
        storageReference = firebaseStorage.getReference().child("profile_images/" + pathPhoto);
        Task<Uri> url = storageReference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                            Picasso.get().load(fileUrl).into(photo_person);
                        }
                    }
                });
        name_surname = findViewById(R.id.nameSurnamePerson);
        name_surname.setText(sharedPreferences.getString("user_name", "") + " " + sharedPreferences.getString("user_surname", ""));
        number = findViewById(R.id.personPhoneNumberForCall);
        message = findViewById(R.id.messagePersonShow);
        message_image = findViewById(R.id.messagePersonShowMessageBtn);
        cell = findViewById(R.id.cellPersonShow);
        cell_image = findViewById(R.id.cellPersonShowCellBtn);
        String a = sharedPreferences.getString("table_name", "");
        String b = sharedPreferences.getString("user_email", "");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(a);
        Query query = databaseReference.orderByChild("email").equalTo(b);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        number.setText(list.getPhone_number());
                        cell.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                String phone_number = "tel:" + list.getPhone_number();
                                intent.setData(Uri.parse(phone_number));
                                startActivity(intent);
                            }
                        });
                        cell_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                String phone_number = "tel:" + list.getPhone_number();
                                intent.setData(Uri.parse(phone_number));
                                startActivity(intent);
                            }
                        });
                        message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                                if (sharedPreferences.getString("email", "").equals("")) {
                                    Toast.makeText(getApplicationContext(), "Դուք մուտք չեք գործել հաշիվ։(", Toast.LENGTH_LONG).show();
                                } else {
                                    sharedPreferences.edit().putString("to_id", b).apply();
                                    startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
                                }
                            }
                        });
                        message_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                                if (sharedPreferences.getString("email", "").equals("")) {
                                    Toast.makeText(getApplicationContext(), "Դուք մուտք չեք գործել հաշիվ։(", Toast.LENGTH_LONG).show();
                                } else {
                                    sharedPreferences.edit().putString("to_id", b).apply();
                                    startActivity(new Intent(getApplicationContext(), OpenMessageActivity.class));
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

    }
}