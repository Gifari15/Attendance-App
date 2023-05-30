package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {

    TextView user, pass, tesid;
    ImageView waveImage;
    String getuser, getpass, status;
    private String JSON_STRING;
    ArrayList<HashMap<String, String>> list;
    public ArrayList<String> list_idk;
    public ArrayList<String> list_nama;
    public ArrayList<String> list_pass;
    public ArrayList<String> list_username;
    public ArrayList<String> stats;
    public ArrayList<String> list_alamat;
    public ArrayList<String> list_hp;
    String getnewpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        user = findViewById(R.id.editTextUser);
        pass = findViewById(R.id.editTextPass);
        waveImage = findViewById(R.id.wave_image);
        tesid = findViewById(R.id.tesid);
        getnewpass = getIntent().getStringExtra("newpassword");
//        if(getnewpass.length() > 1){
//            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                    .setTitleText("SUCCESS")
//                    .setContentText("Silahkan login kembali !")
//                    .show();
//        }
//        else{
//
//        }
//        AnimationDrawable waveAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.shape);
//        waveImage.setBackground(waveAnimation);
//        waveAnimation.start();
        getJSON();
    }

    public void login(View view) {
        getuser = user.getText().toString();
        getpass = pass.getText().toString();
        stats = new ArrayList<String>();
        if (getuser.equals("") || getpass.equals("")){
            //Toast.makeText(getApplicationContext(), "Username dan Password harus diisi", Toast.LENGTH_SHORT).show();
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("Username dan Password harus diisi!")
                    .show();
        }
        else {
            for(int j=0; j<list.toArray().length; j++){
                if(list_pass.get(j).equals(getpass) && list_username.get(j).equals(getuser)) {
                    //tampil.setText(list_idk.get(j).toString());
                    stats.add(list_idk.get(j));
                    String id_karyawan = list_idk.get(j);
                    String getname = list_nama.get(j);
                    String username = list_username.get(j);
                    String password = list_pass.get(j);
                    String alamat = list_alamat.get(j);
                    String hp = list_hp.get(j);
                    Intent home = new Intent(login.this, Menu.class);
                    SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("alamat", alamat);
                    editor.putString("hp", hp);
                    editor.putString("idk", id_karyawan);
                    editor.putString("nama", getname);
                    editor.apply();
                    home.putExtra("passuser", getname);
                    home.putExtra("passkey", id_karyawan);
                    home.putExtra("oldpass", getpass);
                    //tesid.setText(id_karyawan);
                    startActivity(home);
                    finish();
                    //Toast.makeText(getApplicationContext(), "Berhasil Login", Toast.LENGTH_SHORT).show();

                    status = "berhasil";
                }
                else if(list_idk.get(j)!=getpass){
//                    Toast.makeText(getApplicationContext(), "Gagal Login!", Toast.LENGTH_SHORT).show();
                    status="gagal";
                }
            }
            if(stats.size()==0){
                //Toast.makeText(getApplicationContext(), "Gagal Login!", Toast.LENGTH_SHORT).show();
                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Gagal Login !")
                        .show();
            }

        }
//        Intent home = new Intent(login.this, Menu.class);
//        startActivity(home);
//        finish();
    }
    private void show() {
        JSONObject jsonObject = null;
        list = new ArrayList<HashMap<String, String>>();
        list_idk = new ArrayList<String>();
        list_nama = new ArrayList<String>();
        list_pass = new ArrayList<String>();
        list_username = new ArrayList<String>();
        list_alamat = new ArrayList<String>();
        list_hp = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_IDK);
                String name = jo.getString(konfigurasi.TAG_NAMA);
                String password = jo.getString(konfigurasi.TAG_PASSWORD);
                String username = jo.getString(konfigurasi.TAG_USERNAME);
                String alamat = jo.getString(konfigurasi.TAG_ALAMAT);
                String hp = jo.getString(konfigurasi.TAG_HP);
                list_idk.add(id);
                list_nama.add(name);
                list_pass.add(password);
                list_username.add(username);
                list_alamat.add(alamat);
                list_hp.add(hp);
                HashMap<String, String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_IDK, id);
                list.add(employees);
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(login.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                show();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALLK);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void register(View view) {
        Intent intent = new Intent(login.this, RegisterActivity.class);
        startActivity(intent);
    }
}