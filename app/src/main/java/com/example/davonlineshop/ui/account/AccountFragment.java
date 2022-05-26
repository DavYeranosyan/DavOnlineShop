package com.example.davonlineshop.ui.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.ui.account.accmenu.FavoriteActivity;
import com.example.davonlineshop.ui.account.accmenu.MessageActivity;
import com.example.davonlineshop.ui.account.accmenu.OrderActivity;
import com.example.davonlineshop.ui.account.accmenu.SettingsActivity;
import com.example.davonlineshop.ui.account.authentication.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {
    SharedPreferences sharedPreferences;
    ImageView user_photo, logout, favorite, message, orders, setting, feedback, about;
    TextView user_type, textView, message_view, favorite_view, order_view, setting_view, feedback_view, about_view, log_in_out_view;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    String user_id_our_base = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();


        textView = root.findViewById(R.id.name_user);
        user_type = root.findViewById(R.id.user_type);
        message_view = root.findViewById(R.id.message_label);
        favorite_view = root.findViewById(R.id.favorite_label);
        order_view = root.findViewById(R.id.order_view);
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
        logout = root.findViewById(R.id.login_btn);

        if (sharedPreferences.getString("email", "").equals("")) {
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Դուք ցանկանում եք մուտք գործել Ձեր հաշի՞վ։").setNegativeButton("Ոչ, միամիտ սեղմեցի։)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Այո", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            log_in_out_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Դուք ցանկանում եք մուտք գործել Ձեր հաշի՞վ։").setNegativeButton("Ոչ, միամիտ սեղմեցի։)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).setPositiveButton("Այո", new DialogInterface.OnClickListener() {
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
                            user_id_our_base = user.getId();
                            storageReference = firebaseStorage.getReference().child("profile_images/" + user_id_our_base);
                            Task<Uri> url = storageReference.getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                                Picasso.get().load(fileUrl).into(user_photo);
                                            }
                                        }
                                    });
                            if (user.getType() == Type.ADMIN) {
                                textView.setText(user.getName() + " " + user.getSurname());
                                logout.setImageResource(R.drawable.ic_logout);
                                user_type.setText("Ադմինիստրատոր");
                                break;
                            } else if (user.getType() == Type.MANAGER) {
                                textView.setText(user.getName() + " " + user.getSurname());
                                user_type.setText("Գործընկեր");
                                Toast.makeText(getContext(), user.getType() + " ", Toast.LENGTH_SHORT).show();
                                logout.setImageResource(R.drawable.ic_logout);
                                break;
                            } else {
                                textView.setText(user.getName() + " " + user.getSurname());
                                user_type.setText("Հաճախորդ");
                                Toast.makeText(getContext(), user.getType() + " ", Toast.LENGTH_SHORT).show();
                                logout.setImageResource(R.drawable.ic_logout);
                                break;
                            }
                        }
                        log_in_out_view.setText("Դուրս գալ");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            log_in_out_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Դուք ցանկանում եք դուրս գա՞լ Ձեր հաշվից։(")
                            .setNegativeButton("Ոչ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Այո", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    sharedPreferences.edit().putString("email", "").apply();
                                    logout.setImageResource(R.drawable.ic_baseline_login_24);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    log_in_out_view.setText("Դուրս գալ");
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Դուք ցանկանում եք դուրս գա՞լ Ձեր հաշվից։(")
                            .setNegativeButton("Ոչ։)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton("Այո", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    user_type.setText("");
                                    sharedPreferences.edit().putString("email", "").apply();
                                    sharedPreferences.edit().putString("user_id", "").apply();
                                    logout.setImageResource(R.drawable.ic_baseline_login_24);
                                    dialog.dismiss();
                                    getActivity().recreate();
                                }

                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    log_in_out_view.setText("Մուտք գործել");
                }
            });
        }

        user_photo = root.findViewById(R.id.user_Image);
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString("email", "").equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Դուք մուտք չեք գործել Ձեր հաշիվ։")
                            .setNegativeButton("Լավ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Ընտրեք լուսանկարը։"), 1);
                }
            }
        });


        sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
        String user_email = sharedPreferences.getString("email", "");
        favorite_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), FavoriteActivity.class));
                }
            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), FavoriteActivity.class));
                }
            }
        });


        message_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), MessageActivity.class));
                }
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), MessageActivity.class));
                }
            }
        });

        order_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
                if (sharedPreferences.getString("email", "").equals("")){
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(getContext(), OrderActivity.class));
                }
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = getActivity().getSharedPreferences("email", Context.MODE_PRIVATE);
                if (sharedPreferences.getString("email", "").equals("")){
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(getContext(), OrderActivity.class));
                }
            }
        });


        setting_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                }
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_email.equals("")) {
                    Toast.makeText(getContext(), "Դուք մուտք չեք գործել հաշիվ։", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getContext(), SettingsActivity.class));
                }
            }
        });


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phone_number = "tel:" + "+37433309603";
                intent.setData(Uri.parse(phone_number));
                startActivity(intent);
            }
        });
        feedback_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phone_number = "tel:" + "+37433309603";
                intent.setData(Uri.parse(phone_number));
                startActivity(intent);
            }
        });


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Version Program is 1.0.0")
                        .setNegativeButton("Փակել", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        about_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Version Program is 1.0.0")
                        .setNegativeButton("Փակել", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return root;
    }

    public void reopen() {
        getActivity().recreate();
    }

    public void refreshProfilePhoto() {
        storageReference = firebaseStorage.getReference().child("profile_images/" + user_id_our_base);
        Task<Uri> url = storageReference.getDownloadUrl()
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                            Picasso.get().load(fileUrl).into(user_photo);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = firebaseStorage.getReference("profile_images/" + user_id_our_base);
        if (requestCode == 1) {
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Նկարը հաջողությամբ ներբեռնված է։", Toast.LENGTH_LONG).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                        Picasso.get().load(fileUrl).into(user_photo);
                        refreshProfilePhoto();
                    }
                }
            });
        }


    }
}
