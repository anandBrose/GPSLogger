package com.anand.brose.gpslogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anand on 23-06-2017.
 */

public class UserDatabaseReader  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "user.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserModel.User.TABLE_NAME + " (" +
                    UserModel.User.COLUMN_NAME_USERNAME + " TEXT," +
                    UserModel.User.COLUMN_NAME_PASSWORD + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserModel.User.TABLE_NAME;

    public UserDatabaseReader(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
