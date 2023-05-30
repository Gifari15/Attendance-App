package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Menu extends AppCompatActivity {
    private String JSON_STRING;
    ArrayList<HashMap<String, String>> list;
    public ArrayList<String> list_id;
    public ArrayList<String> list_getidk;
    public ArrayList<String> cek_masuk;
    Button arr;
    MaterialButton buttonDropdown;
    TextView tampil ,user;
    String getnamex, getuserkx, getoldpass;
    Dialog dialog;
    String nama, idk;
    private static final String TAG = "Menu";
    private AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_menu);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        idk = sharedPref.getString("idk", "");
        getnamex = getIntent().getStringExtra("passuser");
        getoldpass = getIntent().getStringExtra("oldpass");
        getuserkx = getIntent().getStringExtra("passkey");
        String list[]={getnamex, "Edit Profile"};

        user = findViewById(R.id.user);
        user.setText(nama);

        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("SUCCESS")
                .setContentText("Anda Berhasil Login !")
                .show();

        ImageButton dropdownButton = findViewById(R.id.dropdown_button);
        dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Menu.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_1:
                                // Do something
                                Intent intent = new Intent(Menu.this,ProfilActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.option_2:
                                // Do something
                                new SweetAlertDialog(Menu.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                        .setTitleText("Info")
                                        .setContentText("Apakah anda yakin ingin logout?")
                                        .setCancelText("Cancel")
                                        .showCancelButton(true)
                                        .setCustomImage(R.drawable.info_ic)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent intent = new Intent(Menu.this,login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.cancel();
                                            }
                                        })
                                        .show();
