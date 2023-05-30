package com.example.trackingproject;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    CircleButton btndate,btnadd, btnfloat;
    String varngidect;
    public int batascuti=0;
    String getidkc , getnama, getidc, sisacuti ;
    private String JSON_STRING;
    private ListView lvcuti;
    ArrayList<HashMap<String, String>> listct;
    ArrayList<HashMap<String, String>> listk;
    public ArrayList<String> list_sisa ,listbatas;
    Dialog dialog;
    DatePickerDialog datePickerDialog;
    TextView tvsisa, tvtanggal;
    int hari, bulan, tahun;
    ArrayList<HashMap<String, String>> cari;
    String nama,idk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        nama = sharedPref.getString("nama", "");
        idk = sharedPref.getString("idk", "");
        btndate = findViewById(R.id.circledate);
        tvtanggal=findViewById(R.id.tvtanggal);
        btnadd = findViewById(R.id.circleadd);
        btnfloat = findViewById(R.id.circlebtn2);
        lvcuti = findViewById(R.id.lvct);
        tvsisa = findViewById(R.id.tvsisa);
        varngidect = "true";
        getidkc = getIntent().getStringExtra("idkct");
        getidc = getIntent().getStringExtra("idc");
        getJSON();
        getJSONbatascuti();
        lvcuti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                HashMap<String, String> map = (HashMap)
                        parent.getItemAtPosition(position);
                String getidc = map.get(konfigurasi.TAG_IDC).toString();
                String getmulai = map.get(konfigurasi.TAG_MULAI).toString();
                String getakhir = map.get(konfigurasi.TAG_AKHIR).toString();
                String getmasuk = map.get(konfigurasi.TAG_MK).toString();
                String getjmlcuti = map.get(konfigurasi.TAG_JML).toString();
                String getket = map.get(konfigurasi.TAG_KET).toString();
                String getstatus = map.get(konfigurasi.TAG_STATUSC).toString();
                String getcat = map.get(konfigurasi.TAG_KAT).toString();
                String getalasan = map.get(konfigurasi.TAG_ALASAN).toString();
                parent.getItemAtPosition(position);
                //tvtanggal.setText(getidc+getmulai+getakhir+getmasuk+"\n"+getjmlcuti+getket+getstatus);



                dialog = new Dialog(MainActivity.this);
                //Mengeset layout
                dialog.setContentView(R.layout.detail_cuti);

                //Membuat agar dialog tidak hilang saat di click di area luar dialog
                dialog.setCanceledOnTouchOutside(false);

                Button closeButton = (Button) dialog.findViewById(R.id.btnclose);
                Button deleteButton = (Button) dialog.findViewById(R.id.btndelete);
                EditText detailmulai = (EditText) dialog.findViewById(R.id.detailmulai);
                EditText etakhir = (EditText) dialog.findViewById(R.id.etakhir);
                EditText etmasuk = (EditText) dialog.findViewById(R.id.etmasuk);
                EditText etjmlcuti = (EditText) dialog.findViewById(R.id.etjmlcuti);
                EditText etket = (EditText) dialog.findViewById(R.id.etket);
                EditText etstatus = (EditText) dialog.findViewById(R.id.etstatus);
                EditText etkat = (EditText) dialog.findViewById(R.id.etkat);
                EditText etalasan = (EditText) dialog.findViewById(R.id.etalasan);

                detailmulai.setText(getmulai);
                etakhir.setText(getakhir);
                etmasuk.setText(getmasuk);
                etjmlcuti.setText(getjmlcuti);
                etket.setText(getket);
                etstatus.setText(getstatus);
                etkat.setText(getcat);
                etalasan.setText(getalasan);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText("Info")
                                .setContentText("Anda yakin ingin menghapus data ini?")
                                .setCancelText("Cancel")
                                .showCancelButton(true)
                                .setCustomImage(R.drawable.info)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        String url ="https://tracking-v.000webhostapp.com/tracking/hapuscuti.php";

                                        //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
                                        StringRequest request = new StringRequest(Request.Method.POST, url,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {


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
                                                paramsh.put("idc", getidc);
                                                return paramsh;
                                            }
                                        };
                                        queue.add(request);
                                        Toast.makeText(MainActivity.this, "Record berhasil dihapus",Toast.LENGTH_LONG).show();
                                        sweetAlertDialog.cancel();
                                        getJSON();
                                        dialog.dismiss();
                                    }

                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                        //dialog.dismiss();


                        //dialog.dismiss();
                    }

                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //Menampilkan custom dialog
                dialog.show();

            }
        });
