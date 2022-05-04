package com.example.davonlineshop.ui.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Card;
import com.example.davonlineshop.model.Favorite;
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

import java.util.Map;
import java.util.TreeMap;

public class ShoppingCartFragment extends Fragment {

    SharedPreferences sharedPreferences;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayout cardLayoutFullScreen;
    String user_id, user_email;
    EditText getLocation, getPhoneNumber;
    Button addOrder;
    LinearLayout.LayoutParams param;

    TreeMap<String, Integer> listTree;
    TreeMap<String, String> listTree1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shopping_card, container, false);

        listTree = new TreeMap<>();
        listTree1 = new TreeMap<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        getLocation = root.findViewById(R.id.getContactLocation);
        getPhoneNumber = root.findViewById(R.id.getContactPhoneNumber);
        cardLayoutFullScreen = root.findViewById(R.id.cardLayoutFull);
        addOrder = new Button(getContext());
        addOrder.setText("Պատվիրել");
        addOrder.setVisibility(View.VISIBLE);
        addOrder.setBackgroundResource(R.drawable.border_log);
        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getLocation.getText().toString().equals("") || getPhoneNumber.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Ինչ որ բան այն չէ։(", Toast.LENGTH_LONG).show();
                }else {
                    addProductsInOrder(getLocation.getText().toString(), getPhoneNumber.getText().toString());
                }
            }
        });
        param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardLayoutFullScreen.addView(addOrder, param);

        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        user_email = sharedPreferences.getString("email", "");
        user_id = sharedPreferences.getString("user_id", "");

        getCardProducts();
        return root;
    }

    public void getCardProducts() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("card");
        Query query = databaseReference.orderByChild("user_id").equalTo(user_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Card card = child.getValue(Card.class);
                        searchProductAndShow("card", card.getId());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getProduct(String tableName, String product_id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(product_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        listTree.put(product_id, 1);
                        listTree1.put(product_id, tableName);
                        List list = child.getValue(List.class);
                        LinearLayout fullLayout = new LinearLayout(getContext());
                        LinearLayout newLayout = new LinearLayout(getContext());
                        LinearLayout newLayout1 = new LinearLayout(getContext());
                        LinearLayout newLayout2 = new LinearLayout(getContext());
                        LinearLayout newLayout3 = new LinearLayout(getContext());


                        newLayout.setOrientation(LinearLayout.VERTICAL);
                        newLayout1.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout.setPadding(15, 15, 15, 15);
                        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout3.setOrientation(LinearLayout.HORIZONTAL);
                        newLayout3.setGravity(Gravity.END);
                        fullLayout.setOrientation(LinearLayout.HORIZONTAL);
                        fullLayout.setBackgroundResource(R.drawable.border_log);
                        fullLayout.setPadding(10, 10, 10, 10);
                        fullLayout.setPadding(15, 15, 15, 15);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180);
                        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        ImageView image = new ImageView(getContext());

                        newLayout.addView(newLayout1, layoutParams1);


                        TextView name_product = new TextView(getContext());
                        name_product.setText(list.getNameProduct());
                        name_product.setTextColor(Color.BLACK);
                        name_product.setTextSize(16);
                        fullLayout.addView(image, layoutParams);
                        fullLayout.addView(newLayout, layoutParams3);


                        newLayout1.addView(name_product, layoutParams4);
                        ImageView deleteIcon = new ImageView(getContext());
                        deleteIcon.setImageResource(R.drawable.delete);
                        deleteIcon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteProduct(list.getId());
                                getActivity().recreate();
                            }
                        });
                        newLayout1.addView(deleteIcon, layoutParams4);
                        TextView price = new TextView(getContext());
                        price.setText(list.getPrice() + " AMD");
                        price.setTextSize(20);
                        price.setGravity(Gravity.END);

                        TextView count = new TextView(getContext());
                        count.setText("1");
                        count.setTextSize(24);
                        ImageView up = new ImageView(getContext());
                        up.setImageResource(R.drawable.up);
                        up.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View v) {
                                int i = Integer.parseInt(count.getText().toString()) + 1;
                                count.setText(String.valueOf(i));
                                int j = Integer.parseInt(count.getText().toString()) * list.getPrice();
                                price.setText(String.valueOf(j) + "AMD");
                                listTree.replace(product_id, i);
                            }
                        });
                        ImageView down = new ImageView(getContext());
                        down.setImageResource(R.drawable.down);
                        down.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View v) {
                                int i = Integer.parseInt(count.getText().toString());
                                if (i > 1) {
                                    i = i - 1;
                                }
                                count.setText(String.valueOf(i));
                                listTree.replace(product_id, i);
                                int j = Integer.parseInt(count.getText().toString()) * list.getPrice();
                                price.setText(String.valueOf(j) + "AMD");
                            }
                        });
                        newLayout.addView(newLayout2);

                        newLayout2.setGravity(Gravity.END);
                        newLayout2.addView(down, layoutParams4);
                        newLayout2.addView(count, layoutParams4);
                        newLayout2.addView(up, layoutParams4);
                        newLayout3.addView(price);
                        newLayout2.addView(newLayout3, layoutParams2);
                        cardLayoutFullScreen.addView(fullLayout);
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
                                SharedPreferences.Editor preferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                preferences.putString("tableName", "marketproducts");
                                preferences.putString("marketproducts", list.getId());
                                preferences.apply();
                                startActivity(new Intent(getContext(), ActivityFirstList.class));
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences.Editor preferences = getActivity().getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
                                preferences.putString("tableName", "marketproducts");
                                preferences.putString("marketproducts", list.getId());
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

    public void searchProductAndShow(String tableName, String id) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(tableName);
        Query query = databaseReference.orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Card card = child.getValue(Card.class);
                        getProduct(card.getTable_name(), card.getProduct_id());
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addProductsInOrder(String location, String phone) {
        databaseReference = FirebaseDatabase.getInstance().getReference("orders").push();
        for (Map.Entry<String, Integer> entry : listTree.entrySet()) {
            Order order = new Order();
            order.setId(databaseReference.getKey());
            order.setProduct_id(entry.getKey());
            order.setTable_name(listTree1.get(entry.getKey()));
            order.setCount(entry.getValue().toString());
            order.setEmail(user_email);
            order.setPhone_number(phone);
            order.setLocation(location);
            databaseReference.setValue(order);
            Toast.makeText(getContext(), "Պատվերը գրանցված է։)", Toast.LENGTH_LONG).show();
            deleteProduct(entry.getKey());
        }
        getActivity().recreate();
    }
    public void deleteProduct(String id){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("card");
        Query query = databaseReference.orderByChild("product_id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}