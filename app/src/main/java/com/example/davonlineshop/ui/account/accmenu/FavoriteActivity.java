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
import com.example.davonlineshop.model.Favorite;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.ui.home.infoactivity.ActivityFirstList;
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

public class FavoriteActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    LinearLayout favorite_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);

        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        favorite_layout = findViewById(R.id.favorite_layout_activity);
        searchFavoriteProducts();
    }


    public void searchFavoriteProducts() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");
        Query query = databaseReference.orderByChild("user_id").equalTo(sharedPreferences.getString("user_id", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Favorite list = child.getValue(Favorite.class);
                        favorite_layout.removeAllViews();
                        searchListsProductsAndShow(list.getTable_name(), list.getFavorite_product_id());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchListsProductsAndShow(String tableName, String product_id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(product_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);

                        if (tableName.equals("list1") || tableName.equals("marketproducts")) {
                            LinearLayout fullLayout = new LinearLayout(getApplication());
                            LinearLayout newLayout = new LinearLayout(getApplication());
                            LinearLayout newLayout2 = new LinearLayout(getApplication());

                            newLayout.setOrientation(LinearLayout.VERTICAL);
                            newLayout.setPadding(15, 15, 15, 15);
                            newLayout2.setOrientation(LinearLayout.HORIZONTAL);
                            fullLayout.setOrientation(LinearLayout.HORIZONTAL);
                            fullLayout.setBackgroundResource(R.drawable.border_full_project);
                            fullLayout.setPadding(15, 15, 15, 15);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
                            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);
                            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                            ImageView image = new ImageView(getApplication());
                            TextView name_product = new TextView(getApplication());
                            name_product.setText(list.getNameProduct());
                            name_product.setTextColor(Color.BLACK);
                            name_product.setTextSize(26);
                            fullLayout.addView(image, layoutParams);
                            fullLayout.addView(newLayout, layoutParams3);

                            newLayout.addView(name_product, layoutParams1);

                            TextView price = new TextView(getApplication());
                            price.setText(list.getPrice() + " AMD");
                            price.setTextSize(26);
                            price.setGravity(Gravity.END);
                            newLayout2.setGravity(Gravity.END);
                            newLayout2.addView(price, layoutParams2);
                            newLayout.addView(newLayout2);

                            favorite_layout.addView(fullLayout);
                            String pathPhoto = "";
                            if (tableName.equals("list1")) {
                                pathPhoto = "images/" + list.getNameProduct() + list.getPrice();
                            } else if (tableName.equals("marketproducts")) {
                                pathPhoto = "imagesMarket/" + list.getNameProduct() + list.getPrice();
                            } else {
                                pathPhoto = "images/" + list.getNameProduct();
                            }
                            storageReference = firebaseStorage.getReference().child(pathPhoto);
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
                            name_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences.Editor preferences = getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                    preferences.putString("tableName", tableName);
                                    preferences.putString(tableName, list.getId());
                                    preferences.apply();
                                    startActivity(new Intent(getApplicationContext(), ActivityFirstList.class));
                                }
                            });
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences.Editor preferences = getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                    preferences.putString("tableName", tableName);
                                    preferences.putString(tableName, list.getId());
                                    preferences.apply();
                                    startActivity(new Intent(getApplicationContext(), ActivityFirstList.class));
                                }
                            });
                        } else {
                            LinearLayout newLayout = new LinearLayout(getApplication());
                            newLayout.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
                            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);
                            newLayout.setPadding(15, 15, 15, 15);
                            ImageView image = new ImageView(getApplication());
                            TextView nameProduct = new TextView(getApplication());
                            nameProduct.setText(list.getNameProduct());
                            nameProduct.setTextColor(Color.BLACK);
                            nameProduct.setTextSize(26);
                            newLayout.addView(image, layoutParams);
                            newLayout.addView(nameProduct, layoutParams1);
                            newLayout.setBackgroundResource(R.drawable.border_full_project);
                            favorite_layout.setPadding(15,15,15,15);
                            favorite_layout.addView(newLayout);
                            String pathPhoto = "";
                            if (tableName.equals("list1")) {
                                pathPhoto = "images/" + list.getNameProduct() + list.getPrice();
                            } else if (tableName.equals("marketproducts")) {
                                pathPhoto = "imagesMarket/" + list.getNameProduct() + list.getPrice();
                            } else {
                                pathPhoto = "images/" + list.getNameProduct();
                            }
                            storageReference = firebaseStorage.getReference().child(pathPhoto);
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
                                    SharedPreferences.Editor preferences = getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                    preferences.putString("tableName", tableName);
                                    preferences.putString(tableName, list.getId());
                                    preferences.apply();
                                    startActivity(new Intent(getApplicationContext(), ActivityFirstList.class));
                                }
                            });
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SharedPreferences.Editor preferences = getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                    preferences.putString("tableName", tableName);
                                    preferences.putString(tableName, list.getId());
                                    preferences.apply();
                                    startActivity(new Intent(getApplicationContext(), ActivityFirstList.class));
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}