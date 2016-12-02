package com.example.scontz.carrent.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scontz.carrent.R;
import com.example.scontz.carrent.Setting.MyAlert;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText username_edt, password_edt, name_edt;
    String strUsername, strPassword, strName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //BlindWidget
        initWidget();

    }

    private void initWidget() {
        username_edt = (EditText) findViewById(R.id.username_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        name_edt = (EditText) findViewById(R.id.name_edt);

    }

    public void checkSingUp(View view) {
        strUsername = username_edt.getText().toString().trim();
        strPassword = password_edt.getText().toString().trim();
        strName = name_edt.getText().toString().trim();
        if (strUsername.length() == 0 || strPassword.length() == 0 || strName.length() == 0) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(RegisterActivity.this, "คำเตือน", "กรุณากรอกข้อมูลให้ครบทุกช่อง");
        } else {
            insertUser();
        }

    }

    private void insertUser() {

        new AsyncTask<Void, Void, String>() {

            String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/insertuser.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isCheck", "true")
                    .add("username", strUsername)
                    .add("password", strPassword)
                    .add("name", strName)
                    .build();
            Request request = new Request.Builder()
                    .url(strURL)
                    .post(requestBody)
                    .build();

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    MyAlert myAlert = new MyAlert();
                    if (s.equals("false")) {
                        myAlert.myDialog(RegisterActivity.this, "สมัครสมาชิกผิดพลาด", "มี Username นี้แล้ว");
                    } else {
                        Toast.makeText(RegisterActivity.this, "ลงทะเบียนเรียบร้อย", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.d("getData", "onPost E ==> " + e.toString());
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }
        }.execute();


    }

}
