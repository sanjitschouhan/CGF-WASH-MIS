package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.collectivegood.dbsibycgf.common.SchoolRecord;

public class SchemaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "cgf.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Schema.SchoolDatabaseEntry.TABLE_NAME + " (" +
                    Schema.SchoolDatabaseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schema.SchoolDatabaseEntry.EMAIL_OF_CC + " TEXT," +
                    Schema.SchoolDatabaseEntry.NAME_OF_CC + " TEXT," +
                    Schema.SchoolDatabaseEntry.UID_OF_CC + " NUMBER," +
                    Schema.SchoolDatabaseEntry.NAME_OF_ZONAL_COORDINATOR + " TEXT," +
                    Schema.SchoolDatabaseEntry.EMAIL_OF_SCHOOL + " TEXT," +
                    Schema.SchoolDatabaseEntry.NAME_OF_SCHOOL + " TEXT," +
                    Schema.SchoolDatabaseEntry.SCHOOL_CODE + " TEXT," +
                    Schema.SchoolDatabaseEntry.NAME_OF_BLOCK + " TEXT," +
                    Schema.SchoolDatabaseEntry.DISTRICT + " TEXT," +
                    Schema.SchoolDatabaseEntry.STATE + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Schema.SchoolDatabaseEntry.TABLE_NAME;

    public SchemaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
        values.put(Schema.SchoolDatabaseEntry.EMAIL_OF_CC, record.getEmailOfCC());
        values.put(Schema.SchoolDatabaseEntry.NAME_OF_CC, record.getNameOfCC());
        values.put(Schema.SchoolDatabaseEntry.UID_OF_CC, record.getUidOfCC());
        values.put(Schema.SchoolDatabaseEntry.NAME_OF_ZONAL_COORDINATOR, record.getNameOfZonalCoordinator());
        values.put(Schema.SchoolDatabaseEntry.EMAIL_OF_SCHOOL, record.getEmailOfSchool());
        values.put(Schema.SchoolDatabaseEntry.NAME_OF_SCHOOL, record.getNameOfSchool());
        values.put(Schema.SchoolDatabaseEntry.SCHOOL_CODE, record.getSchoolCode());
        values.put(Schema.SchoolDatabaseEntry.NAME_OF_BLOCK, record.getNameOfBlock());
        values.put(Schema.SchoolDatabaseEntry.DISTRICT, record.getDistrict());
        values.put(Schema.SchoolDatabaseEntry.STATE, record.getState());
        db.insert(Schema.SchoolDatabaseEntry.TABLE_NAME, null, values);
    }

    public Cursor read() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                Schema.SchoolDatabaseEntry.EMAIL_OF_CC,
                Schema.SchoolDatabaseEntry.NAME_OF_CC,
                Schema.SchoolDatabaseEntry.UID_OF_CC,
                Schema.SchoolDatabaseEntry.NAME_OF_ZONAL_COORDINATOR,
                Schema.SchoolDatabaseEntry.EMAIL_OF_SCHOOL,
                Schema.SchoolDatabaseEntry.NAME_OF_SCHOOL,
                Schema.SchoolDatabaseEntry.SCHOOL_CODE,
                Schema.SchoolDatabaseEntry.NAME_OF_BLOCK,
                Schema.SchoolDatabaseEntry.DISTRICT,
                Schema.SchoolDatabaseEntry.STATE
        };

        return db.query(
                Schema.SchoolDatabaseEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                null,                                   // Columns for WHERE clause
                null,                                   // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }

    public int update(int id, String[] columns, String[] values) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schema.SchoolDatabaseEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            return db.update(
                    Schema.SchoolDatabaseEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return 0;
    }


}
