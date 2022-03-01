package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class LivingRoomActivity extends AppCompatActivity {

    DatabaseReference mData;
    Drawable bulbon,bulboff,fanon,fanoff,opendoor,closedoor ;
    ToggleButton x, y, z;
    ImageView img_bulb, img_fan, img_door;
    StringBuilder s;
    String b;
    FirebaseFirestore db;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_room);
        img_bulb = findViewById(R.id.bulb_living);
        img_fan = findViewById(R.id.fan_living);
        img_door = findViewById(R.id.door_living);

        bulbon = AppCompatResources.getDrawable(this,R.drawable.bulbon);
        bulboff = AppCompatResources.getDrawable(this,R.drawable.bulbofff);
        fanon = AppCompatResources.getDrawable(this,R.drawable.fanon);
        fanoff = AppCompatResources.getDrawable(this,R.drawable.fanoff);
        opendoor = AppCompatResources.getDrawable(this, R.drawable.opendoor);
        closedoor = AppCompatResources.getDrawable(this, R.drawable.closedoor);

        x = findViewById(R.id.btn_blub);
        y = findViewById(R.id.btn_fan);
        z = findViewById(R.id.btn_lockliving);

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(LivingRoomActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    code = documentSnapshot.getString("code");
                    mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                    mData.child("SmartHome").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            s = new StringBuilder(snapshot.getValue().toString());
                            if (s.charAt(6) == '1'){
                                x.setChecked(true);
                                img_bulb.setImageDrawable(bulbon);
                            }else {
                                x.setChecked(false);
                                img_bulb.setImageDrawable(bulboff);
                            }
                            if(s.charAt(3)=='1'){
                                y.setChecked(true);
                                img_fan.setImageDrawable(fanon);
                            }
                            else {
                                y.setChecked(false);
                                img_fan.setImageDrawable(fanoff);
                            }
                            if(s.charAt(0)=='1'){
                                z.setChecked(true);
                                img_door.setImageDrawable(opendoor);
                            }
                            else {
                                z.setChecked(false);
                                img_door.setImageDrawable(closedoor);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    x.setOnClickListener(v -> {
                        if (x.isChecked()) {
                            img_bulb.setImageDrawable(bulbon);
                            Toast.makeText(LivingRoomActivity.this, "Đèn 1 bật", Toast.LENGTH_SHORT).show();
                            s.setCharAt(6,'1');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        } else {
                            img_bulb.setImageDrawable(bulboff);
                            Toast.makeText(LivingRoomActivity.this, "Đèn 1 tắt", Toast.LENGTH_SHORT).show();
                            s.setCharAt(6,'0');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        }
                    });
                    y.setOnClickListener(v -> {
                        if (y.isChecked()) {
                            img_fan.setImageDrawable(fanon);
                            Toast.makeText(LivingRoomActivity.this, "Quạt 1 bật", Toast.LENGTH_SHORT).show();
                            s.setCharAt(3,'1');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        } else {
                            img_fan.setImageDrawable(fanoff);
                            Toast.makeText(LivingRoomActivity.this, "Quạt 1 tắt", Toast.LENGTH_SHORT).show();
                            s.setCharAt(3,'0');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        }
                    });
                    z.setOnClickListener(v -> {
                        if (z.isChecked()) {
                            img_door.setImageDrawable(AppCompatResources.getDrawable(LivingRoomActivity.this,R.drawable.opendoor));
                            z.setBackgroundResource(R.drawable.lock);
                            Toast.makeText(LivingRoomActivity.this, "Cửa mở", Toast.LENGTH_SHORT).show();
                            s.setCharAt(0,'1');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        } else {
                            img_door.setImageDrawable(AppCompatResources.getDrawable(LivingRoomActivity.this,R.drawable.closedoor));
                            z.setBackgroundResource(R.drawable.unlock);
                            Toast.makeText(LivingRoomActivity.this, "Cửa đóng", Toast.LENGTH_SHORT).show();
                            s.setCharAt(0,'0');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        }
                    });
                }
            }
        }).addOnFailureListener(LivingRoomActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    public void backMain1(View view) {
        finish();
    }
}