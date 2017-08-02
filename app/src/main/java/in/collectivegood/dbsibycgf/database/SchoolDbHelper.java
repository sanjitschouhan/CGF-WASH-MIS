package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import in.collectivegood.dbsibycgf.profiles.CCProfileActivity;

public class SchoolDbHelper {

    private DbHelper dbHelper;

    public SchoolDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(SchoolRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.SchoolDatabaseEntry.CODE, record.getCode());
        values.put(Schemas.SchoolDatabaseEntry.BLOCK, record.getBlock());
        values.put(Schemas.SchoolDatabaseEntry.VILLAGE, record.getVillage());
        values.put(Schemas.SchoolDatabaseEntry.NAME, record.getName());
        values.put(Schemas.SchoolDatabaseEntry.EMAIL, record.getEmail());
        values.put(Schemas.SchoolDatabaseEntry.DISTRICT, record.getDistrict());
        values.put(Schemas.SchoolDatabaseEntry.STATE, record.getState());
        values.put(Schemas.SchoolDatabaseEntry.UID_OF_CC, record.getUidOfCC());
        db.insert(Schemas.SchoolDatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor read(String attribute, String value) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.SchoolDatabaseEntry.CODE,
                Schemas.SchoolDatabaseEntry.BLOCK,
                Schemas.SchoolDatabaseEntry.VILLAGE,
                Schemas.SchoolDatabaseEntry.NAME,
                Schemas.SchoolDatabaseEntry.EMAIL,
                Schemas.SchoolDatabaseEntry.DISTRICT,
                Schemas.SchoolDatabaseEntry.STATE,
                Schemas.SchoolDatabaseEntry.UID_OF_CC
        };
        String condition = null;
        String[] conditionArgs = null;
        if (attribute != null && value != null) {
            condition = attribute + " = ?";
            conditionArgs = new String[]{value};
        }

        Cursor query = db.query(
                Schemas.SchoolDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                condition,                              // Columns for WHERE clause
                conditionArgs,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
        return query;
    }

    public int update(int code, String[] columns, String[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int update = 0;
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schemas.SchoolDatabaseEntry.CODE + " = ?";
            String[] selectionArgs = {String.valueOf(code)};

            update = db.update(
                    Schemas.SchoolDatabaseEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return update;
    }

    public Cursor read(String queryString) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.SchoolDatabaseEntry.CODE,
                Schemas.SchoolDatabaseEntry.BLOCK,
                Schemas.SchoolDatabaseEntry.VILLAGE,
                Schemas.SchoolDatabaseEntry.NAME,
                Schemas.SchoolDatabaseEntry.EMAIL,
                Schemas.SchoolDatabaseEntry.DISTRICT,
                Schemas.SchoolDatabaseEntry.STATE
        };
        String condition;
        String[] conditionArgs;
        if (queryString != null && queryString.length() > 0) {
            condition = "(" + Schemas.SchoolDatabaseEntry.NAME + " like ? or "
                    + Schemas.SchoolDatabaseEntry.VILLAGE + " like ? or "
                    + Schemas.SchoolDatabaseEntry.DISTRICT + " like ? ) and "
                    + Schemas.SchoolDatabaseEntry.UID_OF_CC + " = ? ";

            conditionArgs = new String[]{"%" + queryString + "%", "%" + queryString + "%", "%" + queryString + "%", CCProfileActivity.getCcUID()};
        } else {
            condition = Schemas.SchoolDatabaseEntry.UID_OF_CC + " = ? ";

            conditionArgs = new String[]{CCProfileActivity.getCcUID()};
        }

        return db.query(
                Schemas.SchoolDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                condition,                              // Columns for WHERE clause
                conditionArgs,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }
}
