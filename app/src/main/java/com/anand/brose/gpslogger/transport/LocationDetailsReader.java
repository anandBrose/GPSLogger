
package com.anand.brose.gpslogger.transport;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import com.anand.brose.gpslogger.UserModel;

/**
 * Created by Anand on 23-06-2017.
 */

public class LocationDetailsReader  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "locationdetails.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocationDetailsModel.LocationDetail.TABLE_NAME + " (" +
                    LocationDetailsModel.LocationDetail.COLUMN_NAME_USERNAME + " TEXT PRIMARY KEY UNIQUE," +
                    LocationDetailsModel.LocationDetail.COLUMN_NAME_LONGITUDE + " TEXT," +
                    LocationDetailsModel.LocationDetail.COLUMN_NAME_LATITUDE + " TEXT,"+
                    " FOREIGN KEY("+LocationDetailsModel.LocationDetail.COLUMN_NAME_USERNAME+") REFERENCES "+ UserModel.User.TABLE_NAME+"("+ UserModel.User.COLUMN_NAME_USERNAME+"))";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationDetailsModel.LocationDetail.TABLE_NAME;

    public LocationDetailsReader(Context context) {
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
