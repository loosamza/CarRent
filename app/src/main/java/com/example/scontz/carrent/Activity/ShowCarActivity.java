package com.example.scontz.carrent.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scontz.carrent.Database.Car;
import com.example.scontz.carrent.Setting.CustomAdapter;
import com.example.scontz.carrent.Database.DBHelper;
import com.example.scontz.carrent.Database.ListAllCar;
import com.example.scontz.carrent.Setting.MyAlert;
import com.example.scontz.carrent.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCarActivity extends AppCompatActivity {

    private Car mCar;
    private DBHelper mDbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private List<ListAllCar> carData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_car);
        mDbHelper = new DBHelper(this);
        mCar = new Car(this);

        //Connected SQLITE
        connectedSQLite();

        //SynAndDelete
        synAndDeleteData();

        //InitWidget


    }

    private void initWidget() {
        carData = new ArrayList<ListAllCar>();
        carData = mCar.allCars();
//        Log.d("DB", ">>>" + carData.toString());
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), carData);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (getIntent().getIntExtra("status", 0) == 1) {
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(ShowCarActivity.this, "คำเตือน", "คุณได้เช่ารถไปแล้วไม่สามารถเช่าเพิ่มได้!!");
                } else if (carData.get(position).getIntCAMOUNT() == 0) {
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(ShowCarActivity.this, "คำเตือน", "ไม่มีรถเหลือในสต๊อกแล้ว!!");
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowCarActivity.this);
                    builder.setIcon(R.drawable.danger);
                    builder.setTitle("ยืนยัน");
                    builder.setMessage("คุณต้องการเช่ารถ " + carData.get(position).getStrCNAME() + " ใช่หรือไม่ ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), RentCarActivity.class);
                            intent.putExtra("cname", carData.get(position).getStrCNAME());
                            intent.putExtra("cdetail", carData.get(position).getStrDEATAIL());
                            intent.putExtra("cimg", carData.get(position).getStrCIMG());
                            intent.putExtra("uid", getIntent().getIntExtra("uid", 0));
                            intent.putExtra("cid", carData.get(position).getStrCID());
                            intent.putExtra("feeperday", carData.get(position).getDoubFEEPERDAY());
                            intent.putExtra("feepermonth", carData.get(position).getDoubFEEPERMONTH());
                            startActivity(intent);
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
            }
        });
        listView.setAdapter(adapter);
    }


    private void synAndDeleteData() {
        SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(mCar.DATABASE_NAME,
                MODE_PRIVATE, null);
        sqLiteDatabase.delete(mCar.TABLE, null, null);
        MySynJSON mySynJSON = new MySynJSON();
        mySynJSON.execute();

    }

    private void connectedSQLite() {

    }


    //Create Inner Class for Connected JSON

    private class MySynJSON extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String strURL = "http://csclub.ssru.ac.th/s56122201021/project_car/getcar.php";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();


            } catch (Exception e) {
                Log.d("JSON", "doInBack === >" + e.toString());
                return null;
            }

        } //doInBackground

        //KUY

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Log.d("JSON", "strJSON ===> " + s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String strCID = jsonObject.getString(Car.Column.CID);
                    String strCNAME = jsonObject.getString(Car.Column.CNAME);
                    int intCAMOUNT = jsonObject.getInt(Car.Column.CAMOUNT);
                    String strDEATAIL = jsonObject.getString(Car.Column.CDETAIL);
                    double doubFEEPERDAY = jsonObject.getDouble(Car.Column.FEEPERDAY);
                    double doubFEEPERMONTH = jsonObject.getDouble(Car.Column.FEEPERMONTH);
                    String strCIMG = jsonObject.getString(Car.Column.CIMG);
                    mCar.addCar(strCID, strCNAME, intCAMOUNT, strDEATAIL, doubFEEPERDAY, doubFEEPERMONTH, strCIMG);
                }//for
                initWidget();
                Toast.makeText(getApplicationContext(), "Synchronize mySQL to SQLIte Finish", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("JSON", "onPost == > " + e.toString());

            }

        }// onPostExecute

    }// MySynJson


}
