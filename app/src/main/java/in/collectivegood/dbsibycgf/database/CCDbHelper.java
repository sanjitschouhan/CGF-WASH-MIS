package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CCDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cgf.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Schemas.CCDatabaseEntry.TABLE_NAME + " (" +
                    Schemas.CCDatabaseEntry.UID + " TEXT PRIMARY KEY," +
                    Schemas.CCDatabaseEntry.NAME + " TEXT," +
                    Schemas.CCDatabaseEntry.EMAIL + " TEXT," +
                    Schemas.CCDatabaseEntry.PROJECT_COORDINATOR + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Schemas.CCDatabaseEntry.TABLE_NAME;

    public CCDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insert(CCRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.CCDatabaseEntry.UID, record.getUid());
        values.put(Schemas.CCDatabaseEntry.NAME, record.getName());
        values.put(Schemas.CCDatabaseEntry.EMAIL, record.getEmail());
        values.put(Schemas.CCDatabaseEntry.PROJECT_COORDINATOR, record.getProjectCoordinator());
        db.insert(Schemas.CCDatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor read(String attribute, String value) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                Schemas.CCDatabaseEntry.UID,
                Schemas.CCDatabaseEntry.NAME,
                Schemas.CCDatabaseEntry.EMAIL,
                Schemas.CCDatabaseEntry.PROJECT_COORDINATOR,
        };
        String condition = null;
        String[] conditionArgs = null;
        if (attribute != null && value != null) {
            condition = attribute + " = ?";
            conditionArgs = new String[]{value};
        }

        return db.query(
                Schemas.CCDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                condition,                              // Columns for WHERE clause
                conditionArgs,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }

    public int update(String uid, String[] columns, String[] values) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schemas.CCDatabaseEntry.UID + " = ?";
            String[] selectionArgs = {uid};

            return db.update(
                    Schemas.CCDatabaseEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return 0;
    }


}
