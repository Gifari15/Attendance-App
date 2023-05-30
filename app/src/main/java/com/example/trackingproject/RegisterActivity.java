package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    Button btnreg;
    EditText etidk,etnama,etalamat,etpw,ethp, etusername;
    Spinner sjk;
    RadioButton rbp, rbl;
    String gender;
    private String JSON_STRING;
    public ArrayList<String> listmax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        btnreg = findViewById(R.id.btnreg);
        etidk = findViewById(R.id.etidk);
        etnama = findViewById(R.id.etnama);
        etalamat = findViewById(R.id.etalamat);
        etpw = findViewById(R.id.etpw);
        ethp = findViewById(R.id.ethp);
        rbl = findViewById(R.id.rb_l);
        rbp = findViewById(R.id.rb_p);
        etusername = findViewById(R.id.etusername);
//        sjk = findViewById(R.id.sjk);
//        String list[]={"L","P"};
//        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(RegisterActivity.this,
//                android.R.layout.simple_spinner_dropdown_item,list);
//        sjk.setAdapter(AdapterList);
        getJSON();
    }
    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Mengambil Data", "Mohon Tunggu...", false, false);
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
                String s = rh.sendGetRequest(konfigurasi.URL_GET_COUNT_MAX);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void show() {
        JSONObject jsonObject = null;
        listmax = new ArrayList<String>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result =
                    jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            for (int i = 0; i < result.length(); i++) {
                JSONObject jo = result.getJSONObject(i);
                String max = jo.getString(konfigurasi.TAG_MAX);
                etidk.setText(max);
                HashMap<String, String> counttp = new HashMap<>();
                counttp.put(konfigurasi.TAG_MAX, max);
                //listct.add(cuti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void reg(View view) {
        if(rbl.isChecked()){
            gender = "L";
        } else if (rbp.isChecked()) {
            gender = "P";
        }

        String getsjk = gender;
        String newidk = etidk.getText().toString().trim();
        String newnama = etnama.getText().toString().trim();
        String newalamat = etalamat.getText().toString().trim();
        String newpw = etpw.getText().toString().trim();
        String newhp = ethp.getText().toString().trim();
        String getusername = etusername.getText().toString().trim();


        if (TextUtils.isEmpty(newidk)) {
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("ID Karyawan harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newnama)) {
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("Nama harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newalamat)) {
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("Alamat harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newpw)) {
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("Password harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newhp)) {
            new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("No HP harus diisi!")
                    .show();
            return;
        }
        else {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="https://tracking-v.000webhostapp.com/tracking/register.php";


            //MENGIRIM DATA MENGGUNAKAN METHOD POST
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                         //Response ketika berhasil menghubungi serverd
                        Intent intent = new Intent(RegisterActivity.this,login.class);
                        new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("SUCCESS")
                                .setContentText("Silahkan Login")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .show();
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
                    paramsh.put("idk", newidk);
                    paramsh.put("nama", newnama );
                    paramsh.put("alamat", newalamat );
                    paramsh.put("username", getusername );
                    paramsh.put("jk", getsjk );
                    paramsh.put("hp", newhp );
                    paramsh.put("password", newpw );
                    return paramsh;
                }
            };
            queue.add(request);
        }
    }
}