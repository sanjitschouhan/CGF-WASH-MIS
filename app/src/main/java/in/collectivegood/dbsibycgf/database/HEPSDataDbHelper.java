package in.collectivegood.dbsibycgf.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HEPSDataDbHelper {

    private DbHelper dbHelper;

    public HEPSDataDbHelper(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void insert(HEPSDataRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Schemas.HEPSFormEntry.UID_OF_CC, record.getUidOfCC());
        values.put(Schemas.HEPSFormEntry.SCHOOL_CODE, record.getSchoolCode());
        values.put(Schemas.HEPSFormEntry.SCHOOL_NAME, record.getSchoolName());
        values.put(Schemas.HEPSFormEntry.SCHOOL_ADDRESS, record.getSchoolAddress());
        values.put(Schemas.HEPSFormEntry.MALE_TEACHERS, record.getMaleTeachers());
        values.put(Schemas.HEPSFormEntry.FEMALE_TEACHERS, record.getFemaleTeachers());
        values.put(Schemas.HEPSFormEntry.TOTAL_TEACHERS, record.getMaleTeachers() + record.getFemaleTeachers());
        values.put(Schemas.HEPSFormEntry.CLASS_1_BOYS, record.getBoys()[0]);
        values.put(Schemas.HEPSFormEntry.CLASS_1_GIRLS, record.getGirls()[0]);
        values.put(Schemas.HEPSFormEntry.CLASS_1_TOTAL, record.getTotalClassStrength(1));
        values.put(Schemas.HEPSFormEntry.CLASS_2_BOYS, record.getBoys()[1]);
        values.put(Schemas.HEPSFormEntry.CLASS_2_GIRLS, record.getGirls()[1]);
        values.put(Schemas.HEPSFormEntry.CLASS_2_TOTAL, record.getTotalClassStrength(2));
        values.put(Schemas.HEPSFormEntry.CLASS_3_BOYS, record.getBoys()[2]);
        values.put(Schemas.HEPSFormEntry.CLASS_3_GIRLS, record.getGirls()[2]);
        values.put(Schemas.HEPSFormEntry.CLASS_3_TOTAL, record.getTotalClassStrength(3));
        values.put(Schemas.HEPSFormEntry.CLASS_4_BOYS, record.getBoys()[3]);
        values.put(Schemas.HEPSFormEntry.CLASS_4_GIRLS, record.getGirls()[3]);
        values.put(Schemas.HEPSFormEntry.CLASS_4_TOTAL, record.getTotalClassStrength(4));
        values.put(Schemas.HEPSFormEntry.CLASS_5_BOYS, record.getBoys()[4]);
        values.put(Schemas.HEPSFormEntry.CLASS_5_GIRLS, record.getGirls()[4]);
        values.put(Schemas.HEPSFormEntry.CLASS_5_TOTAL, record.getTotalClassStrength(5));
        values.put(Schemas.HEPSFormEntry.CLASS_TOTAL_BOYS, record.getTotalBoys());
        values.put(Schemas.HEPSFormEntry.CLASS_TOTAL_GIRLS, record.getTotalGirls());
        values.put(Schemas.HEPSFormEntry.CLASS_TOTAL_TOTAL, record.getTotalBoys() + record.getTotalGirls());
        values.put(Schemas.HEPSFormEntry.TOILETS_BOYS, record.getBoysToilets());
        values.put(Schemas.HEPSFormEntry.TOILETS_BOYS_FUNCTIONING, record.getFunctioningBoysToilets());
        values.put(Schemas.HEPSFormEntry.TOILETS_GIRLS, record.getGirlsToilets());
        values.put(Schemas.HEPSFormEntry.TOILETS_GIRLS_FUNCTIONING, record.getFunctioningGirlsToilets());
        values.put(Schemas.HEPSFormEntry.TOILETS_TOTAL, record.getTotalToilets());
        values.put(Schemas.HEPSFormEntry.TOILETS_TOTAL_FUNCTIONING, record.getFunctioningTotalToilets());
        values.put(Schemas.HEPSFormEntry.URINALS_BOYS, record.getBoysUrinals());
        values.put(Schemas.HEPSFormEntry.URINALS_BOYS_FUNCTIONING, record.getFunctioningBoysUrinals());
        values.put(Schemas.HEPSFormEntry.URINALS_GIRLS, record.getGirlsUrinals());
        values.put(Schemas.HEPSFormEntry.URINALS_GIRLS_FUNCTIONING, record.getFunctioningGirlsUrinals());
        values.put(Schemas.HEPSFormEntry.URINALS_TOTAL, record.getTotalUrinals());
        values.put(Schemas.HEPSFormEntry.URINALS_TOTAL_FUNCTIONING, record.getFunctioningTotalUrinals());
        values.put(Schemas.HEPSFormEntry.WATER_SOURCE, record.getWaterSource());
        values.put(Schemas.HEPSFormEntry.NO_OF_TAPS, record.getNoOfTaps());

        db.insert(Schemas.HEPSFormEntry.TABLE_NAME, null, values);
    }

    public Cursor read(String attribute, String value) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Schemas.HEPSFormEntry._ID,
                Schemas.HEPSFormEntry.UID_OF_CC,
                Schemas.HEPSFormEntry.SCHOOL_CODE,
                Schemas.HEPSFormEntry.SCHOOL_NAME,
                Schemas.HEPSFormEntry.SCHOOL_ADDRESS,
                Schemas.HEPSFormEntry.MALE_TEACHERS,
                Schemas.HEPSFormEntry.FEMALE_TEACHERS,
                Schemas.HEPSFormEntry.TOTAL_TEACHERS,
                Schemas.HEPSFormEntry.CLASS_1_BOYS,
                Schemas.HEPSFormEntry.CLASS_1_GIRLS,
                Schemas.HEPSFormEntry.CLASS_1_TOTAL,
                Schemas.HEPSFormEntry.CLASS_2_BOYS,
                Schemas.HEPSFormEntry.CLASS_2_GIRLS,
                Schemas.HEPSFormEntry.CLASS_2_TOTAL,
                Schemas.HEPSFormEntry.CLASS_3_BOYS,
                Schemas.HEPSFormEntry.CLASS_3_GIRLS,
                Schemas.HEPSFormEntry.CLASS_3_TOTAL,
                Schemas.HEPSFormEntry.CLASS_4_BOYS,
                Schemas.HEPSFormEntry.CLASS_4_GIRLS,
                Schemas.HEPSFormEntry.CLASS_4_TOTAL,
                Schemas.HEPSFormEntry.CLASS_5_BOYS,
                Schemas.HEPSFormEntry.CLASS_5_GIRLS,
                Schemas.HEPSFormEntry.CLASS_5_TOTAL,
                Schemas.HEPSFormEntry.CLASS_TOTAL_BOYS,
                Schemas.HEPSFormEntry.CLASS_TOTAL_GIRLS,
                Schemas.HEPSFormEntry.CLASS_TOTAL_TOTAL,
                Schemas.HEPSFormEntry.TOILETS_BOYS,
                Schemas.HEPSFormEntry.TOILETS_BOYS_FUNCTIONING,
                Schemas.HEPSFormEntry.TOILETS_GIRLS,
                Schemas.HEPSFormEntry.TOILETS_GIRLS_FUNCTIONING,
                Schemas.HEPSFormEntry.TOILETS_TOTAL,
                Schemas.HEPSFormEntry.TOILETS_TOTAL_FUNCTIONING,
                Schemas.HEPSFormEntry.URINALS_BOYS,
                Schemas.HEPSFormEntry.URINALS_BOYS_FUNCTIONING,
                Schemas.HEPSFormEntry.URINALS_GIRLS,
                Schemas.HEPSFormEntry.URINALS_GIRLS_FUNCTIONING,
                Schemas.HEPSFormEntry.URINALS_TOTAL,
                Schemas.HEPSFormEntry.URINALS_TOTAL_FUNCTIONING,
                Schemas.HEPSFormEntry.WATER_SOURCE,
                Schemas.HEPSFormEntry.NO_OF_TAPS
        };
        String condition = null;
        String[] conditionArgs = null;
        if (attribute != null && value != null) {
            condition = attribute + " = ?";
            conditionArgs = new String[]{value};
        }

        return db.query(
                Schemas.HEPSFormEntry.TABLE_NAME,  // Table name
                projection,                             // Columns to return
                condition,                              // Columns for WHERE clause
                conditionArgs,                           // Values for WHERE clause
                null,                                   // GROUP BY clause
                null,                                   // HAVING clause
                null                                    // SORT BY clause
        );
    }

    public int update(int id, String[] columns, String[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int update = 0;
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length) {
            for (int i = 0; i < columns.length; i++) {
                contentValues.put(columns[i], values[i]);
            }

            String selection = Schemas.HEPSFormEntry._ID + " = ?";
            String[] selectionArgs = {String.valueOf(id)};

            update = db.update(
                    Schemas.HEPSFormEntry.TABLE_NAME,
                    contentValues,
                    selection,
                    selectionArgs);
        }
        return update;
    }
}
