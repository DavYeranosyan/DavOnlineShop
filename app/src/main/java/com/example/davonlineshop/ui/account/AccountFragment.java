package com.example.davonlineshop.ui.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.davonlineshop.MainActivity;
import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.account.authentication.LoginActivity;
import com.example.davonlineshop.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    SharedPreferences sharedPreferences;
    ImageView logout, favorite, message, orders, setting, feedback, about;
    TextView textView, message_view, favorite_view, order_view, setting_view, feedback_view, about_view, log_in_out_view;
    DatabaseReference databaseReference;
    FirebaseAuth auth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);

        textView = root.findViewById(R.id.name_user);
        message_view = root.findViewById(R.id.message_label);
        favorite_view = root.findViewById(R.id.favorite_label);
        order_view = root.findViewById(R.id.order_label);
        setting_view = root.findViewById(R.id.setting_label);
        feedback_view = root.findViewById(R.id.feedback_label);
        about_view = root.findViewById(R.id.about_label);
        log_in_out_view = root.findViewById(R.id.login_label);
        feedback = root.findViewById(R.id.feedback_btn);
        about = root.findViewById(R.id.about_btn);
        orders = root.findViewById(R.id.orders);
        message = root.findViewById(R.id.mesages);
        favorite = root.findViewById(R.id.favorite);
        setting = root.findViewById(R.id.settings);
        auth = FirebaseAuth.getInstance();
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
                            startActivity(new Intent(getActivity(), LoginActivity.class));
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
                            Toast.makeText(getContext(), user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                            textView.setText(user.getName() + " " + user.getSurname());
                            Toast.makeText(getContext(), user.getType() + " ", Toast.LENGTH_SHORT).show();
                            logout.setImageResource(R.drawable.ic_logout);
                            break;
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            log_in_out_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder bilder1 = new AlertDialog.Builder(getContext());
                    bilder1.setTitle("Log Out?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    sharedPreferences.edit().putString("email", "").apply();
                                    logout.setImageResource(R.drawable.ic_baseline_login_24);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = bilder1.create();
                    alertDialog.show();
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder bilder1 = new AlertDialog.Builder(getContext());
                    bilder1.setTitle("Log Out?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    sharedPreferences.edit().putString("email", "").apply();
                                    logout.setImageResource(R.drawable.ic_baseline_login_24);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = bilder1.create();
                    alertDialog.show();
                }
            });
        }
        favorite_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in faworit list", Toast.LENGTH_SHORT).show();
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in faworit list", Toast.LENGTH_SHORT).show();
            }
        });

        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in message", Toast.LENGTH_SHORT).show();
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in message", Toast.LENGTH_SHORT).show();
            }
        });

        order_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in orders", Toast.LENGTH_SHORT).show();

            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in orders", Toast.LENGTH_SHORT).show();
            }
        });


        setting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in settings", Toast.LENGTH_SHORT).show();

            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "click in settings", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }
}
