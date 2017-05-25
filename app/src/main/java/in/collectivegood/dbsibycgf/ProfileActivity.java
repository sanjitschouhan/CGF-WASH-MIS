package in.collectivegood.dbsibycgf;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import in.collectivegood.dbsibycgf.gallery.GalleryMainActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        startActivity(new Intent(this, GalleryMainActivity.class));
        finish();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference("list.csv");
        final File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/list.csv");
        if (file.exists()) {
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(buf));
                int count = 0;
                reader.readLine();
                while (true) {
                    String s = reader.readLine();
                    if (s != null) {
                        Log.e("onComplete: ", s);
                        count++;
                    } else {
                        break;
                    }
                }
                Log.e("onComplete: ", String.valueOf(count));
                buf.close();
                startActivity(new Intent(this, GalleryMainActivity.class));
                finish();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            reference.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        BufferedReader reader = new BufferedReader(new InputStreamReader(buf));
                        int count = 0;
                        while (true) {
                            String s = reader.readLine();
                            if (s != null) {
                                Log.e("onComplete: ", s);
                                count++;
                            } else {
                                break;
                            }
                        }
                        Log.e("onComplete: ", String.valueOf(count));
                        buf.close();

                        startActivity(new Intent(ProfileActivity.this, GalleryMainActivity.class));
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
