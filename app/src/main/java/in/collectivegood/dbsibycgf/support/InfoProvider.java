package in.collectivegood.dbsibycgf.support;


import android.content.Context;
import android.database.Cursor;
import android.os.Environment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import in.collectivegood.dbsibycgf.R;
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
        return getCCState(context, getCcUID(context));
    }

    /**
     * Get State of current cluster coordinator
     *
     * @param context
     */
    public static String getCCState(Context context, String Uid) {
        SchoolDbHelper schoolDbHelper = new SchoolDbHelper(new DbHelper(context));
        Cursor read = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.UID_OF_CC, Uid);
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
        return getCCData(context, getCcUID(context), attribute);
    }

    /**
     * Get information about current cluster coordinator
     */
    public static String getCCData(Context context, String uid, String attribute) {
        CCDbHelper ccDbHelper = new CCDbHelper(new DbHelper(context));
        Cursor read = ccDbHelper.read(Schemas.CCDatabaseEntry.UID, uid.toUpperCase());
        read.moveToNext();
        String result = read.getString(read.getColumnIndexOrThrow(attribute));
        read.close();
        return result;
    }


    public static File getFile(String url, Context context) {
        File file = null;
        boolean success = true;
        File folder = new File(Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name));
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        if (success) {
            folder = new File(folder.getPath() + "/Gallery");
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            String[] paths = url.split("/");
            for (int i = 0; i < paths.length - 2 && success; i++) {
                if (!paths[i].equalsIgnoreCase("gallery")) {
                    folder = new File(folder.getPath() + "/" + paths[i]);
                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                }
            }
            if (success) {
                String userName = InfoProvider.getCCData(context, paths[paths.length - 2], Schemas.CCDatabaseEntry.NAME);
                File userFolder = new File(folder.getPath() + "/" + userName);
                if (!userFolder.exists()) {
                    success = userFolder.mkdir();
                }
                if (success) {
                    file = new File(userFolder.getPath() + "/" + paths[paths.length - 1].split("\\.")[0] + ".jpeg");
                }
            }
        }
        return file;
    }
}
