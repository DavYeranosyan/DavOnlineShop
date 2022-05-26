package com.example.davonlineshop.ui.market;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.ui.home.infoactivity.ActivityFirstList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MarketProductsFragment extends Fragment {

    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    LinearLayout market_layout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_producs_market, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        market_layout = root.findViewById(R.id.marketProductsLabel);

        searchProductsMarket();

        return root;
    }

    public void searchProductsMarket() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("marketproducts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        LinearLayout fullLayout = new LinearLayout(getContext());
                        LinearLayout newLayout = new LinearLayout(getContext());
                        LinearLayout newLayout2 = new LinearLayout(getContext());

                        newLayout.setOrientation(LinearLayout.VERTICAL);
                        newLayout.setPadding(15,15, 15,15);
                        newLayout2.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams full_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        full_param.setMargins(0,0,0,20);
                        fullLayout.setOrientation(LinearLayout.HORIZONTAL);
                        fullLayout.setBackgroundResource(R.drawable.border_full_project);
                        fullLayout.setPadding(15,15, 15,15);
                        fullLayout.setLayoutParams(full_param);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300,300);
                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200                                                                                                                                                              );
                        LinearLayout.LayoutParams layoutParams2  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        LinearLayout.LayoutParams layoutParams3  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                        ImageView image = new ImageView(getContext());
                        TextView name_product = new TextView(getContext());
                        name_product.setText(list.getNameProduct());
                        name_product.setTextColor(Color.BLACK);
                        name_product.setTextSize(28);
                        fullLayout.addView(image, layoutParams);
                        fullLayout.addView(newLayout, layoutParams3);

                        newLayout.addView(name_product, layoutParams1);

                        TextView price = new TextView(getContext());
                        price.setText(list.getPrice() + " AMD");
                        price.setTextSize(26);
                        price.setGravity(Gravity.END);
                        newLayout2.setGravity(Gravity.END);
                        newLayout2.addView(price, layoutParams2);
                        newLayout.addView(newLayout2);

                        market_layout.addView(fullLayout);
                        storageReference = firebaseStorage.getReference().child("imagesMarket/" + list.getImage_id());
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
}