package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SchoolDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cgf.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Schemas.SchoolDatabaseEntry.TABLE_NAME + " (" +
                    Schemas.SchoolDatabaseEntry.CODE + " INTEGER PRIMARY KEY," +
                    Schemas.SchoolDatabaseEntry.BLOCK + " TEXT," +
                    Schemas.SchoolDatabaseEntry.VILLAGE + " TEXT," +
                    Schemas.SchoolDatabaseEntry.NAME + " TEXT," +
                    Schemas.SchoolDatabaseEntry.EMAIL + " TEXT," +
                    Schemas.SchoolDatabaseEntry.DISTRICT + " TEXT," +
                    Schemas.SchoolDatabaseEntry.STATE + " TEXT," +
                    Schemas.SchoolDatabaseEntry.UID_OF_CC + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Schemas.SchoolDatabaseEntry.TABLE_NAME;

    public SchoolDbHelper(Context context) {
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

    public void insert(SchoolRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public Cursor read(long code) {
        SQLiteDatabase db = this.getReadableDatabase();
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
        if (code != 0) {
            condition = Schemas.SchoolDatabaseEntry.CODE + " = ?";
            conditionArgs = new String[]{String.valueOf(code)};
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

    public int update(int code, String[] columns, String[] values) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schemas.SchoolDatabaseEntry.CODE + " = ?";
            String[] selectionArgs = {String.valueOf(code)};

            return db.update(
                    Schemas.SchoolDatabaseEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return 0;
    }


}
