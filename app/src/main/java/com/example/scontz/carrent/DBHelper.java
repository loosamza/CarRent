package com.example.scontz.carrent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by scOnTz on 16/11/2559.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = getClass().getSimpleName();


    public DBHelper(Context context) {
        super(context, Car.DATABASE_NAME, null, Car.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CAR_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT,%s TEXT,%s INTEGER,%s TEXT,%s DOUBLE,%s DOUBLE,%s TEXT)",
                Car.TABLE,
                Car.Column.ID,
                Car.Column.CID,
                Car.Column.CNAME,
                Car.Column.CAMOUNT,
                Car.Column.CDETAIL,
                Car.Column.FEEPERDAY,
                Car.Column.FEEPERMONTH,
                Car.Column.CIMG
        );


        //Create Table Car
        db.execSQL(CREATE_CAR_TABLE);
        Log.d(TAG, "SUCCESS");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_CAR_TABLE = "DROP TABLE IF EXISTS " + Car.TABLE;

        db.execSQL(DROP_CAR_TABLE);

        Log.d(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);

        onCreate(db);


    }
}
