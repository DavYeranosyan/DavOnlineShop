package com.example.davonlineshop.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentFirstList;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentThirdListWorker;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentFourthList;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentProductInMarket;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentSecondList;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentThirdList;
import com.example.davonlineshop.ui.dashboard.AddFragments.AddFragmentSecondListDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DashboardFragmentForAdminAndManager extends Fragment {

    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    boolean bool;
    Button avelacnel;
    RadioGroup radioGroup;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard_for_manager, container, false);
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("email").equalTo(sharedPreferences.getString("email", ""));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        if (child.getValue(User.class).getType().equals(Type.ADMIN)) {
                            Toast.makeText(getContext(), "Բարև Ադմին։)", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                } else {
                    replaceFragments(DashboardFragment.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        radioGroup = root.findViewById(R.id.radioGroup);
        avelacnel = root.findViewById(R.id.btn_avelacnel);
        avelacnel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) root.findViewById(radioId);
                switch (radioButton.getId()) {
                    case R.id.radio1:
                        replaceFragments(AddFragmentFirstList.class);
                        break;
                    case R.id.radio2:
                        replaceFragments(AddFragmentSecondList.class);
                        break;
                    case R.id.radio3:
                        replaceFragments(AddFragmentSecondListDriver.class);
                        break;
                    case R.id.radio4:
                        replaceFragments(AddFragmentThirdList.class);
                        break;
                    case R.id.radio5:
                        replaceFragments(AddFragmentThirdListWorker.class);
                        break;
                    case R.id.radio6:
                        replaceFragments(AddFragmentFourthList.class);
                        break;
                    case R.id.radio7:
                        replaceFragments(AddFragmentProductInMarket.class);
                        break;
                    default:
                        Toast.makeText(getContext(), "Դուք չեք նշել թե ինչ եք ուզում ավելացնել։", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();

    }
}