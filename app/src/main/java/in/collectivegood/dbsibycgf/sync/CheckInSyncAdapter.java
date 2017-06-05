package in.collectivegood.dbsibycgf.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import in.collectivegood.dbsibycgf.database.CheckInDbHelper;
import in.collectivegood.dbsibycgf.database.CheckInRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;


public class CheckInSyncAdapter extends AbstractThreadedSyncAdapter {
    private CheckInDbHelper dbHelper;

    public CheckInSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        dbHelper = new CheckInDbHelper(new DbHelper(getContext()));
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("check_ins");
        Cursor cursor = dbHelper.read();

        final ArrayList<Integer> ids = new ArrayList<>();
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
                            ids.add(id);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        cursor.close();
        for (int i = 0; i < ids.size(); i++) {
            dbHelper.delete(ids.get(i));
        }
    }
}
