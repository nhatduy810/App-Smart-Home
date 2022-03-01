package com.example.smarthome;

import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

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

import java.util.Calendar;

public class BedRoomActivity extends AppCompatActivity {

    DatabaseReference mData;
    Drawable bulbon, bulboff, fanon, fanoff;
    ToggleButton x, y;
    ImageView img_bulb, img_fan;
    StringBuilder s;
    String b;
    FirebaseFirestore db;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_room);
        bulbon = AppCompatResources.getDrawable(this,R.drawable.bulbon);
        bulboff = AppCompatResources.getDrawable(this,R.drawable.bulbofff);
        fanon = AppCompatResources.getDrawable(this,R.drawable.fanon);
        fanoff = AppCompatResources.getDrawable(this,R.drawable.fanoff);

        x = findViewById(R.id.btn_blub);
        y = findViewById(R.id.btn_fan);

        img_bulb = findViewById(R.id.bulb_bed);
        img_fan = findViewById(R.id.fan_bed);

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(BedRoomActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                code = documentSnapshot.getString("code");
                mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                mData.child("SmartHome").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        s = new StringBuilder(snapshot.getValue().toString());
                        if (s.charAt(8) == '1'){
                            x.setChecked(true);
                            img_bulb.setImageDrawable(bulbon);
                        }else {
                            x.setChecked(false);
                            img_bulb.setImageDrawable(bulboff);
                        }
                        if(s.charAt(4)=='1'){
                            y.setChecked(true);
                            img_fan.setImageDrawable(fanon);
                        }
                        else {
                            y.setChecked(false);
                            img_fan.setImageDrawable(fanoff);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                x.setOnClickListener(v -> {
                    if (x.isChecked()) {
                        img_bulb.setImageDrawable(bulbon);
                        Toast.makeText(BedRoomActivity.this, "Đèn bật", Toast.LENGTH_SHORT).show();
                        s.setCharAt(8,'1');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    } else {
                        img_bulb.setImageDrawable(bulboff);
                        Toast.makeText(BedRoomActivity.this, "Đèn tắt", Toast.LENGTH_SHORT).show();
                        s.setCharAt(8,'0');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    }
                });

                y.setOnClickListener(v -> {
                    if (y.isChecked()) {
                        img_fan.setImageDrawable(fanon);
                        Toast.makeText(BedRoomActivity.this, "Quạt bật", Toast.LENGTH_SHORT).show();
                        s.setCharAt(4,'1');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    } else {
                        img_fan.setImageDrawable(fanoff);
                        Toast.makeText(BedRoomActivity.this, "Quạt tắt", Toast.LENGTH_SHORT).show();
                        s.setCharAt(4,'0');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public void backMain2(View view) {
        finish();
    }
}