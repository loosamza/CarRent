package com.example.scontz.carrent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scOnTz on 22/11/2559.
 */
public class Car {
    //Database
    public static final String DATABASE_NAME = "car_database.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "atb_carstore";

    //Explicit
    private DBHelper objDbHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public Car(Context context) {
        objDbHelper = new DBHelper(context);
        writeSqLiteDatabase = objDbHelper.getWritableDatabase();
        readSqLiteDatabase = objDbHelper.getReadableDatabase();
    }

    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String CID = "cid";
        public static final String CNAME = "cname";
        public static final String CAMOUNT = "camount";
        public static final String CDETAIL = "cdetail";
        public static final String FEEPERDAY = "feeperday";
        public static final String FEEPERMONTH = "feepermonth";
        public static final String CIMG = "cimg";

    }

    public long addCar(String strCID,
                       String strCNAME,
                       int intCAMOUNT,
                       String strDEATAIL,
                       double doubFEEPERDAY,
                       double doubFEEPERMONTH,
                       String strCIMG) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.CID, strCID);
        contentValues.put(Column.CNAME, strCNAME);
        contentValues.put(Column.CAMOUNT, intCAMOUNT);
        contentValues.put(Column.CDETAIL, strDEATAIL);
        contentValues.put(Column.FEEPERDAY, doubFEEPERDAY);
        contentValues.put(Column.FEEPERMONTH, doubFEEPERMONTH);
        contentValues.put(Column.CIMG, strCIMG);
        return writeSqLiteDatabase.insert(TABLE, null, contentValues);

    }

    public List<ListAllCar> allCars() {
        Cursor cursor = readSqLiteDatabase.rawQuery("SELECT * FROM " + TABLE , null);
        //Log.d("DB", " " + cursor.getCount());
        List<ListAllCar> listAlls = new ArrayList<ListAllCar>();
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ListAllCar listAllCar = new ListAllCar();
                        listAllCar.setStrCID(cursor.getString(1));
                        listAllCar.setStrCNAME(cursor.getString(2));
                        listAllCar.setIntCAMOUNT(cursor.getInt(3));
                        listAllCar.setStrDEATAIL(cursor.getString(4));
                        listAllCar.setDoubFEEPERDAY(cursor.getDouble(5));
                        listAllCar.setDoubFEEPERMONTH(cursor.getDouble(6));
                        listAllCar.setStrCIMG(cursor.getString(7));
                       // Log.d("DB", ">>>" + cursor.getString(1));
                        listAlls.add(listAllCar);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

            return listAlls;
        } catch (Exception e) {
            Log.d("DB", "allData Error" + e);
            return null;

        }
    }


}
