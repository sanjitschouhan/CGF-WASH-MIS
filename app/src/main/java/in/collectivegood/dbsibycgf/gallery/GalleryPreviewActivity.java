package in.collectivegood.dbsibycgf.gallery;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import in.collectivegood.dbsibycgf.R;
import in.collectivegood.dbsibycgf.support.InfoProvider;

public class GalleryPreviewActivity extends AppCompatActivity {
    private static final String TAG = "gallery_preview_activity";
    private ImageView imageView;
    private StorageReference reference;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);

        imageView = (ImageView) findViewById(R.id.preview_image);

        url = getIntent().getExtras().getString("url");
        System.out.println(url);
        System.out.println(url.split("\\.")[0]);

        reference = FirebaseStorage.getInstance().getReference(url);
        File file = InfoProvider.getFile(url, this);
        if (file.exists()) {
            Glide.with(this)
                    .load(file)
                    .into(imageView);
            Button downloadButton = (Button) findViewById(R.id.btn_download);
            downloadButton.setText(R.string.downloaded);
            downloadButton.setEnabled(false);
        } else {
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(reference)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .thumbnail((float) 0.02)
                    .into(imageView);
        }
    }

    public void download(View view) {
        File downloadFile = InfoProvider.getFile(url, this);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.downloading_image));
        dialog.setProgressPercentFormat(null);
        dialog.setProgressNumberFormat(null);
        dialog.setCancelable(false);
        dialog.show();

        if (downloadFile != null) {
            reference.getFile(downloadFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(GalleryPreviewActivity.this, getString(R.string.picture_saved), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(GalleryPreviewActivity.this, "Failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.cannot_create_file), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void deleteConfirm() {
        FirebaseDatabase.getInstance().getReference(url.split("\\.")[0]).removeValue();
        reference.delete();
        finish();
    }

    public void deletePicture(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_prompt_title_single)
                .setMessage(R.string.delete_prompt_message_single)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteConfirm();
                    }
                });
        builder.show();

    }
}
