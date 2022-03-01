package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

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

public class Kitchen extends AppCompatActivity {
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
        setContentView(R.layout.activity_kitchen);
        bulbon = AppCompatResources.getDrawable(this,R.drawable.bulbon);
        bulboff = AppCompatResources.getDrawable(this,R.drawable.bulbofff);
        fanon = AppCompatResources.getDrawable(this,R.drawable.fanon);
        fanoff = AppCompatResources.getDrawable(this,R.drawable.fanoff);

        x = findViewById(R.id.btn_blub);
        y = findViewById(R.id.btn_fan);

        img_bulb = findViewById(R.id.bulb_kit);
        img_fan = findViewById(R.id.fan_kit);

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                code = documentSnapshot.getString("code");
                mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                mData.child("SmartHome").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        s = new StringBuilder(snapshot.getValue().toString());
                        if (s.charAt(9) == '1'){
                            x.setChecked(true);
                            img_bulb.setImageDrawable(bulbon);
                        }else {
                            x.setChecked(false);
                            img_bulb.setImageDrawable(bulboff);
                        }
                        if(s.charAt(5)=='1'){
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
                        Toast.makeText(Kitchen.this, "Đèn bật", Toast.LENGTH_SHORT).show();
                        s.setCharAt(9,'1');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    } else {
                        img_bulb.setImageDrawable(bulboff);
                        s.setCharAt(9,'0');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    }
                });

                y.setOnClickListener(v -> {
                    if (y.isChecked()) {
                        img_fan.setImageDrawable(fanoff);
                        Toast.makeText(Kitchen.this, "Quạt bật", Toast.LENGTH_SHORT).show();
                        s.setCharAt(5,'1');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    } else {
                        img_fan.setImageDrawable(fanon);
                        Toast.makeText(Kitchen.this, "Quạt tắt", Toast.LENGTH_SHORT).show();
                        s.setCharAt(5,'0');
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

    public void backMain3(View view) {finish();
    }
}