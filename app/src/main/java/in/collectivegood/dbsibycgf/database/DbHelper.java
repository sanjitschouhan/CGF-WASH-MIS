package in.collectivegood.dbsibycgf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;
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
                    Schemas.CCDatabaseEntry.PHONE + " TEXT," +
                    Schemas.CCDatabaseEntry.EMAIL + " TEXT UNIQUE," +
                    Schemas.CCDatabaseEntry.PROJECT_COORDINATOR + " TEXT)";

    private static final String SQL_DELETE_ENTRIES_CC =
            "DROP TABLE IF EXISTS " + Schemas.CCDatabaseEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_DISCUSSION =
            "CREATE TABLE " + Schemas.DiscussionDatabaseEntry.TABLE_NAME + " (" +
                    Schemas.DiscussionDatabaseEntry.TIME + " NUMBER PRIMARY KEY," +
                    Schemas.DiscussionDatabaseEntry.NAME + " TEXT," +
                    Schemas.DiscussionDatabaseEntry.MESSAGE + " TEXT )";

    private static final String SQL_DELETE_ENTRIES_DISCUSSION =
            "DROP TABLE IF EXISTS " + Schemas.DiscussionDatabaseEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_CHECK_IN =
            "CREATE TABLE " + Schemas.CheckInEntry.TABLE_NAME + " (" +
                    Schemas.CheckInEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schemas.CheckInEntry.UID_OF_CC + " TEXT," +
                    Schemas.CheckInEntry.SCHOOL_CODE + " TEXT," +
                    Schemas.CheckInEntry.START_TIME + " NUMBER," +
                    Schemas.CheckInEntry.END_TIME + " NUMBER )";

    private static final String SQL_DELETE_ENTRIES_CHECK_IN =
            "DROP TABLE IF EXISTS " + Schemas.CheckInEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_HEPS_FORM =
            "CREATE TABLE " + Schemas.HEPSFormEntry.TABLE_NAME + " (" +
                    Schemas.HEPSFormEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Schemas.HEPSFormEntry.UID_OF_CC + " TEXT," +
                    Schemas.HEPSFormEntry.SCHOOL_CODE + " TEXT," +
                    Schemas.HEPSFormEntry.SCHOOL_NAME + " TEXT," +
                    Schemas.HEPSFormEntry.SCHOOL_ADDRESS + " TEXT," +
                    Schemas.HEPSFormEntry.MALE_TEACHERS + " NUMBER," +
                    Schemas.HEPSFormEntry.FEMALE_TEACHERS + " NUMBER," +
                    Schemas.HEPSFormEntry.TOTAL_TEACHERS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_1_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_1_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_1_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_2_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_2_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_2_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_3_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_3_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_3_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_4_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_4_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_4_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_5_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_5_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_5_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_TOTAL_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_TOTAL_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.CLASS_TOTAL_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_BOYS_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_GIRLS_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.TOILETS_TOTAL_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_BOYS + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_BOYS_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_GIRLS + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_GIRLS_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_TOTAL + " NUMBER," +
                    Schemas.HEPSFormEntry.URINALS_TOTAL_FUNCTIONING + " NUMBER," +
                    Schemas.HEPSFormEntry.WATER_SOURCE + " NUMBER," +
                    Schemas.HEPSFormEntry.NO_OF_TAPS + " NUMBER," +
                    Schemas.HEPSFormEntry.IS_SYNCED + " NUMBER" +
                    ")";

    private static final String SQL_DELETE_ENTRIES_HEPS_FORM =
            "DROP TABLE IF EXISTS " + Schemas.HEPSFormEntry.TABLE_NAME;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_SCHOOL);
        db.execSQL(SQL_CREATE_ENTRIES_CC);
        db.execSQL(SQL_CREATE_ENTRIES_DISCUSSION);
        db.execSQL(SQL_CREATE_ENTRIES_CHECK_IN);
        db.execSQL(SQL_CREATE_ENTRIES_HEPS_FORM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_SCHOOL);
        db.execSQL(SQL_DELETE_ENTRIES_CC);
        db.execSQL(SQL_DELETE_ENTRIES_DISCUSSION);
        db.execSQL(SQL_DELETE_ENTRIES_CHECK_IN);
        db.execSQL(SQL_DELETE_ENTRIES_HEPS_FORM);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES_SCHOOL);
        db.execSQL(SQL_DELETE_ENTRIES_CC);
        db.execSQL(SQL_CREATE_ENTRIES_SCHOOL);
        db.execSQL(SQL_CREATE_ENTRIES_CC);
    }
}