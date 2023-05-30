package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Grafik extends AppCompatActivity {
    private String JSON_STRING;
    String idkaryawan;
    TextView tpt, tlt, tvjml;
    public ArrayList<String> listtpt;
     public ArrayList<String> listtlt;
    String counttlt, counttpt;
    int count_tepat, count_telat, jml, hsl_tpt, hsl_tlt;
    PieChart pieChart;
    String nama,idk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_grafik);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        idk = sharedPref.getString("idk", "");
        idkaryawan = getIntent().getStringExtra("idkaryawan");
        tpt = findViewById(R.id.tvtpt);
        tlt = findViewById(R.id.tvtlt);
        tvjml = findViewById(R.id.tvjml);
        pieChart = findViewById(R.id.piechart);
        getJSON();
        getJSONTLT();
        //count_tepat = Integer.valueOf(listtpt.get(0).toString());
//        count_telat = Integer.valueOf(listtlt.get(0).toString());
//        jml = count_tepat+count_telat;
//        tpt.setText(String.valueOf(jml));
//        hsl_tpt = (count_tepat/jml)*100;
//        tpt.setText(String.valueOf(hsl_tpt));
        //setData();
    }
    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Grafik.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_COUNT_TPT,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void show() {
        JSONObject jsonObject = null;
        listtpt = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                counttpt = jo.getString(konfigurasi.TAG_TPT);
//                count_tepat = Integer.valueOf(counttpt);
//                count_telat = Integer.valueOf(counttlt);
//                jml = count_tepat+count_telat;
//                hsl_tpt = (count_tepat/jml)*100;
//                tpt.setText(String.valueOf(hsl_tpt));
                //tpt.setText(counttpt);
                listtpt.add(counttpt);
                //tpt.setText(listtpt.get(0).toString());
                int gettpt = Integer.valueOf(listtpt.get(0).toString());
                        pieChart.addPieSlice(
                        new PieModel(
                        "Tepat",
                        gettpt,
                        Color.parseColor("#00F7FF")));
                //liststatus.add(status);
                HashMap<String, String> counttp = new HashMap<>();
                counttp.put(konfigurasi.TAG_TPT, counttpt);
                //listct.add(cuti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSONTLT() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Grafik.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showtlt();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_COUNT_TLT,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showtlt() {
        JSONObject jsonObject = null;
        listtlt = new ArrayList<String>();
//        listct = new
//                ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                counttlt = jo.getString(konfigurasi.TAG_TLT);
                listtlt.add(counttlt);
                //tlt.setText(listtlt.get(0).toString());
                //tpt.setText(listtpt.get(0));
                String vartpt = listtpt.get(0);
                String vartlt = listtlt.get(0);
                float getcount_tpt = Integer.valueOf(listtpt.get(0));
                float getcount_tlt = Integer.valueOf(listtlt.get(0));
                float jml = getcount_tpt+getcount_tlt;
                float hasiltpt, hasiltlt;
                hasiltpt = getcount_tpt*100/jml;
                hasiltlt = getcount_tlt*100/jml;
                String fixtpt = String.format("%.2f", hasiltpt);
                String fixtlt = String.format("%.2f", hasiltlt);
                tpt.setText(fixtpt+"%");
                tlt.setText(fixtlt+"%");
                //String jmlformat = String.valueOf(jml);
                tvjml.setText(String.format("%.0f", jml));
                int gettpt = Integer.valueOf(listtlt.get(0).toString());
                        pieChart.addPieSlice(
                        new PieModel(
                        "Telat",
                        gettpt,
                        Color.parseColor("#0055FF")));
                        pieChart.startAnimation();
                HashMap<String, String> counttl = new HashMap<>();
                counttl.put(konfigurasi.TAG_TLT, counttlt);
                //listct.add(cuti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData()
    {
        //tpt.setText(listtpt.get(0).toString());
        //tlt.setText(listtlt.get(0).toString());
        //int gettlt = Integer.valueOf(tlt.getText().toString());
        //int gettpt = Integer.valueOf(tpt.getText().toString());
        // Set the data and color to the pie chart
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Tepat",
//                        gettlt,
//                        Color.parseColor("#FFA726")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Telat",
//                        gettpt,
//                        Color.parseColor("#66BB6A")));
//
//
//        // To animate the pie chart
//        pieChart.startAnimation();
    }
}