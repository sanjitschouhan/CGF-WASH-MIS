package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cgf.db";
    private static final String SQL_CREATE_ENTRIES_SCHOOL =
            "CREATE TABLE " + Schemas.SchoolDatabaseEntry.TABLE_NAME + " (" +
                    Schemas.SchoolDatabaseEntry.CODE + " TEXT PRIMARY KEY," +
                    Schemas.SchoolDatabaseEntry.BLOCK + " TEXT," +
                    Schemas.SchoolDatabaseEntry.VILLAGE + " TEXT," +
                    Schemas.SchoolDatabaseEntry.NAME + " TEXT," +
                    Schemas.SchoolDatabaseEntry.EMAIL + " TEXT," +
                    Schemas.SchoolDatabaseEntry.DISTRICT + " TEXT," +
                    Schemas.SchoolDatabaseEntry.STATE + " TEXT," +
                    Schemas.SchoolDatabaseEntry.UID_OF_CC + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_SCHOOL =
            "DROP TABLE IF EXISTS " + Schemas.SchoolDatabaseEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_CC =
            "CREATE TABLE " + Schemas.CCDatabaseEntry.TABLE_NAME + " (" +
                    Schemas.CCDatabaseEntry.UID + " TEXT PRIMARY KEY," +
                    Schemas.CCDatabaseEntry.NAME + " TEXT," +
                    Schemas.CCDatabaseEntry.EMAIL + " TEXT," +
                    Schemas.CCDatabaseEntry.PROJECT_COORDINATOR + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_CC =
            "DROP TABLE IF EXISTS " + Schemas.CCDatabaseEntry.TABLE_NAME;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_SCHOOL);
        db.execSQL(SQL_CREATE_ENTRIES_CC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_SCHOOL);
        db.execSQL(SQL_DELETE_ENTRIES_CC);
        onCreate(db);
    }

}