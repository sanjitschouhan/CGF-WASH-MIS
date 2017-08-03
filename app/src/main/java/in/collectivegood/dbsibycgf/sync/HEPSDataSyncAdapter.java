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
import in.collectivegood.dbsibycgf.database.CheckInRecord;
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

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("check_ins");

        if (dbHelper == null)
            dbHelper = new HEPSDataDbHelper(new DbHelper(getContext()));

        Cursor cursor = dbHelper.read(Schemas.HEPSFormEntry.UID_OF_CC, getCCData());

        while (cursor.moveToNext()) {
            final int id = cursor.getInt(cursor.getColumnIndexOrThrow(Schemas.HEPSFormEntry._ID));
            myRef.child(String.valueOf(getCCData() + "_" + id))
                    .setValue(new HEPSDataRecord())
                    //// TODO: 3/8/17 heps data query from db
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                            dbHelper.delete(id);
                        }
                    });
        }
        cursor.close();
    }

    /**
     * Get information about current cluster coordinator
     */
    private String getCCData() {
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
