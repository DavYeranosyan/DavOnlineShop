package com.example.davonlineshop.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.ui.home.infoactivity.ActivityFirstList;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class HomeFragment extends Fragment {

    ImageView imageView;
    SharedPreferences sharedPreferences;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        LinearLayout linearLayoutList1 = root.findViewById(R.id.linearLayoutLis1);
        searchListsProductsAndShow("list1", linearLayoutList1);

        LinearLayout linearLayoutList2 = root.findViewById(R.id.linearLayoutList2);
        searchListsProductsAndShow("list2", linearLayoutList2);

        LinearLayout linearLayoutList3 = root.findViewById(R.id.linearLayoutList3);
        searchListsProductsAndShow("list3", linearLayoutList3);

        LinearLayout linearLayoutList4 = root.findViewById(R.id.linearLayoutList4);
        searchListsProductsAndShow("list4", linearLayoutList4);

        return root;
    }


    public void searchListsProductsAndShow(String tableName, LinearLayout linearLayout) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        LinearLayout newLayout = new LinearLayout(getActivity());
                        newLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(350, 350);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(350, 150);
                        newLayout.setPadding(15, 15, 15, 15);
                        ImageView image = new ImageView(getContext());
                        TextView nameProduct = new TextView(getContext());
                        nameProduct.setText(list.getNameProduct());
                        nameProduct.setTextColor(Color.BLACK);
                        nameProduct.setTextSize(16);
                        newLayout.addView(image, layoutParams);
                        newLayout.addView(nameProduct, layoutParams1);
                        linearLayout.addView(newLayout);
                        String pathPhoto = "";
                        if (tableName.equals("list1")){
                            pathPhoto = list.getNameProduct() + list.getPrice()                                                                                                                                                                                                                    ;
                        }else {pathPhoto = list.getNameProduct();}
                        storageReference = firebaseStorage.getReference().child("images/" + pathPhoto);
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
                        nameProduct.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                preferences.putString("tableName", tableName);
                                preferences.putString(tableName, list.getId());
                                preferences.apply();
                                startActivity(new Intent(getContext(), ActivityFirstList.class));
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                preferences.putString("tableName", tableName);
                                preferences.putString(tableName, list.getId());
                                preferences.apply();
                                startActivity(new Intent(getContext(), ActivityFirstList.class));
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = firebaseStorage.getReference("images/" + UUID.randomUUID().toString());
        if (requestCode == 1) {
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Ստացվեց", Toast.LENGTH_LONG).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                        Picasso.get().load(fileUrl).into(imageView);
                    }
                }
            });
        }


    }
}