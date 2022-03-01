package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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

public class WeatherAcvity extends AppCompatActivity {
    WebView weather_view;
    DatabaseReference mData;
    Button btn;
    FirebaseFirestore db;
    String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_acvity);
        weather_view = (WebView) findViewById(R.id.weather_webview);
        btn = findViewById(R.id.btn_xong);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final String[] locationsarray ={"https://www.accuweather.com/vi/vn/hanoi/353412/weather-forecast/353412",  //0 Ha Noi
                "https://www.accuweather.com/vi/vn/ho-chi-minh-city/353981/weather-forecast/353981",     //1 Ho Chi Minh
                "https://www.accuweather.com/vi/vn/da-nang/352954/weather-forecast/352954",              //2 Da Nang
                "https://www.accuweather.com/vi/vn/can-tho/352508/weather-forecast/352508",              //3 Can Tho
                "https://www.accuweather.com/vi/vn/ha-long/355736/weather-forecast/355736",              //4 Ha Long
                "https://www.accuweather.com/vi/vn/hue/356204/weather-forecast/356204",                  //5 Hue
                "https://www.accuweather.com/vi/vn/nha-trang/354222/weather-forecast/354222",            //6 Nha Trang
                "https://www.accuweather.com/vi/vn/pleiku/353265/weather-forecast/353265"};                //7 Pleiku
        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                code = documentSnapshot.getString("code");
                mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                mData.child("Location").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        weather_view.setWebViewClient(new WebViewClient());
                        weather_view.loadUrl(locationsarray[Integer.parseInt(snapshot.getValue().toString())]);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                WebSettings webSettings = weather_view.getSettings();
                webSettings.setJavaScriptEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


}