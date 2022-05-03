package com.example.davonlineshop.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.davonlineshop.R;
import com.example.davonlineshop.model.List;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class HomeFragment extends Fragment {

    ImageView imageView;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView photo1;
    ImageView photo2;
    TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
//       textView = getActivity().findViewById(R.id.textView6);
//       textView.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               startActivity(new Intent(getContext(), ActivityFirstList.class));
//           }
//       });
        TextView name = root.findViewById(R.id.nameHome);
        photo1 = root.findViewById(R.id.photo1);
        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ActivityFirstList.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("list1");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        List list = child.getValue(List.class);
                        name.setText(list.getNameProduct());
                        storageReference = firebaseStorage.getReference().child("images/" + list.getNameProduct() + list.getPrice());
                        Task<Uri> url =  storageReference.getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                                    Picasso.get().load(fileUrl).into(photo1);
                                }
                            }
                        });
//                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        String url = uri.toString();
////                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////                                        builder.setMessage(url).setPositiveButton("Շատ բարի։)", new DialogInterface.OnClickListener() {
////                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////                                            @Override
////                                            public void onClick(DialogInterface dialog, int which) {
//////                                                replaceFragments(DashboardFragmentForAdminAndManager.class);
////                                            }
////                                        });
////                                        AlertDialog alertDialog = builder.create();
////                                        alertDialog.show();
////                                        Picasso.get().load(url).into(photo1);
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle any errors
//                            }
//                        });
//                        UploadTask uploadTask = storageReference.putBytes(data);
//                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
//                                        new OnCompleteListener<Uri>() {
//
//                                            @Override
//                                            public void onComplete(@NonNull Task<Uri> task) {
//                                                String fileLink = task.getResult().toString();
//                                                //next work with URL
//
//                                            }
//                                        });
//
//                            UploadTask uploadTask = storageReference.putFile(new Intent().getData());
//                            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                @Override
//                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
//                                    }
//                                    return storageReference.getDownloadUrl();
//                                }
//                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Uri> task) {
//                                    if (task.isSuccessful()) {
//                                        Log.d("AddImage", "Hasav");
//                                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
//                                        Picasso.get().load(fileUrl).into(photo1);
//                                    }
//                                }
//                            });

//                        getImage = databaseReference.child("images/");
//
//                        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.hasChild(list.getImage_id()))
//                                {
//                                    String image = snapshot.child(list.getImage_id()).getValue().toString();
//                                    Picasso.get().load(image).into(photo1);
//                                }
////                                String link = dataSnapshot.getValue(String.class);
////                                Picasso.get().load(link).into(photo1);
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Toast.makeText(getContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        photo1 = root.findViewById(R.id.photo1);
//        photo1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), ActivityFirstList.class));
//            }
//        });
//        imageView = root.findViewById(R.id.img1);
//        firebaseStorage = FirebaseStorage.getInstance();
//        btn = root.findViewById(R.id.btnHome);
//        btn2 = root.findViewById(R.id.btnHome2);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent();
//                i.setType("image/*");
//                i.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(i, "Select pecture"), 1);
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
////                String noteId = "com.example.davonlineshop";
////                NotificationCompat.Builder noteBuild = null;
////                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
////                    noteBuild = new NotificationCompat.Builder(Objects.requireNonNull(getContext(), noteId));
////                    noteBuild.setAutoCancel(true)
////                            .setDefaults(Notification.DEFAULT_ALL)
////                            .setWhen(System.currentTimeMillis())
////                            .setSmallIcon(R.drawable.ic_baseline_notifications)
////                            .setContentTitle("New Watches")
////                            .setContentText("New Аssortment");
////                    notificationManager.notify(new Random().nextInt(), noteBuild.build());
////                }
//                String noteId = "com.example.davonlineshop";
//                Intent noteIntent = new Intent(getContext(), MainActivity.class);
//                noteIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, noteIntent, 0);
//
//                NotificationCompat.Builder builder =
//                        new NotificationCompat.Builder(getContext(), noteId)
//                                .setAutoCancel(true)
//                                .setDefaults(Notification.DEFAULT_ALL)
//                                .setSmallIcon(R.drawable.ic_baseline_notifications)
//                                .setContentInfo("info")
//                                .setWhen(System.currentTimeMillis())
//                                .setContentTitle("Title")
//                                .setContentText("Notification text")
//                                .setSubText("Good")
//                                .setStyle(new NotificationCompat.BigTextStyle().bigText("Bareev dzezzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"))
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                                .setContentIntent(pendingIntent);
//
//                NotificationManager notificationManager =
//                        (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    String channelId = "Your_channel_id";
//                    NotificationChannel channel = new NotificationChannel(
//                            channelId,
//                            "Channel human readable title",
//                            NotificationManager.IMPORTANCE_HIGH);
//                    notificationManager.createNotificationChannel(channel);
//                    builder.setChannelId(channelId);
//                }
//                notificationManager.notify(new Random().nextInt(), builder.build());
//            }
//        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageReference = firebaseStorage.getReference("images/" + UUID.randomUUID().toString());
        if (requestCode == 1) {
            UploadTask uploadTask = storageReference.putFile(data.getData());
            Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
//                        Picasso.get().load()
                        Log.d("AddImage", "Hasav");
                        String fileUrl = task.getResult().toString().substring(0, task.getResult().toString().indexOf("token"));
                        Picasso.get().load(fileUrl).into(imageView);
                    }
                }
            });
        }


    }
}