package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Outside extends AppCompatActivity {

    DatabaseReference mData;
    Button x,y,z;
    CheckBox data1,data2,data3,data4,data5,data6,data7,data8;
    StringBuilder s;
    String b,i;
    FirebaseFirestore db;
    String code;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside);
        x = findViewById(R.id.btn_in);
        y = findViewById(R.id.btn_out);
        z = findViewById(R.id.btn_weather);
        data1 = findViewById(R.id.hanoi);
        data2 = findViewById(R.id.hochiminh);
        data3 = findViewById(R.id.danang);
        data4 = findViewById(R.id.cantho);
        data5 = findViewById(R.id.halong);
        data6 = findViewById(R.id.hue);
        data7 = findViewById(R.id.nhatrang);
        data8 = findViewById(R.id.pleiku);

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
                        x.setOnClickListener(v -> {
                            Toast.makeText(Outside.this, "Kéo cây phơi đồ ra", Toast.LENGTH_SHORT).show();
                            s.setCharAt(2,'1');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        });
                        y.setOnClickListener(v -> {
                            Toast.makeText(Outside.this, "Kéo cây phơi đồ vào", Toast.LENGTH_SHORT).show();
                            s.setCharAt(2,'0');
                            b = s.toString();
                            mData.child("SmartHome").setValue(b);
                        });
                        z.setOnClickListener(v -> {
                            if(data1.isChecked()) i = "0";
                            if(data2.isChecked()) i = "1";
                            if(data3.isChecked()) i = "2";
                            if(data4.isChecked()) i = "3";
                            if(data5.isChecked()) i = "4";
                            if(data6.isChecked()) i = "5";
                            if(data7.isChecked()) i = "6";
                            if(data8.isChecked()) i = "7";
                            mData.child("Location").setValue(i);
                            Intent intent = new Intent(Outside.this,WeatherAcvity.class);
                            startActivity(intent);
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    public void backMain4(View view) {finish();
    }

}