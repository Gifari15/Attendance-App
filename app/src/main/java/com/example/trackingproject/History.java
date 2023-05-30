package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class History extends AppCompatActivity {
    private ListView lvhistory;
    private String JSON_STRING;
    public ArrayList<String> liststatus;
    String getidk, uhuy, status;
    String varngide, varsort;
    CircleButton btn1,btn2;
    TextView tvstatus, tvtanggal;
    int hari, bulan, tahun;
    String datenow;
    Dialog dialog;
    DatePickerDialog datePickerDialog;
    ArrayList<HashMap<String, String>> list;
    ArrayList<HashMap<String, String>> cari;
    String nama,idk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);
        getidk = getIntent().getStringExtra("passidk");
        btn1 = findViewById(R.id.circlebtn);
        btn2 = findViewById(R.id.circlebtn3);
        tvtanggal = findViewById(R.id.tvtanggal);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        idk = sharedPref.getString("idk", "");

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
        tvtanggal.setText(datenow);
        //tvstatus = findViewById(R.id.tvstatus);
        lvhistory = findViewById(R.id.lv);
        varngide = "true";
        varsort = "true";
        getJSON();
    }

    private void show() {
        JSONObject jsonObject = null;
         list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String tanggal = jo.getString(konfigurasi.TAG_TANGGAL);
                String keluar = jo.getString(konfigurasi.TAG_JAM_OUT);
                String time = jo.getString(konfigurasi.TAG_TIME);
                status = jo.getString(konfigurasi.TAG_STATUS);
                HashMap<String, String> history = new HashMap<>();
                history.put(konfigurasi.TAG_TANGGAL, tanggal);
                history.put(konfigurasi.TAG_JAM_OUT, keluar);
                history.put(konfigurasi.TAG_TIME, time);
                history.put(konfigurasi.TAG_STATUS, status);
                list.add(history);
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

//        ListAdapter adapter = new SimpleAdapter(
//                History.this, list, R.layout.list_item,
//                new String[]{konfigurasi.TAG_TANGGAL, konfigurasi.TAG_TIME, konfigurasi.TAG_STATUS},
//                new int[]{R.id.tvtgl, R.id.tvtime, R.id.tvstatus});
//        lvhistory.setAdapter(adapter);
        ListAdapter adapter = new SimpleAdapter(History.this,
                list,
                R.layout.list_item,
                new String[] {konfigurasi.TAG_TANGGAL, konfigurasi.TAG_TIME, konfigurasi.TAG_STATUS, konfigurasi.TAG_JAM_OUT},
                new int[] {R.id.tvtgl, R.id.tvtime, R.id.tvstatus, R.id.tvkeluar}) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView,
                        parent);
                //float ratarata = Float.parseFloat(rata_rata);
                String getstatus = list.get(position).get(konfigurasi.TAG_STATUS);
                int posisi = position;
                int textColorId;
                TextView text;
                text = (TextView) view.findViewById(R.id.tvstatus);

                if (getstatus.equals("Tepat Waktu")) {
                    textColorId = R.color.green;
                } else{
                    textColorId = R.color.red;
                }
                text.setTextColor(getResources().getColor(textColorId));

                return view;
            }
        };
        lvhistory.setAdapter(adapter);

    }


    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(History.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_ABSEN_DETAIL,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void sortasc() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(History.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_ASCAB,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void floatingbtn(View view) {

        if(varngide.equals("true")){
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            varngide = "false";
        }
        else if(varngide.equals("false")){
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            varngide = "true";
        }
        else{

        }
    }

    public void srchdate(View view) {
        dateDialog();
        btn1.setVisibility(View.INVISIBLE);
        btn2.setVisibility(View.INVISIBLE);
        varngide = "true";
    }

    void dateDialog(){
        //final Dialog dialog = new Dialog(this);
        dialog = new Dialog(this);
        //Mengeset judul dialog
        dialog.setTitle("Edit Task");

        //Mengeset layout
        dialog.setContentView(R.layout.custom_dialog);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) dialog.findViewById(R.id.button_cancell);
        Button searchButton = (Button) dialog.findViewById(R.id.button_search);
        EditText tgl1 = (EditText) dialog.findViewById(R.id.editTexttg1);
        EditText tgl2 = (EditText) dialog.findViewById(R.id.editTexttg);

        tgl1.setText(datenow);
        tgl2.setText(datenow);


        //EDIT TEXT SEARCH TANGGAL
        tgl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate1();
            }
        });

        tgl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate2();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettgl1 = tgl1.getText().toString();
                String gettgl2 = tgl2.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://tracking-v.000webhostapp.com/tracking/filterdatehs.php";


                  //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Tindakan setelah mendapatkan respons dari server
                                JSONObject jsonObject = null;
                                cari = new ArrayList<HashMap<String, String>>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String getdatesrc = data.getString(konfigurasi.TAG_TANGGAL);
                                        String keluar = data.getString(konfigurasi.TAG_JAM_OUT);
                                        String gettimesrc = data.getString(konfigurasi.TAG_TIME);
                                        String stats = data.getString(konfigurasi.TAG_STATUS);
                                        HashMap<String, String> historydate = new HashMap<>();
                                        historydate.put(konfigurasi.TAG_TANGGAL, getdatesrc);
                                        historydate.put(konfigurasi.TAG_JAM_OUT, keluar);
                                        historydate.put(konfigurasi.TAG_TIME, gettimesrc);
                                        historydate.put(konfigurasi.TAG_STATUS, stats);
                                        cari.add(historydate);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ListAdapter adapter = new SimpleAdapter(History.this,
                                        cari,
                                        R.layout.list_item,
                                        new String[] {konfigurasi.TAG_TANGGAL, konfigurasi.TAG_TIME, konfigurasi.TAG_STATUS, konfigurasi.TAG_JAM_OUT},
                                        new int[] {R.id.tvtgl, R.id.tvtime, R.id.tvstatus, R.id.tvkeluar}) {
                                    @Override
                                    public View getView(int position, View convertView,
                                                        ViewGroup parent) {
                                        View view = super.getView(position, convertView,
                                                parent);
                                        String getstatus = cari.get(position).get(konfigurasi.TAG_STATUS);
                                        int posisi = position;
                                        int textColorId;
                                        TextView text;
                                        text = (TextView) view.findViewById(R.id.tvstatus);

                                        if (getstatus.equals("Tepat Waktu")) {
                                            textColorId = R.color.green;
                                        } else{
                                            textColorId = R.color.red;
                                        }
                                        text.setTextColor(getResources().getColor(textColorId));

                                        return view;
                                    }
                                };
                                lvhistory.setAdapter(adapter);
                                // tambahkan data ke dalam list
                                //dataList.add(new Data(name, address));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Tindakan jika gagal menghubungi server

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramsh = new HashMap<String, String>();
                        paramsh.put("idk", idk);
                        paramsh.put("from", gettgl1);
                        paramsh.put("to", gettgl2);
                        return paramsh;
                    }
                };
                queue.add(request);
                // membuat request pencarian menggunakan Volley
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Menampilkan custom dialog
        dialog.show();
    }

    void getdate1(){
        Calendar newCalendar = Calendar.getInstance();
        //datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        EditText tgl1 = (EditText) dialog.findViewById(R.id.editTexttg1);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */
                String tglsekarang = "";
                String getdate;
                Calendar calendar = Calendar.getInstance();
                hari = calendar.get(Calendar.DAY_OF_MONTH);
                bulan = calendar.get(Calendar.MONTH);
                tahun = calendar.get(Calendar.YEAR);
                tglsekarang = (new StringBuilder()
                        .append(hari).append("-").append(bulan + 1).append("-").append(tahun).toString());
                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                //getdate = newDate.getTime().toString();
                String gethari = String.valueOf(dayOfMonth);
                monthOfYear = monthOfYear+1;
                String getbulan = String.valueOf(monthOfYear);
                if (gethari.length() < 2){
                    gethari = "0"+dayOfMonth;
                }
                else {
                    gethari = dayOfMonth+"";
                }
                if(getbulan.length() < 2){
                    getbulan = "0"+monthOfYear;
                }
                else {
                    getbulan = monthOfYear+"";
                }
                getdate = year + "-" + (getbulan) + "-" + gethari;

                tgl1.setText(getdate);

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void getdate2(){
        Calendar newCalendar = Calendar.getInstance();
        //datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        EditText tgl2 = (EditText) dialog.findViewById(R.id.editTexttg);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */
                String tglsekarang = "";
                String getdate;
                Calendar calendar = Calendar.getInstance();
                hari = calendar.get(Calendar.DAY_OF_MONTH);
                bulan = calendar.get(Calendar.MONTH);
                tahun = calendar.get(Calendar.YEAR);
                tglsekarang = (new StringBuilder()
                        .append(hari).append("-").append(bulan + 1).append("-").append(tahun).toString());
                //AKHIR DARI NGIDE SYNTAX
                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                //getdate = newDate.getTime().toString();
                String gethari = String.valueOf(dayOfMonth);
                monthOfYear = monthOfYear+1;
                String getbulan = String.valueOf(monthOfYear);
                if (gethari.length() < 2){
                    gethari = "0"+dayOfMonth;
                }
                else {
                    gethari = dayOfMonth+"";
                }
                if(getbulan.length() < 2){
                    getbulan = "0"+monthOfYear;
                }
                else {
                    getbulan = monthOfYear+"";
                }
                getdate = year + "-" + (getbulan) + "-" + gethari;
                tgl2.setText(getdate);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void sort(View view) {
        if (varsort.equals("true")){
            sortasc();
            varsort="false";
        }
        else {
            getJSON();
            varsort="true";
        }
    }

    void statusdialog(){
        //final Dialog dialog = new Dialog(this);
        dialog = new Dialog(this);
        //Mengeset judul dialog
        //dialog.setTitle("Edit Task");

        //Mengeset layout
        dialog.setContentView(R.layout.search_status);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) dialog.findViewById(R.id.button_batal);
        Button searchButton = (Button) dialog.findViewById(R.id.button_cari);
        Spinner status = (Spinner) dialog.findViewById(R.id.spinner_status);
        String list[]={"Tepat Waktu", "Terlambat"};
        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        status.setAdapter(AdapterList);


        //AKHIR DARI BUTTON PENAMBAHAN TANGGAL

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getstatus = status.getSelectedItem().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://tracking-v.000webhostapp.com/tracking/filterstatushs.php";


                //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Tindakan setelah mendapatkan respons dari server
                                JSONObject jsonObject = null;
                                cari = new ArrayList<HashMap<String, String>>();
                                try {
                                    jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        String getdatesrc = data.getString(konfigurasi.TAG_TANGGAL);
                                        String keluar = data.getString(konfigurasi.TAG_JAM_OUT);
                                        String gettimesrc = data.getString(konfigurasi.TAG_TIME);
                                        String stats = data.getString(konfigurasi.TAG_STATUS);
                                        HashMap<String, String> historydate = new HashMap<>();
                                        historydate.put(konfigurasi.TAG_TANGGAL, getdatesrc);
                                        historydate.put(konfigurasi.TAG_JAM_OUT, keluar);
                                        historydate.put(konfigurasi.TAG_TIME, gettimesrc);
                                        historydate.put(konfigurasi.TAG_STATUS, stats);
                                        cari.add(historydate);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ListAdapter adapter = new SimpleAdapter(History.this,
                                        cari,
                                        R.layout.list_item,
                                        new String[] {konfigurasi.TAG_TANGGAL, konfigurasi.TAG_TIME, konfigurasi.TAG_STATUS, konfigurasi.TAG_JAM_OUT},
                                        new int[] {R.id.tvtgl, R.id.tvtime, R.id.tvstatus, R.id.tvkeluar}) {
                                    @Override
                                    public View getView(int position, View convertView,
                                                        ViewGroup parent) {
                                        View view = super.getView(position, convertView,
                                                parent);
                                        String getstatus = cari.get(position).get(konfigurasi.TAG_STATUS);
                                        int posisi = position;
                                        int textColorId;
                                        TextView text;
                                        text = (TextView) view.findViewById(R.id.tvstatus);

                                        if (getstatus.equals("Tepat Waktu")) {
                                            textColorId = R.color.green;
                                        } else{
                                            textColorId = R.color.red;
                                        }
                                        text.setTextColor(getResources().getColor(textColorId));

                                        return view;
                                    }
                                };
                                lvhistory.setAdapter(adapter);
                                // tambahkan data ke dalam list
                                //dataList.add(new Data(name, address));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Tindakan jika gagal menghubungi server

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> paramsh = new HashMap<String, String>();
                        paramsh.put("idk", idk);
                        paramsh.put("stats", getstatus);
                        return paramsh;
                    }
                };
                queue.add(request);
                // membuat request pencarian menggunakan Volley
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Menampilkan custom dialog
        dialog.show();
    }
    public void status(View view) {
        statusdialog();
    }
}