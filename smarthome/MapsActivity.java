package com.example.smarthome;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.smarthome.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locationManager;
    private LocationListener locationListener;
    LatLng latLng,latLng1;
    Button btn_direction,btn_gps;
    String address1 = null;
    String city1 = null;
    String state1 = null;
    String country1 = null;
    String postalCode1 = null;
    String knonName1 = null;
    String address = null;
    String city = null;
    String state = null;
    String country = null;
    String postalCode = null;
    String knonName = null;
    double latitude,latitude1 ;
    double longitude,longitude1 ;
    String point,point1;
    DatabaseReference mData;

    FirebaseFirestore db;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String user = firebaseUser.getEmail();
        db.collection("users").document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                code = documentSnapshot.getString("code");
                mData = FirebaseDatabase.getInstance().getReference().child("code"+code);
                mData.child("Address").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String address = snapshot.getValue().toString();
                        getLocation(address);

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



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        btn_direction = findViewById(R.id.bt_direction);
        btn_gps = findViewById(R.id.bt_gps);

        btn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayTrack(point1,point);
            }
        });
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    showLocation();
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void showLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if(location!=null){
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        latitude1 = addressList.get(0).getLatitude();
                        longitude1 = addressList.get(0).getLongitude();
                        address1 = addressList.get(0).getAddressLine(0);
                        city1 = addressList.get(0).getLocality();
                        country1 = addressList.get(0).getCountryName();
                        state1 = addressList.get(0).getAdminArea();
                        postalCode1 = addressList.get(0).getPostalCode();
                        knonName1 = addressList.get(0).getFeatureName();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MapsActivity.this,"Location null error",Toast.LENGTH_SHORT).show();
                }
                latLng1 = new LatLng(latitude1, longitude1);
                point1 = latitude1 + "," + longitude1;
                mMap.addMarker(new MarkerOptions().position(latLng1).title(address1 + city1 + state1 + country1 + postalCode1 + knonName1));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1,8));

            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void getLocation(String add){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(add, 1);
            address = addresses.get(0).getAddressLine(0);
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knonName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        latLng = new LatLng(latitude, longitude);
        point = latitude + "," + longitude;
        mMap.addMarker(new MarkerOptions().position(latLng).title(address + city + state + country + postalCode + knonName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,8));

    }

    private void DisplayTrack(String sSource, String sDestination) {
        try {
            Uri uri = Uri.parse("http://www.google.co.in/maps/dir/" + sSource + "/" + sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}