//                                        Button myBtn = (Button) findViewById(cn.pedant.SweetAlert.R.id.cancel_button);
//                                       myBtn.setBackgroundColor(ContextCompat.getColor(Menu.this,R.color.red));

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
        JSONabs();
        notification();
        //Inisialisasi AlarmManager
        String varg="kosong";


    }
    
    //AWAL DARI FUNGSI NOTIFIKASI
    private void notification(){
//        if(list_getidk.size()==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "V-Attendance";
                String description = "Ayo Absen Masuk";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel = new NotificationChannel("Notify", name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
//        }
    }

    void pwdialog(){
        //final Dialog dialog = new Dialog(this);
        dialog = new Dialog(this);

        //Mengeset layout
        dialog.setContentView(R.layout.edit_pw);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) dialog.findViewById(R.id.btncancel);
        Button changeButton = (Button) dialog.findViewById(R.id.btnchange);
        EditText etuser = (EditText) dialog.findViewById(R.id.etuser);
        EditText etpwlama = (EditText) dialog.findViewById(R.id.etpwlama);
        EditText etpwbaru = (EditText) dialog.findViewById(R.id.etpwbaru);
        EditText etpwkon = (EditText) dialog.findViewById(R.id.etpwkon);
        etuser.setText(getnamex);
        getuserkx = getIntent().getStringExtra("passkey");
        etpwlama.setText(getoldpass);



//        Spinner status = (Spinner) dialog.findViewById(R.id.spinner_status);
//        String list[]={"Tepat Waktu", "Terlambat"};
//        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item,list);
//        status.setAdapter(AdapterList);


        //AKHIR DARI BUTTON PENAMBAHAN TANGGAL

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etpwbaru.getText().toString().trim();
                String confirmPassword = etpwkon.getText().toString().trim();

                if (TextUtils.isEmpty(newPassword)) {
                    new SweetAlertDialog(Menu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("new password harus diisi!")
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    new SweetAlertDialog(Menu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("confirm password harus diisi!")
                            .show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    new SweetAlertDialog(Menu.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("new password dan confirm password harus sama!")
                            .show();
                    return;
                }else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="https://tracking-v.000webhostapp.com/tracking/changepw.php";


                    //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                Intent intent = new Intent(Menu.this,login.class);
                                intent.putExtra("newpassword", newPassword);
                                 new SweetAlertDialog(Menu.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS")
                                .setContentText("Silahkan login kembali !")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                 @Override
                                 public void onClick(SweetAlertDialog sweetAlertDialog) {
                                     startActivity(intent);
                                     finish();
                                   }
                                })
                                .show();
                                //finish();
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
                            paramsh.put("idk", getuserkx);
                            paramsh.put("pass", newPassword );
                            return paramsh;
                        }
                    };
                    queue.add(request);
                }
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
    private void show() {
        JSONObject jsonObject = null;
         list = new ArrayList<HashMap<String, String>>();
        list_id = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_IDK);
                list_id.add(id);
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
                loading = ProgressDialog.show(Menu.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    private void showabsen() {
        JSONObject jsonObject = null;
        list = new ArrayList<HashMap<String, String>>();
        list_getidk = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_IDK);
                list_getidk.add(id);
                HashMap<String, String> employees = new HashMap<>();
                employees.put(konfigurasi.TAG_IDK, id);
                list.add(employees);
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }
        //notifmasuk();

    }

    private void JSONabs() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Menu.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showabsen();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_ABSEN_DETAIL,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void absen(View view) {
        if(list_getidk.size()!=0){
            new SweetAlertDialog(Menu.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Anda sudah melakukan absen masuk !")
                    .show();
        }
        else{
            getnamex = getIntent().getStringExtra("passuser");
            getuserkx = getIntent().getStringExtra("passkey");
            Intent absen = new Intent(Menu.this, MainActivity2.class);
            absen.putExtra("username", getnamex);
            absen.putExtra("password", getuserkx);
            startActivity(absen);
        }
    }

    public void cuti(View view) {
        getnamex = getIntent().getStringExtra("passuser");
        getuserkx = getIntent().getStringExtra("passkey");
        Intent cuti = new Intent(Menu.this, MainActivity.class);
        cuti.putExtra("usernamect", getnamex);
        cuti.putExtra("idkct", getuserkx);
        startActivity(cuti);
//        getnamex = getIntent().getStringExtra("passkey");
//        getuserx = getIntent().getStringExtra("passuser");
//        tampil.setText(getnamex);
    }

    public void history(View view) {
        //getnamex = getIntent().getStringExtra("passuser");
        getuserkx = getIntent().getStringExtra("passkey");
        Intent hs = new Intent(Menu.this, History.class);
        hs.putExtra("passidk", getuserkx);
        startActivity(hs);
    }

    public void abs_keluar(View view) {
        if(list_getidk.size()==0){
            new SweetAlertDialog(Menu.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Anda belum absen masuk !")
                    .show();
        }
        else {
            getnamex = getIntent().getStringExtra("passuser");
            getuserkx = getIntent().getStringExtra("passkey");
            Intent abkeluar = new Intent(Menu.this, LocationOut.class);
            abkeluar.putExtra("idkaryawan", getuserkx);
            abkeluar.putExtra("username", getnamex);
            startActivity(abkeluar);
        }
    }

    public void grafik(View view) {
        getuserkx = getIntent().getStringExtra("passkey");
        Intent grafik = new Intent(Menu.this, Grafik.class);
        grafik.putExtra("idkaryawan", getuserkx);
        startActivity(grafik);
    }

    public void notifmasuk(){
        if(list_getidk.size()==0) {
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Date date = new Date();
            Calendar cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();

            cal_now.setTime(date);
            cal_alarm.setTime(date);
            // Set waktu alarm ke 07.55 pagi
            cal_alarm.set(Calendar.HOUR_OF_DAY, 12);
            cal_alarm.set(Calendar.MINUTE, 58);
            cal_alarm.set(Calendar.SECOND, 0);
            if (cal_alarm.before(cal_now)) {
                cal_alarm.add(Calendar.DATE, 0);
            }

            // Buat PendingIntent untuk BroadcastReceiver

            Intent intent = new Intent(Menu.this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(Menu.this, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
        }
    }
}