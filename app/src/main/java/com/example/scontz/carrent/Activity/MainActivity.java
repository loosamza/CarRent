package com.example.scontz.carrent.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scontz.carrent.Setting.MyAlert;
import com.example.scontz.carrent.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private TextView name_tv, car_tv, rentdate_tv, totalprice_tv, sendate_tv, dateremain_tv;
    private String strName, strCar, strRent, strSend, strUsername, strPassword;
    private double price;
    private int day = 0;
    private int rday = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d("getEx", "== " + getIntent().getStringExtra("date_rent"));
        //name_tv = (TextView) findViewById(R.id.name_tv);
        //strName = getIntent().getStringExtra("date_rent");
        //name_tv.setText(strName);

        //BlindWidget
        initWidget();



        // LoadPreferences(this);


    }


    private void setView() {
        name_tv.setText(strName);
        car_tv.setText(strCar);
        rentdate_tv.setText(strRent);
        sendate_tv.setText(day + " วัน");
        if (rday < 0) {
            dateremain_tv.setText("เลยกำหนดมา " + (-rday) + " วัน");
        } else {
            dateremain_tv.setText(rday + " วัน");
        }
        totalprice_tv.setText(price + " บาท");


    }

    private void initWidget() {
        name_tv = (TextView) findViewById(R.id.name_tv);
        car_tv = (TextView) findViewById(R.id.car_tv);
        rentdate_tv = (TextView) findViewById(R.id.rentdate_tv);
        sendate_tv = (TextView) findViewById(R.id.sendate_tv);
        dateremain_tv = (TextView) findViewById(R.id.remaindate_tv);
        totalprice_tv = (TextView) findViewById(R.id.totalprice_tv);

        strUsername = getIntent().getStringExtra("username");
        strPassword = getIntent().getStringExtra("password");
        strName = getIntent().getStringExtra("name");
        strCar = getIntent().getStringExtra("cname");
        strRent = getIntent().getStringExtra("date_rent");
        strSend = getIntent().getStringExtra("date_senback");
        price = getIntent().getDoubleExtra("total_price", 0.0);
        if (!strSend.equals("-")) {

            day = calDate(strRent, strSend);
            rday = calRDate(strSend);
            setView();
            //Log.d("days", " " + calDate(strRent, strSend));
        } else {
            setView();
        }


    } //initWidget


    private int calRDate(String mstrSend) {
        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        int rdays = 0;
        try {
            Date dS = sdf.parse(mstrSend);
            Date dN = new Date();
            rdays = Days.daysBetween(new DateTime(dN), new DateTime(sdf.format(dS))).getDays();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rdays;

    }

    private int calDate(String mstrRent, String mstrSend) {
        DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        int days = 0;
        try {
            Date dR = sdf.parse(mstrRent);
            Date dS = sdf.parse(mstrSend);

            days = Days.daysBetween(new DateTime(dR), new DateTime(dS)).getDays();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    public void returnCar(View view) {
        if (getIntent().getIntExtra("status", 0) == 0) {
            Toast.makeText(getApplicationContext(), "คุณยังไม่ได้เช่ารถ", Toast.LENGTH_SHORT).show();
        } else {
            myConfirm(MainActivity.this, "คืนรถ", "คุณต้องการคืนรถใช่หรือไม่ ?");

        }
    }

    public void myConfirm(Context context,
                          String strTitle,
                          String strMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.danger);
        builder.setTitle(strTitle);
        builder.setMessage(strMessage);
        builder.setCancelable(false);
        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "ตอบตกลง", Toast.LENGTH_SHORT).show();
                //คืนรถ
                sendCarBack();
                //   Log.d("INTENT", "" + getIntent().getIntExtra("uid", -1));
                //  Log.d("INTENT", "" + getIntent().getIntExtra("cid", -1));
            }
        });
        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.show();
    }

    private void sendCarBack() {
        new AsyncTask<Void, Void, String>() {

            String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/sendcarback.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isCheck", "true")
                    .add("uid", Integer.toString(getIntent().getIntExtra("uid", -1)))
                    .add("cid", getIntent().getStringExtra("cid"))
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
                    myAlert.myDialog(MainActivity.this, "Error", "คืนล้มเหลว");

                } else {
                    myAlert.myDialog(MainActivity.this, "Success", "คืนเรียบร้อย");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);


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

    public void carRent(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowCarActivity.class);

        intent.putExtra("uid", getIntent().getIntExtra("uid", 0));
        //Log.d("UID", "" + getIntent().getIntExtra("uid", 0));
        intent.putExtra("status", getIntent().getIntExtra("status", 0));
        startActivity(intent);
    }




}
