package com.example.davonlineshop.ui.account.authentication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Type;
import com.example.davonlineshop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {
    EditText name, surname, email, age, password;
    FirebaseAuth firebaseAuth;
    Button signIn, save;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        name = root.findViewById(R.id.name);
        surname = root.findViewById(R.id.surname);
        email = root.findViewById(R.id.email);
        age = root.findViewById(R.id.age);
        password = root.findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        signIn = root.findViewById(R.id.signInReg);
        save = root.findViewById(R.id.saveMe);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                            } else {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String nameF = name.getText().toString();
                                            String surnameF = surname.getText().toString();
                                            String emailF = email.getText().toString().toLowerCase();
                                            Log.e("my", "onComplete: " + email.getText().toString());
                                            String ageF = age.getText().toString();
                                            databaseReference = FirebaseDatabase.getInstance().getReference("users").push();
                                            User model = new User();
                                            model.setId(databaseReference.getKey());
                                            model.setName(nameF);
                                            model.setSurname(surnameF);
                                            model.setEmail(emailF);
                                            model.setType(Type.USER);
                                            model.setAge(Integer.parseInt(ageF));
                                            databaseReference.setValue(model);
                                            Toast.makeText(getContext(), "Werryyy Gooood", Toast.LENGTH_LONG).show();
//                                            ((AccountActivity) requireActivity()).replaceFragments2(LoginFragment.class);
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                        });
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                ((AccountActivity) requireActivity()).replaceFragments2(LoginFragment.class);
            }
        });


        return root;
    }
}