package in.collectivegood.dbsibycgf.gallery;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import in.collectivegood.dbsibycgf.R;

class ViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    String url;

    public ViewHolder(final View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        imageView.getLayoutParams().width = GetScreenWidthPx() / 2;
        url = null;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), GalleryPreviewActivity.class);
                intent.putExtra("url", url);
                itemView.getContext().startActivity(intent);
            }
        });
    }

    private int GetScreenWidthPx() {
        DisplayMetrics displayMetrics = itemView.getContext().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels - DpToPx(60);
    }

    public int DpToPx(int dp) {
        DisplayMetrics displayMetrics =
                itemView.getContext()
                        .getResources()
                        .getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5f);
    }

}