//        lvcuti.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//                alertDialogBuilder.setTitle("Anda Yakin Ingin Hapus Data ?");
//                //membuat pesan keluar aplikasi
//                alertDialogBuilder
//                        .setMessage("\n\t\t\tKlik Ya untuk hapus data")
//                        .setIcon(R.mipmap.icon)
//                        .setCancelable(false)
//                        .setPositiveButton("Ya",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        HashMap<String, String> map = (HashMap)
//                                                parent.getItemAtPosition(position);
//                                        String delcuti = map.get(konfigurasi.TAG_MK).toString();
//                                        parent.getItemAtPosition(position);
//
//                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//                                        String url ="https://tracking-v.000webhostapp.com/tracking/hapuscuti.php";
//
//
//                                        //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
//                                        StringRequest request = new StringRequest(Request.Method.POST, url,
//                                                new Response.Listener<String>() {
//                                                    @Override
//                                                    public void onResponse(String response) {
//                                                        getJSON();
//                                                    }
//                                                },
//                                                new Response.ErrorListener() {
//                                                    @Override
//                                                    public void onErrorResponse(VolleyError error) {
//                                                        // Tindakan jika gagal menghubungi server
//
//                                                    }
//                                                }
//                                        ) {
//                                            @Override
//                                            protected Map<String, String> getParams() {
//                                                Map<String, String> paramsh = new HashMap<String, String>();
//                                                paramsh.put("idc", delcuti);
//                                                paramsh.put("idk", getidkc);
//                                                return paramsh;
//                                            }
//                                        };
//                                        queue.add(request);
//                                        Toast.makeText(MainActivity.this, "Record berhasil dihapus",Toast.LENGTH_LONG).show();
//                                        //System.exit(0);
//                                        JSONKaryawan();
//                                    }
//                                })
//
//                        .setNegativeButton("Tidak",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//
//                //membuat alert dari dialog
//                AlertDialog alertDialog = alertDialogBuilder.create();
//
//                //menampilkan alert dialog
//                alertDialog.show();
//                return false;
//            }
//        });
        JSONKaryawan();

        }


    private void showsisa() {
        JSONObject jsonObject = null;
        listk = new ArrayList<HashMap<String, String>>();
        list_sisa = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                 sisacuti = jo.getString(konfigurasi.TAG_SISA);
                list_sisa.add(sisacuti);
                tvsisa.setText(list_sisa.get(0).toString()); //SET SISA CUTI KARYAWAN
                HashMap<String, String> karyawan = new HashMap<>();
                karyawan.put(konfigurasi.TAG_IDK, sisacuti);
                listk.add(karyawan);
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }

    private void JSONKaryawan() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showsisa();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_KARYAWAN_DETAIL,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_EMP,idk);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void show() {
        JSONObject jsonObject = null;
        listct = new
                ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String idc = jo.getString(konfigurasi.TAG_IDC);
                String nama = jo.getString(konfigurasi.TAG_NAMAC);
                String jml = jo.getString(konfigurasi.TAG_JML);
                String mulai = jo.getString(konfigurasi.TAG_MULAI);
                String akhir = jo.getString(konfigurasi.TAG_AKHIR);
                String ket = jo.getString(konfigurasi.TAG_KET);
                String statusc = jo.getString(konfigurasi.TAG_STATUSC);
                String masuk = jo.getString(konfigurasi.TAG_MK);
                String kategori = jo.getString(konfigurasi.TAG_KAT);
                String alasan = jo.getString(konfigurasi.TAG_ALASAN);
                //status = jo.getString(konfigurasi.TAG_STATUS);
                //liststatus.add(status);
                HashMap<String, String> cuti = new HashMap<>();
                cuti.put(konfigurasi.TAG_IDC, idc);
                cuti.put(konfigurasi.TAG_NAMAC, nama);
                cuti.put(konfigurasi.TAG_JML, jml);
                cuti.put(konfigurasi.TAG_MULAI, mulai);
                cuti.put(konfigurasi.TAG_AKHIR, akhir);
                cuti.put(konfigurasi.TAG_KET, ket);
                cuti.put(konfigurasi.TAG_STATUSC, statusc);
                cuti.put(konfigurasi.TAG_MK, masuk);
                cuti.put(konfigurasi.TAG_KAT, kategori);
                cuti.put(konfigurasi.TAG_ALASAN, alasan);
                listct.add(cuti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                listct,
                R.layout.list_itemc,
                new String[] {konfigurasi.TAG_KET, konfigurasi.TAG_MK, konfigurasi.TAG_STATUSC},
                new int[] {R.id.tvket, R.id.tvmasuk, R.id.tvstatusct}) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {
                View view = super.getView(position, convertView,
                        parent);
                //float ratarata = Float.parseFloat(rata_rata);
                String getstatusct = listct.get(position).get(konfigurasi.TAG_STATUSC);
                int posisi = position;
                int textColorId = R.color.green;
                TextView textct;
                textct = (TextView) view
                        .findViewById(R.id.tvstatusct);

                if (getstatusct.equals("Diterima")) {
                    textColorId = R.color.green;
                } else if(getstatusct.equals("Ditolak")){
                    textColorId = R.color.red;
                }
                else{
                    textColorId = R.color.terkirim;
                }
                textct.setTextColor(getResources().getColor(textColorId));
                return view;
            }
        };
        lvcuti.setAdapter(adapter);

    }

    public void floatingbtn(View view) {

    }


    public void floatingct(View view) {
        if(varngidect.equals("true")){
            btnadd.setVisibility(View.VISIBLE);
            btndate.setVisibility(View.VISIBLE);
            varngidect = "false";
        }
        else if(varngidect.equals("false")){
            btnadd.setVisibility(View.INVISIBLE);
            btndate.setVisibility(View.INVISIBLE);
            varngidect = "true";
        }
        else{

        }
    }

    void adddialog(){
        //final Dialog dialog = new Dialog(this);
        dialog = new Dialog(this);

        //Mengeset layout
        dialog.setContentView(R.layout.add_cuti);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) dialog.findViewById(R.id.btncancel);
        Button saveButton = (Button) dialog.findViewById(R.id.btnsave);
        TextView titlee = (TextView) dialog.findViewById(R.id.titlesearch);
        EditText etuser = (EditText) dialog.findViewById(R.id.etuser);
        EditText etmulai = (EditText) dialog.findViewById(R.id.etmulai);
        EditText etakhir = (EditText) dialog.findViewById(R.id.etakhir);
        EditText etket = (EditText) dialog.findViewById(R.id.etket);
        Spinner kat = (Spinner) dialog.findViewById(R.id.spinner_kat);
        String list[]={"cuti","izin","sakit","cutikhusus"};
        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,list);
        kat.setAdapter(AdapterList);
        ImageButton btnmulai = (ImageButton) dialog.findViewById(R.id.btnmulai);
        ImageButton btnakhir = (ImageButton) dialog.findViewById(R.id.btnakhir);
        getnama = getIntent().getStringExtra("usernamect");
        etuser.setText(nama);
        titlee.setText("Add Cuti");


