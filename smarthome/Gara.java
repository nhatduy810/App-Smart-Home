package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Gara extends AppCompatActivity {
    DatabaseReference mData;
    Drawable bulbon, bulboff, doorup, doordown;
    Button x,y;
    ToggleButton z;
    ImageView img_gara, img_bulb;
    StringBuilder s;
    String b;
    FirebaseFirestore db;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gara);
        bulbon = AppCompatResources.getDrawable(this,R.drawable.bulbon);
        bulboff = AppCompatResources.getDrawable(this,R.drawable.bulbofff);
        doordown = AppCompatResources.getDrawable(this,R.drawable.garadoor_down);
        doorup = AppCompatResources.getDrawable(this,R.drawable.garaup);
        x = findViewById(R.id.btn_up);
        y = findViewById(R.id.btn_down);
        z = findViewById(R.id.btn_blubgara);

        img_bulb = findViewById(R.id.bulb_gara);
        img_gara = findViewById(R.id.door_gara);

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
                        if (s.charAt(1) == '1'){
                            img_gara.setImageDrawable(doorup);
                        }else {
                            img_gara.setImageDrawable(doordown);
                        }

                        if(s.charAt(7)=='1'){
                            z.setChecked(true);
                            img_bulb.setImageDrawable(bulbon);

                        }
                        else {
                            z.setChecked(false);
                            img_bulb.setImageDrawable(bulboff);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                y.setOnClickListener(v -> {
                    img_gara.setImageDrawable(doordown);
                    Toast.makeText(Gara.this, "Cửa mở", Toast.LENGTH_SHORT).show();
                    s.setCharAt(1,'0');
                    b = s.toString();
                    mData.child("SmartHome").setValue(b);
                });
                x.setOnClickListener(v -> {
                    img_gara.setImageDrawable(doorup);
                    Toast.makeText(Gara.this, "Cửa đóng", Toast.LENGTH_SHORT).show();
                    s.setCharAt(1,'1');
                    b = s.toString();
                    mData.child("SmartHome").setValue(b);
                });
                z.setOnClickListener(v -> {
                    if (z.isChecked()) {
                        img_bulb.setImageDrawable(bulbon);
                        Toast.makeText(Gara.this, "Đèn bật", Toast.LENGTH_SHORT).show();
                        s.setCharAt(7,'1');
                        b = s.toString();
                        mData.child("SmartHome").setValue(b);
                    } else {
                        img_bulb.setImageDrawable(bulboff);
                        Toast.makeText(Gara.this, "Đèn tắt", Toast.LENGTH_SHORT).show();
                        s.setCharAt(7,'0');
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

    public void backMain6(View view) {finish();
    }
}