package com.example.davonlineshop.ui.dashboard.AddFragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.example.davonlineshop.ui.dashboard.DashboardFragmentForAdminAndManager;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class AddFragmentThirdList extends Fragment {
    ImageButton addImage;
    EditText addName, addDescription;
    Button add;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_third_list, container, false);

        firebaseStorage = FirebaseStorage.getInstance();

        addName = root.findViewById(R.id.addNameProductList3);
        addDescription = root.findViewById(R.id.addTextDescriptionList3);
        addImage = root.findViewById(R.id.addPhotoList3);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addName.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Խնդրում ենք լրացրեք բոլոր դաշտերը հետո նոր ավելացրեք լուսանկարը։")
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
                    startActivityForResult(Intent.createChooser(i, "Ընտրեք լուսանկարը"), 1);
                }
            }
        });

        add = root.findViewById(R.id.addBtnList3);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("list3").push();
                String name = addName.getText().toString();
                String description = addDescription.getText().toString();
                String image_id = name;
                if (name.equals("") || description.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Ինչ որ բան լրացված չէ։").setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    List model = new List();
                    model.setId(databaseReference.getKey());
                    model.setNameProduct(name);
                    model.setDescription(description);
                    model.setImage_id(image_id);
                    databaseReference.setValue(model);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Ձեր պրոդուկտը հաջողությամբ ավելացված է։").setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = firebaseStorage.getReference("images/" + addName.getText().toString());//UUID.randomUUID().toString()
        if (requestCode == 1) {
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Հաջողությամբ ներբեռնված է։", Toast.LENGTH_SHORT).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                        Picasso.get().load(fileUrl).into(addImage);
                    }
                }
            });
        }
    }
}