package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {

//    ArrayList<HashMap<String, String>> list;
    public ArrayList<String> cekkx , izinbray , sakitbray , cutikhusus;
    TextView cuti , izin , sakit , tvhp ,tvnama , tvkhusus;
    private String JSON_STRING;
    String nama , hp , idk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        hp = sharedPref.getString("hp", "");
        idk = sharedPref.getString("idk", "");

        cuti = findViewById(R.id.tvcuti);
        izin = findViewById(R.id.tvizin);
        sakit = findViewById(R.id.tvsakit);
        tvnama = findViewById(R.id.tvnama);
        tvhp = findViewById(R.id.tvhp);
        tvkhusus = findViewById(R.id.tvcutikhusus);

        tvhp.setText(hp);
        tvnama.setText(nama);

        getJSON();
        getJSONIZIN();
        getJSONSAKIT();
        getJSON_CUTIKHUSUS();
    }

    private void show() {
        JSONObject jsonObject = null;
        cekkx = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String total = jo.getString(konfigurasi.TAG_TTLCUTI);
                cekkx.add(total);
                if(cekkx.get(0).equals("null")) {
                    cuti.setText("0");
                }
                else {
                    cuti.setText(cekkx.get(0));
                }
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
                loading = ProgressDialog.show(ProfilActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_TTLCUTI, idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showcutikhusus() {
        JSONObject jsonObject = null;
        cutikhusus = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String total = jo.getString(konfigurasi.TAG_CUTIKHUSUS);
//                String totalizin = jo.getString(konfigurasi.TAG_TTLIZIN);
//                String totalsakit = jo.getString(konfigurasi.TAG_TTLSAKIT);
                cutikhusus.add(total);
//                izinbray.add(totalizin);
//                cekkx.add(totalsakit);
                if(cutikhusus.get(0).equals("null")) {
                    tvkhusus.setText("0");
//                izin.setText(izinbray.get(0));
                }
                else {
                    tvkhusus.setText(cutikhusus.get(0));
                }
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }

    private void getJSON_CUTIKHUSUS() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfilActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showcutikhusus();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_CUTIKHUSUS, idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void editprofil(View view) {
        Intent intent = new Intent(ProfilActivity.this, EditProfilActivity.class);
        startActivity(intent);
    }
    private void showizin() {
        JSONObject jsonObject = null;
        izinbray = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
//                String total = jo.getString(konfigurasi.TAG_TTLCUTI);
                String totalizin = jo.getString(konfigurasi.TAG_TTLIZIN);
//                String totalsakit = jo.getString(konfigurasi.TAG_TTLSAKIT);
//                cekkx.add(total);
                izinbray.add(totalizin);
                if(izinbray.get(0).equals("null")) {
                    izin.setText("0");
//                izin.setText(izinbray.get(0));
                }
                else {
                    izin.setText(izinbray.get(0));
                }

            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }
    private void getJSONIZIN() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfilActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showizin();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_TTLIZIN, idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showsakit() {
        JSONObject jsonObject = null;
        sakitbray = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
//                String total = jo.getString(konfigurasi.TAG_TTLCUTI);
//                String totalizin = jo.getString(konfigurasi.TAG_TTLIZIN);
                String totalsakit = jo.getString(konfigurasi.TAG_TTLSAKIT);
//                cekkx.add(total);
                sakitbray.add(totalsakit);
                if(sakitbray.get(0).equals("null")) {
                    sakit.setText("0");
                }
                else {
                    sakit.setText(sakitbray.get(0));
                }

            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }

    private void getJSONSAKIT() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfilActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showsakit();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_TTLSAKIT, idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}