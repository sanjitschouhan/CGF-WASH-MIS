package in.collectivegood.dbsibycgf.gallery;

import android.content.Context;
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
import in.collectivegood.dbsibycgf.support.InfoProvider;


class GalleryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<String> list;


    GalleryAdapter(@NonNull Context context, @NonNull List<String> objects) {
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
        File localFile = InfoProvider.getFile(list.get(position), context);
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
}
