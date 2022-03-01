package com.example.smarthome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterTabFragment extends Fragment {
    EditText mnewuser,mpass,mconfirm,maddress;
    Button msignup;
    FirebaseAuth mAuthencation;
    DatabaseReference databaseReference;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment, container, false);
        mnewuser = root.findViewById(R.id.newuser);
        mpass = root.findViewById(R.id.newpass);
        mconfirm = root.findViewById(R.id.confirm_pass);
        msignup = root.findViewById(R.id.btn_sign);
        maddress = root.findViewById(R.id.address);
        db = FirebaseFirestore.getInstance();

        mAuthencation = FirebaseAuth.getInstance();
        msignup.setOnClickListener(v -> {
            String newuser = mnewuser.getText().toString();
            String pass = mpass.getText().toString();
            String code = mconfirm.getText().toString();
            String address = maddress.getText().toString();
            mAuthencation.createUserWithEmailAndPassword(newuser,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String users = firebaseUser.getEmail();
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("code" + code);
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("code",code);
                        db.collection("users").document(users).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child("SmartHome").setValue("0000000000000000");
                                databaseReference.child("Address").setValue(address);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(), "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });


        return root;
    }

}
