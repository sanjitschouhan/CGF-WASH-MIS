package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CCDbHelper {
    private DbHelper dbHelper;

    public CCDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(CCRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.CCDatabaseEntry.UID, record.getUid());
        values.put(Schemas.CCDatabaseEntry.NAME, record.getName());
        values.put(Schemas.CCDatabaseEntry.EMAIL, record.getEmail());
        values.put(Schemas.CCDatabaseEntry.PHONE, record.getPhone());
        values.put(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR, record.getProjectCoordinator());
        db.insert(Schemas.CCDatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor read(String attribute, String value) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.CCDatabaseEntry.UID,
                Schemas.CCDatabaseEntry.NAME,
                Schemas.CCDatabaseEntry.EMAIL,
                Schemas.CCDatabaseEntry.PHONE,
                Schemas.CCDatabaseEntry.PROJECT_COORDINATOR,
        };
        String condition = null;
        String[] conditionArgs = null;
        if (attribute != null && value != null) {
            condition = attribute + " = ?";
            conditionArgs = new String[]{value};
        }

        Cursor query = db.query(
                Schemas.CCDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                condition,                              // Columns for WHERE clause
                conditionArgs,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );

        return query;
    }

    public int update(String uid, String[] columns, String[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int update = 0;
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schemas.CCDatabaseEntry.UID + " = ?";
            String[] selectionArgs = {uid};

            update = db.update(
                    Schemas.CCDatabaseEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return update;
    }


}
