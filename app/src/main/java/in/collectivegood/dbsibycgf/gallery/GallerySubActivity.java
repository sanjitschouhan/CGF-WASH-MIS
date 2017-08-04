package in.collectivegood.dbsibycgf.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.support.InfoProvider;
import in.collectivegood.dbsibycgf.support.UserTypes;

public class GallerySubActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE = 1;
    private String uid;
    private GalleryAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private boolean deleting = false;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_sub);

        recyclerView = (RecyclerView) findViewById(R.id.gallery_item_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.empty).setVisibility(View.GONE);
                if (list.size() == 0)
                    findViewById(R.id.empty2).setVisibility(View.VISIBLE);

            }
        }, 5000);

        list = new ArrayList<>();
        adapter = new GalleryAdapter(this, list);
        recyclerView.setAdapter(adapter);

        //noinspection ConstantConditions
        uid = getIntent().getExtras().getString("uid").toLowerCase();
        //noinspection ConstantConditions
        state = getIntent().getExtras().getString("state").toLowerCase();
        final DatabaseReference gallery = firebaseDatabase.getReference("gallery")
                .child(state)
                .child(uid);

        gallery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(state + "/" + uid + "/" + dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
                findViewById(R.id.empty).setVisibility(View.GONE);
                findViewById(R.id.empty2).setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (!deleting) {
                    list.remove(state + "/" + uid + "/" + dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                    if (list.size() == 0) {
                        findViewById(R.id.empty2).setVisibility(View.VISIBLE);
                    }
                }
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
        adminOptions(menu);
        return true;
    }

    private void adminOptions(final Menu menu) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            //noinspection ConstantConditions
            final DatabaseReference user_type = FirebaseDatabase.getInstance().getReference("user_types").child(currentUser.getEmail().replaceAll("\\.", "(dot)"));
            user_type.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    if (value.equals(UserTypes.USER_TYPE_ADMIN)) {
                        menu.findItem(R.id.menu_gallery_sub_delete_all).setVisible(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_gallery_sub_download_all) {
            downloadAll();
            return true;
        } else if (item.getItemId() == R.id.menu_gallery_sub_delete_all) {
            deleteAll();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_prompt_title)
                .setMessage(R.string.delete_prompt_message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllPictures();
                    }
                });

        if (list.size() == 0) {
            Toast.makeText(this, R.string.no_pictures_to_delete, Toast.LENGTH_SHORT).show();
        } else {
            builder.show();
        }
    }

    private void deleteAllPictures() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage(getString(R.string.deleting_picture));
        dialog.setCancelable(false);
        dialog.setProgress(0);
        dialog.setMax(list.size());
        dialog.show();
        deleting = true;
        for (final String fileName : list) {
            FirebaseDatabase.getInstance().getReference("gallery").child(fileName).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dialog.setProgress(dialog.getProgress() + 1);
                            if (dialog.getProgress() == dialog.getMax()) {
                                dialog.dismiss();
                                Toast.makeText(GallerySubActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            FirebaseStorage.getInstance().getReference("gallery").child(fileName + ".jpeg").delete();
        }
        findViewById(R.id.empty2).setVisibility(View.VISIBLE);
        list.clear();
        deleting = false;
        adapter.notifyDataSetChanged();
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
            File downloadFile = InfoProvider.getFile(fileName, this);

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
        if (list.size() == 0) {
            dialog.dismiss();
            Toast.makeText(this, R.string.no_pictures_to_download, Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadPicture(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                ImageView imageView = new ImageView(this);
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
                imageView.setLayoutParams(layoutParams);
                linearLayout.addView(imageView);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.upload_image))
                        .setMessage(getString(R.string.upload_confirm_prompt))
                        .setView(linearLayout)
                        .setPositiveButton(getString(R.string.upload), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadImage(bitmap);
                            }
                        }).setNegativeButton(getString(R.string.cancel), null)
                        .setCancelable(false);
                builder.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(Bitmap bitmap) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.uploading_image));
        dialog.setProgressPercentFormat(null);
        dialog.setProgressNumberFormat(null);
        dialog.setCancelable(false);
        dialog.show();

        //noinspection ConstantConditions
        final String path =
                "gallery/" + state + "/" + uid
                        + "/" + System.currentTimeMillis();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(path + ".jpeg");

        int byteCount = bitmap.getByteCount() / 1024;

        int quality = 100;
        if (byteCount > 500) {
            quality = 100 * 500 / byteCount;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();

        storageReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //noinspection VisibleForTests,ConstantConditions
                        firebaseDatabase.getReference(path).setValue(taskSnapshot.getDownloadUrl().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(GallerySubActivity.this, getString(R.string.image_uploaded), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GallerySubActivity.this, getString(R.string.upload_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

}
