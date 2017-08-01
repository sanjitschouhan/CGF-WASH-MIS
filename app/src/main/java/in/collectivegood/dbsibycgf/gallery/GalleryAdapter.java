package in.collectivegood.dbsibycgf.gallery;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import in.collectivegood.dbsibycgf.R;

/**
 * Created by sanjit on 23/5/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    List<String> list;


    public GalleryAdapter(@NonNull Context context, @NonNull List<String> objects) {
        this.context = context;
        list = objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View newView = inflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File localFile = getFile(list.get(position));
        String url = "gallery/" + list.get(position) + ".jpeg";
        if (localFile.exists()) {
            Glide.with(context)
                    .load(localFile)
                    .into(holder.imageView);
        } else {
            StorageReference reference = FirebaseStorage.getInstance().getReference(url);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(reference)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .thumbnail((float) 0.01)
                    .into(holder.imageView);
        }
        holder.url = url;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public File getFile(String url) {
        File file = null;
        boolean success = true;
        File rootFolder = new File(Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name));
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