//        Spinner status = (Spinner) dialog.findViewById(R.id.spinner_status);
//        String list[]={"Tepat Waktu", "Terlambat"};
//        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item,list);
//        status.setAdapter(AdapterList);
        etmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate_mulai();
            }
        });
        btnmulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate_mulai();
            }
        });

        btnakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate_akhir();
            }
        });
        etakhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate_akhir();
            }
        });

        //AKHIR DARI BUTTON PENAMBAHAN TANGGAL

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newmulai = etmulai.getText().toString().trim();
                String newakhir = etakhir.getText().toString().trim();
                String newket = etket.getText().toString().trim();
                String reversemulai="",reverseakhir="";
                String getnilai="", getsisa_cuti="";
                String getkat = kat.getSelectedItem().toString();
                int jmlct;
                getsisa_cuti = tvsisa.getText().toString();
                int siscut = Integer.valueOf(getsisa_cuti);
                Date currentDate = new Date();

                String mulai = etmulai.getText().toString();
                String akhir = etakhir.getText().toString();
//                int panjang = mulai.length();
//                for (int i = panjang-1 ; i >= 0 ; i--) {
//                    reversemulai = reversemulai+mulai.charAt(i);
//                }
//                int panjang2 = akhir.length();
//                for (int i = panjang2-1 ; i >= 0 ; i--) {
//                    reverseakhir = reverseakhir+mulai.charAt(i);
//                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                Date datemulai = null;
                Date dateakhir = null;
                long diffDays = 0;
                try {
                    datemulai = format.parse(mulai);
                    dateakhir = format.parse(akhir);
                    long diff = dateakhir.getTime() - datemulai.getTime();
                    System.out.println(datemulai);
                    System.out.println(diff);
                    diffDays = diff / (24 * 60 * 60 * 1000);
//                System.out.println("Hari :"+diffDays);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                System.out.println("Hari :"+diffDays);
                getnilai = String.valueOf(diffDays);
                jmlct = Integer.valueOf(getnilai);


                if (TextUtils.isEmpty(newmulai)) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("mulai cuti harus diisi!")
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(newakhir)) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("akhir cuti harus diisi!")
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(newket)) {
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("WARNING!!")
                            .setContentText("keterangan cuti harus diisi!")
                            .show();
                    return;
                }
                if(jmlct>siscut){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("WARNING!")
                            .setContentText("Jumlah cuti melebihi sisa cuti!")
                            .show();
                    return;
                }
                if(batascuti>5){
                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("WARNING!")
                            .setContentText("Batas Cuti Minggu Ini Sudah Maksimal!")
                            .show();
                    return;
                }
                else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url ="https://tracking-v.000webhostapp.com/tracking/tambahcuti.php";


                    //MENGIRIM DATA MENGGUNAKAN METHOD POST MENGGUNAKAN GETPARAMS
                    StringRequest request = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    getJSON();
                                    JSONKaryawan();
