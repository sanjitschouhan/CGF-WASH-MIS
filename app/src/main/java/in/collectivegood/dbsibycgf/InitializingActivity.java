package in.collectivegood.dbsibycgf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import in.collectivegood.dbsibycgf.database.CCDbHelper;
import in.collectivegood.dbsibycgf.database.CCRecord;
import in.collectivegood.dbsibycgf.database.DbHelper;
import in.collectivegood.dbsibycgf.database.Schemas;
import in.collectivegood.dbsibycgf.database.SchoolDbHelper;
import in.collectivegood.dbsibycgf.database.SchoolRecord;
import in.collectivegood.dbsibycgf.discussion.DiscussionActivity;
import in.collectivegood.dbsibycgf.profiles.CCProfileActivity;
import in.collectivegood.dbsibycgf.support.UserTypes;

import static in.collectivegood.dbsibycgf.sync.SyncFunctions.CreateSyncAccount;

public class InitializingActivity extends AppCompatActivity {
    private static final String TAG = "Activity_Initializing";
    private static final int PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE = 1;
    private StorageReference reference;
    private SchoolDbHelper schoolDbHelper;
    private CCDbHelper ccDbHelper;
    private Intent intent;
    private ProgressDialog dialog;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initializing);

        intent = new Intent(InitializingActivity.this, DiscussionActivity.class);
        schoolDbHelper = new SchoolDbHelper(new DbHelper(this));
        ccDbHelper = new CCDbHelper(new DbHelper(this));

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.loading_data));
        dialog.setProgressPercentFormat(null);
        dialog.setProgressNumberFormat(null);
        dialog.setCancelable(false);
        dialog.show();

        setUserType();
    }

    private void setUserType() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_types");
        //noinspection ConstantConditions
        final String current_key = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("\\.", "(dot)");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                boolean found = false;
                for (DataSnapshot child : children) {
                    String key = child.getKey();
                    if (key.equals(current_key)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    databaseReference.child(current_key).setValue(UserTypes.USER_TYPE_CC);
                }
                databaseReference.removeEventListener(this);
                proceedWithLoading();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void proceedWithLoading() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            load();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                load();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void load() {
        file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/list.csv");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        reference = storage.getReference("list.csv");
        if (file.exists()) {
            final long localTime = file.lastModified();
            if (isNetworkAvailable()) {
                reference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        long onlineTime = storageMetadata.getCreationTimeMillis();
                        if (localTime < onlineTime) {
                            downloadFile();
                        } else {
                            try {
                                readFile(file);
                                setName();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } else {
                try {
                    readFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setName();
            }
        } else {
            try {
                File folder = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
                boolean mkdirs = true;
                if (!folder.exists()) {
                    mkdirs = folder.mkdirs();
                }
                if (mkdirs) {
                    if (file.createNewFile()) {
                        downloadFile();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadFile() {
        reference.getFile(file)
                .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        try {
                            readFile(file);
                            setName();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void readFile(File file) throws IOException {
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(buf));
        reader.readLine();
        while (true) {
            String s = reader.readLine();
            if (s != null) {
                String[] record = s.split(",");
//                Log.d(TAG, "onComplete: " + Arrays.toString(record));

                String block = record[1];
                String village = record[2];
                String schoolCode = record[3];
                String schoolName = record[4];
                String schoolEmail = record[5];
                String schoolDistrict = record[6];
                String schoolState = record[7];
                String ccName = record[8].trim();
                String ccEmail = record[9];
                String ccUid = record[10];
                String projectCoordinator = record[11];
                String ccPhone = record[12];

                SchoolRecord schoolRecord = new SchoolRecord(schoolCode, block, village,
                        schoolName, schoolEmail, schoolState, schoolDistrict, ccUid);
                Cursor school = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.CODE, schoolCode);
                if (school.getCount() <= 0) {
                    school.close();
                    schoolDbHelper.insert(schoolRecord);
                }
                school.close();

                CCRecord ccRecord = new CCRecord(ccUid, ccName, ccPhone, ccEmail, projectCoordinator);
                Cursor cc = ccDbHelper.read(Schemas.CCDatabaseEntry.UID, ccUid);
                if (cc.getCount() <= 0) {
                    cc.close();
                    ccDbHelper.insert(ccRecord);
                }
                cc.close();

            } else {
                break;
            }
        }
        buf.close();
    }

    public void setName() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        Cursor cc = ccDbHelper.read(Schemas.CCDatabaseEntry.EMAIL, user.getEmail());
        String name = "UNKNOWN";
        if (cc.getCount() > 0) {
            cc.moveToFirst();
            name = cc.getString(cc.getColumnIndexOrThrow(Schemas.CCDatabaseEntry.NAME));
            intent = new Intent(InitializingActivity.this, CCProfileActivity.class);
        } else {
            cc = schoolDbHelper.read(Schemas.SchoolDatabaseEntry.EMAIL, user.getEmail());
            if (cc.getCount() > 0) {
                cc.moveToFirst();
                name = cc.getString(cc.getColumnIndexOrThrow(Schemas.SchoolDatabaseEntry.NAME));
                intent = new Intent(InitializingActivity.this, SchoolDbHelper.class);
            }
        }
        if (user.getDisplayName() == null) {
//            Log.d(TAG, "setName: " + name);
            UserProfileChangeRequest userProfileChangeRequest
                    = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            final String finalName = name;
            user.updateProfile(userProfileChangeRequest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CreateSyncAccount(InitializingActivity.this, finalName);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    InitializingActivity.this, e.getMessage(), Toast.LENGTH_SHORT
                            ).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
        } else {
            CreateSyncAccount(InitializingActivity.this, name);
            startActivity(intent);
            dialog.dismiss();
            finish();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
