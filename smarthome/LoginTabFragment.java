package com.example.smarthome;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginTabFragment extends Fragment {
    Button mlog;
    EditText muser, mpass;
    FirebaseAuth mAuth;
    TextView txtfoget;
    float v=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);
        mlog = root.findViewById(R.id.btn_login);
        muser = root.findViewById(R.id.user);
        txtfoget = root.findViewById(R.id.fogetpass);
        mpass = root.findViewById(R.id.password);

        muser.setTranslationX(800);
        mpass.setTranslationX(800);
        txtfoget.setTranslationX(800);
        mlog.setTranslationX(800);

        muser.setAlpha(v);
        mpass.setAlpha(v);
        txtfoget.setAlpha(v);
        mlog.setAlpha(v);

        muser.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        mpass.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        txtfoget.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        mlog.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        mAuth = FirebaseAuth.getInstance();

        mlog.setOnClickListener(v -> {
            String user = muser.getText().toString();
            String pass = mpass.getText().toString();
            mAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
        txtfoget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(),FogotPassword.class);
            startActivity(intent);
        });
        return root;
    }
}
