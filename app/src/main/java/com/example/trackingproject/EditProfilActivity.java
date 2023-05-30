package com.example.trackingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditProfilActivity extends AppCompatActivity {

    EditText etuser, etalamat, etpass, ethp, etconfirm;
    String idk , username,password,alamat,hp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "");
        idk = sharedPref.getString("idk", "");
        password = sharedPref.getString("password", "");
        alamat = sharedPref.getString("alamat", "");
        hp = sharedPref.getString("hp", "");

        etuser = findViewById(R.id.etuser);
        etalamat = findViewById(R.id.etalamat);
        etpass = findViewById(R.id.etpass);
        etconfirm = findViewById(R.id.etconfirm);
        ethp = findViewById(R.id.ethp);

        etuser.setText(username);
        etpass.setText(password);
        etalamat.setText(alamat);
        ethp.setText(hp);
    }

    public void save(View view) {
        String newhp = ethp.getText().toString().trim();
        String newusername = etuser.getText().toString().trim();
        String newalamat = etalamat.getText().toString().trim();
        String newPassword = etpass.getText().toString().trim();
        String confirmPassword = etconfirm.getText().toString().trim();

        if (TextUtils.isEmpty(newusername)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("username harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newalamat)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("alamat harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(newhp)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("no hp harus diisi minimal 12 digit!")
                    .show();
            return;
        }



        if (TextUtils.isEmpty(newPassword)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("new password harus diisi!")
                    .show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("WARNING!!")
                    .setContentText("confirm password harus diisi!")
                    .show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                            Intent intent = new Intent(EditProfilActivity.this,login.class);
                            intent.putExtra("newpassword", newPassword);
                            new SweetAlertDialog(EditProfilActivity.this, SweetAlertDialog.SUCCESS_TYPE)
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
                    paramsh.put("idk", idk);
                    paramsh.put("pass", newPassword );
                    paramsh.put("user", newusername);
                    paramsh.put("alamat", newalamat );
                    paramsh.put("hp", newhp );
                    return paramsh;
                }
            };
            queue.add(request);
        }
    }
}