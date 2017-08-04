package in.collectivegood.dbsibycgf.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataDbHelper;
import in.collectivegood.dbsibycgf.database.HEPSDataRecord;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.support.InfoProvider;

import static android.content.Context.ACCOUNT_SERVICE;

public class SyncFunctions {

    private static final String AUTHORITY = "in.collectivegood.dbsibycgf.provider";
    private static final String ACCOUNT_TYPE = "collectivegood.in";
    private static final String TAG = "sync_functions";
    private static Account account = null;
    private static ContentResolver mResolver;

    public static void CreateSyncAccount(Context context, String ACCOUNT) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            account = newAccount;
        } else {
            Account[] list = accountManager.getAccounts();

            for (Account ac : list) {
                if (ac.type.equalsIgnoreCase(ACCOUNT_TYPE)) {
                    account = ac;
                    break;
                }
            }
        }
        Log.d(TAG, "CreateSyncAccount: " + account);
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        mResolver = context.getContentResolver();
        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, 10);

        ContentResolver.setMasterSyncAutomatically(true);

    }

    public static void Sync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//        ContentResolver.requestSync(account, AUTHORITY, settingsBundle);
    }

    public static void SyncHEPSData(Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("heps_data");

        final HEPSDataDbHelper dbHelper = new HEPSDataDbHelper(new DbHelper(context));

        Cursor cursor = dbHelper.read(Schemas.HEPSFormEntry.IS_SYNCED, "0");

        while (cursor.moveToNext()) {
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry._ID));
            String schoolCode = cursor.getString(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry.SCHOOL_CODE));

            HEPSDataRecord record = new HEPSDataRecord(
                    InfoProvider.getCcUID(context),
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
            myRef.child(InfoProvider.getCCState(context)).child(InfoProvider.getCcUID(context)).child(schoolCode)
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


}
