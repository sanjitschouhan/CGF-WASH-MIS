package in.collectivegood.dbsibycgf.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;

public class GallerySubActivity extends AppCompatActivity {
    GalleryAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> list;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_sub);

        recyclerView = (RecyclerView) findViewById(R.id.gallery_item_list);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        list = new ArrayList<>();
        adapter = new GalleryAdapter(this, list);
        recyclerView.setAdapter(adapter);

        final String name = getIntent().getExtras().getString("name").toLowerCase();
        DatabaseReference gallery = firebaseDatabase.getReference("gallery").child(name);

        gallery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(name + "/" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(name + "/" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_sub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_gallery_sub_download_all) {
            downloadAll();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void downloadAll() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage(getString(R.string.downloading_images));
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setMax(list.size());
        dialog.show();
        for (final String fileName : list) {
            File downloadFile = getFile(fileName);

            if (downloadFile != null) {
                StorageReference reference = FirebaseStorage.getInstance().getReference("gallery/" + fileName + ".jpeg");
                reference.getFile(downloadFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.e("downloadAll: ", "Downloaded: " + fileName);
                        dialog.setProgress(dialog.getProgress() + 1);
                        if (dialog.getProgress() == list.size()) {
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("downloadAll: ", "Cannot Download: " + fileName);
                        dialog.setProgress(dialog.getProgress() + 1);
                        if (dialog.getProgress() == list.size()) {
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                Log.e("downloadAll: ", "Cannot Create: " + fileName);
                dialog.setProgress(dialog.getProgress() + 1);
                if (dialog.getProgress() == list.size()) {
                    dialog.dismiss();
                }
            }
        }
    }

    public File getFile(String url) {
        File file = null;
        boolean success = true;
        File rootFolder = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));
        if (!rootFolder.exists()) {
            success = rootFolder.mkdir();
        }
        if (success) {
            File galleryFolder = new File(rootFolder.getPath() + "/Gallery");
            if (!galleryFolder.exists()) {
                success = galleryFolder.mkdir();
            }
            if (success) {
                File userFolder = new File(galleryFolder.getPath() + "/" + url.split("/")[0]);
                if (!userFolder.exists()) {
                    success = userFolder.mkdir();
                }
                if (success) {
                    file = new File(userFolder.getPath() + "/" + url.split("/")[1] + ".jpeg");
                }
            }
        }
        return file;
    }
}