//                                    getJSONbatascuti();
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
                            paramsh.put("nama", nama );
                            paramsh.put("mulai", newmulai );
                            paramsh.put("akhir", newakhir );
                            paramsh.put("ket", newket );
                            paramsh.put("kat", getkat );
                            return paramsh;
                        }
                    };
                    queue.add(request);
                }
                getJSONbatascuti();
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

    void statusdialog(){
        //final Dialog dialog = new Dialog(this);
        dialog = new Dialog(MainActivity.this);
        //Mengeset judul dialog
        //dialog.setTitle("Edit Task");

        //Mengeset layout
        dialog.setContentView(R.layout.search_status);

        //Membuat agar dialog tidak hilang saat di click di area luar dialog
        dialog.setCanceledOnTouchOutside(false);

        Button cancelButton = (Button) dialog.findViewById(R.id.button_batal);
        Button searchButton = (Button) dialog.findViewById(R.id.button_cari);
        Spinner status = (Spinner) dialog.findViewById(R.id.spinner_status);
        String list[]={"Terkirim","Ditolak","Diterima"};
        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,list);
        status.setAdapter(AdapterList);


        //AKHIR DARI BUTTON PENAMBAHAN TANGGAL

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getstatusct = status.getSelectedItem().toString();
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://tracking-v.000webhostapp.com/tracking/filterstatusct.php";


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
                                        String idc = data.getString(konfigurasi.TAG_IDC);
                                        String nama = data.getString(konfigurasi.TAG_NAMAC);
                                        String jml = data.getString(konfigurasi.TAG_JML);
                                        String mulai = data.getString(konfigurasi.TAG_MULAI);
                                        String akhir = data.getString(konfigurasi.TAG_AKHIR);
                                        String ket = data.getString(konfigurasi.TAG_KET);
                                        String statusc = data.getString(konfigurasi.TAG_STATUSC);
                                        String masuk = data.getString(konfigurasi.TAG_MK);
                                        HashMap<String, String> cuti = new HashMap<>();
                                        cuti.put(konfigurasi.TAG_IDC, idc);
                                        cuti.put(konfigurasi.TAG_NAMAC, nama);
                                        cuti.put(konfigurasi.TAG_JML, jml);
                                        cuti.put(konfigurasi.TAG_MULAI, mulai);
                                        cuti.put(konfigurasi.TAG_AKHIR, akhir);
                                        cuti.put(konfigurasi.TAG_KET, ket);
                                        cuti.put(konfigurasi.TAG_STATUSC, statusc);
                                        cuti.put(konfigurasi.TAG_MK, masuk);
                                        cari.add(cuti);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                                        cari,
                                        R.layout.list_itemc,
                                        new String[] {konfigurasi.TAG_KET, konfigurasi.TAG_MK, konfigurasi.TAG_STATUSC},
                                        new int[] {R.id.tvket, R.id.tvmasuk, R.id.tvstatusct}) {
                                    @Override
                                    public View getView(int position, View convertView,
                                                        ViewGroup parent) {
                                        View view = super.getView(position, convertView,
                                                parent);
                                        //float ratarata = Float.parseFloat(rata_rata);
                                        String getstatusct = cari.get(position).get(konfigurasi.TAG_STATUSC);
                                        int posisi = position;
                                        int textColorId = R.color.green;
                                        TextView textct;
                                        textct = (TextView) view
                                                .findViewById(R.id.tvstatusct);

                                        if (getstatusct.equals("Diterima")) {
                                            textColorId = R.color.green;
                                        } else if(getstatusct.equals("Ditolak")){
                                            textColorId = R.color.red;
                                        }
                                        else{
                                            textColorId = R.color.terkirim;
                                        }
                                        textct.setTextColor(getResources().getColor(textColorId));
                                        return view;
                                    }
                                };
                                lvcuti.setAdapter(adapter);
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
                        paramsh.put("stats", getstatusct);
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
    public void add(View view) {
    adddialog();
    }

    public void cari(View view) { statusdialog();
    }

    public void seeall(View view) {
        getJSON();
    }

    //FUNGSI MENGAMBIL DATE AWAL CUTI
    void getdate_mulai(){
        Calendar newCalendar = Calendar.getInstance();
        //datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        EditText mulai = (EditText) dialog.findViewById(R.id.etmulai);
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
                getdate = year + "/" + (getbulan) + "/" + gethari;

                mulai.setText(getdate);

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    //FUNGSI MENGAMBIL DATE AKHIR CUTI
    void getdate_akhir(){
        Calendar newCalendar = Calendar.getInstance();
        //datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        EditText akhir = (EditText) dialog.findViewById(R.id.etakhir);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */
                String getdate;

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
                getdate = year + "/" + (getbulan) + "/" + gethari;

                akhir.setText(getdate);

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showbatascuti() {
        JSONObject jsonObject = null;
        listbatas = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String batas = jo.getString(konfigurasi.TAG_BATASCUTI);
//                String totalizin = jo.getString(konfigurasi.TAG_TTLIZIN);
//                String totalsakit = jo.getString(konfigurasi.TAG_TTLSAKIT);
                listbatas.add(batas);
//                izinbray.add(totalizin);
//                cekkx.add(totalsakit);
                batascuti= Integer.valueOf(listbatas.get(0));
//                izin.setText(izinbray.get(0));
            }
        } catch (JSONException e) {


            e.printStackTrace();
        }

    }

    private void getJSONbatascuti() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showbatascuti();
            }
            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_BATASCUTI);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
}