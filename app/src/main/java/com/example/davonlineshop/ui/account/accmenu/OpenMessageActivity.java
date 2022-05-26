package com.example.davonlineshop.ui.account.accmenu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.Message;
import com.example.davonlineshop.model.Messages;
import com.example.davonlineshop.model.User;
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

public class OpenMessageActivity extends AppCompatActivity {

    ImageView photoUser, send;
    TextView nameSurname;
    EditText messageText;
    LinearLayout messagesLayout;
    ScrollView scrollMessageActivity;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_message);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        actionBar.setDisplayShowHomeEnabled(true);

        scrollMessageActivity = findViewById(R.id.scrollViewMessageActivity);


        sharedPreferences = getSharedPreferences("email", Context.MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        requestMessage();

        photoUser = findViewById(R.id.photo_User);
        nameSurname = findViewById(R.id.name_Surname_User);
        messageText = findViewById(R.id.message_Text_For_Send);
        send = findViewById(R.id.message_Send_Btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(messageText.getText().toString());
            }
        });
        messagesLayout = findViewById(R.id.messages_Layout);

        sharedPreferences = getSharedPreferences("Worker", MODE_PRIVATE);
        String to_Id = sharedPreferences.getString("user_id", "");
        getUser(to_Id);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendMessage(String textMessage) {
        sharedPreferences = getSharedPreferences("Worker", MODE_PRIVATE);
        String to_id = sharedPreferences.getString("user_id", "");
        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        String from_Id = sharedPreferences.getString("user_id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference("messages").push();
        if (!textMessage.equals("")) {
            Messages messages = new Messages();
            messages.setId(databaseReference.getKey());
            messages.setFrom_id(from_Id);
            messages.setTo_id(to_id);
            messages.setMessage(textMessage);
            databaseReference.setValue(messages);
            messagesLayout.removeAllViews();
            messageText.setText("");
            createMessage(from_Id, to_id);
            requestMessage();
        }


    }

    public void createMessage(String id_1, String id_2) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("message");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean chatIsEmpty = true;
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Message message = child.getValue(Message.class);
                        if (message.getId_one().equals(id_1) && message.getId_two().equals(id_2)) {
                            chatIsEmpty = false;
                        } else if (message.getId_one().equals(id_2) && message.getId_two().equals(id_1)) {
                            chatIsEmpty = false;
                        }
                    }
                    if (chatIsEmpty) {
                        databaseReference = FirebaseDatabase.getInstance().getReference("message").push();
                        Message message = new Message();
                        message.setId_one(id_1);
                        message.setId_two(id_2);
                        databaseReference.setValue(message);
                        messagesLayout.removeAllViews();
                        messageText.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseReference = FirebaseDatabase.getInstance().getReference("message").push();
                Message message = new Message();
                message.setId_one(id_1);
                message.setId_two(id_2);
                databaseReference.setValue(message);
                messagesLayout.removeAllViews();
                messageText.setText("");
            }
        });

    }

    public void getUser(String idUser) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = databaseReference.orderByChild("id").equalTo(idUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        String user_id_our_base = user.getId();
                        nameSurname.setText(user.getName() + " " + user.getSurname());
                        storageReference = firebaseStorage.getReference().child("profile_images/" + user_id_our_base);
                        Task<Uri> url = storageReference.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                            Picasso.get().load(fileUrl).into(photoUser);
                                        }
                                    }
                                });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestMessage() {
        sharedPreferences = getSharedPreferences("Worker", MODE_PRIVATE);
        String to_Id = sharedPreferences.getString("user_id", "");
        sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
        String from_Id = sharedPreferences.getString("user_id", "");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean itsEmpty = true;
                    messagesLayout.removeAllViews();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Messages messages = child.getValue(Messages.class);
                        if (messages.getTo_id().equals(from_Id)) {
                            if (messages.getFrom_id().equals(to_Id)) {
                                LinearLayout layoutMessageFromId = new LinearLayout(getApplication());
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                param1.setMargins(0, 0, 0, 180);
                                TextView textMessageFromId = new TextView(getApplication());
                                textMessageFromId.setText(messages.getMessage());
                                textMessageFromId.setTextColor(Color.BLACK);
                                textMessageFromId.setBackgroundResource(R.drawable.border_message_from_id);
                                textMessageFromId.setPadding(15, 15, 15, 15);
                                textMessageFromId.setMaxWidth(700);
                                textMessageFromId.setTextSize(18);
                                textMessageFromId.setLayoutParams(param);
                                layoutMessageFromId.setGravity(Gravity.START);
                                layoutMessageFromId.addView(textMessageFromId);
                                messagesLayout.addView(layoutMessageFromId, param1);
                                itsEmpty = false;
                            }
                        } else if (messages.getTo_id().equals(to_Id)) {
                            if (messages.getFrom_id().equals(from_Id)) {
                                LinearLayout layoutMessageToId = new LinearLayout(getApplication());
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                param1.setMargins(0, 0, 0, 180);
                                TextView textMessageToId = new TextView(getApplication());
                                textMessageToId.setText(messages.getMessage());
                                textMessageToId.setTextColor(Color.BLACK);
                                textMessageToId.setPadding(15, 15, 15, 15);
                                textMessageToId.setMaxWidth(700);
                                textMessageToId.setTextSize(18);
                                textMessageToId.setBackgroundResource(R.drawable.border_message_to_id);
                                textMessageToId.setLayoutParams(param);
                                layoutMessageToId.setGravity(Gravity.END);
                                layoutMessageToId.setBottom(20);
                                layoutMessageToId.addView(textMessageToId);
                                messagesLayout.addView(layoutMessageToId, param1);
                                itsEmpty = false;
                            }
                        }
                    }
                    if (itsEmpty) {
                        messagesLayout.setGravity(Gravity.CENTER);
                        TextView textView = new TextView(getApplication());
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("Հաղորդագրություններ դեռևս չկան։");
                        textView.setTextColor(Color.BLACK);
                        messagesLayout.addView(textView);
                    }
                    scrollMessageActivity.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollMessageActivity.scrollTo(0, scrollMessageActivity.getBottom());
                        }
                    });
                }else {
                    messagesLayout.setGravity(Gravity.CENTER);
                    TextView textView = new TextView(getApplication());
                    textView.setGravity(Gravity.CENTER);
                    textView.setText("Հաղորդագրություններ դեռևս չկան։");
                    textView.setTextColor(Color.BLACK);
                    messagesLayout.addView(textView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                messagesLayout.setGravity(Gravity.CENTER);
                TextView textView = new TextView(getApplication());
                textView.setGravity(Gravity.CENTER);
                textView.setText("Հաղորդագրություններ դեռևս չկան։");
                textView.setTextColor(Color.BLACK);
                messagesLayout.addView(textView);
            }
        });
    }


}