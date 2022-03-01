package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogotPassword extends AppCompatActivity {
    EditText muser;
    Button btn_save,btn_done;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);
        muser = findViewById(R.id.userfoget);
        btn_save = findViewById(R.id.btn_receive);
        btn_done = findViewById(R.id.btn_done);

        mAuth = FirebaseAuth.getInstance();


        btn_save.setOnClickListener(v -> {
            String userfoget = muser.getText().toString();
            mAuth.sendPasswordResetEmail(userfoget).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(FogotPassword.this,"Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show();
                }
            });
        });
        btn_done.setOnClickListener(v -> {
            Intent intent = new Intent(FogotPassword.this,LoginActivity.class);
            startActivity(intent);
        });

    }
}