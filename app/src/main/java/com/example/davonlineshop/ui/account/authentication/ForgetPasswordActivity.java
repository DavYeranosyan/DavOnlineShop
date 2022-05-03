package com.example.davonlineshop.ui.account.authentication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.davonlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordActivity extends AppCompatActivity {
    EditText editText;
    Button sendMessage;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget_password);
        editText = findViewById(R.id.emailText);
        sendMessage = findViewById(R.id.sendMess);
        firebaseAuth = FirebaseAuth.getInstance();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return true;
    }

    public void sendMessInMail(View view) {
        if (!editText.getText().toString().isEmpty()) {
            firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Toast.makeText(getApplicationContext(), "Մուտք գործեք Ձեր Էլ․ փոստ։", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Մեր համակարգում Փեր կողմից նշված Էլ․ հասցեն գրանցված չէ։", Toast.LENGTH_LONG).show();
                    }
                }
            });
            SendMail sendMail = new SendMail(getApplicationContext(), editText.getText().toString(), "Շին Պրո - Գաղտնաբառի փոփոխություն", "Սեղմեք հղումի վրա և գրեք Ձեր նոր գաղտնաբառը։");
            sendMail.execute();
        } else {
                Toast.makeText(getApplicationContext(), "Դուք չեք լրացրել Էլ․ հասցեի դաշտը։", Toast.LENGTH_LONG).show();
        }

    }
}