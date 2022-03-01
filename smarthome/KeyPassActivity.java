package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

public class KeyPassActivity extends AppCompatActivity {
    DatabaseReference mData;
    EditText text;
    Button mbtn_change,mbtn_done;
    StringBuilder s;
    FirebaseFirestore db;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_pass);
        text = findViewById(R.id.txt_pass);
        mbtn_change = findViewById(R.id.btn_changepass);

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
                        mbtn_change.setOnClickListener(v -> {
                            Toast.makeText(KeyPassActivity.this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                            String pass = text.getText().toString();
                            String d = s.substring(0,10).concat(pass);
                            mData.child("SmartHome").setValue(d);
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

        mbtn_done = findViewById(R.id.btn_done);
        mbtn_done.setOnClickListener(v -> {
            finish();
        });

    }
}