package com.example.trackingproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Absen_keluar extends AppCompatActivity {
    ImageView img;
    String encodeImage, time;
    int hari, bulan, tahun, hour, minute;
    String getidkaryawan, datenow, getusername, locationout;
    String nama,idk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_absen_keluar);
        //MEMANGGIL SESSION
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        idk = sharedPref.getString("idk", "");
        //AKHIR DARI SESSION

        img = findViewById(R.id.pictureout);
        getidkaryawan = getIntent().getStringExtra("id_karyawan");
        getusername = getIntent().getStringExtra("username");
        locationout = getIntent().getStringExtra("locationout");
        //MEMINTA IZIN UNTUK MENYALAKAN CAMERA
        if(ContextCompat.checkSelfPermission(Absen_keluar.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Absen_keluar.this,
                    new String[]{Manifest.permission.CAMERA}, 100);
        }

        Calendar calendar = Calendar.getInstance();
        hari = calendar.get(Calendar.DAY_OF_MONTH);
        bulan = calendar.get(Calendar.MONTH);
        tahun = calendar.get(Calendar.YEAR);
        String gethari = String.valueOf(hari);
        bulan = bulan+1;
        String getbulan = String.valueOf(bulan);
        if (gethari.length() < 2){
            gethari = "0"+hari;
        }
        else {
            gethari = hari+"";
        }
        if(getbulan.length() < 2){
            getbulan = "0"+bulan;
        }
        else {
            getbulan = bulan+"";
        }
        datenow = tahun+"-"+getbulan+"-"+gethari;


        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        String gethour = String.valueOf(hour);
        String getminute = String.valueOf(minute);
        if (gethour.length() < 2){
            gethour = "0"+hour;
        }
        else {
            gethour = hour+"";
        }
        if(getminute.length() < 2){
            getminute = "0"+minute;
        }
        else {
            getminute = minute+"";
        }
        time = gethour+":"+getminute;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(bitmap);
        }
    }




    public void uploadout(View view) {
        img.buildDrawingCache();
        Bitmap bitmap = img.getDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // --> quality asal 100
        byte[] byteArray = stream.toByteArray();
        //encodeImage = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT);
        encodeImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://trackingimage.000webhostapp.com/upload_out.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")){
                            //Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();

                            new SweetAlertDialog(Absen_keluar.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("SUCCESS")
                                    .setContentText("Absen Berhasil !")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Intent intent = new Intent(Absen_keluar.this,Menu.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })

                                    .show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("image", encodeImage);
                paramV.put("name", nama);
                paramV.put("id", idk);
                return paramV;
            }
        };
        queue.add(stringRequest);
        //TAMBAH DATA
        class Absenkeluar extends AsyncTask<Void, Void , String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Absen_keluar.this,
                        "Menambahkan...", "Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Absen_keluar.this, s,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String, String> paramsx = new HashMap<>();
                HashMap<String, String> params = new HashMap<>();
                params.put(konfigurasi.KEY_EMP_GAMBAR, encodeImage);
                params.put(konfigurasi.KEY_EMP_IDK, idk);
                params.put(konfigurasi.KEY_EMP_TANGGAL, datenow);
                params.put(konfigurasi.KEY_EMP_NAMA, nama);
                params.put(konfigurasi.KEY_JAM_KELUAR, time);
                params.put(konfigurasi.KEY_LOKASI_KELUAR, locationout);
                //params.put(konfigurasi.KEY_EMP_GAMBAR, byteArray.toString());
                RequestHandler rh = new RequestHandler();
                //String res;
                String res = rh.sendPostRequest(konfigurasi.URL_ABSEN_OUT,
                        params);

                return res;
            }

        }
        Absenkeluar at = new Absenkeluar();
        at.execute();
        img.setImageDrawable(null);
    }

    public void captureout(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }
}