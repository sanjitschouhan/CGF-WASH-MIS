package in.collectivegood.dbsibycgf.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataDbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataRecord;
import in.collectivegood.dbsibycgf.database.Schemas;


public class HEPSDataSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private HEPSDataDbHelper dbHelper;

    public HEPSDataSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        dbHelper = new HEPSDataDbHelper(new DbHelper(context));
    }

    public HEPSDataSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        dbHelper = new HEPSDataDbHelper(new DbHelper(context));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("heps_data");

        if (dbHelper == null)
            dbHelper = new HEPSDataDbHelper(new DbHelper(getContext()));

        Cursor cursor = dbHelper.read(Schemas.HEPSFormEntry.IS_SYNCED, "0");

        while (cursor.moveToNext()) {
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry._ID));
            String schoolCode = cursor.getString(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.SCHOOL_CODE));

            HEPSDataRecord record = new HEPSDataRecord(
                    getCCUid(),
                    cursor.getString(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.SCHOOL_CODE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.SCHOOL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.SCHOOL_ADDRESS)),

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.MALE_TEACHERS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.FEMALE_TEACHERS)),

                    new long[]{
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_1_BOYS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_2_BOYS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_3_BOYS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_4_BOYS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_5_BOYS))
                    },

                    new long[]{
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_1_GIRLS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_2_GIRLS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_3_GIRLS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_4_GIRLS)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.CLASS_5_GIRLS))
                    },

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_BOYS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_BOYS_FUNCTIONING)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_GIRLS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_GIRLS_FUNCTIONING)),

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_TOTAL)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.TOILETS_TOTAL_FUNCTIONING)),

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_BOYS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_BOYS_FUNCTIONING)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_GIRLS)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_GIRLS_FUNCTIONING)),

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_TOTAL)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.URINALS_TOTAL_FUNCTIONING)),

                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.WATER_SOURCE)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.NO_OF_TAPS))
            );
            System.out.println(record);
            myRef.child(String.valueOf(getCCUid() + "/" + schoolCode))
                    .setValue(record)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dbHelper.update(id, new String[]{Schemas.HEPSFormEntry.IS_SYNCED}, new String[]{"1"});
                        }
                    });
        }
        cursor.close();
    }

    /**
     * Get information about current cluster coordinator
     */
    private String getCCUid() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String email = currentUser.getEmail();

        CCDbHelper ccDbHelper = new CCDbHelper(new DbHelper(getContext()));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, email);
        read.moveToNext();
        String ccUID = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
        read.close();

        return ccUID;
    }

}
