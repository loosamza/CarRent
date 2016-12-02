package com.example.scontz.carrent.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scontz.carrent.Setting.MyAlert;
import com.example.scontz.carrent.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;


public class RentCarActivity extends AppCompatActivity {


    private TextView cname_tv, cdetail_tv, totalprice_tv, fee_tv;
    private EditText dayr, days;
    private ImageView cimg;

    private String strCname, strCdetail, strCid, strCimg, strRDate, strSDate;
    private int uid;
    private double feeperday, feepermonth, totalprice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);


        //BlindWidget
        initWidget();
        //setView
        setView();

        setCurrentDateOnView();
        //addListenerOnButton();


    }

    private void setView() {
        cname_tv.setText(strCname);
        cdetail_tv.setText(strCdetail);
        fee_tv.setText(feeperday + " บาท/วัน \t" + feepermonth + " บาท/เดือน");
        Picasso.with(getApplicationContext()).load(strCimg).into(cimg);

    }

    private void initWidget() {
        cname_tv = (TextView) findViewById(R.id.cname_tv);
        cdetail_tv = (TextView) findViewById(R.id.cdetail_tv);
        cimg = (ImageView) findViewById(R.id.cimg);
        dayr = (EditText) findViewById(R.id.editText5);
        days = (EditText) findViewById(R.id.editText4);
        totalprice_tv = (TextView) findViewById(R.id.totalprice_tv);
        fee_tv = (TextView) findViewById(R.id.fee_tv);

        strCname = getIntent().getStringExtra("cname");
        strCdetail = getIntent().getStringExtra("cdetail");
        strCid = getIntent().getStringExtra("cid");
        strCimg = getIntent().getStringExtra("cimg");
        uid = getIntent().getIntExtra("uid", 0);
        feeperday = getIntent().getDoubleExtra("feeperday", -1);
        feepermonth = getIntent().getDoubleExtra("feepermonth", -1);
    }


    // display current date
    public void setCurrentDateOnView() {


        Calendar c = Calendar.getInstance();

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dayr.setText(sdf.format(c.getTime()));
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
        days.setText(sdf2.format(c.getTime()) + "-");


    }


    public void calTotalprice(View view) throws ParseException {


        // day = getDifferenceDays(d1, d2);
        //Toast.makeText(RentCarActivity.this, "" + d1 + "AND" + d2 + "\nBetween : " + day, Toast.LENGTH_SHORT).show();
        try {
            strRDate = dayr.getText().toString();
            strSDate = days.getText().toString();
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdf.parse(strRDate);
            Date d2 = sdf.parse(strSDate);
            DateFormat df = new SimpleDateFormat("dd");
            DateFormat mf = new SimpleDateFormat("MM");
            DateFormat yf = new SimpleDateFormat("yyyy");

            Calendar myCal = new GregorianCalendar(Integer.parseInt(yf.format(d2)),
                    Integer.parseInt(mf.format(d2)),
                    Integer.parseInt(df.format(d2)));
            long day;
            day = getDifferenceDays(d1, d2);
            int daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            //Log.d("UID", "day > " + strSDate.substring(8, 10));
            if (day <= 0 || Integer.parseInt(strSDate.substring(8, 10)) > daysInMonth) {
                MyAlert myAlert = new MyAlert();
                myAlert.myDialog(RentCarActivity.this, "ผิดพลาด", "คุณระบุรูปแบบวันที่ไม่ถูกต้อง");
                totalprice_tv.setText("!!! ผิดพลาด !!!");
                totalprice = 0;
            } else if (day < 30) {
                totalprice = feeperday * day;
                totalprice_tv.setText("คุณเช่าทั้งหมด : " + day + " วัน\n"
                        + "ราคาเช่าคือ " + totalprice + " บาท");
            } else {
                totalprice = feepermonth + ((day - 30) * feeperday);
                totalprice_tv.setText("คุณเช่าทั้งหมด : " + day + " วัน\n"
                        + "ราคาเช่าคือ " + totalprice + " บาท");
            }
        } catch (Exception e) {

            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(RentCarActivity.this, "ผิดพลาด", e.toString());

        }


//        Toast.makeText(RentCarActivity.this, "" + d1 + "AND" + d2 + "\nBetween : " + getDifferenceDays(d1, d2), Toast.LENGTH_SHORT).show();


    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    public void updateRentCar(View view) {

        if (totalprice == 0 || strSDate == null) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(RentCarActivity.this, "ผิดพลาด", "กรุณาคำนวณราคาก่อน");


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RentCarActivity.this);
            builder.setIcon(R.drawable.danger);
            builder.setTitle("ยืนยันการเช่า");
            builder.setMessage("date_rent : " + strRDate + "\n"
                    + "date_senback : " + strSDate + "\n"
                    + "uid : " + uid + "\n"
                    + "total_price : " + totalprice + "\n"
                    + "cid : " + strCid);
            builder.setCancelable(false);
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rentCarMYSQL();
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }


    }

    private void rentCarMYSQL() {
        new AsyncTask<Void, Void, String>() {
            String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/rentcar.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("isCheck", "true")
                    .add("uid", Integer.toString(uid))
                    .add("cid", strCid)
                    .add("date_rent", strRDate)
                    .add("date_sendback", strSDate)
                    .add("total_price", Double.toString(totalprice))
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
                    myAlert.myDialog(RentCarActivity.this, "Error", "เช่าล้มเหลว");

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RentCarActivity.this);
                    builder.setIcon(R.drawable.danger);
                    builder.setTitle("ผมการเช่า");
                    builder.setMessage("เช่าสำเร็จ");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();



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
