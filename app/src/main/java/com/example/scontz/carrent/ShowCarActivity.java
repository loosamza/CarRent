package com.example.scontz.carrent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

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

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),carData);
        ListView listView = (ListView) findViewById(R.id.listView);
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
