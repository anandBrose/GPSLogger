package com.anand.brose.gpslogger.transport;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.BaseColumns;

import com.anand.brose.gpslogger.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anand on 23-06-2017.
 */

public class LocationDetailsModel {

    private final LocationDetailsReader dbReader;

    public LocationDetailsModel(LocationDetailsReader dbReader){
        this.dbReader = dbReader;
    }

    public void pushLocation(String username, Location location) {
        SQLiteDatabase dbw = this.dbReader.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LocationDetail.COLUMN_NAME_USERNAME, username);
        values.put(LocationDetail.COLUMN_NAME_LATITUDE, location.getLatitude()+"");
        values.put(LocationDetail.COLUMN_NAME_LONGITUDE, location.getLongitude()+"");
        long rowId = dbw.insertWithOnConflict(LocationDetail.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        dbw.close();
    }
    public HashMap<String, Location> getLocations() {
        SQLiteDatabase db = this.dbReader.getReadableDatabase();
        String[] projection = {
                LocationDetail.COLUMN_NAME_USERNAME,
                LocationDetail.COLUMN_NAME_LATITUDE,
                LocationDetail.COLUMN_NAME_LONGITUDE
        };

        Cursor cursor = db.query(LocationDetail.TABLE_NAME, projection, null, null, null, null, null);
        HashMap<String, Location> map = new HashMap();
        while(cursor.moveToNext()) {
            Location targetLocation = new Location("");//provider name is unnecessary
            targetLocation.setLatitude(Double.parseDouble(cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationDetail.COLUMN_NAME_LONGITUDE))));//your coords of course
            targetLocation.setLongitude(Double.parseDouble(cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationDetail.COLUMN_NAME_LATITUDE))));

            map.put(cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationDetail.COLUMN_NAME_USERNAME)),targetLocation);
        }
        cursor.close();
        return map;
    }

    public static class LocationDetail implements BaseColumns {
        public static final String TABLE_NAME = "locationdetails";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
    }
}
