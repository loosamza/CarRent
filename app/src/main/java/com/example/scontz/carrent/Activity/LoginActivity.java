package com.example.scontz.carrent.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.scontz.carrent.Setting.MyAlert;
import com.example.scontz.carrent.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {
    EditText username_edt, password_edt;
    String strUsername, strPassword;

    final String P_NAME = "User_profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //BlindWidget
        initWidget();

    }


    private void initWidget() {


        username_edt = (EditText) findViewById(R.id.username_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);

    }//initWidget

    public void logInClick(View view) throws IOException {
        strUsername = username_edt.getText().toString().trim();
        strPassword = password_edt.getText().toString().trim();


        //Check empty
        if (strUsername.length() == 0 || strPassword.length() == 0) {
            //Have empty
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "มีช่องว่าง", "กรุณากรอกให้ครับทุกช่องด้วยครับ");
        } else {
            //Not empty
            checkUser();

        }


    }//lonInClick

    private void checkUser() {

        new AsyncTask<Void, Void, String>() {

            String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/checkuser.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isCheck", "true")
                    .add("username", strUsername)
                    .add("password", strPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(strURL)
                    .post(requestBody)
                    .build();

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                MyAlert myAlert = new MyAlert();
                if (s.equals("false")) {
                    myAlert.myDialog(LoginActivity.this, "Error", "ชื่อผู้ใช้หรือรหัสผ่านผิด");

                } else {
                    getSetData();


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

    private void getSetData() {

        new AsyncTask<Void, Void, String>() {

            String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/getuserprofile.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isCheck", "true")
                    .add("username", strUsername)
                    .add("password", strPassword)
                    .build();
            Request request = new Request.Builder()
                    .url(strURL)
                    .post(requestBody)
                    .build();

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  Log.d("JSON", "strJSON ===> " + s);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Log.d("getData", "Commit S ==>" + jsonObject.getString("status"));
                    SavePreferences(jsonObject, jsonObject.getInt("status"));

                    //Log.d("getData", "Commit S ==>" + jsonObject.getString("date_rent"));
/*
                    Map<String, ?> allEntries = sp.getAll();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        Log.d("getData", "onPost Set ==>" + entry.getValue().toString());
                    }
*/

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

    public void SavePreferences(JSONObject jsonObject, int c) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        try {
            if (c == 1) {
                intent.putExtra("username", strUsername);
                intent.putExtra("password", strPassword);
                intent.putExtra("date_rent", jsonObject.getString("date_rent"));
                intent.putExtra("date_senback", jsonObject.getString("date_senback"));
                intent.putExtra("name", jsonObject.getString("name"));
                intent.putExtra("status", jsonObject.getInt("status"));
                intent.putExtra("cstatus", jsonObject.getInt("cstatus"));
                intent.putExtra("cname", jsonObject.getString("cname"));
                intent.putExtra("cimg", jsonObject.getString("cimg"));
                intent.putExtra("total_price", jsonObject.getDouble("total_price"));
                intent.putExtra("uid", jsonObject.getInt("uid"));
                intent.putExtra("cid", jsonObject.getString("cid"));
                startActivity(intent);
            } else if (c == 0) {
                intent.putExtra("username", strUsername);
                intent.putExtra("password", strPassword);
                intent.putExtra("date_rent", "-");
                intent.putExtra("date_senback", "-");
                intent.putExtra("name", jsonObject.getString("name"));
                intent.putExtra("status", jsonObject.getInt("status"));
                intent.putExtra("cstatus", 0);
                intent.putExtra("cname", "-");
                intent.putExtra("cimg", "-");
                intent.putExtra("total_price", 0.0);
                intent.putExtra("uid", jsonObject.getInt("uid"));
                intent.putExtra("cid", "-");
                startActivity(intent);
            }

        /*Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("getData", "onPost Set ==>" + entry.getValue().toString());
        }*/
        } catch (Exception e) {
            Log.d("getData", "onPost E ==> " + e.toString());
        }
    }


    public void ClearPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(P_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void signupGo(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }


}

