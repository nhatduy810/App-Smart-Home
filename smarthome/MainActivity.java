package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mData;
    TextView txtMucLuc3, txtMucLuc4;
    FirebaseFirestore db;
    String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.toastcus,(ViewGroup) findViewById(R.id.toast_lay));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(v);
        toast.show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    code = documentSnapshot.getString("code");
                    mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                    txtMucLuc3 = findViewById(R.id.txt_humidity);
                    txtMucLuc4 = findViewById(R.id.txt_temperature);
                    mData.child("Gas").setValue("0");
                    mData.child("Gas").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            float gas = Float.parseFloat(snapshot.getValue().toString());
                            if(gas > 400) {
                                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                        MainActivity.this,
                                        100,
                                        intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
                                builder.setContentTitle("My Title");
                                builder.setContentText("Warning Gas");
                                builder.setSmallIcon(R.drawable.warning1);
                                builder.setContentIntent(pendingIntent);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat manegerCompat = NotificationManagerCompat.from(MainActivity.this);
                                manegerCompat.notify(2, builder.build());
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    mData.child("Humidity").setValue("80");
                    mData.child("Humidity").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            txtMucLuc3.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    mData.child("Temperature").setValue("20");
                    mData.child("Temperature").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            txtMucLuc4.setText(dataSnapshot.getValue().toString());
                            float temp = Float.parseFloat(dataSnapshot.getValue().toString());
                            if(temp > 35){
                                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(
                                        MainActivity.this,
                                        100,
                                        intent,
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                );
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,"My Notification");
                                builder.setContentTitle("My Title");
                                builder.setContentText("Warning Temp");
                                builder.setSmallIcon(R.drawable.warning1);
                                builder.setContentIntent(pendingIntent);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat manegerCompat = NotificationManagerCompat.from(MainActivity.this);
                                manegerCompat.notify(1, builder.build());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        }).addOnFailureListener(MainActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    public void onLivingRoom(View view) {
        Intent intent = new Intent(MainActivity.this, LivingRoomActivity.class);
        startActivity(intent);
    }

    public void onBedRoom(View view) {
        Intent intent = new Intent(MainActivity.this, BedRoomActivity.class);
        startActivity(intent);
    }

    public void onKitchen(View view) {
        Intent intent = new Intent(MainActivity.this, Kitchen.class);
        startActivity(intent);
    }

    public void onGara(View view) {
        Intent intent = new Intent(MainActivity.this, Gara.class);
        startActivity(intent);
    }

    public void onOutside(View view) {
        Intent intent = new Intent(MainActivity.this, Outside.class);
        startActivity(intent);
    }

    public void onKeyPass(View view) {
        Intent intent = new Intent(MainActivity.this, KeyPassActivity.class);
        startActivity(intent);
    }

    public void onBackLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}