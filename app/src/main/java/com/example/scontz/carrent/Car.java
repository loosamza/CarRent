package com.example.scontz.carrent;

import android.provider.BaseColumns;

/**
 * Created by scOnTz on 22/11/2559.
 */
public class Car {
    //Database
    public static final String DATABASE_NAME = "car_database.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE = "atb_carstore";

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

}
