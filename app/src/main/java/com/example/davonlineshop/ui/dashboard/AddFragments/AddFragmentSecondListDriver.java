package com.example.davonlineshop.ui.dashboard.AddFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.model.User;
import com.example.davonlineshop.model.Worker;
import com.example.davonlineshop.ui.dashboard.DashboardFragmentForAdminAndManager;
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

import java.util.TreeMap;

public class AddFragmentSecondListDriver extends Fragment {

    TreeMap<Integer, String> listTree;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText email, addDescription, addPhoneNumber;
    TextView name_surname;
    Button search, addWorkerList3;
    ImageView profile_img;
    LinearLayout radio_Layout;
    RadioGroup group;
    String userWorkerEmail = "";
    String userId = "";
    String prodId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_second_list_driver, container, false);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        email = root.findViewById(R.id.inputEmailList2);
        name_surname = root.findViewById(R.id.userNameSurnameUserList2);
        profile_img = root.findViewById(R.id.profilePhotoUserList2);
        profile_img.setVisibility(View.GONE);


        radio_Layout = root.findViewById(R.id.radioLayoutList2);
        group = root.findViewById(R.id.radioList2);
        addDescription = root.findViewById(R.id.addDescriptionForDriverList2);
        addPhoneNumber = root.findViewById(R.id.addPhoneNumberDriverList2);
        addWorkerList3 = root.findViewById(R.id.addBtnDriverList2);

        listTree = new TreeMap<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("list2");
        Query query = databaseReference.orderByChild("nameProduct");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            int id = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        RadioButton radioButton = new RadioButton(getContext());
                        radioButton.setId(id);
                        radioButton.setText(list.getNameProduct());
                        group.addView(radioButton);
                        listTree.put(id, list.getId());
                        id++;
                    }
                    if (id == 0) {
                        TextView newText = new TextView(getContext());
                        newText.setTextColor(Color.BLACK);
                        newText.setText("Դուք չեք կարող ավելացնել աշխատող, քանի որ ծառայություն առկա չէ։");
                        radio_Layout.addView(newText);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        search = root.findViewById(R.id.btnSearchUserList2);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                Query query = databaseReference.orderByChild("email").equalTo(email.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                User user = child.getValue(User.class);
                                    userId = user.getId();
                                    profile_img.setVisibility(View.VISIBLE);
                                    name_surname.setText(user.getName() + " " + user.getSurname());
                                    userWorkerEmail = "";
                                    userWorkerEmail = email.getText().toString();
                                    storageReference = firebaseStorage.getReference().child("profile_images/" + user.getId());
                                    Task<Uri> url = storageReference.getDownloadUrl()
                                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        profile_img.setBackgroundColor(Color.BLACK);
                                                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                                        Picasso.get().load(fileUrl).into(profile_img);
                                                    }
                                                }
                                            });
//                        ((User) Objects.requireNonNull(snapshot.getValue())).setType(Type.MANAGER);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        addWorkerList3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userWorkerEmail.equals("") || addDescription.getText().toString().equals("")
                        || addPhoneNumber.getText().toString().equals("") || group.getCheckedRadioButtonId() == -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Խնդրում ենք լրացրեք բոլոր դաշտերը:")
                            .setNegativeButton("Լավ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    databaseReference = FirebaseDatabase.getInstance().getReference("driverslist2").push();
                    Worker worker = new Worker();
                    worker.setId(databaseReference.getKey());
                    worker.setEmail(userWorkerEmail);
                    worker.setDescription(addDescription.getText().toString());
                    worker.setList_product_id(listTree.get(group.getCheckedRadioButtonId()));
                    worker.setPhone_number(addPhoneNumber.getText().toString());
                    worker.setTable_Name("list2");
                    databaseReference.setValue(worker);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Նոր աշխատողը հաջողությամբ ավելացված է։").setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            replaceFragments(DashboardFragmentForAdminAndManager.class);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

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