package com.example.trackingproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LocationOut extends AppCompatActivity {
    FusedLocationProviderClient locationProviderClient;
    Double lat, longt;
    TextView latitude, longitude, altitude, akurasi, tvalamatout;
    Button findLocation;
    String alamat, thisidkaryawan, thisusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_location_out);
        latitude = findViewById(R.id.latitudeout);
        longitude = findViewById(R.id.longitudeout);
        altitude = findViewById(R.id.altitudeout);
        //akurasi = findViewById(R.id.akurasi);
        findLocation = findViewById(R.id.btn_findout);
        tvalamatout = findViewById(R.id.alamatout);
        thisidkaryawan = getIntent().getStringExtra("idkaryawan");
        thisusername = getIntent().getStringExtra("username");
        locationProviderClient = LocationServices.getFusedLocationProviderClient(LocationOut.this);
        //MEMINTA IZIN UNTUK MENYALAKAN LAYANAN LOKASI
        if (ContextCompat.checkSelfPermission(LocationOut.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(LocationOut.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(LocationOut.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(LocationOut.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        findLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Izin lokasi tidak di aktifkan!", Toast.LENGTH_SHORT).show();
            } else {
                getLocation();
            }
        }
    }

    //MENCARI LOKASI TERKHIR
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // get Permission
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
        } else {
            // get Location
            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        altitude.setText(String.valueOf(location.getAltitude()));
                        convertAlamat();
                        tvalamatout.setText(alamat);
                        //akurasi.setText(location.getAccuracy() + "%");
                    }else{
                        //Toast.makeText(getApplicationContext(), "Lokasi tidak aktif!", Toast.LENGTH_SHORT).show();
                        new SweetAlertDialog(LocationOut.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("WARNING!!")
                                .setContentText("Layanan lokasi tidak aktif!")
                                .show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void convertAlamat(){
        lat = Double.valueOf(latitude.getText().toString());
        longt = Double.valueOf(longitude.getText().toString());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(lat,longt,1);
            alamat = list.get(0).getAddressLine(0)+", "+list.get(0).getCountryName();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void next(View view) {
        String getalamat = tvalamatout.getText().toString();
        if(getalamat.equals("")){
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("Lokasi Tidak Ditemukan!")
                    .show();
        }
        else {
            Intent nextpage = new Intent(LocationOut.this, Absen_keluar.class);
            nextpage.putExtra("locationout", getalamat);
            nextpage.putExtra("id_karyawan", thisidkaryawan);
            nextpage.putExtra("username", thisusername);
            startActivity(nextpage);
        }
    }
}