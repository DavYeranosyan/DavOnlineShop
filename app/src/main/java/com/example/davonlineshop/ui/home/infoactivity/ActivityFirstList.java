package com.example.davonlineshop.ui.home.infoactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Card;
import com.example.davonlineshop.model.Favorite;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.model.Worker;
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

public class ActivityFirstList extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ImageView product_img, favorite_img, add_card_img;
    TextView name_product_show, description_product_show;
    Button add_card_btn, delete_btn_admin;
    LinearLayout last_layout, workers_layout_show, cell_layout;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    String user_email, table_name_show, product_id_show, user_id_show;

    @Override
    protected void onStart() {
        super.onStart();
        itsAdmin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        user_email = sharedPreferences.getString("email", "");
        user_id_show = sharedPreferences.getString("user_id", "");
        sharedPreferences = getApplication().getSharedPreferences("Product", Context.MODE_PRIVATE);
        table_name_show = sharedPreferences.getString("tableName", "");
        product_id_show = sharedPreferences.getString(table_name_show, "");

        itsFavorite(table_name_show, product_id_show);

        product_img = findViewById(R.id.imageProductShow);
        name_product_show = findViewById(R.id.nameProductShow);
        description_product_show = findViewById(R.id.descriptionShow);

        delete_btn_admin = findViewById(R.id.deleteBtnForAdmin);

        cell_layout = findViewById(R.id.goCellLayout);
        Button cellBtn = findViewById(R.id.goCellinNumber);
        cell_layout.setVisibility(View.GONE);

        workers_layout_show = findViewById(R.id.workersLayoutShow);
        workers_layout_show.setVisibility(View.GONE);

        last_layout = findViewById(R.id.addCardLayoutShow);

        if (table_name_show.equals("marketproducts")) {
            last_layout.setVisibility(View.VISIBLE);
            searchProductAndShow(table_name_show, product_id_show);
        } else {
            if (!table_name_show.equals("list1") && !table_name_show.equals("list4")) {
                last_layout.setVisibility(View.GONE);
                workers_layout_show.setVisibility(View.VISIBLE);
                searchProductAndShow(table_name_show, product_id_show);
                if (sharedPreferences.getString("tableName", "").equals("list2")) {
                    String s3 = "driverslist2";
                    searchWorkersAndShow(s3, product_id_show);
                } else {
                    String s4 = "workerslist3";
                    searchWorkersAndShow(s4, product_id_show);
                }
            } else if (table_name_show.equals("list1")) {
                last_layout.setVisibility(View.VISIBLE);
                searchProductAndShow(table_name_show, product_id_show);
            } else {
                cell_layout.setVisibility(View.VISIBLE);
                last_layout.setVisibility(View.GONE);
                searchProductAndShow(table_name_show, product_id_show);
                cellBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPhoneNumber(table_name_show, product_id_show);
                    }
                });
            }
        }


        favorite_img = findViewById(R.id.btnFavoriteShow);
        favorite_img.setImageResource(R.drawable.dont_favorite);
        favorite_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itsFavoriteProduct(table_name_show, product_id_show);
            }
        });

        add_card_img = findViewById(R.id.addImageCardShow);
        add_card_img.setVisibility(View.VISIBLE);
        add_card_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInCardList(table_name_show, product_id_show);
            }
        });
        add_card_btn = findViewById(R.id.addBtnCard);
        add_card_btn.setVisibility(View.VISIBLE);
        add_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addInCardList(table_name_show, product_id_show);
            }
        });


    }

    public void searchUser(String email) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);

                        LinearLayout newLayout = new LinearLayout(getApplication());
                        newLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 120);
                        newLayout.setPadding(15, 15, 15, 15);
                        ImageView image = new ImageView(getApplication());
                        TextView name_surname = new TextView(getApplication());
                        name_surname.setText(user.getName() + " " + user.getSurname());
                        name_surname.setTextColor(Color.BLACK);
                        name_surname.setGravity(Gravity.CENTER);

                        name_surname.setTextSize(16);
                        newLayout.addView(image, layoutParams);
                        newLayout.addView(name_surname, layoutParams1);
                        workers_layout_show.addView(newLayout);
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
                                preferences.putString("user_name", user.getName());
                                preferences.putString("user_surname", user.getSurname());
                                preferences.putString("user_email", user.getEmail());
                                if (table_name_show.equals("list2")) {
                                    preferences.putString("table_name", "driverslist2");
                                } else {
                                    preferences.putString("table_name", "workerslist3");
                                }
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), ActivityPersonWorker.class));
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getSharedPreferences("Worker", Context.MODE_PRIVATE).edit();
                                preferences.putString("user_id", user.getId());
                                preferences.putString("user_name", user.getName());
                                preferences.putString("user_surname", user.getSurname());
                                preferences.putString("user_email", user.getEmail());
                                preferences.apply();
                                startActivity(new Intent(getApplicationContext(), ActivityPersonWorker.class));
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

    public void searchWorkersAndShow(String tableName, String product__id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("list_product_id").equalTo(product__id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Worker worker = child.getValue(Worker.class);
                        searchUser(worker.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void getPhoneNumber(String tableName, String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        String p = "tel:" + list.getPhone_number();
                        i.setData(Uri.parse(p));
                        startActivity(i);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchProductAndShow(String tableName, String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        product_img = findViewById(R.id.imageProductShow);
                        name_product_show = findViewById(R.id.nameProductShow);
                        name_product_show.setText(list.getNameProduct());
                        description_product_show = findViewById(R.id.descriptionShow);
                        description_product_show.setText(list.getDescription());
                        String pathPhoto = "";

                        if (tableName.equals("list1")) {
                            TextView price_show = findViewById(R.id.priceShow);
                            String price = list.getPrice() + " AMD";
                            price_show.setText(price);
                            pathPhoto = "images/" + list.getNameProduct() + list.getPrice();
                        } else if (tableName.equals("marketproducts")) {
                            TextView price_show = findViewById(R.id.priceShow);
                            String price = list.getPrice() + " AMD";
                            price_show.setText(price);
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
                                            Picasso.get().load(fileUrl).into(product_img);
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

    public void deleteProduct(String tableName, String product_id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(product_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        child.getRef().removeValue();
                        Toast.makeText(getApplication(), "Պրոդուկտը հեռացված է։", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void itsFavorite(String table_name, String product_id) {
        if (!user_email.equals("")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");
            Query query = databaseReference.orderByChild("user_id").equalTo(user_id_show);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            itsFavorite2();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void itsFavorite2() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");
        Query query = databaseReference.orderByChild("favorite_product_id").equalTo(product_id_show);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Favorite favorite = child.getValue(Favorite.class);
                        if (favorite.getUser_id().equals(user_id_show)) {
                            favorite_img.setImageResource(R.drawable.is_favorite_icon);
                            favorite_img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    child.getRef().removeValue();
                                    favorite_img.setImageResource(R.drawable.dont_favorite);
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

    public void itsFavoriteProduct(String table_name, String product_id) {
        if (!user_email.equals("")) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("favorites");
            Query query = databaseReference.orderByChild("favorite_product_id").equalTo(product_id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            favorite_img.setImageResource(R.drawable.is_favorite_icon);
                            favorite_img.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    child.getRef().removeValue();
                                    favorite_img.setImageResource(R.drawable.dont_favorite);
                                }
                            });
                        }
                    } else {
                        addInFavorites(table_name, product_id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Դուք մուտք չեք գործել Ձեր հաշիվ։", Toast.LENGTH_SHORT).show();
        }
    }

    public void addInFavorites(String table_name, String product_id) {
        if (!user_email.equals("")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("favorites").push();
            Favorite favorite = new Favorite();
            favorite.setId(databaseReference.getKey());
            favorite.setTable_name(table_name);
            favorite.setFavorite_product_id(product_id);
            favorite.setUser_id(user_id_show);
            databaseReference.setValue(favorite);
            favorite_img.setImageResource(R.drawable.is_favorite_icon);
        } else {
            Toast.makeText(getApplicationContext(), "Դուք մուտք չեք գործել Ձեր հաշիվ։", Toast.LENGTH_SHORT).show();
        }
    }

    public void addInCardList(String table_name, String productId) {
        if (!user_email.equals("")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("card").push();
            Card card = new Card();
            card.setId(databaseReference.getKey());
            card.setTable_name(table_name);
            card.setProduct_id(productId);
            card.setUser_id(user_id_show);
            databaseReference.setValue(card);
            last_layout.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Պրոդուկտը հաջողությամբ ավելացված է զամբյուղ։", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Դուք մուտք չեք գործել Ձեր հաշիվ։", Toast.LENGTH_SHORT).show();
        }
    }

    public void itsAdmin() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(user_email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        if (user.getType() == Type.ADMIN) {
                            delete_btn_admin.setVisibility(View.VISIBLE);
                            delete_btn_admin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteProduct(table_name_show, product_id_show);
                                }
                            });
                        } else {
                            delete_btn_admin.setVisibility(View.GONE);
                        }
                    }
                }else {
                    delete_btn_admin.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                delete_btn_admin.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("Product", MODE_PRIVATE).edit();
        sharedPreferencesEditor.putString("tableName", "");
        sharedPreferencesEditor.putString(table_name_show, "");
        sharedPreferencesEditor.apply();
    }
}