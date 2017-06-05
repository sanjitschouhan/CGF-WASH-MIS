package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CheckInDbHelper {

    private DbHelper dbHelper;

    public CheckInDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(CheckInRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.CheckInEntry.UID_OF_CC, record.getUidOfCC());
        values.put(Schemas.CheckInEntry.SCHOOL_CODE, record.getSchoolCode());
        values.put(Schemas.CheckInEntry.START_TIME, record.getStartTime());
        values.put(Schemas.CheckInEntry.END_TIME, record.getEndTime());
        db.insert(Schemas.CheckInEntry.TABLE_NAME, null, values);
    }

    public Cursor read() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.CheckInEntry._ID,
                Schemas.CheckInEntry.UID_OF_CC,
                Schemas.CheckInEntry.SCHOOL_CODE,
                Schemas.CheckInEntry.START_TIME,
                Schemas.CheckInEntry.END_TIME
        };

        return db.query(
                Schemas.CheckInEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                null,                              // Columns for WHERE clause
                null,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }

    public int delete(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                Schemas.CheckInEntry.TABLE_NAME,
                Schemas.CheckInEntry._ID + "= ? ",
                new String[]{String.valueOf(id)}
        );
    }

}
