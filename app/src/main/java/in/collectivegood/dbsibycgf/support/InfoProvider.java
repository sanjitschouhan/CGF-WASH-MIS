package in.collectivegood.dbsibycgf.support;


import android.content.Context;
import android.database.Cursor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;

public class InfoProvider {

    /**
     * Get State of current cluster coordinator
     *
     * @param context
     */
    public static String getCCState(Context context) {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(context));
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, getCcUID(context));
        read.moveToNext();
        String state = read.getString(read.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.STATE));
        read.close();

        return state;
    }

    /**
     * Get UID of current cluster coordinator
     *
     * @param context
     */
    public static String getCcUID(Context context) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String email = currentUser.getEmail();

        CCDbHelper ccDbHelper = new CCDbHelper(new DbHelper(context));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, email);
        read.moveToNext();
        String ccUID = read.getString(read.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.UID));
        read.close();

        return ccUID;
    }


    /**
     * Get information about current cluster coordinator
     */
    public static String getCCData(Context context, String attribute) {
        CCDbHelper ccDbHelper = new CCDbHelper(new DbHelper(context));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.UID, getCcUID(context));
        read.moveToNext();
        String result = read.getString(read.getColumnIndexOrThrow(attribute));
        read.close();
        return result;
    }
}
