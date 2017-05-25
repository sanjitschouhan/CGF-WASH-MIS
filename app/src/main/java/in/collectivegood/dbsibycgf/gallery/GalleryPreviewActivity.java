package in.collectivegood.dbsibycgf.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import in.collectivegood.dbsibycgf.R;

public class GalleryPreviewActivity extends AppCompatActivity {
    ImageView imageView;
    StorageReference reference;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_preview);

        imageView = (ImageView) findViewById(R.id.preview_image);

        url = getIntent().getExtras().getString("url");

        reference = FirebaseStorage.getInstance().getReference(url);
        File file = getFile();
        if (file.exists()) {
            Glide.with(this)
                    .load(file)
                    .into(imageView);
            Button downloadButton = (Button) findViewById(R.id.download_button);
            downloadButton.setText("Downloaded");
            downloadButton.setEnabled(false);
        } else {
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(reference)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(imageView);
        }
    }

    public void download(View view) {
        File downloadFile = getFile();
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

    public File getFile() {
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
                File userFolder = new File(galleryFolder.getPath() + "/" + url.split("/")[1]);
                if (!userFolder.exists()) {
                    success = userFolder.mkdir();
                }
                if (success) {
                    file = new File(userFolder.getPath() + "/" + url.split("/")[2]);
                }
            }
        }
        return file;
    }
}
