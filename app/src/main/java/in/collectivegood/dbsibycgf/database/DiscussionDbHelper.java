package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DiscussionDbHelper {

    private DbHelper dbHelper;

    public DiscussionDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(DiscussionRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.DiscussionDatabaseEntry.TIME, record.getTime());
        values.put(Schemas.DiscussionDatabaseEntry.NAME, record.getName());
        values.put(Schemas.DiscussionDatabaseEntry.MESSAGE, record.getMessage());
        db.insert(Schemas.DiscussionDatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor read() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.DiscussionDatabaseEntry.TIME,
                Schemas.DiscussionDatabaseEntry.NAME,
                Schemas.DiscussionDatabaseEntry.MESSAGE
        };

        return db.query(
                Schemas.DiscussionDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                null,                              // Columns for WHERE clause
                null,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }

}
