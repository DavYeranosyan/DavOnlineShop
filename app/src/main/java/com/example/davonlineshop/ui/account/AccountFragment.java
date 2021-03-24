package com.example.davonlineshop.ui.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;




import com.example.davonlineshop.R;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.account.authentication.AccountActivity;
import com.example.davonlineshop.ui.account.authentication.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {


    SharedPreferences sharedPreferences;
    ImageView logout;
    TextView textView;
    DatabaseReference databaseReference;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        textView = root.findViewById(R.id.name_user);
//        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        logout = root.findViewById(R.id.login_btn);
        Log.e("my", "onCreateView: " + sharedPreferences.getString("email", ""));
        if (sharedPreferences.getString("email", "").equals("")) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("You need login account?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(getActivity(), AccountActivity.class));
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            Query query = databaseReference.orderByChild("email").equalTo(sharedPreferences.getString("email", ""));
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            Toast.makeText(getContext(), user.getName() + " " +user.getSurname(), Toast.LENGTH_LONG).show();
                            textView.setText(user.getName() + " " + user.getSurname());
                            logout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sharedPreferences.edit().putString("email", "").apply();
                                }
                            });

                            break;
                        }
                    } else {
                        logout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getActivity(), AccountActivity.class));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        return root;
    }

}