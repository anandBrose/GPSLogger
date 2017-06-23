package com.anand.brose.gpslogger;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anand on 23-06-2017.
 */

public class UserModel {

    private final UserDatabaseReader dbReader;

    public  UserModel(UserDatabaseReader dbReader){
        this.dbReader = dbReader;
        setUpUserData();

    }

    private void setUpUserData() {
        SQLiteDatabase db = this.dbReader.getReadableDatabase();
        String[] projection = {
                User.COLUMN_NAME_USERNAME,
                User.COLUMN_NAME_PASSWORD
        };


        Cursor cursor = db.query(User.TABLE_NAME, projection, null, null, null, null, null);
        if(cursor.getCount()==0){
            SQLiteDatabase dbw = this.dbReader.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(User.COLUMN_NAME_USERNAME, "anand@gmail.com");
            values.put(User.COLUMN_NAME_PASSWORD, "111");
            dbw.insert(User.TABLE_NAME, null, values);
            values.put(User.COLUMN_NAME_USERNAME, "suresh@gmail.com");
            values.put(User.COLUMN_NAME_PASSWORD, "222");
            long newRowId = dbw.insert(User.TABLE_NAME, null, values);
        }

    }

    void authenticate(final String username, String password, final AuthListener authListener) {
        SQLiteDatabase db = this.dbReader.getReadableDatabase();
        String[] projection = {
                User.COLUMN_NAME_USERNAME,
                User.COLUMN_NAME_PASSWORD
        };

        String selection = User.COLUMN_NAME_USERNAME+ " = ? AND " + User.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = { username, password };

        Cursor cursor = db.query(User.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        List user = new ArrayList<>();
        while(cursor.moveToNext()) {
            String item = cursor.getString(
                    cursor.getColumnIndexOrThrow(User.COLUMN_NAME_USERNAME));
            user.add(item);
        }
        cursor.close();
        if(user.size()>0){
            authListener.onAuthSuccess((String)user.get(0));
        } else {
            authListener.onAuthFailure();
        }

    }

    public interface AuthListener {
        void onAuthSuccess(String username);
        void onAuthFailure();
    }

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
    }

}
