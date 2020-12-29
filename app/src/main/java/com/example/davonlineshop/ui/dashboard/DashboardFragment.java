package com.example.davonlineshop.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.davonlineshop.R;
import com.google.firebase.database.DatabaseReference;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {
    Spinner spinner;
    SharedPreferences sharedPreferences;
    Button addPhoto, addProduct;
    EditText productName, productDescription, productCount, productPrice;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        String s = sharedPreferences.getString("email", "");
        if (s.isEmpty()){
        }
        addPhoto = root.findViewById(R.id.buttonAddPhoto);
        addProduct = root.findViewById(R.id.addProduct);
        productName = root.findViewById(R.id.nameProduct);
        productDescription = root.findViewById(R.id.descProduct);
        productCount = root.findViewById(R.id.productCount);
        productPrice = root.findViewById(R.id.productPrice);
        spinner = (Spinner) root.findViewById(R.id.category);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        return root;
    }


}