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

import android.util.Log;
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

public class AddFragmentProductInMarket extends Fragment {
    ImageButton addImage;
    EditText addName, addDescription, addPrice;
    Button add;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;
    String photoPath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_add_product_in_market, container, false);

        firebaseStorage = FirebaseStorage.getInstance();

        addName = root.findViewById(R.id.addNameProductForMarket);
        addDescription = root.findViewById(R.id.addTextDescriptionProductForMarket);
        addPrice = root.findViewById(R.id.addPriceProductForMarket);

        addImage = root.findViewById(R.id.addPhotoProductForMarket);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addName.getText().toString().equals("") || addPrice.getText().toString().equals("") || addDescription.getText().toString().equals("")) {
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

        add = root.findViewById(R.id.addBtnProductForMarket);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference("marketproducts").push();
                String name = addName.getText().toString();
                String description = addDescription.getText().toString();
                if (addPrice.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Խնդրում ենք լրացրեք արժեքը։").setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if(photoPath.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Ներբեռնեք լուսանկար։").setMessage("Առանց լուսանկարի հնարավոր չէ հրապարակել ապրանքը կամ ծառայությունը։").setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    int price = Integer.parseInt(addPrice.getText().toString());

                    if (name.equals("") || description.equals("") || String.valueOf(price).equals("")) {
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
                        String image_id = name + price;
                        List model = new List();
                        model.setId(databaseReference.getKey());
                        model.setNameProduct(name);
                        model.setDescription(description);
                        model.setPrice(price);
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
            }
        });



        return root;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = firebaseStorage.getReference("imagesMarket/" + addName.getText().toString() + addPrice.getText().toString());//UUID.randomUUID().toString()
        if (requestCode == 1) {
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Նկարը հաջողությամբ ներբեռնված է։", Toast.LENGTH_LONG).show();
                    }
                    photoPath = storageReference.getDownloadUrl().toString();
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