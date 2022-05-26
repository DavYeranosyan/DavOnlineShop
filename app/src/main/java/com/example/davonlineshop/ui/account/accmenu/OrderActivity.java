package com.example.davonlineshop.ui.account.accmenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.model.Order;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
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

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    LinearLayout orderFullLayout;
    java.util.List<Order> list1;
    java.util.List<Order> list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);


        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        orderFullLayout = findViewById(R.id.fullScreenOrderLayout);
        orderFullLayout.setPadding(25, 15, 25, 15);


        getTypeUser(sharedPreferences.getString("email", ""));


    }

    public void getTypeUser(String email) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = dR.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        if (user.getType() == Type.ADMIN && user.getEmail().equals(email)) {
                            getOrders(Type.ADMIN, email);
                        } else getOrders(user.getType(), email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getOrders(Type type, String email) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("orders");
        if (type == Type.ADMIN) {
            dR.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        orderFullLayout.removeAllViews();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Order order = child.getValue(Order.class);
                            list1.add(order);
                        }
                        forAdm(list1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        } else {
            Query query = dR.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        orderFullLayout.removeAllViews();
                        LinearLayout childInOrderLayout = new LinearLayout(getApplication());
                        childInOrderLayout.setBackgroundResource(R.drawable.border_full_project);
                        childInOrderLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams par1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
                        TextView location = new TextView(getApplication());
                        TextView phone = new TextView(getApplication());

                        childInOrderLayout.addView(location, par);
                        childInOrderLayout.addView(phone, par);
                        childInOrderLayout.setPadding(15, 15, 15, 15);
                        orderFullLayout.addView(childInOrderLayout, par1);
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Order order = child.getValue(Order.class);
                            String email2 = order.getEmail();
                            location.setText(order.getLocation());
                            phone.setText(order.getPhone_number());
                            if (email2.equals(email)) {
                                getProducts(order.getTable_name(), order.getProduct_id(), order.getCount(), childInOrderLayout);
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

    public void getProducts(String tableName, String product_id, int c, LinearLayout childLayout) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = dR.orderByChild("id").equalTo(product_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        LinearLayout fullLayout = new LinearLayout(getApplication());
                        LinearLayout newLayout = new LinearLayout(getApplication());
                        LinearLayout newLayout1 = new LinearLayout(getApplication());
                        LinearLayout newLayout2 = new LinearLayout(getApplication());
                        LinearLayout newLayout3 = new LinearLayout(getApplication());


                        newLayout.setOrientation(LinearLayout.VERTICAL);
                        newLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout.setPadding(15, 15, 15, 15);
                        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout3.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout3.setGravity(Gravity.END);
                        fullLayout.setOrientation(LinearLayout.HORIZONTAL);
                        fullLayout.setBackgroundResource(R.drawable.border_full_project);
                        fullLayout.setPadding(10, 10, 10, 10);
                        fullLayout.setPadding(15, 15, 15, 15);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        ImageView image = new ImageView(getApplication());

                        newLayout.addView(newLayout1, layoutParams1);


                        TextView name_product = new TextView(getApplication());
                        name_product.setText(list.getNameProduct());
                        name_product.setTextColor(Color.BLACK);
                        name_product.setTextSize(16);
                        fullLayout.addView(image, layoutParams);
                        fullLayout.addView(newLayout, layoutParams3);


                        newLayout1.addView(name_product, layoutParams4);

                        TextView count = new TextView(getApplication());
                        count.setText(String.valueOf(c));
                        count.setTextSize(24);
                        newLayout.addView(newLayout2);

                        newLayout2.setGravity(Gravity.END);

                        newLayout2.addView(count, layoutParams4);
                        newLayout2.addView(newLayout3, layoutParams2);
                        childLayout.addView(fullLayout);
                        String photoPath = "";
                        if (tableName.equals("marketproducts")) {
                            photoPath = "imagesMarket/" + list.getImage_id();
                        } else {
                            photoPath = "images/" + list.getImage_id();
                        }
                        storageReference = firebaseStorage.getReference().child(photoPath);
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
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void forAdm(java.util.List<Order> l) {
        String id1 = "";
        String id2 = "";
        int i = 0;
        boolean ok = false;
        for (int i1 = 0; i1 < l.size(); i1++) {
            id2 = l.get(i1).getId();
            if (i == 0) {
                list2.add(l.get(i1));
                id1 = id2;
                i++;
            } else if (id2.equals(id1)) {
                list2.add(l.get(i1));
                id1 = id2;
                ok = false;
            } else {
                ok = true;

            }
            if (ok || i1 == l.size()-1) {
                forAdmin();
                list2.add(l.get(i));
                id1 = id2;
                i = 1;
                ok = false;
            }
        }
    }

    public void forAdmin() {

        TextView location = new TextView(getApplication());
        TextView phone = new TextView(getApplication());
        location.setText(list2.get(0).getLocation());
        phone.setText(list2.get(0).getPhone_number());
        LinearLayout childInOrderLayout = new LinearLayout(getApplication());
        childInOrderLayout.setBackgroundResource(R.drawable.border_full_project);
        childInOrderLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams par1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams par = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        childInOrderLayout.setPadding(15, 15, 15, 15);

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = dR.orderByChild("email").equalTo(list2.get(list2.size() - 1).getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        ImageView photo_user = new ImageView(getApplication());
                        TextView name_surname = new TextView(getApplication());
                        name_surname.setText(user.getName() + " " + user.getSurname());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250, 250);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
                        childInOrderLayout.addView(photo_user, params);
                        childInOrderLayout.addView(name_surname, params1);
                        storageReference = firebaseStorage.getReference().child("profile_images/" + user.getId());
                        Task<Uri> url = storageReference.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            photo_user.setBackgroundColor(Color.BLACK);
                                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                            Picasso.get().load(fileUrl).into(photo_user);
                                        }
                                    }
                                });
                        childInOrderLayout.addView(location, par);
                        childInOrderLayout.addView(phone, par);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        orderFullLayout.addView(childInOrderLayout, par1);
        for (Order orders : list2) {
            DatabaseReference dR1 = FirebaseDatabase.getInstance().getReference().child("orders");
            Query query1 = dR1.orderByChild("id").equalTo(orders.getId());
            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        for (DataSnapshot child : snapshot.getChildren()) {
                            Order order1 = child.getValue(Order.class);
                            if (order1.getProduct_id().equals(orders.getProduct_id())) {
                                getProducts(order1.getTable_name(), order1.getProduct_id(), order1.getCount(), childInOrderLayout);
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        list2.clear();
    }
}