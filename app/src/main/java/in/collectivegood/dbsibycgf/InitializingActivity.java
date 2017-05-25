package in.collectivegood.dbsibycgf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.CCRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.database.SchoolRecord;
import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;

public class InitializingActivity extends AppCompatActivity {

    SchoolDbHelper schoolDbHelper;
    CCDbHelper ccDbHelper;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initializing);
//        startActivity(new Intent(this, GalleryMainActivity.class));
//        finish();

        dbHelper = new DbHelper(this);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.loading_data));
        dialog.setProgressPercentFormat(null);
        dialog.setProgressNumberFormat(null);
        dialog.setCancelable(false);
        dialog.show();
        final File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/list.csv");
        if (file.exists()) {
            try {
                readFile(file);
                setName(FirebaseAuth.getInstance().getCurrentUser());
                dialog.dismiss();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference reference = storage.getReference("list.csv");
            reference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    try {
                        readFile(file);
                        setName(FirebaseAuth.getInstance().getCurrentUser());
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void readFile(File file) throws IOException {
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(buf));
        int count = 0;
        schoolDbHelper = new SchoolDbHelper(dbHelper);
        ccDbHelper = new CCDbHelper(dbHelper);
        reader.readLine();
        while (true) {
            String s = reader.readLine();
            if (s != null) {
                String[] record = s.split(",");
                Log.e("onComplete: ", Arrays.toString(record));
                String block = record[1];
                String village = record[2];
                String schoolCode = record[3];
                String schoolName = record[4];
                String schoolEmail = record[5];
                String schoolDistrict = record[6];
                String schoolState = record[7];
                String ccName = record[8];
                String ccEmail = record[9];
                String ccUid = record[10];
                String projectCoordinator = record[11];
                SchoolRecord schoolRecord = new SchoolRecord(schoolCode, block, village,
                        schoolName, schoolEmail, schoolState, schoolDistrict, ccUid);
                Cursor school = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.CODE, schoolCode);
                if (school.getCount() <= 0) {
                    school.close();
                    schoolDbHelper.insert(schoolRecord);
                }
                school.close();

                CCRecord ccRecord = new CCRecord(ccUid, ccName, ccEmail, projectCoordinator);
                Cursor cc = ccDbHelper.read(Schemas.CCDatabaseEntry.UID, ccUid);
                if (cc.getCount() <= 0) {
                    cc.close();
                    ccDbHelper.insert(ccRecord);
                }
                cc.close();
                count++;
            } else {
                break;
            }
        }
        Log.e("onComplete: ", String.valueOf(count));
        buf.close();
    }

    public void setName(final FirebaseUser user) {
        Cursor cc = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, user.getEmail());
        String name = "UNKNOWN";
        if (cc.getCount() > 0) {
            cc.moveToFirst();
            name = cc.getString(cc.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
        }
        UserProfileChangeRequest userProfileChangeRequest
                = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(InitializingActivity.this, GalleryMainActivity.class));
                finish();
            }
        });
    }
}
