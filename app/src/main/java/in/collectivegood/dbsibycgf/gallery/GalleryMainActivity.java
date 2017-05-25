package in.collectivegood.dbsibycgf.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import in.collectivegood.dbsibycgf.R;

@SuppressWarnings("VisibleForTests")
public class GalleryMainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE = 1;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_main);

        GridView gridView = (GridView) findViewById(R.id.gallery_cc_list);
        gridView.setEmptyView(findViewById(R.id.empty));
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.gallery_cc, R.id.gallery_cc_name, list);
        gridView.setAdapter(adapter);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference gallery = firebaseDatabase.getReference("gallery");
        gallery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getKey().toUpperCase());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getKey().toUpperCase());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = list.get(position);
                Intent intent = new Intent(GalleryMainActivity.this, GallerySubActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });
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

        final String path =
                "gallery/" +
                        FirebaseAuth.getInstance().getCurrentUser()
                                .getDisplayName().toLowerCase()
                        + "/" + System.currentTimeMillis();
        StorageReference storageReference = firebaseStorage.getReference(path + ".jpeg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        firebaseDatabase.getReference(path).setValue(taskSnapshot.getDownloadUrl().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialog.dismiss();
                                Toast.makeText(GalleryMainActivity.this, getString(R.string.image_uploaded), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GalleryMainActivity.this, getString(R.string.upload_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }
}
