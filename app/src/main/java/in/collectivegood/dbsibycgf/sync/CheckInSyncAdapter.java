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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import in.collectivegood.dbsibycgf.database.CheckInDbHelper;
import in.collectivegood.dbsibycgf.database.CheckInRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;


public class CheckInSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private CheckInDbHelper dbHelper;

    public CheckInSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        dbHelper = new CheckInDbHelper(new DbHelper(context));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("check_ins");

        if (dbHelper == null)
            dbHelper = new CheckInDbHelper(new DbHelper(getContext()));

        Cursor cursor = dbHelper.read();

        while (cursor.moveToNext()) {
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow(Schemas.CheckInEntry._ID));
            final String uidOfCC = cursor.getString(cursor.getColumnIndexOrThrow(Schemas.CheckInEntry.UID_OF_CC));
            final String schoolCode = cursor.getString(cursor.getColumnIndexOrThrow(Schemas.CheckInEntry.SCHOOL_CODE));
            final long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.CheckInEntry.START_TIME));
            final long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(Schemas.CheckInEntry.END_TIME));
            myRef.child(String.valueOf(uidOfCC + "_" + startTime))
                    .setValue(new CheckInRecord(uidOfCC, schoolCode, startTime, endTime))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dbHelper.delete(id);
                        }
                    });
        }
        cursor.close();
    }
}
